package com.kagubuzz.controllers;

import java.util.Date;
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

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.utilities.DescriptionProofReader;

@Controller
@RequestMapping(value="/ad")
public class AdCreateController {
	
	@Autowired
	AdDAO adDAO;	
	
	@Autowired
	CategoryDAO categoryDAO;
	
	@Autowired
	KaguLocationDAO kaguDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    AdService adService;
    
    @Autowired
    PostsService postsService;
    
    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    SecurityService securityService;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired
    UserDAO userDAO;
    
    static Logger logger = Logger.getLogger(AdCreateController.class);  
    
    String iPhoneAd;
	
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value="edit", method = RequestMethod.GET)
    public String adEdit(HttpSession httpSession, 
                         ModelMap model,
                         @RequestParam(required = true) Long id) {
        
        TBLUser user = userSessionService.getUser();
        
        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);
        
        setEditingAd(httpSession, ad);
        
        injectCreateAdModel(model, ad);
        
        return "/jsp/ad_create";
    }
    
	@RequestMapping(value="create", method = RequestMethod.GET)
	public String adCreate(HttpSession httpSession, 
						   ModelMap model) {
	    
	    injectCreateAdModel(model, null);
	    
		return "/jsp/ad_create";
	}

    public void injectCreateAdModel(ModelMap model, TBLAd ad) {
        
        adService.injectAdCategories(model);
		
		String adImage1URL = adService.getAdImageURL1(ad);
		String adImage2URL = adService.getAdImageURL2(ad);
		String adImage3URL = adService.getAdImageURL3(ad);
		String adImage4URL = adService.getAdImageURL4(ad);
		
		model.addAttribute("image1", (adImage1URL == null) ? "/static/images/adblankimage1.jpg" : adImage1URL);
		model.addAttribute("image2", (adImage2URL == null) ? "" : adImage2URL);
		model.addAttribute("image3", (adImage3URL == null) ? "" : adImage3URL);
		model.addAttribute("image4", (adImage4URL == null) ? "/static/images/adblankimage4.jpg" : adImage4URL);
		
		model.addAttribute("ad", ad);
        model.addAttribute("template_service", stringTemplateService);
    }
	
	@RequestMapping(value = "post", method = RequestMethod.POST)
	public String adPost(HttpSession httpSession, 
	                     ModelMap modelMap) {
	
	    TBLAd editingAd = getEditingAd(httpSession);

	    if(editingAd == null) {
            modelMap.addAttribute("error_page_message", "Please start a new ad, or pick an ad to edit from the manage posts menu.");
            logger.warn("No active add");
            return "/jsp/error";
        }	    
        
	    editingAd.setPostedDate(new Date());
	    editingAd.setActive(true);
	    
	    crudDAO.update(editingAd);
	    
	    postsService.notifyFollowersOfNewPost(editingAd);
	    
	    httpSession.removeAttribute("editing_ad");
	    modelMap.addAttribute("newpost", true);

	    return "redirect:view/" + editingAd.getId() + "/" + editingAd.getFriendlyURL();
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Notification saveAdDraftAjax(@RequestParam(required = true) String title,                              
                                                      @RequestParam(required = true) String category,
                                                      @RequestParam(required = true) String description,
                                                      @RequestParam(required = false, defaultValue ="0") Integer compensation,
                                                      @RequestParam(value="zip_code", required = true) String zipCode,   
                                                      @RequestParam(value="ad_group", required = true) AdGroup adGroup,
                                                      @RequestParam(value="ad_type", required = true, defaultValue="Offered") AdType adType,                                                                                                            
                                                      //optional parameters
                                                      @RequestParam(value="firm", required = false, defaultValue="false") Boolean firm,
                                                      @RequestParam(value="per_hour", required = false, defaultValue="false") Boolean perHour,
                                                      @RequestParam(value="accept_timebanking", required = false, defaultValue="false") Boolean acceptsTimebanking,
                                                      @RequestParam(value="adimage1", required = false) String adImage1,
                                                      @RequestParam(value="adimage2", required = false) String adImage2,
                                                      @RequestParam(value="adimage3", required = false) String adImage3,
                                                      @RequestParam(value="adimage4", required = false) String adImage4,                                  
                                                      @RequestParam(value="address", required = false) String address,
                                                      HttpSession httpSession,
                                                      ModelMap modelMap) {
        
	    DescriptionProofReader descriptionProofReader = new DescriptionProofReader();

        if(descriptionProofReader.CheckForEmail(description)) {
            return new Notification("Email Detected In Ad",
                                    stringTemplateService.getTemplateNotification("ad_error_contact_info")
                                    .add("contact_information", "an email address")
                                    .render(),
                                    NotificationTypes.error);
        }
        
        if(descriptionProofReader.CheckForPhoneNumbers(description)) {
            return new Notification("Phone Number Dectected In Ad",
                                     stringTemplateService.getTemplateNotification("ad_error_contact_info")
                                     .add("contact_information", "a phone number")
                                     .render(),
                                     NotificationTypes.error);
        }
        
        TBLUser user = userSessionService.getUser();     
                
	    TBLAd ad =  adService.createOrUpdateAd(getEditingAd(httpSession),	                        
                                               title, 
                                               category, 
                                               description,
                                               compensation,
                                               adGroup,
                                               adType,
                                               zipCode, 
                                               adImage1, 
                                               adImage2, 
                                               adImage3, 
                                               adImage4, 
                                               address, 
                                               user,
                                               perHour,
                                               firm,
                                               acceptsTimebanking);    
	    setEditingAd(httpSession,ad);
	    
	    return new Notification( Messages.getString("EventAddController.0"), Messages.getString("EventAddController.23"));
	}

	@RequestMapping(value = "saveIphone", method = RequestMethod.POST)
    public String saveAdiPhoneDraftAjax(@RequestParam(value="ad_title", required = true) String title,                              
                                                      @RequestParam(value="ad_category", required = true) String category,
                                                      @RequestParam(value="ad_desc", required = true) String description,
                                                      @RequestParam(value="ad_price", required = false, defaultValue ="0") Integer compensation,
                                                      @RequestParam(value="zip_code", required = false) String zipCode,   
                                                      @RequestParam(value="ad_group", required = false) AdGroup adGroup,
                                                      @RequestParam(value="ad_type", required = false, defaultValue="Offered") AdType adType,                                                                                                            
                                                      //optional parameters
                                                      @RequestParam(value="firm", required = false, defaultValue="false") Boolean firm,
                                                      @RequestParam(value="per_hour", required = false, defaultValue="false") Boolean perHour,
                                                      @RequestParam(value="accept_timebanking", required = false, defaultValue="false") Boolean acceptsTimebanking,
                                                      @RequestParam(value="adimage1", required = false) String adImage1,
                                                      @RequestParam(value="adimage2", required = false) String adImage2,
                                                      @RequestParam(value="adimage3", required = false) String adImage3,
                                                      @RequestParam(value="adimage4", required = false) String adImage4,                                  
                                                      @RequestParam(value="address", required = false) String address,
                                                      HttpSession httpSession,
                                                      ModelMap modelMap) {
      
        
        TBLUser user = userSessionService.getUser();     
        zipCode = user.getZipCode();
        adGroup = AdGroup.ForSale;
                
        TBLAd ad =  adService.createOrUpdateAd(getEditingAd(httpSession),	                        
        										title, 
								                category, 
								                description,
								                compensation,
								                adGroup,
								                adType,
								                zipCode, 
								                adImage1, 
								                adImage2, 
								                adImage3, 
								                adImage4, 
								                address, 
								                user,
								                perHour,
								                firm,
								                acceptsTimebanking);    
							        
	    setEditingAd(httpSession,ad); 
	    iPhoneAd = "iPhoneAd"; 
       
	    return "redirect:reviewIphone";
	}

	 @RequestMapping(value="save_geo_coors", method=RequestMethod.POST)
	 public String adSaveGeoCooridnates(@RequestParam long id,
	                                    @RequestParam Double longitude,
	                                    @RequestParam Double latitude,
	                                    ModelMap modelMap) {

        TBLUser user = userSessionService.getUser();

        TBLAd ad = (TBLAd) securityService.checkEntityOwner(id, TBLAd.class, user);

        postsService.setGeographicalCoordinates(longitude, latitude, ad);

        return "/ajax/blank";
    }

	@RequestMapping(value = "review", method = RequestMethod.GET)
	public String adReview(HttpSession httpSession,
						   ModelMap modelMap) {

        TBLAd ad = getEditingAd(httpSession);

        modelMap.addAttribute("offer_btn_text", (ad.isFirm()) ? ("I'm interested") : "Make an Offer");

        modelMap.addAttribute("adService", adService);
        modelMap.addAttribute("ad", ad);
        modelMap.addAttribute("editing", true);

        // Save google map geo coordinates

        modelMap.addAttribute("save_lat_long_url", "/ad/save_geo_coors");
        modelMap.addAttribute("post_id", ad.getId());

        userAccountService.injectUserProfile(modelMap, null, ad.getOwner());
        
        return "/jsp/ad_view";
    }
	
	@RequestMapping(value = "reviewIphone", method = RequestMethod.GET)
	public String adReviewIphone(HttpSession httpSession,
						   ModelMap modelMap) {

        TBLAd ad = getEditingAd(httpSession);

        modelMap.addAttribute("offer_btn_text", (ad.isFirm()) ? ("I'm interested") : "Make an Offer");

        modelMap.addAttribute("ad_service", adService);
        modelMap.addAttribute("ad", ad);
        modelMap.addAttribute("editing", true);

        // Save google map geo coordinates

        modelMap.addAttribute("save_lat_long_url", "/ad/save_geo_coors");
        modelMap.addAttribute("post_id", ad.getId());
         
        modelMap.addAttribute("iPhoneAd", iPhoneAd );

        userAccountService.injectUserProfile(modelMap, null, ad.getOwner());
        
        return "/jsp/ad_view";
    }



	 TBLAd getEditingAd(HttpSession httpSession) {
	        return crudDAO.getById(TBLAd.class, (Long) httpSession.getAttribute("editing_ad"));
	 }
	 
	 void setEditingAd(HttpSession httpSession, TBLAd ad) {
        httpSession.setAttribute("editing_ad", ad.getId()); //$NON-NLS-1$
	 }
}
