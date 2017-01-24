package com.kagubuzz.messagetemplates.ads.offermessages;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messagetemplates.ads.AdMessageTemplateFields;

class OfferMessageSMSTemplates extends OfferMessageTemplates implements AdMessageTemplateFields {

        protected OfferMessageSMSTemplates(TBLUser recipient, TBLDiscussionAd discussion) {
            super(recipient, discussion);
        }

        @Override
        public void sellerInitialContact()   {
            body = create("sms-offer-seller");
        }
        
        @Override
        public void sellerAccepted() {
            body = create("sms-offer-seller-accept");
        }
        
        @Override
        public void sellerDeclined() {
            body = create("sms-offer-seller-decline");
        }
        
        @Override
        public void sellerThinkOnIt() {
            body = create("sms-offer-seller-thinkonit");
        }

        @Override
        public void buyerInitialContact() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void buyerAccepted() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void buyerDeclined() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void buyerThinkOnIt() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void sellerAlreadyAccepted() {
            body = create("sms-offer-seller-already-accepted");
            
        }

        @Override
        public void offerCanceled(TBLUser recipient) {
            body = create("sms-exchange-canceled");
        }

        @Override
        public void exchangeFinalized(TBLUser recipient) {
            body = create("sms-exchange-finalized"); 
        }

        @Override
        public void offerReminder() {
            body = create("sms-exchange-reminder");
        }

        @Override
        public void exchangeRateReminder(TBLUser recipient) {
            body = create("sms-exchange-rate-reminder");
        }
    }