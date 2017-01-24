package com.kagubuzz.controllers;

import java.util.Hashtable;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.SpringRoles;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;

@Controller
@RequestMapping(value ="/services/")
public class WebServicesController {

    @Autowired
    UserDAO userDao;
    
    @Autowired
    AdDAO adDAO;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    SecurityService securityService;

    @RequestMapping(value = "user_email_from_code", method = RequestMethod.GET)
    public @ResponseBody Hashtable<String, String> userEmailFromSecurityToken(HttpSession httpSession,
                                                                              @RequestParam(required = true, value="user_email") String userEmail,
                                                                              @RequestParam(required = true, value="user_password") String userPassword,
                                                                              @RequestParam(required = true, value="security_code_list") String[] securityCodeList)  {
        
        if(securityService.checkUserCredentialsAndRole(userEmail, userPassword, SpringRoles.Partner) == null) {
            return new Hashtable<String,String>();
        }
        
        Hashtable<String, String> securityCodePairs = new Hashtable<String, String>();
        
        for(String securityCode: securityCodeList) {            
            
            TBLUser user = userDao.getUserBySecurityCode(securityCode);   
            
            if(user == null) continue;            
            
            securityCodePairs.put(securityCode, user.getEmail());
            userAccountService.refreshSecurityCode(user);
        }
        
        return securityCodePairs;        
    }
    

}


