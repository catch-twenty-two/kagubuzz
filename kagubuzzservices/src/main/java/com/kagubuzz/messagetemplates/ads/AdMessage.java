package com.kagubuzz.messagetemplates.ads;

import com.kagubuzz.datamodels.enums.TransactionCancelTypes;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.Message;

public abstract class AdMessage extends Message implements AdMessageTemplateFields {
    
    protected TBLUser systemUser;
    
    protected AdMessage(TBLUser systemUser) {
        this.systemUser = systemUser;
    }
    
	@Override
	public TBLDiscussionAd getDiscussion() {return (TBLDiscussionAd) discussion;};
	
	@Override
	public TBLMessageAdOffer getReceipt() {	return (TBLMessageAdOffer) receipt;	}
}