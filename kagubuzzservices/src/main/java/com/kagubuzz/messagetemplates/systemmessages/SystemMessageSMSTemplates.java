package com.kagubuzz.messagetemplates.systemmessages;

import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.SystemMessageTemplateFields;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessageTemplates;

// System text messgaes

class SystemMessageSMSTemplates extends SystemMessageTemplates implements SystemMessageTemplateFields {

	protected SystemMessageSMSTemplates(TBLUser recipient) {
        super(recipient);
    }

    @Override
    public void welcome() {
        //
	}

	@Override
	public void appointment() {	
	    //
	}

    @Override
    public void passwordReset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void passwordChanged() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beingFollowed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void verifyAccount() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void verifyPhone() {
        body = create("sms-verify-phone");           
    }
    
    @Override
    public void welcomePhoneVerify() {
        body = create("sms-welcome-phone-verify");           
    }

    @Override
    public void invalidText() {
        body = create("sms-invalid-text");           
    }


    @Override
    public void eventRating() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void followingPost(Post post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postExpiring(Post post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void bookmarkReminder(Post post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postRenewed(Post post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void discussionPostNewMessage(TBLMessageDiscussionPublic post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void autoSearchAlert(Post post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pulicDiscussionForEvent(TBLDiscussionPublic post) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void recommended(TBLUser user) {
        // TODO Auto-generated method stub
        
    }
}
