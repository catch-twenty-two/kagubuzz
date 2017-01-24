package com.kagubuzz.messagetemplates.ads.offermessages;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.ads.AdMessageTemplateFields;

class OfferMessageEmailTemplates extends OfferMessageTemplates implements AdMessageTemplateFields {
    
	protected OfferMessageEmailTemplates(TBLUser recipient, TBLDiscussionAd discussion) {
        super(recipient, discussion);
    }

    @Override
    public void sellerInitialContact()	{
		body = create("email-offer-body-seller");
		subject = create("email-offer-subject-seller");
	}

	@Override
	public void sellerAccepted() {
		body = create("email-offer-body-seller-accept");
		subject = create("email-offer-subject-seller-accept");
	}

	@Override
	public void sellerDeclined() {
		body = create("email-offer-body-seller-decline");
		subject = create("email-offer-subject-seller-decline");
	}

	@Override
	public void sellerThinkOnIt() {
		body = create("email-offer-body-seller-thinkonit");
		subject = create("email-offer-subject-seller-thinkonit");		
	}
	
    @Override
    public void buyerInitialContact() {
        subject = create("email-offer-subject-buyer");
        body =  create("email-offer-body-buyer");
    }
    
    @Override
    public void buyerAccepted() {
        subject =  create("email-offer-subject-buyer-accept");
        body = create("email-offer-body-buyer-accept");
    }
    
    @Override
    public void buyerDeclined() {
        subject = create("email-offer-subject-buyer-decline");
        body =  create("email-offer-body-buyer-decline");
    }
    
    @Override
    public void buyerThinkOnIt() {
        subject =  create("email-offer-subject-buyer-thinkonit");
        body = create("email-offer-body-buyer-thinkonit");
    }

    @Override
    public void sellerAlreadyAccepted() {
        body = create("email-offer-body-seller-already-accepted");
        subject = create("email-offer-subject-seller");
    }

    @Override
    public void offerCanceled(TBLUser recipient) {
        body = create("email-exchange-canceled-body");
        subject = create("email-exchange-canceled-subject");  
    }

    @Override
    public void exchangeFinalized(TBLUser recipient) {
        body = create("email-exchange-finalized-body");
        subject = create("email-exchange-finalized-subject");  
    }

    @Override
    public void offerReminder() {
        body = create("email-exchange-body-seller-reminder");
        subject = create("email-exchange-subject-seller-reminder");  
    }

    @Override
    public void exchangeRateReminder(TBLUser recipient) {
        body = create("email-exchange-rate-body-reminder");
        subject = create("email-exchange-rate-subject-reminder");  
    }
}