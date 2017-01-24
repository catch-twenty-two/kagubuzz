package com.kagubuzz.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.kagubuzz.database.dao.DiscussionDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.AdBookmark;
import com.kagubuzz.datamodels.Bookmark;
import com.kagubuzz.datamodels.PublicDiscussionBookmark;
import com.kagubuzz.datamodels.Weather;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.enums.NotificationGroups;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.EventSuggester;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.weather.WeatherReport;

import edu.emory.mathcs.backport.java.util.Collections;

@Controller
@RequestMapping(value = "/home")
public class HomeController { 
	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	UserDAO  userDAO;
	@Autowired
	EventDAO eventDAO;
	@Autowired
	MessageDAO messageDAO;
	@Autowired
	DiscussionDAO discussionDAO;
    @Autowired
    CRUDDAO crudDAO;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserSessionService sessionUserService;
    @Autowired
    AdDAO adDAO;
    
    @Autowired
    StringTemplateService stringTemplateService;

    
    final int FOLLOWERS_TO_DISPLAY = 7;
    final int INBOX_MESSAGES_TO_DISPLAY = 40;
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
     
	@RequestMapping(value = "browse", method = RequestMethod.GET)
	public String home(HttpServletRequest request,
	                   ModelMap modelMap,
	                   @RequestParam(required = false, defaultValue = "false", value="new_account") boolean newAccount) {
		
	    TBLUser user = sessionUserService.getUser();
	    
	    // record user ip address
	    
	    userAccountService.saveIPToUser(request.getRemoteAddr(), user);
		
		// Add weather report
		
		WeatherReport weatherReport = sessionUserService.getWeatherReport();
		
		if(weatherReport !=null ) {
		    
    		modelMap.addAttribute("currentWeather", weatherReport.getCurrentConditions());
    		
    		List<Weather> forecast = weatherReport.getForecast();
    		
    		modelMap.addAttribute("tomorrowforecast", forecast.get(1));
    		modelMap.addAttribute("todayforecast", forecast.get(0));
    		modelMap.addAttribute("location", user.getTblKaguLocation());
    		
    		EventSuggester eventSuggester = new EventSuggester(weatherReport, user);
            
            modelMap.addAttribute("eventsuggestion",eventSuggester.getClimateOutsideMessage());
            
            modelMap.addAttribute("suggested_events", eventDAO.getSuggestedEventsForUser(user, eventSuggester.getEvaluatedLocation()));
		}
		
		// TODO: This needs to also get, ads and do it categories
		
		List<Bookmark> bookmarks = userDAO.getUserRatedAndBookMarkedEvents(user);
	    
		for(TBLAd ad : user.getBookmarkedAds()) {
		    bookmarks.add(new AdBookmark(ad));
		}
		
		for(TBLDiscussionPublic discussion : user.getBookmarkedDiscussions()) {
            bookmarks.add(new PublicDiscussionBookmark(discussion));
        }
		
		bookmarks.addAll(user.getRoleBuyerTransactions());
		
		Collections.sort(bookmarks);
		
		if(bookmarks.size() > 0) {
		    modelMap.addAttribute("dateFloor", bookmarks.get(0).bookmarkRelevantDate());
		    modelMap.addAttribute("dateCeiling", bookmarks.get(bookmarks.size() - 1).bookmarkRelevantDate());
		}
		
		modelMap.addAttribute("bookmarks", bookmarks);
		
		// Add following users
		
		List<TBLUser> following = new ArrayList<TBLUser>(user.getFollowing());

		modelMap.addAttribute("following_users", following.subList(0, (following.size() >= FOLLOWERS_TO_DISPLAY) ? FOLLOWERS_TO_DISPLAY : following.size()));
		
		modelMap.addAttribute("user_overflow", ((following.size() - FOLLOWERS_TO_DISPLAY) > 0) ? following.size() - FOLLOWERS_TO_DISPLAY : null);
		
		// Add message activity
		
		List<TBLMessage>  messageList = messageDAO.getUnreadMessagesInGroupForUser(sessionUserService.getUser(),
                                                                                   NotificationGroups.Everything, 
                                                                                   INBOX_MESSAGES_TO_DISPLAY, 
                                                                                   0);
        if(messageList.size() == INBOX_MESSAGES_TO_DISPLAY) {  
            modelMap.addAttribute("show_next", true);
        }
        
        modelMap.addAttribute("next_page", 1);
        modelMap.addAttribute("group", NotificationGroups.Everything);
		modelMap.addAttribute("inboxmessages", messageList);
		
		int adSearchHits = 0;
		
		//TODO: Optimize SQL
		
		if(user.getSearchableKeyWordList() != null) {
		    adSearchHits = user.getSearchableKeyWordList().getAutoSearchAds().size();
		}
		
		modelMap.addAttribute("adsearchhits", adSearchHits);
		
        int eventSearchHits = 0;

      //TODO: Optimize SQL
        
        if (user.getSearchableKeyWordList() != null) {
            eventSearchHits = user.getSearchableKeyWordList().getAutoSearchEvents().size();
        }

        modelMap.addAttribute("eventsearchhits", eventSearchHits);
		
		// Load local discussions
        
		List<TBLDiscussionPublic> discussionList = discussionDAO.getLatestPublicDiscussionTrends(10);
		
		modelMap.addAttribute("discussionList",discussionList);		
		
		modelMap.addAttribute("ad_offers", adDAO.getOffersForActiveAds(user));
		
		if(!newAccount) {
		    modelMap.addAttribute("welcome_message"," Back ");
		}
		else {
		    modelMap.addAttribute("new_account", newAccount);
		}

		modelMap.addAttribute("system_user", userAccountService.getSystemUser());
		modelMap.addAttribute("notifcation_types", NotificationGroups.values());
		modelMap.addAttribute("template_service", stringTemplateService);
        
		return "/jsp/home";
	}
	
