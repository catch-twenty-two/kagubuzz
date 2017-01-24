package com.kagubuzz.controllers;

import java.util.Calendar;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventDuration;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionPublicTag;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.LSTTag;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;

@Controller
public class EventCreateController {
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    EventDAO eventDAO;

    @Autowired
    EventService eventService;

    @Autowired
    CRUDDAO crudDAO;

    @Autowired
    CategoryDAO categoryDAO;
    
    @Autowired
    SecurityService securityService;

    @Autowired 
    FileService fileService;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    PostsService postsService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired    
    SearchService searchService;
    
    static Logger logger = Logger.getLogger(EventCreateController.class);  
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    TBLEvent getEditingEvent(HttpSession httpSession) {
        return crudDAO.getById(TBLEvent.class, (Long) httpSession.getAttribute("editingevent")); 
    }    
    
    @RequestMapping(value = "/eventedit", method = RequestMethod.GET)
    public String eventAddEdit(@RequestParam(required = true) Long id,
                               HttpSession httpSession, 
                               ModelMap modelMap) {
        
        TBLUser user = userSessionService.getUser(); 
        
        TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);
        
        if(event == null) return "/jsp/error"; 
        
        httpSession.setAttribute("editingevent", event.getId()); 
        
        injectAddEvent(modelMap, httpSession);
        
        modelMap.addAttribute("userdirectory", fileService.getUserPublicDirectoryURL(user, UserSubfolder.Images)); 
        modelMap.addAttribute("backgroundImageURL", eventService.getBackgroundImageURL(event)); 
        modelMap.addAttribute("sideBarImageURL", eventService.getSidebarImageURL(event));         
        
