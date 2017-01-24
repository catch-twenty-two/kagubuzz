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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.stringtemplate.v4.ST;

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.DiscussionAdDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdQuestion;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessage;
import com.kagubuzz.messagetemplates.qaadmessages.AdQAMessage;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value="/ad")
public class AdViewController {
	@Autowired
	AdDAO adDAO;
	
	@Autowired
	KaguLocationDAO kaguDAO;
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	DiscussionAdDAO TBLDiscussionAdDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	CategoryDAO categoryDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired
    AdService adService;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    PostsService postsService;
    
    @Autowired
    NotificationCache notificationCache;
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    static Logger logger = Logger.getLogger(AdViewController.class);
    
	@RequestMapping(value="view/{id}/{seoFriendlyUrl}", method = RequestMethod.GET)
	public String adView (@PathVariable Long id,
					      @RequestParam(required = false) Long scroll_id,
					  	  @RequestParam(value="newpost", required = false, defaultValue = "false") Boolean newPost,
					  	  ModelMap modelMap) {
	    
	    TBLUser user = userSessionService.getUser();
	    
		TBLAd ad = crudDAO.getById(TBLAd.class, id);
		
		if(ad == null){
		    logger.warn("Ad doesn't exist");
		    return "/jsp/error";
		}
		
		if(!ad.isActive()) {
		    ST message = stringTemplateService.getTemplateError("post_not_active");
		    message.add("post_type", "Ad");
            modelMap.addAttribute("error_page_message", message.render());
            logger.warn("User accessing non active post");
            return "/jsp/error";
        }	      
	    
        if (user.equals(ad.getOwner())) {
            
            postsService.setAllNotificationsToRead(ad.getQuestionsAndAnswers().getMessages(), ad.getQuestionsAndAnswers(), user, false);
            
            if(newPost) {
                modelMap.addAttribute("save_lat_long_url", "/ad/save_geo_coors");
                notificationCache.add(new Notification("Shiney New Ad Posted!", stringTemplateService.getTemplateNotification("ad_new_posted").render()), user);
            }
        }
        
        if(user.isLoggedIn()) {
            modelMap.addAttribute("following", user.getFollowing().contains(ad.getOwner())); 
        }
        
        modelMap.addAttribute("offer_btn_text", "I'm interested");
   
		modelMap.addAttribute("adService", adService);
		modelMap.addAttribute("acceptedoffer", adDAO.getOfferAccepted(ad));
		modelMap.addAttribute("ad", ad);
        modelMap.addAttribute("scroll_id", scroll_id);
        modelMap.addAttribute("newpost", newPost);

        userAccountService.injectUserProfile(modelMap, user, ad.getOwner());
        
        return "/jsp/ad_view"; 
	}

    @RequestMapping(value = { "remove" }, method = RequestMethod.POST)
    public @ResponseBody Notification adRemove(ModelMap modelMap, @RequestParam long id) {
        return postsService.deletePost(id, TBLAd.class, userSessionService.getUser());
    }
	
	@RequestMapping(value="make_inquiry", method = RequestMethod.POST)
	public @ResponseBody Notification  adMakeInquiry(@RequestParam("ad_id") Long adId,
												     @RequestParam("contact_method") DeliveryMethod buyerDelieveryMethod,
												     @RequestParam(value = "offer", required = false, defaultValue = "0") Integer offer,
												     @RequestParam(value = "timebanking", defaultValue = "false") Boolean timeBanking,
												     ModelMap modelMap) {	
	    Notification notification = null;
	    
	    TBLUser user = userSessionService.getUser();

		TBLAd ad = crudDAO.getById(TBLAd.class, adId);
		
		if(ad.getOwner() == user) {
		    return new Notification(stringTemplateService.getTemplateError("generic_error"));
		}
		
		// See if this user has already made an inquiry on the item
		
        TBLDiscussionAd adDiscussion = adDAO.getUserTransactionForAd(ad, user);
        
        if(adDiscussion == null) { 
            adDiscussion = adService.createOffer(user, ad);           
        }
        else {
            
            if((adDiscussion.getOfferAmount().equals(offer) && timeBanking.equals(adDiscussion.getTimeBanking())) && 
               (adDiscussion.getAdDiscussionState().equals(AdDiscussionState.WaitingForResponse))) {
                
                return new Notification(stringTemplateService.getTemplateNotification("ad_offer_same_buyer")
                                                             .add("sellername", ad.getOwner().getFirstName())
                                                             .add("offer", offer));
            }
            
            // Make sure they don't spam the seller
            
            if((new Date().getTime() - adDiscussion.getLastOfferDate().getTime()) < TimeUnit.MINUTES.toMillis(10)) {
                return new Notification(stringTemplateService.getTemplateError("offer_to_soon"));
            }
            
        }

		int listedPrice = ad.getPrice();
		
		// Low ball threshold - If Ad price is under 25, no threshold

		if (listedPrice > 25){
			double lowBallPct = .70;
			int lowBallThreshold= (int)(listedPrice * lowBallPct);
		
			if(offer < lowBallThreshold) {
				return new Notification(stringTemplateService.getTemplateNotification("ad_offer_too_low"));
			}
		}

        if (adDiscussion.getAdDiscussionState() == AdDiscussionState.Accepted) {
            return new Notification(stringTemplateService.getTemplateNotification("ad_offer_already_accepted_buyer").add("sellername", ad.getOwner().getFirstName()));
        }        		        
		
		adService.makeOffer(offer, timeBanking, buyerDelieveryMethod, adDiscussion);
        
		OfferMessage buyerMessage = new OfferMessage(adDiscussion, userAccountService.getSystemUser());		
		OfferMessage sellerMessage = new OfferMessage(adDiscussion, userAccountService.getSystemUser());
		
		if(ad.getAdState() == AdState.OfferAccepted) {
		    sellerMessage.sellerAlreadyAccepted(); 
		    notification = new Notification(stringTemplateService.getTemplateNotification("ad_offer_already_made_buyer").add("sellername", ad.getOwner().getFirstName()));

	    } else {
		    notification = new Notification(stringTemplateService.getTemplateNotification("ad_offer_made_buyer").add("sellername", ad.getOwner().getFirstName()));
		    sellerMessage.sellerInitialContact();
		}		
		
        buyerMessage.buyerInitialContact();
        
        messageDispatcher.sendMessage(buyerMessage);
        messageDispatcher.sendMessage(sellerMessage);
        
		return notification;
	}
    
