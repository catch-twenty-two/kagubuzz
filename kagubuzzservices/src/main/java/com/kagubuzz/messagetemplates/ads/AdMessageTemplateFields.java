package com.kagubuzz.messagetemplates.ads;

import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface AdMessageTemplateFields {
    
     abstract void buyerInitialContact() ;
     abstract void buyerDeclined();
     abstract void buyerThinkOnIt();  
     abstract void buyerAccepted();
     abstract void sellerInitialContact();
     abstract void sellerAccepted();
     abstract void sellerAlreadyAccepted();
     abstract void sellerThinkOnIt();
     abstract void sellerDeclined();
     abstract void offerCanceled(TBLUser recipient);
     abstract void exchangeFinalized(TBLUser recipient);
     abstract void offerReminder();
     abstract void exchangeRateReminder(TBLUser recipient);
}

