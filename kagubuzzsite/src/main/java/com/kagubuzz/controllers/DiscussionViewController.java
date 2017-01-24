package com.kagubuzz.controllers;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.DiscussionDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.PostsDAO;
import com.kagubuzz.database.dao.TagDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.discussions.PublicDiscussionMessage;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.services.notifications.NotificationTypes;

@Controller
@RequestMapping("/discussion")
public class DiscussionViewController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    DiscussionDAO discussionDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    CRUDDAO crudDAO;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    SecurityService securityService;
    @Autowired
    MessageDispatcher messageDispatcher;
    @Autowired
    TagDAO tagDAO;
    @Autowired
    SearchService searchService;
    @Autowired
    UserSessionService sessionUserService;
    @Autowired
    PostsService postsService;
    @Autowired
    PostsDAO postsDAO;
    @Autowired
    NotificationCache notificationCache;
    @Autowired
    StringTemplateService stringTemplateService;
    
    static Logger logger = Logger.getLogger(DiscussionViewController.class);  
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    /* Since public and private discussions share the same jsp page
     * 
     * Every ajax call in the discussion public view URL has to have public/view/{id}/(ajax call)
     * Every ajax call in the discussion private view URL has to have private/view/{id}/(ajax call)
     * 
     * These could be separated into different controllers later so the call is only for /view/id/(ajax call)
     * 
     */

    @RequestMapping(value = "private/view/{id}/{seoFriendlyURL}", method = RequestMethod.GET)
    public String discussionViewPrivate(HttpSession httpSession,                                        
                                        @PathVariable Long id, 
                                        ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();

        TBLDiscussionPrivate discussion = crudDAO.getById(TBLDiscussionPrivate.class, id);

        if (discussion == null) {
            modelMap.addAttribute("error_page_message", "Sorry this disucssion no longer exists. Please note that all notifications from Kagu Buzz are deleted after you read them.");
            logger.warn("Request for non-exsistant discussion"); 
            return "/jsp/error";
        }
        
        if (!discussion.getParticipants().contains(user)) {
            logger.warn("In discussionViewPrivate User trying to access non-participating discussion " + user.getId()); 
            return "/jsp/error";
        }
        
        postsService.setAllNotificationsToRead(discussion.getMessages(), discussion, user, true);
        
        modelMap.addAttribute("discussion", discussion);

        return "/jsp/discussion_view";
    }

    @RequestMapping(value = "public/unsubscribe/{id}", method = RequestMethod.GET)
    public String discussionPublicUnsubscribe(@PathVariable Long id, ModelMap modelMap) {
        
        TBLDiscussionPublic discussion = crudDAO.getById(TBLDiscussionPublic.class, id);
        
        TBLUser user = sessionUserService.getUser();
        
        if (discussion == null) {
            logger.warn("In discussionPublicUnsubscribe User trying to access non-exisant discussion");            
            return "/jsp/error";
        }      
        
        if (!discussion.getParticipants().contains(user)) {
            logger.warn("In discussionPublicUnsubscribe User trying to access non-participating discussion " + user.getId()); 
            return "/jsp/error";
        }
        
        discussion.removeParticipant(user);
        
        crudDAO.update(discussion);
        
        notificationCache.add(new Notification("Removed From Discussion","You were removed from the discussion " + discussion.getTitle() + ", and will no longer receive updates when people contribute to it."), user);
        
        return "redirect:/discussions/browse/categories";       
    }
    
    @RequestMapping(value = "public/view/{id}/{seoFriendlyURL}", method = RequestMethod.GET)
    public String discussionViewPublic(@PathVariable Long id,   
                                       @RequestParam(required = false, value="scroll_id") Long scroll_id,
                                       ModelMap modelMap) {

        TBLDiscussionPublic discussion = crudDAO.getById(TBLDiscussionPublic.class, id);

        if (discussion == null) {
            logger.warn("In discussionViewPublic User trying to access non-existant discussion"); 
            return "/jsp/error";
        }        
        
        LSTDiscussionCategory category = discussion.getCategory();

        if (category == null) {
            category = crudDAO.getById(LSTDiscussionCategory.class, 1L);
        }
        
        postsService.setAllNotificationsToRead(discussion.getMessages(), discussion, sessionUserService.getUser(), false);
        
        modelMap.addAttribute("discussion", discussion);
        modelMap.addAttribute("firstmessage", discussion.getFirstMessageInThread());
        modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(category, false));
        modelMap.addAttribute("scroll_id", scroll_id);
        
        return "/jsp/discussion_view";
    }
    
    @RequestMapping(value = "public/view/{id}/reply", method = RequestMethod.POST)
    public String discussionReplyAjax(@RequestParam("messageid") Long messageId, 
                                      @RequestParam("parentid") Long parentId, 
                                      @RequestParam String message,
                                      ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();

        TBLDiscussionPublic discussion = crudDAO.getById(TBLDiscussionPublic.class, parentId);

        TBLMessageDiscussionPublic originalMessage = (TBLMessageDiscussionPublic) messageDAO.getMessage(messageId, TBLMessageDiscussionPublic.class);

        PublicDiscussionMessage publicMessage = 
                new PublicDiscussionMessage(discussion, 
                user, 
                originalMessage.getSender(), 
                userAccountService.getSystemUser(),
                message, 
                originalMessage.getSubject());
        
        messageDispatcher.queueMessage(publicMessage);
        
        postsService.notifyParticipantsOfNewDiscussionPost(publicMessage.getReceipt());
        
        modelMap.addAttribute("message", publicMessage.getReceipt());
        modelMap.addAttribute("ajaxurl", "");
        modelMap.addAttribute("domattachid", "attachhere");
        modelMap.addAttribute("optionedit", "Y");
        
        return "/ajax/messages_update";
    }

    @RequestMapping(value = "public/view/{id}/edit", method = RequestMethod.POST)
    public @ResponseBody Notification discussionEditAjax(@RequestParam(value = "messageid", required = true) Long id, 
                                                         @RequestParam(required = true) String message,
                                                         ModelMap modelMap) {      
        
        TBLUser user = sessionUserService.getUser();

       try {
            
            TBLMessageDiscussionPublic originalMessage = 
                    (TBLMessageDiscussionPublic) securityService.checkEntityOwner(id, TBLMessageDiscussionPublic.class, user);
            
            originalMessage.setMessage(message);         
            crudDAO.update(originalMessage);
        }
        catch (SecurityException e) {
            return new Notification("Error", "Error", NotificationTypes.error);
        }

        return new Notification("Post Updated", "Your post was updated.");
    }
    
    @RequestMapping(value ="public/view/{id}/remove", method = RequestMethod.POST)
    public @ResponseBody Notification discussionRemoveAjax(HttpSession httpSession,
                                                                       ModelMap modelMap,
                                                                       @RequestParam long id) {        
        return postsService.deletePost(id,TBLMessageDiscussionPublic.class,sessionUserService.getUser());
    }
    
    @RequestMapping(value={ "public/view/{id}/flag", "public/flag" }, method=RequestMethod.POST)
    public @ResponseBody Notification publicDiscussionMessageFlag(@RequestParam Long id,
                                                                  @RequestParam (required = true, value = "data" ) FlagTypes flagType,
                                                                  ModelMap modelMap,
                                                                  HttpSession httpSession) {
            
        return postsService.flagPost(TBLMessageDiscussionPublic.class, id, sessionUserService.getUser(), flagType);       
    }
    
    @RequestMapping(value={"public/view/{id}/bookmark", "public/bookmark"}, method=RequestMethod.POST)
    public @ResponseBody Notification publicDiscussionBookmark(@RequestParam Long id,
                                                               ModelMap modelMap,                                                         
                                                               HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();
        
        TBLDiscussionPublic discussion = (TBLDiscussionPublic) crudDAO.getById(TBLMessageDiscussionPublic.class, id).getParent();
        
        user.addToBookmarkedDiscussions(discussion);
       
        crudDAO.update(user);
        
        discussion.addParticipant(user);
        
        crudDAO.update(discussion);
        
        return new Notification("Bookmarked", stringTemplateService.getTemplateNotification("discussion_bookmarked").add("discussion_title", discussion.getTitle()).render());
    }
    
    @RequestMapping(value="public/bookmark/delete", method=RequestMethod.POST)
    public String adBookmarkDelete(@RequestParam Long id,
                                    ModelMap modelMap,                                                         
                                    HttpSession httpSession)  {
        
        TBLUser user = sessionUserService.getUser();
        
        TBLDiscussionPublic discussion = crudDAO.getById(TBLDiscussionPublic.class, id);
        
        user.removeUserBookmarkedDiscussion(discussion);              
        
        crudDAO.update(user);
        
        discussion.removeParticipant(user);
        
        crudDAO.update(discussion);
        
        return "/ajax/no_data";
    }
}
