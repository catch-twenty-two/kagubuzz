package com.kagubuzz.services;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.weather.WeatherReport;

@Service("userSessionService")
public class UserSessionService  {

    @Autowired
    UserDAO userDAO;
    
    @Autowired
    CRUDDAO crudDAO;
    
    @Value("${google.ads}")
    private boolean showGoogleAds;

    static Logger log = Logger.getLogger(UserSessionService.class);
    
    private static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }
    
    public TBLUser getUser() { 
        
        HttpSession session = session();
        TBLUser user = (TBLUser) session.getAttribute("kagu_user");
        
        // The Kagu User id is stored in the currently logged in user which is stored in the current thread context
        // this might not work in a clustered environment with hibernate

        User springSecurityUser = SpringSecurityUtilities.getLoggedInUser();
             
        if(springSecurityUser != null) {
            
           // if(user != null && user.isLoggedIn()) return user;
            
            // user accessing that is not logged in
            // load the session with the hibernate entity associated with this id
                
            user = crudDAO.getById(TBLUser.class, Long.parseLong(springSecurityUser.getUsername())); 
            user.setLoggedIn(true);
        }
        
        if(user == null) {
                user = new TBLUser().setLoggedIn(false);
        }

        session.setAttribute("kagu_user", user);
        
        return user;
    }

    public void setModelMapSessionObjects(ModelMap modelMap, TBLUser user) {  
        
        modelMap.addAttribute("formatter", new KaguTextFormatter());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("adSense", showGoogleAds);
        
        if(user.isLoggedIn()) {
            modelMap.addAttribute("inboxmessgecount", userDAO.getUnreadMessageCountForUser(user));
        }
    }
    
    public WeatherReport getWeatherReport() {
        
        HttpSession httpSession = session();
        TBLUser user = getUser();
        
        if(!user.isLoggedIn()) {
            return null;
       }
        
        //if(true) return null;
        
        WeatherReport weatherReport = (WeatherReport) httpSession.getAttribute("weather_report");
 
        try {                        
                if ((weatherReport == null) ||
                    (weatherReport.getCurrentConditions() == null) ||
                    (weatherReport.getForecast().isEmpty())) {                        
                    weatherReport = new WeatherReport(user.getTblKaguLocation(), user.getTimeZoneOffset(), user.getWeatherStationId(), 2);             
                    httpSession.setAttribute("weather_report", weatherReport);
                }        
                
                if(TimeUnit.MILLISECONDS.toHours(weatherReport.getAge()) > 3) {
                    weatherReport = new WeatherReport(user.getTblKaguLocation(), user.getTimeZoneOffset(), user.getWeatherStationId(), 2);             
                    httpSession.setAttribute("weather_report", weatherReport);
                }
        
                if ((weatherReport.getCurrentConditions() == null) || 
                    (weatherReport.getForecast().isEmpty())) {
                    return null;
                }
        
                httpSession.setAttribute("weather_report", weatherReport);
        }
        catch (Exception e) {
            log.error("Failed to get weather report");
            return null;
        }
        
        return weatherReport;
    }
}
