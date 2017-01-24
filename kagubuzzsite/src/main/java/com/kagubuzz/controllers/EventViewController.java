package com.kagubuzz.controllers;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

import com.kagubuzz.database.dao.BallotDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessageEventComment;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguTextFormatter;

import edu.emory.mathcs.backport.java.util.Collections;

@Controller
@RequestMapping(value ="/event")
public class EventViewController {
    
	@Autowired
	EventDAO eventDAO;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	CategoryDAO categoryDAO;
	
	@Autowired 
	CRUDDAO crudDAO;
	
	@Autowired 
    FileService fileService;
	
	@Autowired
	NotificationCache notificationCache;
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	BallotDAO ballotDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	KaguLocationDAO kaguLocationDAO;
	
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired
    PostsService postsService;
    
    @Autowired
    UserAccountService userAccountService;
    
    static Logger logger = Logger.getLogger(EventViewController.class); 
    
	@InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	@RequestMapping(value="view/{id}/{seoFriendlyURL}", method = RequestMethod.GET)
	public String eventView(@PathVariable Long id,
                			@RequestParam(required = false) Long scroll_id,
                			@RequestParam(value = "newpost", required = false, defaultValue ="false") boolean newpost,
                			ModelMap modelMap) 
	{	
		 TBLUser user = sessionUserService.getUser();
		 
		 if(newpost && user.isLoggedIn()) {
		     notificationCache.add(new Notification("Shiney New Event Posted!", stringTemplateService.getTemplateNotification("event_new_posted").render()), user);
		 }
		
		TBLEvent event = crudDAO.getById(TBLEvent.class, id);
		
		if(event == null) {
	            modelMap.addAttribute("error_page_message", Messages.getString("EventViewController.0"));
	            modelMap.addAttribute("error_page_link", "/events/browse");  
	            modelMap.addAttribute("error_page_button", "Other Cool Events...");
	            logger.warn("In eventView,  Edit event does not exist");
		    return "/jsp/error"; 
		}
		
		postsService.setAllOwnerPostNotificationsToRead(event,  event.getComments(), user);
		
		if(!event.isActive()) {
            modelMap.addAttribute("error_page_message", Messages.getString("EventViewController.0"));
            modelMap.addAttribute("error_page_link", "/events/search?keywords="+ event.getTitle());  
            modelMap.addAttribute("error_page_button", "Find Similar Events");  
            logger.warn("In eventView,  Edit event is not active");
            return "/jsp/error"; 
        }
		
		modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(event.getCategory(), false)); 
		modelMap.addAttribute("event", event); 
        modelMap.addAttribute("backgroungImageURL", eventService.getBackgroundImageURL(event));               
        modelMap.addAttribute("sideBarImageURL", eventService.getSidebarImageURL(event));        
        modelMap.addAttribute("scroll_id", scroll_id);        
         
        if(event.getBallot() != null) {
            Set<TBLMessageUserEventFeedback> eventFeedback = event.getBallot().getEventRatings();
            postsService.setAllOwnerPostNotificationsToRead(event, event.getBallot().getEventRatings(), user);
            modelMap.addAttribute("feedback", eventFeedback);  
        }     

        postsService.setAllOwnerPostNotificationsToRead(event, event.getComments(), user);
        
        List<TBLMessageEventComment> eventComments = event.getComments();                 
        Collections.reverse(eventComments);              
        modelMap.addAttribute("comments", eventComments); 
        
        if(user.isLoggedIn()) {
            modelMap.addAttribute("following", user.getFollowing().contains(event.getOwner())); 
        }
 
