                package com.kagubuzz.controllers;

import javax.servlet.http.HttpSession;
import javax.xml.ws.soap.Addressing;

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
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.AdMessageTypes;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessage;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.sms.SMSTransactionCode;

@Controller
@RequestMapping("/nexmo")
public class SMSController {
    
	@Autowired
	AdDAO adDao;
	
	@Autowired
	UserDAO userDAO;
	
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    AdService adService;
    
	Logger logger = Logger.getLogger(SMSController.class);
	
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
	@RequestMapping(value="/receipt", method = {RequestMethod.POST , RequestMethod.HEAD})
	public String nexmoReceipt(ModelMap model) {	
		// TODO: Gather receipt info
		// Nexmo service expects HTTP response 200 ok from server no need for body
		
		return "/jsp/blank";
	}
	
	@RequestMapping(value="/message", method = RequestMethod.HEAD)
	public String nexmoMessageHead(ModelMap model) {	
		// Nexmo service expects HTTP response 200 ok from server no need for body
		
		return "/jsp/blank";
	}
	
	// Nexmo service expects HTTP response 200 ok from server no need for body send blank
	
	@RequestMapping(value="/message", method = RequestMethod.POST)
	public String nexmoMessagePost(HttpSession httpSession, 
						   	 ModelMap model,
						   	 String type,
						   	 String to, // KaguBuzz 17162352655
						   	 @RequestParam(required = false) String msisdn, // Number sending back from
						   	 @RequestParam(value = "network-code", required = false) String networkCode,
						   	 @RequestParam(value = "messageid", required = false) String messageID,
						   	 @RequestParam (value = "message-timestamp", required = false) String timeStamp,
						   	 String text, //reponse text
						   	 String concat,
						   	 @RequestParam (value="concat-ref", required = false) String concatRef,
						   	 @RequestParam (value="concat-total", required = false) String concatTotal,
						   	 String data,
						   	 String udh) {
	    
        if(text == null || msisdn == null) {
            logger.warn("Data missing in request");
            return "/jsp/blank";
        }
        
		logger.info("SMS: got " + text + " from " + msisdn); // 15032359028

		TBLUser user = userDAO.getUserByPhoneNumber(msisdn);
		
		if(user == null) {
			logger.warn("User not found.");
			return "/jsp/blank";
		}
		
		if(text == null || msisdn == null) {
		    logger.warn("Bad message.");
            return "/jsp/blank";
		}
		
		SMSTransactionCode decoder = null;		
		
		// Query user from phone number
		
		SystemMessage invalidTextMessage = new SystemMessage(user, userAccountService.getSystemUser());
		
        try {

    		if(text.equals("#kagubuzz")) {
                decoder = new SMSTransactionCode(text, null);
        	}else{
        		decoder = new SMSTransactionCode(text);
        	}
            
            if (!decoder.hasValidHeader()) {
                logger.warn("Bad header.");
                invalidTextMessage.invalidText();
    	        messageDispatcher.sendMessage(invalidTextMessage);
                return "/jsp/blank";
            }
        }
        catch (Exception e) {
            logger.error("Bad header.",e);
            invalidTextMessage.invalidText();
	        messageDispatcher.sendMessage(invalidTextMessage);
            return "/jsp/blank";
        }
		
		logger.info("Message from " + user.getFirstName()); // 15032359028
		
		// Query ad from keyword
		
		if(text.equals("#kagubuzz")) {
			user.setPhoneVerified(true);
			crudDAO.update(user);
			SystemMessage verifiedPhone = new SystemMessage(user, userAccountService.getSystemUser());
			verifiedPhone.welcomePhoneVerify();
	        messageDispatcher.sendMessage(verifiedPhone);
			return "/jsp/blank";
		}
		
		TBLAd adRespondingTo = adDao.getAdFromSMSKeyword(decoder.getKeyword(), user);
		
		if(adRespondingTo == null) {
			logger.warn("Ad not found.");
            invalidTextMessage.invalidText();
	        messageDispatcher.sendMessage(invalidTextMessage);
			return "/jsp/blank";
		}
		
		logger.info("Responding to ad " + adRespondingTo.getTitle());
		
		// Query offer from transaction code
		
		if((decoder.getOfferId() > adRespondingTo.getOffers().size()) ||
		   (decoder.getOfferId() < 0)) {
			logger.warn("Offer not found.");
            invalidTextMessage.invalidText();
	        messageDispatcher.sendMessage(invalidTextMessage);
			return "/jsp/blank";
		}
		
		TBLDiscussionAd offer = adRespondingTo.getOffers().get(decoder.getOfferId());
		
		if(offer == null) {
			logger.warn("Offer not found.");
            invalidTextMessage.invalidText();
	        messageDispatcher.sendMessage(invalidTextMessage);
			return "/jsp/blank";
		}   
		
		logger.info("Offer id " + decoder.getOfferId());
			
		logger.info("Responding to offer from " + offer.getBuyer().getFirstName());
		
        // Make sure an offer has not already been accepted           
		
        if((AdMessageTypes.Accept == decoder.response) && (adService.acceptOffer(offer) == false)) {
            OfferMessage sellerOffer = new OfferMessage(offer, userAccountService.getSystemUser());
            sellerOffer.sellerAlreadyAccepted();
            messageDispatcher.sendMessage(sellerOffer);
            return "/jsp/blank";
        }
		
        // This sets all previous offer messages to active = false must set before new offermessage is attached
                
        adDao.setAllOffersInactiveForTransaction(offer);
        
        OfferMessage sellerOffer = new OfferMessage(offer, userAccountService.getSystemUser());
        OfferMessage buyerOffer = new OfferMessage(offer, userAccountService.getSystemUser());
        
        switch (decoder.response) {
            case Accept:       
                
                buyerOffer.buyerAccepted();
                sellerOffer.sellerAccepted();            
                break;
                
            case Decline: 
                
                buyerOffer.buyerDeclined();
                sellerOffer.sellerDeclined();
                break;
                
            case ThinkOnIt: 
                
                buyerOffer.buyerThinkOnIt();
                sellerOffer.sellerThinkOnIt();
                break;
        }
        
        messageDispatcher.sendMessage(sellerOffer);
		messageDispatcher.sendMessage(buyerOffer);
		
		return "/jsp/blank";
	}
}
