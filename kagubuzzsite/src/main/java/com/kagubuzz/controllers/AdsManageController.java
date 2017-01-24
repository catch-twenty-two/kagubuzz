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
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.PostsDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;

@Controller
@RequestMapping(value ="/ads/manage")
public class AdsManageController  {
    
	@Autowired
	AdDAO adDao;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	CRUDDAO crudDAO;
	
    @Autowired
    SecurityService securityService;
    
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
    PostsService postService;
    
    static Logger logger = Logger.getLogger(AdsManageController.class);  
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value ="activate", method = RequestMethod.GET)
    public String  adActivate(HttpSession httpSession,
                              @RequestParam long id) {
        TBLUser user = sessionUserService.getUser();

        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);

        postService.setActive(ad, true);

        return "redirect:/ads/manage/browse";
    }

    
	@RequestMapping(value ="remove", method = RequestMethod.GET)
	public String adRemove(HttpSession httpSession,
						   @RequestParam long id) {
        TBLUser user = sessionUserService.getUser();

        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);
        
        postService.setActive(ad, false);        
        
        return "redirect:/ads/manage/browse";
    }
	
    @RequestMapping(value = "duplicate", method = RequestMethod.GET)
    public String adDuplicate(HttpSession httpSession, ModelMap modelMap, @RequestParam long id) {
        
        TBLUser user = sessionUserService.getUser();

        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);

        if (ad == null) {
            logger.warn("Ad not found 1");
            return "/jsp/error"; 
        }
        
       // adService.duplicateEvent(ad);

        return "redirect:/ads/manage/browse"; 
    }
 
    
	@RequestMapping(value ="delete", method = RequestMethod.GET)
    public String adDelete(HttpSession httpSession,
                              ModelMap modelMap,
                              @RequestParam Long id) {   
	    
	    TBLUser user = sessionUserService.getUser();

        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);
        
        if(ad == null){
            logger.warn("Ad not found");
            return "/jsp/error"; 
        }
        
        postService.deletePost(ad);
        
        return "redirect:/ads/manage/browse"; 
    }
	
    @RequestMapping(value = "auto_renew", method = RequestMethod.GET)
    public String adAutoRenew(@RequestParam(required = true) Long id,
                             HttpSession httpSession, 
                             ModelMap modelMap) {     
        
        TBLUser user = sessionUserService.getUser();
        
        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);
        
        if(ad == null) {
            logger.warn("Ad not found 2");
            return "/jsp/error"; 
        }
        
        postService.renewPost(ad);
        
        SystemMessage message = new SystemMessage(user, userAccountService.getSystemUser());
        message.postRenewed(ad);
        messageDispatcher.sendMessage(message);
        
        return "redirect:/home/browse"; 
    }
	   
    @RequestMapping(value = "renew", method = RequestMethod.GET)
    public String adRenew(@RequestParam(required = true) Long id,
                                 HttpSession httpSession, 
                                 ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();
        
        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);
        
        if(ad == null) {
            logger.warn("Ad not found 3");
            return "/jsp/error"; 
        }
        
        postService.renewPost(ad);
        
        return "redirect:/ads/manage/browse"; 
    }
	    
	@RequestMapping(value ="browse", method = RequestMethod.GET)
	public String adsManage(@RequestParam(required = false, defaultValue="0" ) Long page,
						    ModelMap modelMap) {
	    
	    TBLUser user = sessionUserService.getUser();
	    
	    Pagination<TBLAd> userAds = postsDAO.getPostsForUser(page, user, TBLAd.class);
	    
	    userAds.setModel(modelMap);
	    modelMap.addAttribute("userads", userAds.getList()); 
        modelMap.addAttribute("userdirectory", fileservice.getUserPublicDirectoryURL(user, UserSubfolder.Images)); 
        modelMap.addAttribute("adService", adService);
        
        return "/jsp/ads_manage"; 
    }
	
	 @RequestMapping(value ="search", method = RequestMethod.GET)
	 public String adsManageSearch(@RequestParam(required = false, defaultValue="0" ) Long page,
                                      ModelMap modelMap,
                                      @RequestParam(required = false) Boolean expired,
                                      @RequestParam(required = false) Boolean active,
                                      @RequestParam(required = false) String title) {
           
         TBLUser user = sessionUserService.getUser();  
               
         Pagination<TBLAd> pagination =  searchService.searchAds(title, 
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
                                                                 false,
                                                                 page);
         
         // If active is not null it is automatically true
         
         if(active != null) {
             modelMap.addAttribute("not_active", "checked");
         }
         
         pagination.addQueryOption("expired",  expired);
         pagination.addQueryOption("title",  title);
         pagination.addQueryOption("active",  active);    
         pagination.setModel(modelMap);         
         
         modelMap.addAttribute("userads", pagination.getList());
         modelMap.addAttribute("userdirectory", fileservice.getUserPublicDirectoryURL(user, UserSubfolder.Images));     
         modelMap.addAttribute("ad_service", adService);
        
         return "/jsp/ads_manage";
       }
}