        if(event.getEventCycle() != EventPeriod.Once && user.getTimeZoneOffset() != null) {
            modelMap.addAttribute("repeats", event.getEventCycle().getFormattedRepeatString(new KaguTextFormatter().gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset()))); 
        }
        
        modelMap.addAttribute("event_rating", ballotDAO.getBallotEventRating(event.getBallot())); 
        modelMap.addAttribute("rating_count", ballotDAO.getBallotEventRatingCount(event.getBallot())); 
        
        userAccountService.injectUserProfile(modelMap, user, event.getOwner());
        
		return "/jsp/event_view"; 
	}
	
	@RequestMapping(value="flag", method=RequestMethod.POST)
    public @ResponseBody Notification flagEventAjax(@RequestParam Long id,
                                                     @RequestParam (required = true, value = "data" ) FlagTypes flagType,
                                                     ModelMap modelMap,
                                                     HttpSession httpSession) {
	    
	    return postsService.flagPost(TBLEvent.class, id, sessionUserService.getUser(), flagType);	    
    }
	
	@RequestMapping(value="comment/flag", method=RequestMethod.POST)
	public @ResponseBody Notification flaEventComment(@RequestParam Long id,
	                                                  @RequestParam (required = true, value = "data" ) FlagTypes flagType,
	                                                  ModelMap modelMap,
	                                                  HttpSession httpSession) {
	        
	    return postsService.flagPost(TBLMessageEventComment.class, id, sessionUserService.getUser(), flagType);       
	}
	   
	@RequestMapping(value="rate", method=RequestMethod.POST)
    public @ResponseBody Notification eventRateAjax(@RequestParam ("parentid") Long id,
                                                    String message,
                                                    @RequestParam ("userdata") Float rating,
                                                    ModelMap modelMap,
                                                    HttpSession httpSession) {    
	    if(rating.longValue() <= 0) {
	        return new Notification("Error", "Error", NotificationTypes.error);  
	    }
	    
	     TBLUser user = sessionUserService.getUser();        
	    
	    eventService.rateEvent(id, rating, user, message);
	    
	    return new Notification(stringTemplateService.getTemplateNotification("post_rating_saved").add("post_type", "event"));   
    }
	
	@RequestMapping(value="comment", method=RequestMethod.POST)
	public String commentOnPostAjax(@RequestParam ("parentid") long id,
				    			    @RequestParam String message,
				    			    ModelMap modelMap,
				    			    HttpSession httpSession) 
	{	
		 TBLUser user = sessionUserService.getUser();				
		
	      TBLEvent event = crudDAO.getById(TBLEvent.class, id); 
		
		TBLMessageEventComment comment = new TBLMessageEventComment();
		
		comment.setSubject(event.getTitle());
		comment.setCreatedDate(new Date());
		comment.setMessage(message);
		comment.setIsPrivate(false);
		comment.setSender(user);
		comment.setRecipient(event.getOwner());
		comment.setEvent(event);
		
		crudDAO.create(comment);
		
	    notificationCache.add(comment);
		
		modelMap.addAttribute("message", comment); 
		modelMap.addAttribute("ajaxurl", "/event/comment/");  
		modelMap.addAttribute("domattachid", "comments");  
		modelMap.addAttribute("option_remove", true);
		
		return "/ajax/messages_update"; 
	}
	
    @RequestMapping(value = "discuss/{id}", method = RequestMethod.GET)
    public String discussionFromPost(@PathVariable Long id, ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();
        boolean newPost = false;

        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        TBLDiscussionPublic publicDiscussion = event.getEventDiscussion();
        
        if (publicDiscussion == null) {
            newPost = true;
            publicDiscussion = eventService.attachDiscussionToEvent(event, user);
        }

        modelMap.addAttribute("discussion", publicDiscussion);
        modelMap.addAttribute("new_post", newPost);
        modelMap.addAttribute("firstmessage", publicDiscussion.getFirstMessageInThread());
        modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(publicDiscussion.getCategory(), false));

        return String.format("redirect:/discussion/public/view/" + publicDiscussion.getId() +"/" + publicDiscussion.getFriendlyURL());
    }
    
    @RequestMapping(value="bookmark", method=RequestMethod.POST)
    public @ResponseBody Notification updateBookmarks(@RequestParam long id,
                                                                  ModelMap modelMap,                                                            
                                                                  HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();
        
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        user.addToBookmarkedEvents(event);
        
        crudDAO.update(user);
        
        return new Notification("Bookmarked", stringTemplateService.getTemplateNotification("event_bookmarked").add("event_title", event.getTitle()).render());
    }
    
    @RequestMapping(value="bookmark/delete", method=RequestMethod.POST)
    public String eventBookmarkDelete(@RequestParam long id,
                                      ModelMap modelMap,                                                         
                                      HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();
        
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        user.removeUserBookmarkedEvent(event);  
        
        crudDAO.update(user);
        
        return "/ajax/no_data";
    }
}