        return "/jsp/event_create"; 
    }
    
    // create a new post
    
    @RequestMapping(value = "/event_create", method = RequestMethod.GET)
    public String eventAddCreate(HttpSession httpSession, ModelMap modelMap) {
        
        TBLUser user = userSessionService.getUser();
        httpSession.removeAttribute("editingevent"); 
        injectAddEvent(modelMap, httpSession);
        Calendar defaultStartDate = user.getCalendarInUserTimeZone();
        
        defaultStartDate.add(Calendar.DAY_OF_MONTH, 1);
        defaultStartDate.set(Calendar.MINUTE, 0);
        
        modelMap.addAttribute("default_date",defaultStartDate.getTime()); 
        
        return "/jsp/event_create"; 
    }
    
    // edit an existing post

    @RequestMapping(value = "/event_create", method = RequestMethod.POST)
    public String eventAddUpdate(HttpSession httpSession, 
                                 ModelMap modelMap) {
        
        injectAddEvent(modelMap, httpSession);
        
        TBLEvent event = getEditingEvent(httpSession);
        
        modelMap.addAttribute("backgroundImageURL", eventService.getBackgroundImageURL(event)); 
        modelMap.addAttribute("sideBarImageURL", eventService.getSidebarImageURL(event));         
        
        return "/jsp/event_create"; 
    }

    private void injectAddEvent(ModelMap modelMap, HttpSession httpSession) {

        List<LSTCategoryBase> categoryList = categoryDAO.getAllDescendantsForCategory(categoryDAO.getCategoryByName("Events", LSTEventCategory.class), true);

        modelMap.addAttribute("eventcategories", categoryList); 

        modelMap.addAttribute("eventperiods", EventPeriod.values()); 
        modelMap.addAttribute("ages", EventAgeAppropriate.values()); 
        modelMap.addAttribute("eventvenues", EventVenue.values()); 
        modelMap.addAttribute("eventdurations", EventDuration.values()); 
        
        modelMap.addAttribute("event", getEditingEvent(httpSession)); 
        modelMap.addAttribute("template_service", stringTemplateService);
    }

    @RequestMapping(value = "/eventpost", method = RequestMethod.POST)
    public String eventPostNewEvent(HttpSession httpSession, ModelMap modelMap) {

        TBLEvent editingEvent = getEditingEvent(httpSession);
        
        if(editingEvent == null) {
            logger.warn("In eventPostNewEvent,  Edit event does not exist");
            return "/jsp/error"; 
        }
        
        if(editingEvent.isExpired()) {
            modelMap.addAttribute("error", Messages.getString("EventAddController.18"));  //$NON-NLS-2$
            logger.warn("Edit event is expired");
            return "/jsp/error"; 
        }
        
        eventService.renewEvent(editingEvent);

        httpSession.removeAttribute("editingevent"); 
        
        postsService.notifyFollowersOfNewPost(editingEvent);
        modelMap.addAttribute("newpost", true);

        return "redirect:/event/view/"+ editingEvent.getId() + "/" + editingEvent.getFriendlyURL();  //$NON-NLS-2$
    }
    

    @RequestMapping(value = "/eventsavedraft", method = RequestMethod.POST)
    public @ResponseBody Notification eventSaveDraftAjax(
            @RequestParam(required = true) String title,
            @RequestParam(required = true) long eventCategory,
            @RequestParam(required = true) String startDate,
            @RequestParam(required = true) String startTime,
            @RequestParam(required = true) String description,
            @RequestParam(required = true) String duration,
            // optional parameters
            @RequestParam(value = "eventsidebarimage", required = false) String sideBarImage,
            @RequestParam(value = "eventbackgroundimage", required = false) String backgroundImage,
            @RequestParam(value = "price", required = false) String price, 
            @RequestParam(value = "age", required = false) String ages,
            @RequestParam(value = "venue", required = false) String venue, 
            @RequestParam(value = "repeats", required = false) String repeatsEvery,
            @RequestParam(value = "address", required = false) String address, 
            @RequestParam(value = "kw1", defaultValue ="", required = false) String keyWord1,
            @RequestParam(value = "kw2", defaultValue ="", required = false) String keyWord2, 
            @RequestParam(value = "kw3", defaultValue ="", required = false) String keyWord3,
            @RequestParam(value = "kw4", defaultValue ="", required = false) String keyWord4, 
            @RequestParam(value = "kw5", defaultValue ="", required = false) String keyWord5,
            HttpSession httpSession, 
            ModelMap modelMap) {
        
        TBLUser user = userSessionService.getUser();
        
            saveEvent(title, 
                    false,
                    eventCategory, 
                    startDate, 
                    startTime, 
                    description, 
                    duration, 
                    sideBarImage, 
                    backgroundImage, 
                    price, 
                    ages, 
                    venue,
                    repeatsEvery, 
                    address, 
                    keyWord1, 
                    keyWord2, 
                    keyWord3, 
                    keyWord4, 
                    keyWord5, 
                    user,
                    httpSession);
        
        return new Notification( Messages.getString("EventAddController.0"), Messages.getString("EventAddController.23"));  //$NON-NLS-2$
    }

    
    @RequestMapping(value = "/eventreview", method = RequestMethod.POST)
    public String reviewEventPost(
            @RequestParam(required = true) String title,
            @RequestParam(required = true) long eventCategory,
            @RequestParam(required = true) String startDate,
            @RequestParam(required = true) String startTime,
            @RequestParam(required = true) String description,
            @RequestParam(required = true) String duration,
            // optional parameters
            @RequestParam(value = "eventsidebarimage", required = false) String sideBarImage,
            @RequestParam(value = "eventbackgroundimage", required = false) String backgroundImage,
            @RequestParam(value = "price", required = false) String price, 
            @RequestParam(value = "age", required = false) String age,
            @RequestParam(value = "venue", required = false) String venue, 
            @RequestParam(value = "repeats", required = false) String repeatsEvery,
            @RequestParam(value = "address", required = false) String address, 
            @RequestParam(value = "kw1", defaultValue ="", required = false) String keyWord1,
            @RequestParam(value = "kw2", defaultValue ="", required = false) String keyWord2, 
            @RequestParam(value = "kw3", defaultValue ="", required = false) String keyWord3,
            @RequestParam(value = "kw4", defaultValue ="", required = false) String keyWord4, 
            @RequestParam(value = "kw5", defaultValue ="", required = false) String keyWord5,
            HttpSession httpSession, 
            ModelMap modelMap) {
        
        TBLUser user = userSessionService.getUser();

        TBLEvent event = saveEvent(title, 
                                   false,
                                   eventCategory, 
                                   startDate, 
                                   startTime, 
                                   description, 
                                   duration, 
                                   sideBarImage, 
                                   backgroundImage, 
                                   price, 
                                   age, 
                                   venue,
                                   repeatsEvery, 
                                   address, 
                                   keyWord1, 
                                   keyWord2, 
                                   keyWord3, 
                                   keyWord4, 
                                   keyWord5, 
                                   user,
                                   httpSession);

        modelMap.addAttribute("event", event); 
        modelMap.addAttribute("addevent", Boolean.TRUE); 
        modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(event.getCategory(), false)); 
        
        modelMap.addAttribute("backgroungImageURL", eventService.getBackgroundImageURL(event));               
        modelMap.addAttribute("sideBarImageURL", eventService.getSidebarImageURL(event)); 
        modelMap.addAttribute("save_lat_long_url", "save_geo_coors");
        
        if(event.getEventCycle() != EventPeriod.Once) {
            modelMap.addAttribute("repeats", event.getEventCycle()
                    .getFormattedRepeatString(new KaguTextFormatter()
                    .gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset()))); 
        }

        userAccountService.injectUserProfile(modelMap, null, user);
        
        return "/jsp/event_view"; 
    }
    
    @RequestMapping(value="save_geo_coors", method=RequestMethod.POST)
    public String adSaveGeoCooridnates(@RequestParam long id,
                                       @RequestParam Double longitude,
                                       @RequestParam Double latitude,
                                       ModelMap modelMap) {

       TBLUser user = userSessionService.getUser();

       TBLEvent event = (TBLEvent) securityService.checkEntityOwner(id, TBLEvent.class, user);

       postsService.setGeographicalCoordinates(longitude, latitude, event);

       return "/ajax/blank";
   }
    @RequestMapping(value = "/event/similar", method = RequestMethod.GET)
    public @ResponseBody List<String> eventsGetSimilar(@RequestParam String query, 
                                                              ModelMap modelMap, 
                                                              HttpSession httpSession) {

        List<TBLEvent> eventList = searchService.searchEvents(query, userSessionService.getUser().getTblKaguLocation(), 30); 
        
        return eventService.getEventListAsStringList(eventList);
    }
    
    private TBLEvent saveEvent(String title,
                               Boolean active,
                               long eventCategory, 
                               String startDate, 
                               String startTime, 
                               String description, 
                               String duration, 
                               String sideBarImage,
                               String backgroundImage, 
                               String price, 
                               String ages, 
                               String takesPlace, 
                               String repeatsEvery, 
                               String address, 
                               String keyWord1, 
                               String keyWord2,
                               String keyWord3, 
                               String keyWord4, 
                               String keyWord5,
                               TBLUser user,
                               HttpSession httpSession) {
        
        String keywords = String.format("%s %s %s %s %s", keyWord1, keyWord2, keyWord3, keyWord4, keyWord5); 

        EventAgeAppropriate ageEnum = null;
        EventVenue venueEnum = null;
        EventPeriod periodEnum = null;
        EventDuration durationEnum = null;
        
        
        if (ages != null) {
            ageEnum = EventAgeAppropriate.valueOf(ages);
        } 
            
        if (takesPlace != null)
        {
           venueEnum = EventVenue.valueOf(takesPlace);
        } 
        
        if (takesPlace != null) {
            venueEnum = EventVenue.valueOf(takesPlace);
        }

        if (repeatsEvery != null) {
            periodEnum = EventPeriod.valueOf(repeatsEvery);
        }

        if (repeatsEvery != null) {
            durationEnum = EventDuration.valueOf(duration);
        }
        
        // Anything other than a number or 0 is considered free
        
        if(KaguTextFormatter.isNumeric(price)) {
            if(Double.parseDouble(price) == 0) {
                price = null;
            }
        } else {
            price = null;
        }
        
        TBLEvent event = eventService.createOrUpdateEvent(getEditingEvent(httpSession), active,
                title, 
                startDate + startTime, 
                description, 
                sideBarImage,
                backgroundImage, 
                price, 
                ageEnum, 
                venueEnum, 
                periodEnum, 
                durationEnum, 
                address, 
                keywords, 
                user, 
                crudDAO.getById(LSTEventCategory.class, eventCategory));

        httpSession.setAttribute("editingevent", event.getId()); 
        return event;
    }
}
