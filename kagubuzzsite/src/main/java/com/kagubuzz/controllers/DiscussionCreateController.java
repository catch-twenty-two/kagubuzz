package com.kagubuzz.controllers;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.DiscussionDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.TagDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionPublicTag;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.discussions.PublicDiscussionMessage;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

@Controller
@RequestMapping("/discussion")
public class DiscussionCreateController {
	
	@Autowired
	UserDAO  userDAO;
	@Autowired
	EventDAO eventDAO;
	@Autowired
	MessageDAO messageDAO;
	@Autowired
	DiscussionDAO discussionDAO;
	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	CRUDDAO crudDAO;
	@Autowired
	EventService eventService;
    @Autowired
    UserAccountService userAccountService;   
    @Autowired
    SecurityService securityService;
    @Autowired
    MessageDispatcher messageDispatcher;
	@Autowired
	TagDAO tagDAO;
    @Autowired
    UserSessionService sessionUserService;
    @Autowired
    PostsService postsService;
    
    static Logger logger = Logger.getLogger(DiscussionCreateController.class);  
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	@RequestMapping(value ="create", method = RequestMethod.GET)
	public String addDiscussion(@RequestParam(required = false) Long id,	                            
	                            ModelMap modelMap) {
	    		
        if(id != null) { 
            modelMap.addAttribute("discussion", crudDAO.getById(TBLDiscussionPublic.class, id));
        }
        
        List<LSTCategoryBase> list = categoryDAO.getAllDescendantsForCategory(categoryDAO.getCategoryByName("Discussions", LSTDiscussionCategory.class), true);
        
        modelMap.addAttribute("discussion_categories", list);
        
		return "/jsp/discussion_create";
	}
	
    @RequestMapping(value ="post", method = RequestMethod.POST)
    public String discussionReview(ModelMap modelMap,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = true, value="discussion_category_id") Long discussionCategoryId,
                                   @RequestParam(required = true) String message,
                                   @RequestParam(required = true) String subject,
                                   @RequestParam(required = false, value = "post_tags") String tags) {
        
        TBLUser user = sessionUserService.getUser();

        TBLDiscussionPublic publicDisucssion = null;
        
        try {
            
            if(id == null) {
                publicDisucssion = new TBLDiscussionPublic();
            } else {
                publicDisucssion = (TBLDiscussionPublic) securityService.checkEntityOwner(id, TBLDiscussionPublic.class, user);
            }  
            
        } catch (SecurityException e) {
            logger.warn(e);
            return "/jsp/error";
        }
        
        if(tags != null) {
            
            String tagsParsed[] = tags.split(",");
        
            for(String tag: tagsParsed) {   
                publicDisucssion.addTag((LSTDiscussionPublicTag) tagDAO.getTagByName(tag, LSTDiscussionPublicTag.class));
            }
        }
        
        LSTCategoryBase category = crudDAO.getById(LSTDiscussionCategory.class, discussionCategoryId);
        
        publicDisucssion.setCategory((LSTDiscussionCategory) category);
        publicDisucssion.setCreatedDate(new Date());
        
        PublicDiscussionMessage publicMessage = new PublicDiscussionMessage(publicDisucssion, 
                                                                            user, 
                                                                            userAccountService.getSystemUser(), 
                                                                            userAccountService.getSystemUser(),
                                                                            message, 
                                                                            subject);
        publicDisucssion.setActive(true);
        publicDisucssion.setSticky(SpringSecurityUtilities.isAdmin(user));
        
        messageDispatcher.queueMessage(publicMessage);
        
        postsService.notifyFollowersOfNewPost(publicDisucssion);
        
        modelMap.addAttribute("new_post", true);
        modelMap.addAttribute("discussion", publicDisucssion);
        
        return "/jsp/discussion_view";
    }
}
