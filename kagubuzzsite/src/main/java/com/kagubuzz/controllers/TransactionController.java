package com.kagubuzz.controllers;

import java.util.List;

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
import com.kagubuzz.database.dao.DiscussionAdDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.ExchangeRatingTypes;
import com.kagubuzz.datamodels.enums.TransactionCancelTypes;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessage;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessageTemplates;
import com.kagubuzz.messagetemplates.privatemessage.PrivateMessage;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguTextFormatter;

@Controller
@RequestMapping(value = "/transaction")
public class TransactionController {
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	AdDAO adDAO;
	
	@Autowired
	DiscussionAdDAO transactionDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    UserSessionService userSessionService;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    AdService adService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired
    SecurityService securityService;
    
    @Autowired
    NotificationCache notificationCache;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    PostsService postsService;
    
    static Logger logger = Logger.getLogger(TransactionController.class); 
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
	@RequestMapping(value="reply", method=RequestMethod.POST)
	public String replyToTransactionPost(@RequestParam ("parentid") Long parentId,
                                         @RequestParam ("messageid") Long messageId,
                                         @RequestParam ("message") String message,
                                         ModelMap modelMap) {
	    
		TBLUser user = userSessionService.getUser();			
		
		TBLMessageAdOffer originalMessage = crudDAO.getById(TBLMessageAdOffer.class, messageId);		
		TBLDiscussionAd transaction = originalMessage.getDiscussionAd();
		
		TBLMessageAdOffer newReply = new TBLMessageAdOffer();
		transaction.addMessage(newReply);
		newReply.setDiscussionAd(transaction);
		
		PrivateMessage privateMessage = new PrivateMessage(transaction, 
		                                                   user, 
		                                                   originalMessage.getSender(),
		                                                   newReply,
		                                                   message);
		
	    messageDispatcher.sendMessage(privateMessage);
	        
		modelMap.addAttribute("message", newReply);
		modelMap.addAttribute("ajaxurl", "transactionreply");
		modelMap.addAttribute("domattachid", "attachhere");
		
		return "/ajax/messages_update";
	}
	
	@RequestMapping(value ="view/{id}/{seoFriendlyURL}", method = RequestMethod.GET)
	public String transactionView(@RequestParam(required = false) Long  scroll_id,
								  @PathVariable("id") long id,
								  ModelMap modelMap) {
	    
	    TBLUser user = userSessionService.getUser();        
		
		TBLDiscussionAd transaction = securityService.isTransactionParticipant(user, id);
		  
		if(transaction == null) {      
		    logger.warn("In transactionView,  Discussion does not exist");
	        return "/jsp/error";
	    }
		  
		List<TBLMessageAdOffer> messages = adDAO.getAdMessagesForUser(transaction, user);

		TBLMessageUserTransactionFeedback feedback = messageDAO.getUserTransactionFeedback(user, transaction);
		TBLMessageUserTransactionFeedback otherPartyFeedback = messageDAO.getUserTransactionFeedback(transaction.getOppositeParty(user), transaction);

		if(otherPartyFeedback != null) {		    
		    otherPartyFeedback.setReadByRecipient(true);
		    crudDAO.update(otherPartyFeedback);
		}

        postsService.setAllNotificationsToRead(messages, transaction, user, false);     
        
		modelMap.addAttribute("transaction", transaction);
		modelMap.addAttribute("messages", messages);
		modelMap.addAttribute("scroll_id", scroll_id);
		modelMap.addAttribute("system_user", userAccountService.getSystemUser());
		modelMap.addAttribute("adService", adService);
		modelMap.addAttribute("feedback", feedback);
		modelMap.addAttribute("other_party",transaction.getOppositeParty(user));
		modelMap.addAttribute("other_party_feedback", otherPartyFeedback);

        modelMap.addAttribute("offerSnippet", OfferMessageTemplates.getOfferSnippit(transaction));
        
        userAccountService.injectUserProfile(modelMap, user, transaction.getOppositeParty(user));
        
        
        boolean showOmnui =  (((user.equals(transaction.getBuyer()) && transaction.getAd().getAdType().equals(AdType.Offered)) ||
                               (user.equals(transaction.getSeller()) && transaction.getAd().getAdType().equals(AdType.Request))) &&
                                transaction.getAdDiscussionState().equals(AdDiscussionState.Accepted) && transaction.getTimeBanking());
        
        modelMap.addAttribute("showOmnui", showOmnui);

		return "/jsp/transaction_view";
	}
	
	@RequestMapping(value ="browse", method = RequestMethod.GET)
	public String transactionsBrowse(ModelMap model)  {   	
		return "/jsp/transactions_browse";
	}
	
