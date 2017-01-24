package com.kagubuzz.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.ExchangeRatingTypes;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

import org.apache.log4j.Logger;

@Controller
@RequestMapping("/modal")
public class ModalDialogController {

    @Autowired
    StringTemplateService stringTemplateService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    CRUDDAO crudDAO;

    @Autowired
    EventDAO eventDAO;

    @Autowired
    UserSessionService sessionUserService;

    @Autowired
    SecurityService securityService;
    
    @Autowired
    MessageDAO messageDAO;
    
    @Autowired
    UserAccountService userAccountService;

    static Logger logger = Logger.getLogger(ModalDialogController.class);

    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    @RequestMapping(value = "userprofile/{id}", method = RequestMethod.GET)
    public String userProfile(ModelMap modelMap, @PathVariable Long id, HttpSession httpSession) {
        
        TBLUser user = sessionUserService.getUser();
        TBLUser userInquiry = crudDAO.getById(TBLUser.class, id);
        
        userAccountService.injectUserProfile(modelMap, user, userInquiry);
        
        return "/ajax/modal_profile";
    }

    @RequestMapping(value = "flagpost", method = RequestMethod.POST)
    public String flagPost(ModelMap modelMap, HttpSession httpSession, 
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "ajax_url", required = false) String ajaxURL,
                           @RequestParam(value = "id", required = false) String postId) {

        modelMap.addAttribute("id", postId);
        modelMap.addAttribute("ajax_url", ajaxURL);
        modelMap.addAttribute("name", name);
        modelMap.addAttribute("flagtypes", FlagTypes.values());

        return "/ajax/modal_flag";
    }
    
    @PreAuthorize("permitAll")
    @RequestMapping(value = "ad_ask_contact_method/{id}", method = RequestMethod.GET)
    public String adContactMethod(ModelMap modelMap, String name, HttpSession httpSession, @PathVariable Long id) {

        TBLAd ad = crudDAO.getById(TBLAd.class, id);
        TBLUser user = sessionUserService.getUser();

        if (!user.isLoggedIn()) {
            STGroup group = stringTemplateService.getTemplateModalGroup("info_offer_please_sign_in");
            
            ST title = group.getInstanceOf("title");
            ST body = group.getInstanceOf("body");
            
            modelMap.addAttribute("modal_body", body.render());
            modelMap.addAttribute("modal_title", title.render());
            
            return "/ajax/modal_info";
        }
        
        STGroup contactMessage = stringTemplateService.getTemplateGroupAds(ad);
        
        ST st = null;
        
        switch(ad.getAdGroup()) {
        case CommunityServices:
            st = contactMessage.getInstanceOf("contact_method_community_service");
            break;
        case ForSale:
            st = contactMessage.getInstanceOf("contact_method_community_service");
            break;
        }
        
        modelMap.addAttribute("ad_contact_message", st.render());
        modelMap.addAttribute("deliveryMethod", DeliveryMethod.values());
        modelMap.addAttribute("ad", ad);

        return "/ajax/modal_ad_ask_contact_method";
    }

    
    @RequestMapping(value = "ask_zip_code", method = RequestMethod.GET)
    public String askZip(HttpSession httpSession) {
        return "/ajax/modal_ask_zip";
    }

    @RequestMapping(value = "ask_beta_test_code", method = RequestMethod.GET)
    public String askBetaCode() {
        return "/ajax/modal_ask_beta_id";
    }
    
    @RequestMapping(value = "ask_captcha", method = RequestMethod.GET)
    public String askCaptcha(ModelMap modelMap, HttpSession httpSession) {
    	
    	modelMap.addAttribute("recaptcha_public_key", securityService.getRecaptchaPublicKey()); //$NON-NLS-1$
        return "/ajax/modal_ask_captcha";
    }

    @RequestMapping(value = "rate_event/{id}/{rating}", method = RequestMethod.GET)
    public String rateEvent(ModelMap modelMap, @PathVariable Float rating, @PathVariable Long id , HttpSession httpSession) {

        TBLUser user = sessionUserService.getUser();
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        if (event == null) {
            return "/ajax/blank";
        }
        
        modelMap.addAttribute("event_rating", rating);
        modelMap.addAttribute("event", event);
        modelMap.addAttribute("feedback", eventDAO.getUserRatingForEvent(event, user));

        return "/ajax/modal_rate_event";
    }
    
    @RequestMapping(value = "event_expired/{id}", method = RequestMethod.GET)
    public String eventExpired(ModelMap modelMap, @PathVariable Long id, HttpSession httpSession) {

        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        if (!event.isActive()) {
            return "/ajax/blank";
        }

        modelMap.addAttribute("event", event);

        return "/ajax/modal_event_expired";
    }
    
    @RequestMapping(value = "transaction/cancel/{id}", method = RequestMethod.GET)
    public String transactionCancel(ModelMap modelMap, @PathVariable Long id, HttpSession httpSession) {

        TBLUser user = sessionUserService.getUser();
        TBLDiscussionAd transaction = crudDAO.getById(TBLDiscussionAd.class, id);

        //modelMap.addAttribute("cancelation_types", TransactionCancelTypes.values());
        modelMap.addAttribute("transaction", transaction);
        modelMap.addAttribute("other_user", transaction.getOppositeParty(user));

        return "/ajax/modal_transaction_cancel";
    }

    @RequestMapping(value = "transaction/rate/{id}/{rating}", method = RequestMethod.GET)
    public String rateTransaction(ModelMap modelMap, @PathVariable Float rating, @PathVariable Long id, HttpSession httpSession) {

        TBLUser user = sessionUserService.getUser();
        TBLDiscussionAd transaction = crudDAO.getById(TBLDiscussionAd.class, id);                      
    
        TBLMessageUserTransactionFeedback feedback = messageDAO.getUserTransactionFeedback(user, transaction);
        
        modelMap.addAttribute("transaction_rating", rating);
        modelMap.addAttribute("transaction", transaction);
        modelMap.addAttribute("feedback", feedback);
        modelMap.addAttribute("feedback_types", ExchangeRatingTypes.values());
        
        return "/ajax/modal_rate_transaction";
    }
    
    @RequestMapping(value = "info/{dialog}", method = RequestMethod.GET)
    public String modalInfo(ModelMap modelMap, @PathVariable String dialog, HttpSession httpSession) {
        
        STGroup group = stringTemplateService.getTemplateModalGroup("info_" + dialog);
        
        if (group == null) {
            return "/ajax/blank";
        }
        
        ST title = group.getInstanceOf("title");
        ST body = group.getInstanceOf("body");
        
        modelMap.addAttribute("modal_body", body.render());
        modelMap.addAttribute("modal_title", title.render());
        
        return "/ajax/modal_info";
    }
}
