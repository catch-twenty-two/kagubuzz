package com.kagubuzz.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.utilities.KaguTextFormatter;

public class CurrentUserInfo implements ApplicationContextAware {
    public TBLUser user;
    public TBLKaguLocation kaguLocation;
    private ApplicationContext applicationContext = null;

    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    static public CurrentUserInfo getInstance(HttpSession httpSession, ModelMap modelMap) {
        
        CurrentUserInfo currentUserInfo = new CurrentUserInfo();
        
        currentUserInfo.CurrentUserInfoInit(httpSession, modelMap);
        
        return currentUserInfo;
    }
    
    protected CurrentUserInfo CurrentUserInfoInit(HttpSession httpSession, ModelMap modelMap) {
        KaguTextFormatter kaguTextFormatter = new KaguTextFormatter();
        
        UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
        
        if (!loggedIn()) return null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        user = userDAO.getUserById(Long.parseLong(auth.getName()));

        System.out.println("Request from user " + user.getId());
        
        modelMap.addAttribute("timeZoneOffset", httpSession.getAttribute("timezone"));

        modelMap.addAttribute("user", user);

        modelMap.addAttribute("isloggedin", loggedIn());

        kaguLocation = user.getTblKaguLocation();

        modelMap.addAttribute("kaguLocation", kaguLocation);

        httpSession.setAttribute("userid", user.getId());
        
        TimeZone tz = TimeZone.getTimeZone(TimeZone.getAvailableIDs(user.getTimeZoneInMilliseconds())[0]);
        
        Calendar time = Calendar.getInstance(tz);
        Date date = time.getTime();
        
        modelMap.addAttribute("date", kaguTextFormatter.getDate(date));
        modelMap.addAttribute("time", kaguTextFormatter.getTime(date));
        
        modelMap.addAttribute("flagtypes",FlagTypes.values());
        modelMap.addAttribute("formatter", new KaguTextFormatter());
        return this;
    }

    public boolean loggedIn() {
        
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            
            SecurityContext securityContext = SecurityContextHolder.getContext();
            return securityContext.getAuthentication().isAuthenticated();
        
        }

        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext apllicationContext) throws BeansException {
        this.applicationContext = apllicationContext;
    }

    public TBLUser getUser() { return user; }
    public TBLKaguLocation getKaguLocation() { return kaguLocation; }
}
