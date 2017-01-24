package com.kagubuzz.controllers;

import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.Weather;
import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.enums.SearchRadius;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.weather.WeatherReport;

@Controller
@RequestMapping(value ="/events")
public class EventsBrowseController  {
    
	@Autowired
	EventDAO eventDAO;
	
	@Autowired
	CategoryDAO categoryDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    SearchService searchService;
    
    @Autowired
    UserDAO userDAO;

    @Autowired
    KaguLocationDAO locationDAO;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    EventService eventService;
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value ="/search", method = RequestMethod.GET)
    public String searchEvents(ModelMap modelMap,
                                             @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage,
                                             @RequestParam(required=false) Integer price,
                                             @RequestParam(required=false, value="age_selections") EventAgeAppropriate[] ageSelections,
                                             @RequestParam(required=false) EventVenue[] venue,
                                             @RequestParam(required=false, value = "category_selection") Long categorySelection,
                                             @RequestParam(required=false) Integer radius,
                                             @RequestParam(required=false, value="keywords") String keywords) 
    {
        modelMap.addAttribute("age_appropriate", StringUtils.join(ageSelections, ","));
        modelMap.addAttribute("keywords", keywords);
        modelMap.addAttribute("category_selection", categorySelection);
        modelMap.addAttribute("age_selections", ageSelections);
        modelMap.addAttribute("price", price);
        modelMap.addAttribute("radius_selection", radius);
        modelMap.addAttribute("show_options", true);
        
        LSTEventCategory category = (LSTEventCategory) crudDAO.getById(LSTEventCategory.class, categorySelection);
        
        Pagination<TBLEvent> pagination =  searchService.searchEvents(keywords, 
                                                                     price,
                                                                     sessionUserService.getUser().getTblKaguLocation(),
                                                                     radius,
                                                                     ageSelections,
                                                                     venue,
                                                                     category, 
                                                                     true,
                                                                     null,
                                                                     null,
                                                                     Calendar.getInstance().getTime(),
                                                                     false,
                                                                     currentPage);               
        
        if(categorySelection == null) {
            category = (LSTEventCategory) categoryDAO.getCategoryByName("Events", LSTEventCategory.class); 
        }
        
        modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(category, false));          
        
        modelMap.addAttribute("events", pagination.getList());
        modelMap.addAttribute("events_service", eventService);
        loadEventsBrowseSideBar(modelMap);
        pagination.setModel(modelMap);
        
        return "/jsp/events_browse";
    }

    @RequestMapping(value ="/browse/category/{id}/{seoFriendlyName}", method = RequestMethod.GET)
    public String broseEventsInjection(ModelMap modelMap,
                                       @PathVariable Long id,
                                       @RequestParam(required=false, defaultValue = "30") Integer radius,
                                       @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage) {    
        
        LSTEventCategory category = (LSTEventCategory) crudDAO.getById(LSTEventCategory.class, id); 
        
        loadEventsBrowseMainContent(modelMap, radius, currentPage, category);   
        
        return "/jsp/events_browse";
    }
    
	@RequestMapping(value ="/browse", method = RequestMethod.GET)
	public String broseEventsInjection(ModelMap modelMap,
	                                   @RequestParam(required=false, defaultValue = "30") Integer radius,
									   @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage) {	
	    
	    LSTEventCategory category = (LSTEventCategory) categoryDAO.getCategoryByName("Events", LSTEventCategory.class);
		
		loadEventsBrowseMainContent(modelMap, radius, currentPage, category);
        
		return "/jsp/events_browse";
	}	
    
	
    @RequestMapping(value ="browse/autosearch", method = RequestMethod.GET)
    public String browseAutoSearchAds(ModelMap modelMap, 
                                     @RequestParam(required=false, value="page", defaultValue = "0") Integer currentPage) {
        
        TBLUser user = sessionUserService.getUser();

        Pagination<TBLEvent> pagination = eventDAO.getAutoSearchEvents(user, currentPage);
                
        modelMap.addAttribute("events", pagination.getList());
        modelMap.addAttribute("hidesearchoptions", true);
        
        pagination.setModel(modelMap);
        loadEventsBrowseSideBar(modelMap);
        modelMap.addAttribute("events_service", eventService);
        
        return "/jsp/events_browse";
    }  
    
    public void loadEventsBrowseMainContent(ModelMap modelMap, 
                                            Integer radius, 
                                            Long currentPage, 
                                            LSTEventCategory category) {

            Pagination<TBLEvent> pagination = eventDAO.browseEvents(category, 
                                                     currentPage, 
                                                     true, 
                                                     radius, 
                                                     sessionUserService.getUser().getTblKaguLocation());
    
        modelMap.addAttribute("events_service", eventService);
        modelMap.addAttribute("events", pagination.getList());
        modelMap.addAttribute("main_category", category.getName());
        
        modelMap.addAttribute("radius_selection", radius);

        modelMap.addAttribute("breadcrumbs", categoryDAO.getAllAncestorsForCategory(category, false));

        loadEventsBrowseSideBar(modelMap);
        pagination.setModel(modelMap);
    }
    
    void loadEventsBrowseSideBar(ModelMap model)  {
        
        // Add weather report
        
        WeatherReport weatherReport = sessionUserService.getWeatherReport();
        
       if(weatherReport != null) {
           
           List<Weather> forecast = weatherReport.getForecast();
       
            model.addAttribute("currentWeather", weatherReport.getCurrentConditions());       
            model.addAttribute("tomorrowforecast", forecast.get(1));
            model.addAttribute("todayforecast", forecast.get(0));
       }
       
        List<LSTCategoryBase> categoryList = categoryDAO.getAllDescendantsForCategory(categoryDAO.getCategoryByName("Events", LSTEventCategory.class), true);

        model.addAttribute("eventcategories", categoryList);
        model.addAttribute("ages", EventAgeAppropriate.values());
        model.addAttribute("serachradius", SearchRadius.values());
        model.addAttribute("ask_zip", (sessionUserService.getUser().getZipCode() == null));
    }
}