    @RequestMapping(value="close_notification", method=RequestMethod.POST)
    public String markMessageReadAndLoadMoreAjax(@RequestParam long id,
                                                 @RequestParam MessageType mType,
                                                 ModelMap modelMap) 
    {
        messageDAO.markMessageReadAndDelete(id, mType.getClassType());
        
        List<TBLMessage> messages = messageDAO.getUnreadMessagesInGroupForUser(sessionUserService.getUser(), 
                                                                               mType.getGroup(), 
                                                                               1, 
                                                                               INBOX_MESSAGES_TO_DISPLAY - 1); 
        
        if(messages.size() == 0) {
            return "/ajax/no_data";
        }

        modelMap.addAttribute("system_user", userAccountService.getSystemUser());
        modelMap.addAttribute("inboxmessages", messages);
   
        return "/ajax/messages_notifications_update";        
    }
    
    
    @RequestMapping(value="reload_notifications", method=RequestMethod.GET)
    public String loadLatestestMessagesAjax(ModelMap modelMap,
                                            @RequestParam(required = false) NotificationGroups group,
                                            HttpSession httpSession) 
    {   
        List<TBLMessage> messageList =  null;
        
        if(group == NotificationGroups.Everything) {
            messageList =  messageDAO.getUnreadMessagesForUser(sessionUserService.getUser(), INBOX_MESSAGES_TO_DISPLAY,0);
        }
        else {
            messageList = messageDAO.getUnreadMessagesInGroupForUser(sessionUserService.getUser(),
                                                                                  group, 
                                                                                  INBOX_MESSAGES_TO_DISPLAY, 
                                                                                  0);
        }
        
        if(messageList.size() == 0) { 
            return "/ajax/no_data";
        }
        
        
        if(messageList.size() == INBOX_MESSAGES_TO_DISPLAY) {  
            modelMap.addAttribute("show_next", true);
        }
        
        modelMap.addAttribute("next_page", 1);
        modelMap.addAttribute("group", group.name());
        modelMap.addAttribute("system_user", userAccountService.getSystemUser());
        modelMap.addAttribute("inboxmessages",messageList);
        
        return "/ajax/messages_notifications_update";        
    }
    
    @RequestMapping(value="load_more_notifications", method=RequestMethod.GET)
    public String loadMoreMessagesAjax(ModelMap modelMap,
                                       @RequestParam(required = false, value="next_page", defaultValue="0") Integer next_page, 
                                       @RequestParam(required = false, defaultValue="Everything") NotificationGroups group,
                                       HttpSession httpSession) 
    {   
        List<TBLMessage> messageList =  null;
        
        if(group == NotificationGroups.Everything) {
            messageList =  messageDAO.getUnreadMessagesForUser(sessionUserService.getUser(), INBOX_MESSAGES_TO_DISPLAY, INBOX_MESSAGES_TO_DISPLAY * next_page);
        }
        else {
            messageList = messageDAO.getUnreadMessagesInGroupForUser(sessionUserService.getUser(),
                                                                                  group, 
                                                                                  INBOX_MESSAGES_TO_DISPLAY, 
                                                                                  (INBOX_MESSAGES_TO_DISPLAY * next_page) - 1);
        }
        
        if(messageList.size() == 0) { 
            return "/ajax/no_data";
        }
        
        if(messageList.size() == INBOX_MESSAGES_TO_DISPLAY) {  
            modelMap.addAttribute("show_next", true);
        }
        
        modelMap.addAttribute("next_page", next_page + 1);
        modelMap.addAttribute("group", group.name());
        modelMap.addAttribute("system_user", userAccountService.getSystemUser());
        modelMap.addAttribute("inboxmessages",messageList);
        
        return "/ajax/messages_notifications_update";        
    }
}
