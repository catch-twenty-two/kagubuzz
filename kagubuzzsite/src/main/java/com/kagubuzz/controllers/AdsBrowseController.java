
package com.kagubuzz.controllers;

import org.apache.commons.lang.WordUtils;
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
import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.SearchRadius;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserSessionService;

@Controller
@RequestMapping(value ="/ads")
public class AdsBrowseController {
	
	@Autowired
	AdDAO adDAO;
	
	@Autowired
	CategoryDAO categoryDAO;
	
	@Autowired
	SearchService searchService;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    AdService adService;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    /* Converts empty strings into null when a form is submitted */
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    
    @RequestMapping(value ="browse/autosearch", method = RequestMethod.GET)
    public String browseAutoSearchAds(ModelMap modelMap, 
                                     @RequestParam(required=false, value="page", defaultValue = "0") Integer currentPage) {
        
        TBLUser user = sessionUserService.getUser();
        
        Pagination<TBLAd> pagination = adDAO.getAutoSearchAds(user, currentPage);
        pagination.setModel(modelMap);

        modelMap.addAttribute("hidesearchoptions", true);
        injectAdBrowse(modelMap, null, currentPage, AdGroup.ForSale , null, null, pagination);
        
        return "/jsp/ads_browse";
    }  

	
	@RequestMapping(value ="browse/category/{id}/{seoFriendlyName}", method = RequestMethod.GET)
	public String adsBrowseCategory(ModelMap modelMap,
	                                @PathVariable Long id,
	                                @RequestParam(required=false, defaultValue = "30") Integer radius,
	                                @RequestParam(required=false, value="page", defaultValue = "0") Integer currentPage) { 
	    
	    TBLUser user = sessionUserService.getUser(); 
	    
	    LSTAdCategory category = (LSTAdCategory) crudDAO.getById(LSTAdCategory.class, id); 	    	   
	    
	    Pagination<TBLAd> pagination = adDAO.browseAds(category.getAdGroup(), null, category, null, true, currentPage, radius,  user.getTblKaguLocation());
        pagination.setModel(modelMap);
        
        injectAdBrowse(modelMap, radius, currentPage, category.getAdGroup(), null, category, pagination);
	       
	    return "/jsp/ads_browse";
	}
	
	@RequestMapping(value ="browse/{adGroupUnderScoreName}/{adType}", method = RequestMethod.GET)
    public String adsBrowseCategory(ModelMap modelMap,
                                    @PathVariable String adGroupUnderScoreName,
                                    @PathVariable AdType adType,
                                    @RequestParam(required=false, defaultValue = "30") Integer radius,
                                    @RequestParam(required=false, value="page", defaultValue = "0") Integer currentPage) { 
        
        TBLUser user = sessionUserService.getUser();       
        
        AdGroup adGroup = AdGroup.valueOf(WordUtils.capitalizeFully(adGroupUnderScoreName, new char[]{'_'}).replaceAll("_", ""));
        
        Pagination<TBLAd> pagination = adDAO.browseAds(adGroup, adType, null, null, true, currentPage, radius,  user.getTblKaguLocation());
        pagination.setModel(modelMap);
        
        injectAdBrowse(modelMap, radius, currentPage, adGroup,  adType, null,pagination);
           
        return "/jsp/ads_browse";
    }
	   
	@RequestMapping(value ="browse/{adGroupUnderScoreName}", method = RequestMethod.GET)
	public String adsBrowse(ModelMap modelMap,
	                        @PathVariable String adGroupUnderScoreName,
                            @RequestParam(required=false, defaultValue = "30") Integer radius,
                            @RequestParam(required=false, value="page", defaultValue = "0") Integer currentPage) { 
	    
	    TBLUser user = sessionUserService.getUser(); 
	    
	    AdGroup adGroup = AdGroup.valueOf(WordUtils.capitalizeFully(adGroupUnderScoreName, new char[]{'_'}).replaceAll("_", ""));
    
        Pagination<TBLAd> pagination = adDAO.browseAds(adGroup,null, null, null, true, currentPage, radius, user.getTblKaguLocation());
        pagination.setModel(modelMap);
        
	    injectAdBrowse(modelMap, radius, currentPage, adGroup, null, null, pagination);

		return "/jsp/ads_browse";
	}

    @RequestMapping(value ="search", method = RequestMethod.GET)
    public String adsSearch(ModelMap modelMap,
                            @RequestParam(required=false, value="page", defaultValue = "0") Long currentPage,
                            @RequestParam(required=false) String keywords,
                            @RequestParam(required=false, defaultValue = "30") Integer radius,
                            @RequestParam(required=false) Integer price,
                            @RequestParam(required=false, value = "ad_group") AdGroup adGroup,
                            @RequestParam(required=false, value = "timebanking") Boolean timeBanking,
                            @RequestParam(required=false, value = "category_selection") Long categorySelection) { 
            
        LSTAdCategory category = crudDAO.getById(LSTAdCategory.class, categorySelection);
        
        TBLUser user = sessionUserService.getUser();          
        
        Pagination<TBLAd> pagination =  searchService.searchAds(keywords,
                                                                adGroup,
                                                                price,
                                                                user.getTblKaguLocation(),
                                                                radius,
                                                                category,
                                                                true, 
                                                                null,
                                                                null,
                                                                null,
                                                                false,
                                                                timeBanking,
                                                                currentPage);
        pagination.addQueryOption("timebanking",  timeBanking);
        pagination.addQueryOption("radius",  radius);
        pagination.addQueryOption("keywords",  keywords);    
        pagination.addQueryOption("category_selection", categorySelection);
        pagination.addQueryOption("price", price);
        pagination.addQueryOption("ad_group", adGroup);
        pagination.setModel(modelMap);         
        
        injectAdBrowse(modelMap, radius, currentPage.intValue(), adGroup, null, category, pagination);
        
        return "/jsp/ads_browse";
    }
    
    public void injectAdBrowse(ModelMap modelMap, 
                               Integer radius, 
                               Integer currentPage,
                               AdGroup adGroup,
                               AdType adType,
                               LSTAdCategory category,
                               Pagination<TBLAd> pagination) {		
		
		modelMap.addAttribute("radius", radius);
		modelMap.addAttribute("ad_service", adService);
		modelMap.addAttribute("ads", pagination.getList());
		modelMap.addAttribute("adcategories", adDAO.getAdCategoryByGroup(adGroup));
		modelMap.addAttribute("serach_radius_options", SearchRadius.values());
		modelMap.addAttribute("ad_groups", AdGroup.values());
		modelMap.addAttribute("ad_group", adGroup);
		modelMap.addAttribute("ad_type", adType);
		modelMap.addAttribute("category", category);		
        modelMap.addAttribute("template_service", stringTemplateService);
        modelMap.addAttribute("ask_zip", (sessionUserService.getUser().getZipCode() == null));
    }
}