	@RequestMapping(value ="accept", method = RequestMethod.GET)
	public String transactionsAcceptOffer(ModelMap model,
					      			      @RequestParam("id") long id,
					      			      @RequestParam("adcode") String securityCode) {
	    
        TBLUser user = userSessionService.getUser();        
        
        TBLDiscussionAd newOffer = transactionDAO.getTransactionByIDAndSecurityCode(id, securityCode);

        if(newOffer == null) {
            logger.warn("In transactionsAcceptOffer,  Discussion does not exist");
            return "/jsp/error";
        }
          
        securityService.checkEntityOwner(newOffer.getAd().getId(), TBLAd.class, user);
        
        // Make sure an offer has not already been accepted
        
        if(adService.acceptOffer(newOffer) == false) {
            
            TBLDiscussionAd alreadyAcceptedOffer = adDAO.getOfferAccepted(newOffer.getAd());
            
            // if an offer has already been accepted and it is not the new offer redirect to warning page
            
            if (!alreadyAcceptedOffer.equals(newOffer)) {
                
                Notification alreadyAcceptedNotification = new Notification();           
                alreadyAcceptedNotification.setModalNotification("ad_offer_already_accepted_warning"); 
                notificationCache.add(alreadyAcceptedNotification, user);
                
                return redirectToTransaction(alreadyAcceptedOffer);
            }
            
            model.addAttribute("error_page_message", stringTemplateService.getTemplateError("offer_already_accepted").render());
            
            logger.warn("In transactionsAcceptOffer offer already accepted");
            return "/jsp/error";
        }
        
        // See if user is attempting to accept an offer when they already have accept one from another buyer
        
        OfferMessage buyerOfferMessage = new OfferMessage(newOffer, user); 
        OfferMessage sellerOfferMessage = new OfferMessage(newOffer, user);

        buyerOfferMessage.buyerAccepted(); 
        sellerOfferMessage.sellerAccepted();
        
        adDAO.setAllOffersInactiveForTransaction(newOffer);
		messageDispatcher.sendMessage(buyerOfferMessage);
	    messageDispatcher.sendMessage(sellerOfferMessage);
		
	    return redirectToTransaction(newOffer);
	}
	
	@RequestMapping(value ="decline", method = RequestMethod.GET)
	public String transactionsDeclineOffer(ModelMap model,
					      			      @RequestParam("id") long id,
					      			      @RequestParam("adcode") String securityCode) {
	    
        TBLUser user = userSessionService.getUser();        
        
        TBLDiscussionAd transaction = transactionDAO.getTransactionByIDAndSecurityCode(id, securityCode);

        if(transaction == null) {
            return "/jsp/error";
        }
          
        securityService.checkEntityOwner(transaction.getAd().getId(), TBLAd.class, user);
		
		if (transaction.getAdDiscussionState() == AdDiscussionState.Accepted ||
	        transaction.getAdDiscussionState() == AdDiscussionState.Declined) {
	            model.addAttribute("error_page_message", stringTemplateService.getTemplateError("offer_already_accepted").render());
	            logger.warn("In transactionsDeclineOffer offer already accepted");
            return "/jsp/error";
        }
	     
	    adDAO.setAllOffersInactiveForTransaction(transaction);
	     
        OfferMessage offerMessage = new OfferMessage(transaction, user);
        offerMessage.sellerDeclined(); 
        messageDispatcher.sendMessage(offerMessage);
        
        OfferMessage buyerOfferMessage = new OfferMessage(transaction, user);
        buyerOfferMessage.buyerDeclined();
        messageDispatcher.sendMessage(buyerOfferMessage);
		
		return redirectToTransaction(transaction);
	}

    public String redirectToTransaction(TBLDiscussionAd transaction) {
        return String.format("redirect:/transaction/view/%s/%s", transaction.getId(), transaction.getFriendlyURL());
    }
	
	@RequestMapping(value ="think_on_it", method = RequestMethod.GET)
	public String transactionThinkOnIt(ModelMap model,
					      			   @RequestParam("id") long id,
					      			   @RequestParam("adcode") String securityCode)  {
	    
	    TBLUser user = userSessionService.getUser();        
		
        TBLDiscussionAd transaction = transactionDAO.getTransactionByIDAndSecurityCode(id, securityCode);

		if(transaction == null) {
            logger.warn("In transactionThinkOnIt transaction does not exist");
		    return "/jsp/error";
		}
	      
        securityService.checkEntityOwner(transaction.getAd().getId(), TBLAd.class, user);
		
		if (transaction.getAdDiscussionState() != AdDiscussionState.WaitingForResponse) {
		    model.addAttribute("error_page_message", stringTemplateService.getTemplateError("offer_already_accepted").render());
		    logger.warn("In transactionThinkOnIt offer already accpeted");
            return "/jsp/error";
        }
		
		adDAO.setAllOffersInactiveForTransaction(transaction);
		
        OfferMessage offerMessage = new OfferMessage(transaction, user);
        offerMessage.sellerThinkOnIt();        
        messageDispatcher.sendMessage(offerMessage);
        
        OfferMessage buyerOfferMessage = new OfferMessage(transaction, user);
        buyerOfferMessage.buyerThinkOnIt();
        messageDispatcher.sendMessage(buyerOfferMessage);
		
        return redirectToTransaction(transaction);
	}
	