    @RequestMapping(value="bookmark", method=RequestMethod.POST)
    public @ResponseBody Notification adBookmark(@RequestParam long id,
                                                             ModelMap modelMap,                                                         
                                                             HttpSession httpSession)  {
        TBLUser user = userSessionService.getUser();
        
        if(!user.isLoggedIn()) return null;
        
        TBLAd ad = crudDAO.getById(TBLAd.class, id);
        
        user.addToBookmarkedAds(ad);
        
        crudDAO.update(user);
        
        return new Notification("Bookmarked", stringTemplateService.getTemplateNotification("ad_bookmarked").add("ad_title", ad.getTitle()).render());
    }
    
    @RequestMapping(value="flag", method=RequestMethod.POST)
    public @ResponseBody Notification adFlag(@RequestParam Long id,
                                             @RequestParam (required = true, value = "data" ) FlagTypes flagType,
                                             ModelMap modelMap,
                                             HttpSession httpSession) {
            
        return postsService.flagPost(TBLAd.class, id, userSessionService.getUser(), flagType);       
    }
    
    @RequestMapping(value="bookmark/delete", method=RequestMethod.POST)
    public String adBookmarkDelete(@RequestParam long id,
                                    ModelMap modelMap,                                                         
                                    HttpSession httpSession)  {
        
        TBLUser user = userSessionService.getUser();
        
        if(!user.isLoggedIn()) return null;
        
        TBLAd ad = crudDAO.getById(TBLAd.class, id);
        
        user.removeUserBookmarkedAd(ad);                
        
        crudDAO.update(user);
        
        return "/ajax/no_data";
    }
    
    @RequestMapping(value="question", method=RequestMethod.POST)
    public String adQuestionAjax(@RequestParam ("parentid") Long parentId,
                                 @RequestParam ("messageid") Long messageId,
                                 @RequestParam ("message") String question,
                                 ModelMap modelMap) {
        
        TBLUser user = userSessionService.getUser();
        TBLDiscussionAdQA tblAd = ((TBLAd)crudDAO.getById(TBLAd.class, parentId)).getQuestionsAndAnswers(); 
        
        AdQAMessage adQandAMsg = new AdQAMessage(tblAd);
        adQandAMsg.question(question, user);
        messageDispatcher.queueMessage(adQandAMsg);
        
        modelMap.addAttribute("message", adQandAMsg.getReceipt());
        modelMap.addAttribute("ajaxurl", "/ad/question/");
        modelMap.addAttribute("domattachid", "questions");
        modelMap.addAttribute("option_remove", true);
          
        return "/ajax/messages_update";
    }


    @RequestMapping(value = "question/reply", method = RequestMethod.POST)
    public String replyToAdQuestionAjax(@RequestParam("messageid") Long messageId, 
                                        @RequestParam("parentid") Long parentId, 
                                        @RequestParam("message")  String answer,
                                        ModelMap modelMap) {
        
        TBLDiscussionAdQA tblAd = ((TBLAd)crudDAO.getById(TBLAd.class, parentId)).getQuestionsAndAnswers(); 
        TBLMessageAdQuestion question = (TBLMessageAdQuestion) messageDAO.getMessage(messageId, TBLMessageAdQuestion.class);

        AdQAMessage adQandAMsg = new AdQAMessage(tblAd);
        adQandAMsg.answer(question, answer);
        messageDispatcher.queueMessage(adQandAMsg);
        
        modelMap.addAttribute("message", adQandAMsg.getReceipt());
        modelMap.addAttribute("ajaxurl", "/ad/question/");
        modelMap.addAttribute("domattachid", "questions");
        modelMap.addAttribute("option_remove", true);
        
        return "/ajax/messages_update";
    }
    
    @RequestMapping(value = "/question/remove", method = RequestMethod.POST)
    public @ResponseBody Notification adQuestionRemove(ModelMap modelMap, @RequestParam long id) {
        
        // if the owner of the ad is attempting to delete pretend we are the original poster in order to gain rights to delete
        
        TBLUser user = userSessionService.getUser();
        TBLMessageAdQuestion question = crudDAO.getById(TBLMessageAdQuestion.class, id);
        
        return postsService.deletePost(id, TBLMessageAdQuestion.class, (((TBLAd)question.getParent()).getOwner().equals(user)) ? question.getOwner() : user);
    }

    
    @RequestMapping(value="question/flag", method=RequestMethod.POST)
    public @ResponseBody Notification flagAdQuestion(@RequestParam Long id,
                                                     @RequestParam (required = true, value = "data" ) FlagTypes flagType,
                                                     ModelMap modelMap,
                                                     HttpSession httpSession) {
            
        return postsService.flagPost(TBLMessageAdQuestion.class, id, userSessionService.getUser(), flagType);       
    }
}
