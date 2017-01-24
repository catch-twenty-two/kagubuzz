package com.kagubuzz.messagetemplates.ads.offermessages;

import java.util.Date;

import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.messagetemplates.ads.AdMessage;

public class OfferMessage extends AdMessage {

    OfferMessageEmailTemplates emailTemplates;
    OfferMessageTemplates templates;
    OfferMessageSMSTemplates smsTemplates;

    public OfferMessage(TBLDiscussionAd discussion, TBLUser systemUser) {
        super(systemUser);

        this.discussion = discussion;

        receipt = new TBLMessageAdOffer();

        receipt.setSubject(getDiscussion().getAd().getTitle());
        receipt.setSender(systemUser);
        receipt.setIsPrivate(true);
        receipt.setSystemMessage(true);
        receipt.setCreatedDate(new Date());
        receipt.setRecipientCanReply(false);
        discussion.addMessage(getReceipt());
        getReceipt().setDiscussionAd(discussion);
        getDiscussion().setSticky(true);
        
        templates = new OfferMessageTemplates(systemUser, discussion);
        emailTemplates = new OfferMessageEmailTemplates(systemUser, discussion);
        smsTemplates = new OfferMessageSMSTemplates(systemUser, discussion);
    }

    // Buyer receipts

    @Override
    public void buyerInitialContact() {
        
        getDiscussion().setAdDiscussionState(AdDiscussionState.WaitingForResponse);
        getDiscussion().setUpdatedDate(new Date());
        
        receipt.setMessage(templates.create("receipt-offer-buyer"));
        receipt.setRecipient(getDiscussion().getBuyer());
        
        emailTemplates.buyerInitialContact();
        smsTemplates.buyerThinkOnIt();
    }

    @Override
    public void buyerThinkOnIt() {

        getDiscussion().setAdDiscussionState(AdDiscussionState.ThinkingAboutIt);
        getDiscussion().setUpdatedDate(new Date());
        
        receipt.setMessage(templates.create("receipt-offer-buyer-thinkonit"));
        receipt.setRecipient(getDiscussion().getBuyer());
        receipt.setSender(getDiscussion().getSeller());              
        
        emailTemplates.buyerThinkOnIt();
        smsTemplates.buyerThinkOnIt();
    }

    @Override
    public void buyerDeclined() {

        getDiscussion().setUpdatedDate(new Date());
        receipt.setMessage(templates.create("receipt-offer-buyer-decline"));
        receipt.setRecipient(getDiscussion().getBuyer());
        receipt.setSender(getDiscussion().getSeller());
        
        emailTemplates.buyerDeclined();
        smsTemplates.buyerDeclined();
    }

    @Override
    public void buyerAccepted() {

        getDiscussion().setUpdatedDate(new Date());
        receipt.setMessage(templates.create("receipt-offer-buyer-accept"));
        receipt.setSender(getDiscussion().getSeller());
        receipt.setRecipient(getDiscussion().getBuyer());

        receipt.setRecipientCanReply(true);

        emailTemplates.buyerAccepted();
        smsTemplates.buyerAccepted();

    }

    @Override
    public void sellerInitialContact() {

        getReceipt().setOfferActive(true);  
        getDiscussion().setUpdatedDate(new Date());
        
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());        
        receipt.setMessage(templates.create("receipt-offer-seller"));
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setSender(getDiscussion().getBuyer());
        
        emailTemplates.sellerInitialContact();
        smsTemplates.sellerInitialContact();
    }

    @Override
    public void sellerAccepted() {
        
        getDiscussion().setUpdatedDate(new Date());
        receipt.setMessage(templates.create("receipt-offer-seller-accept"));
        receipt.setSender(getDiscussion().getBuyer());
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setRecipientCanReply(true);        
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());

        emailTemplates.sellerAccepted();
        smsTemplates.sellerAccepted();
    }

    @Override
    public void sellerThinkOnIt() {

        getDiscussion().setUpdatedDate(new Date());
        getDiscussion().refreshSecurityCode();
        getDiscussion().setAdDiscussionState(AdDiscussionState.ThinkingAboutIt);
        
        receipt.setMessage(templates.create("receipt-offer-seller-thinkonit"));        
        receipt.setSender(getDiscussion().getBuyer());
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setRecipientCanReply(true);              
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        getReceipt().setOfferActive(true);
        
        emailTemplates.sellerThinkOnIt();
        smsTemplates.sellerThinkOnIt();
    }

    @Override
    public void sellerDeclined() {
       
        getDiscussion().setUpdatedDate(new Date());
        getDiscussion().refreshSecurityCode();
        getDiscussion().setAdDiscussionState(AdDiscussionState.Declined);
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());     
        receipt.setMessage(templates.create("receipt-offer-seller-decline"));
        receipt.setSender(getDiscussion().getBuyer());
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setRecipientCanReply(true);

        emailTemplates.sellerDeclined();
        smsTemplates.sellerDeclined();
    }
    
    @Override
    public void sellerAlreadyAccepted() {

        getDiscussion().refreshSecurityCode();
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        
        receipt.setMessage(templates.create("receipt-offer-seller-already-accepted"));
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setSender(getDiscussion().getBuyer());
        getReceipt().setOfferActive(true);        
        
        emailTemplates.sellerAlreadyAccepted();
        smsTemplates.sellerAlreadyAccepted();
    }

    @Override
    public void offerReminder() {
        
        getReceipt().setOfferActive(true);  
        
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());        
        receipt.setMessage(templates.create("receipt-offer-reminder"));
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setSender(getDiscussion().getBuyer());
        
        emailTemplates.offerReminder();
        smsTemplates.offerReminder();
    }
    
    @Override
    public void exchangeFinalized(TBLUser recipient) {
        
        getDiscussion().setUpdatedDate(new Date());
        
        if(recipient == getDiscussion().getAd().getOwner()) {
            receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        }
        
        receipt.setMessage(templates.create("receipt-exchange-finalized"));
        receipt.setRecipient(recipient);
        receipt.setSender(getDiscussion().getOppositeParty(recipient));
        getReceipt().setOfferActive(false);        
        
        emailTemplates.exchangeFinalized(recipient);
        smsTemplates.exchangeFinalized(recipient);
    }
    
    @Override
    public void offerCanceled(TBLUser recipient) {
        
        getDiscussion().setUpdatedDate(new Date());
        
        if(recipient == getDiscussion().getAd().getOwner()) {
            receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        }
        
        receipt.setMessage(templates.create("receipt-exchange-canceled"));
        receipt.setRecipient(recipient);
        receipt.setSender(getDiscussion().getOppositeParty(recipient));
        getReceipt().setOfferActive(false);        
        receipt.setRecipientCanReply(true);
        
        emailTemplates.offerCanceled(recipient);
        smsTemplates.offerCanceled(recipient);    
    }
    
    @Override
    public void exchangeRateReminder(TBLUser recipient) {
        
        if(recipient == getDiscussion().getAd().getOwner()) {
            receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        }
        
        getReceipt().setOfferActive(true);  
        
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());        
        receipt.setMessage(templates.create("receipt-exchange-rate-reminder"));
        receipt.setRecipient(getDiscussion().getSeller());
        receipt.setSender(getDiscussion().getBuyer());
        
        emailTemplates.exchangeRateReminder(recipient);
        smsTemplates.exchangeRateReminder(recipient);
    }

    @Override
    public TemplateMessage getInstanceOfSMS() { return smsTemplates; }

    @Override
    public TemplateMessage getInstanceOfEmail() { return emailTemplates; }

}

