package com.kagubuzz.controllers;

import javax.servlet.http.HttpSession;

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
import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.PostsDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;

@Controller
@RequestMapping(value="/events/manage")
public class EventsManageController  {
    
	@Autowired
	AdDAO adDao;
	
	@Autowired
	EventDAO eventDao;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	CRUDDAO crudDAO;
	
    @Autowired
    SecurityService securityService;
    
    @Autowired
    EventService eventService;
    
    @Autowired
    FileService fileservice;
    
    @Autowired 
    UserDAO userDAO;
    
    @Autowired
    SearchService searchService;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    AdService adService;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    PostsDAO postsDAO;
    
    @Autowired
    PostsService postsService;
    
    static Logger logger = Logger.getLogger(EventsManageController.class);  
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	@RequestMapping(value ="remove", method = RequestMethod.GET)
	public String  eventRemove(HttpSession httpSession,
									   ModelMap modelMap,
									   @RequestParam long id) {	
	    TBLUser user = sessionUserService.getUser();
		
        securityService.checkEntityOwner(id, TBLEvent.class, user);
		eventService.removeEvent(id);
		
        return "redirect:/events/manage/browse"; 
	}
	
	@RequestMapping(value ="activate", method = RequestMethod.GET)
    public String  eventActivate(HttpSession httpSession,
                               ModelMap modelMap,
                               @RequestParam long id) {
	    
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        if(event.isExpired()) {
            modelMap.addAttribute("error", Messages.getString("PostsManageController.4")); 
            logger.warn("In eventActivate Edit event is expired");
            return "/jsp/error"; 
        }
       
        eventService.renewEvent(event);
        
        return "redirect:/events/manage/browse"; 
    }
	
    @RequestMapping(value = "duplicate", method = RequestMethod.GET)
    public String eventDuplicate(HttpSession httpSession, ModelMap modelMap, @RequestParam long id) {
        
        TBLUser user = sessionUserService.getUser();

        TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);

        if (event == null) {
            logger.warn("In eventDuplicate event does not exist");
            return "/jsp/error"; 
        }
        eventService.duplicateEvent(event);

        return "redirect:/events/manage/browse"; 
    }
 
    
	@RequestMapping(value ="delete", method = RequestMethod.GET)
    public String eventDelete(HttpSession httpSession,
                              ModelMap modelMap,
                              @RequestParam long id) {   
	    
	    TBLUser user = sessionUserService.getUser();

        TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);
        
        if(event == null) {
            logger.warn("In eventDelete event does not exist");
            return "/jsp/error"; 
        }
        
        eventService.deleteEvent(event);
        
        return "redirect:/events/manage/browse"; 
    }
	
    @RequestMapping(value = "autorenew", method = RequestMethod.GET)
    public String eventAutoRenew(@RequestParam(required = true) Long id,
                             HttpSession httpSession, 
                             ModelMap modelMap) {     
        
        TBLUser user = sessionUserService.getUser();
        
        TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);
        
        if(event == null) {
            return "/jsp/error"; 
        }
               
        SystemMessage message = new SystemMessage(user, userAccountService.getSystemUser());
        message.postRenewed(event);
        messageDispatcher.queueMessage(message);
        
        return "redirect:/home/browse"; 
    }
	   
    @RequestMapping(value = "renew", method = RequestMethod.GET)
    public String eventRenew(@RequestParam(required = true) Long id,
                                 HttpSession httpSession, 
                                 ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();
        
        TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);
        
        if(event == null) {
            logger.warn("In eventRenew event does not exist");
            return "/jsp/error"; 
        }
        
        eventService.renewEvent(event);
        
        return "redirect:/events/manage/browse"; 
    }
	
	@RequestMapping(value ="browse", method = RequestMethod.GET)
    public String eventsManageController(@RequestParam(required = false, defaultValue="0" ) Long page,
                                         HttpSession httpSession,
                                         ModelMap modelMap) {
       
       TBLUser user = sessionUserService.getUser();
        
        Pagination<TBLEvent> userEventsPagination = postsDAO.getPostsForUser(page, user, TBLEvent.class);
        
        userEventsPagination.setModel(modelMap);
        modelMap.addAttribute("events", userEventsPagination.getList()); 
        modelMap.addAttribute("event_service", eventService);
        modelMap.addAttribute("userdirectory", fileservice.getUserPublicDirectoryURL(user, UserSubfolder.Images)); 
        
        return "/jsp/events_manage"; 
    }
   
   @RequestMapping(value ="search", method = RequestMethod.GET)
   public String eventsManageSearch(@RequestParam(required = false, defaultValue="0" ) Long page,
                                    ModelMap modelMap,
                                    @RequestParam(required = false) Boolean expired,
                                    @RequestParam(required = false) Boolean active,
                                    @RequestParam(required = false) String title) {
       
      TBLUser user = sessionUserService.getUser();     
      
     Pagination<TBLEvent> pagination =  searchService.searchEvents(title, 
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   (active != null) ? false : null, 
                                                                   user,
                                                                   null,
                                                                   null,
                                                                   false,                                                                   
                                                                   page);
     
     if(active != null) {
         modelMap.addAttribute("not_active", "checked");
     }

     pagination.addQueryOption("expired",  expired);
     pagination.addQueryOption("title",  title);
     pagination.addQueryOption("active",  active);    
     pagination.setModel(modelMap); 
     
     modelMap.addAttribute("events", pagination.getList());
     modelMap.addAttribute("userdirectory", fileservice.getUserPublicDirectoryURL(user, UserSubfolder.Images));     
       
     return "/jsp/events_manage"; 
   }
}
