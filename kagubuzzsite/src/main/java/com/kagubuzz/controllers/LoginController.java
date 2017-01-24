package com.kagubuzz.controllers;

import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.services.UserSessionService;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    @Autowired
    UserSessionService sessionUserService;

    @Autowired
    UserDAO userDao;
    
    @Autowired
    AdDAO adDAO;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultPage(HttpSession httpSession,
                              ModelMap model)  {
        return "redirect:/home/browse";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model, HttpSession httpSession) {
        
        Calendar cal = Calendar.getInstance();
        
        cal.set(2013, 11, 15);
        
        long msLeft = cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        
        if(msLeft > 0) {
            model.addAttribute("show_campaign", true);
            model.addAttribute("ad_count", adDAO.getAdCount());
            model.addAttribute("days_remaining", TimeUnit.MILLISECONDS.toDays(msLeft));
        }
        
        model.addAttribute("show_log_in", true);
        
        return "/jsp/landing_page";
    }
    
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String redirectToLogIn(HttpSession httpSession) {
        return "redirect:/login";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(ModelMap model, HttpSession httpSession) {
        
        try {
            model.addAttribute("jimmy_id", userDao.getUserByEmail("jimmy.johnson@kagubuzz.com").getId());
            model.addAttribute("thone_id", userDao.getUserByEmail("thone.soungpanya@kagubuzz.com").getId());
            model.addAttribute("rachel_id", userDao.getUserByEmail("rachel.seligman@kagubuzz.com").getId());
            model.addAttribute("marvin_id", userDao.getUserByEmail("marvin.bauzon@kagubuzz.com").getId());
        }
        catch (NullPointerException e) {
            
        }
        
        return "/jsp/about";
    }
    
    @RequestMapping(value = "/tos", method = RequestMethod.GET)
    public String termsOfUse(HttpSession httpSession) {
        return "/jsp/terms_and_conditions";
    }
    
    @RequestMapping(value = "/privacy", method = RequestMethod.GET)
    public String privacy(HttpSession httpSession) {
        return "/jsp/privacy";
    }
    
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(HttpSession httpSession) {
        return "/jsp/help";
    }
    
    // for use with google webmaster tools
    
    @RequestMapping(value = "/google06f9a61976f18f73.html", method = RequestMethod.GET) 
    public String googleWebMasterVerification() {
        return "/jsp/google_webmaster_verification";
    }
}