	@RequestMapping(value ="cancel", method = RequestMethod.POST)
    public String transactionCancel(ModelMap model,
                                    Long id,
                                    TransactionCancelTypes reason)  {
        
        TBLUser user = userSessionService.getUser();        
        
        TBLDiscussionAd transaction = securityService.isTransactionParticipant(user, id);
        
        if(transaction == null) {
            logger.warn("In transactionCancel offer does not exist");
            return "/jsp/error";
        }
        
        if(transaction.getAdDiscussionState() != AdDiscussionState.Accepted) {
            logger.warn("In transactionCancel offer not accepted");
            return "/jsp/error";
        }
        
        TBLAd ad = transaction.getAd();
        
        transaction.setAdDiscussionState(AdDiscussionState.Canceled);
        crudDAO.update(transaction);
        
        ad.setAdState(AdState.InquiriesMade);
        
        crudDAO.update(ad);
        
        adDAO.setAllOffersInactiveForTransaction(transaction);
        
        for(TBLUser participant: transaction.getParticipants()) {
            OfferMessage cancelMessage = new OfferMessage(transaction, participant);
            cancelMessage.offerCanceled(participant);      
            messageDispatcher.sendMessage(cancelMessage);
        }
        
        return redirectToTransaction(transaction);
    }
	
    @RequestMapping(value = "rate", method = RequestMethod.POST)
    public @ResponseBody Notification eventRateAjax(@RequestParam("parentid") Long id, 
                                                    @RequestParam("message") String feedback, 
                                                    @RequestParam("userdata") ExchangeRatingTypes ratingType,
                                                    ModelMap modelMap, 
                                                    HttpSession httpSession) {
        
        TBLUser user = userSessionService.getUser();
        TBLDiscussionAd transaction = null;
        
        try {
            transaction = (TBLDiscussionAd) securityService.isTransactionParticipant(user, id);
            
            adService.rateTransaction(transaction, ratingType, user, feedback);
        }
        catch (SecurityException e) {
            return new Notification("Error", "Error", NotificationTypes.error);
        }
        
        // Mark Ad As Sold
        Notification notification = null;
        
        if((user == transaction.getSeller()) &&
            (transaction.getAdDiscussionState() == AdDiscussionState.Accepted)) {
            transaction.setActive(false);
            transaction.getAd().setAdState(AdState.Complete);
            transaction.getAd().setActive(false);
            transaction.setAdDiscussionState(AdDiscussionState.Complete);
            crudDAO.update(transaction);
            crudDAO.update(transaction.getAd());
            
            notification = new Notification(stringTemplateService.getTemplateNotification("post_rating_saved_transaction_closed").add("post_type", "ad"));
            
            adService.notifyUsersOfClosedItem(transaction.getAd());
            
            for(TBLUser participant: transaction.getParticipants()) {
                OfferMessage closed = new OfferMessage(transaction, participant);
                closed.exchangeFinalized(participant);     
                messageDispatcher.sendMessage(closed);
            }
        } 
        else {
            notification = new Notification(stringTemplateService.getTemplateNotification("post_rating_saved").add("post_type", "ad"));
        }
                
        notificationCache.add(notification, user);
        
        notification = new Notification();
                
        notification.setReload(true);
        
        return notification;
    }
    
    @RequestMapping(value = "omnui/{id}", method = RequestMethod.GET)
    public String  omnuiRedirect(@PathVariable Long id, 
                                 ModelMap modelMap, 
                                 HttpSession httpSession) {
        
        TBLUser user = userSessionService.getUser();                
        TBLDiscussionAd transaction = securityService.isTransactionParticipant(user, id);
         
        userAccountService.refreshSecurityCode(user);
        userAccountService.refreshSecurityCode(transaction.getOppositeParty(user));
        
        modelMap.addAttribute("skip_intercept", true);
        modelMap.addAttribute("key", "dk3hiHhaieKa5u6u55888k");
        modelMap.addAttribute("email", user.getSecurityCode());
        modelMap.addAttribute("fullName", String.format("%s%s", user.getFirstName(), (user.getLastName() != null) ? " " + user.getLastName():""));
        modelMap.addAttribute("toEmail", transaction.getOppositeParty(user).getSecurityCode());
        modelMap.addAttribute("description", transaction.getAd().getTitle());
        modelMap.addAttribute("returnUrl", String.format("%s/transaction/view/%s/%s", TemplateMessage.getServerURL(), transaction.getId(), transaction.getFriendlyURL()));
        
        return "redirect:http://www.omnui.com/Account/ThirdPartyAuthentication";
    }
}
