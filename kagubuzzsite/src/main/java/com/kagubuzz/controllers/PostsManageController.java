package com.kagubuzz.controllers;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageEventComment;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.notifications.Notification;

@Controller
public class PostsManageController  {
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	CRUDDAO crudDAO;
	
    @Autowired
    PostsService postsService;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    SecurityService securityService;
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value ="/event/comment/remove", method = RequestMethod.POST)
    public @ResponseBody Notification  eventCommentRemoveAjax(ModelMap modelMap, @RequestParam long id) {     

        return postsService.deletePost(id, TBLMessageEventComment.class, sessionUserService.getUser());               
    }
    
    @RequestMapping(value = {"/events/remove", "/event/remove"}, method = RequestMethod.POST)    
    public @ResponseBody Notification adRemove(ModelMap modelMap, @RequestParam long id) { 
        TBLUser user = sessionUserService.getUser();
        
        securityService.checkEntityOwner(id, TBLEvent.class, user);   
        
        return postsService.deletePost(id, TBLEvent.class, sessionUserService.getUser());      
    }
}
