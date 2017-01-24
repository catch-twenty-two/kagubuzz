package com.kagubuzz.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.services.notifications.NotificationTypes;

@Controller
public class UserAccountController {
    
	@Autowired 
	EventDAO eventDAO;
	
	@Autowired 
	MessageDAO messageDAO;
	
	@Autowired 
	UserDAO userDAO;
	
	@Autowired 
	AdDAO adDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    NotificationCache notificationCache;

    @Autowired
    SecurityService securityService;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    KaguLocationDAO kaguLocationDAO;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    static Logger logger = Logger.getLogger(UserAccountController.class); 
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value="/notifications", method=RequestMethod.GET)
    public @ResponseBody List<Notification> notifications(ModelMap modelMap,                                                            
                                                          HttpSession httpSession)  {   
        TBLUser user = sessionUserService.getUser();
        List<Notification> notificationList = null;
        
        try {
            notificationList = notificationCache.get(user.getId());
        }
        catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return notificationList;   
    }
    
    @RequestMapping(value ="/request_save_zip", method = RequestMethod.POST)
    public @ResponseBody Notification saveZipCode(HttpSession httpSession,
                                                  ModelMap modelMap,
                                                  String zipCode) {

        TBLUser user = sessionUserService.getUser();
   
        try {
            userAccountService.updateUser(user,
                                      null, 
                                      null, 
                                      null, 
                                      null, 
                                      null,
                                      zipCode,
                                      null, 
                                      user.isSocialAccount(), 
                                      null,
                                      null,
                                      null,
                                      null,
                                      null,
                                      null);
        }
        catch (Exception e) {
            return new Notification("Sorry we couldn't find your zip code.");   //$NON-NLS-1$
        }    

        notificationCache.add(new Notification(Messages.getString("UserAccountController.3")), user);
       
        return new Notification().sendReload();
    }
    
    @RequestMapping(value="/userreccomend", method=RequestMethod.POST)
    public @ResponseBody Notification reccomendUser(@RequestParam long userId,                                                             
                                                    ModelMap modelMap,                                                            
                                                    HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();
        TBLUser userToReccomend = crudDAO.getById(TBLUser.class, userId);
        
        if(userToReccomend == null) {
            logger.warn("In user account controller,  user not found");
            return null;
        }
        
        if(user.equals(userToReccomend)) {
            logger.warn("In user account controller,  recommending self");
            return null;
        }
        
        if(user.getRecommended().contains(userToReccomend)) {
            return new Notification("User already reccomended");                    
        }
        
        user.addRecommended(userToReccomend);
        
        SystemMessage message = new SystemMessage(userToReccomend, userAccountService.getSystemUser());
        message.recommended(user);     
        messageDispatcher.sendMessage(message);        
        
        crudDAO.update(user);
        
        return new Notification(stringTemplateService.getTemplateNotification("recommendation_sent").add("recipient", userToReccomend.getFirstName()));
    }
    
    @RequestMapping(value="/userfollow", method=RequestMethod.POST)
    public @ResponseBody Notification followUser(@RequestParam long userId,
                                                             @RequestParam boolean follow,
                                                             ModelMap modelMap,                                                            
                                                             HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();

        Notification notification = null;
        
        if(user.getId().longValue() == userId) {
            return new Notification(Messages.getString("UserAccountController.5"), Messages.getString("UserAccountController.6"), NotificationTypes.error); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        TBLUser userToFollow = crudDAO.getById(TBLUser.class, userId);  
        boolean following = user.getFollowing().contains(userToFollow);
        
        if(following == follow) { 
            return new Notification(Messages.getString("UserAccountController.7"), 
                                    Messages.getString("UserAccountController.8") + ((follow) ? "" : Messages.getString("UserAccountController.10")) + Messages.getString("UserAccountController.11") + userToFollow.getFirstName() +Messages.getString("UserAccountController.12"),NotificationTypes.error); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        }
                
        if(follow) {
            
            user.addFollowing(userToFollow);   
            
            SystemMessage emailMessage = new SystemMessage(userToFollow, userAccountService.getSystemUser());
            emailMessage.beingFollowed();       
            messageDispatcher.queueMessage(emailMessage);
            
            notification = new Notification(Messages.getString("UserAccountController.13") + userToFollow.getFirstName(),  //$NON-NLS-1$
                                            Messages.getString("UserAccountController.14") + user.getFirstName() +Messages.getString("UserAccountController.15") + //$NON-NLS-1$ //$NON-NLS-2$
                                            Messages.getString("UserAccountController.16") + userToFollow.getFirstName() +  //$NON-NLS-1$
                                            Messages.getString("UserAccountController.17") + //$NON-NLS-1$
            		                        Messages.getString("UserAccountController.18"));        //$NON-NLS-1$
        } else {
            
            user.removeFollowing(userToFollow);
            
            notification = new Notification(Messages.getString("UserAccountController.19") +  //$NON-NLS-1$
                                            userToFollow.getFirstName(),
                                            Messages.getString("UserAccountController.20") + user.getFirstName() +Messages.getString("UserAccountController.21") + //$NON-NLS-1$ //$NON-NLS-2$
                                            Messages.getString("UserAccountController.22") + userToFollow.getFirstName() + Messages.getString("UserAccountController.23") + //$NON-NLS-1$ //$NON-NLS-2$
                                            Messages.getString("UserAccountController.24")); //$NON-NLS-1$
        }
                
        crudDAO.update(user);
        
        return notification;
    }
    
    @RequestMapping(value="/markmessageread", method=RequestMethod.POST)
    public @ResponseBody Notification messageMarkRead(@RequestParam long id,
                                                                  @RequestParam String mType,
                                                                  ModelMap modelMap,                                                            
                                                                  HttpSession httpSession)  {
        TBLUser user = sessionUserService.getUser();

        messageDAO.markMessageReadAndDelete(id, MessageType.valueOf(mType).getClassType());
                
        return new Notification();
    }
	
	@RequestMapping(value="/accountsettings", method=RequestMethod.GET)
    public String accountSettings(ModelMap modelMap,                                                         
                                   HttpSession httpSession)  {
	       modelMap.addAttribute("template_service", stringTemplateService);
        return "/jsp/account_settings"; //$NON-NLS-1$
    }
	
    @RequestMapping(value = "/accountfeedback/{id}", method = RequestMethod.GET)
    public String userProfile(ModelMap modelMap, @PathVariable Long id, HttpSession httpSession) {
        TBLUser userInquiry = crudDAO.getById(TBLUser.class, id);
        
        modelMap.addAttribute("feedback",messageDAO.getAllFeedbackForUser(userInquiry));
        modelMap.addAttribute("userInquiry", userInquiry);
        
        return "/jsp/user_feedback_view"; //$NON-NLS-1$
    }
}
