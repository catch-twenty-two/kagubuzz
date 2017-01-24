package com.kagubuzz.messagetemplates.systemmessages;

import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.SystemMessageTemplateFields;

class SystemMessageEmailTemplates extends SystemMessageTemplates implements SystemMessageTemplateFields {

    protected SystemMessageEmailTemplates(TBLUser recipient) {
        super(recipient);
    }

    @Override
    public void welcome() {
        subject = create("email-welcome-subject");
        body = create("email-welcome-body");
    }

    @Override
    public void appointment() {
        subject = create("email-appointment-subject");
        body = create("email-appointment-body");
    }

    @Override
    public void passwordReset() {
        subject = create("email-password-reset-subject");
        body = create("email-password-reset-body");
    }

    @Override
    public void passwordChanged() {
        subject = create("email-password-changed-subject");
        body = create("email-password-changed-body");
    }

    @Override
    public void beingFollowed() {
        subject = create("email-being-following-subject");
        body = create("email-being-following-body");
    }

    @Override
    public void followingPost(Post post) {
        subject = create("email-following-post-subject", post);
        body = create("email-following-post-body", post);
    }

    @Override
    public void verifyAccount() {
        subject = create("email-account-verify-subject");
        body = create("email-account-verify-body");
    }

    @Override
    public void verifyPhone() {
        // TODO Auto-generated method stub   
    }
    
    @Override
    public void welcomePhoneVerify() {
    	   // TODO Auto-generated method stub
    }
    
    @Override
    public void postExpiring(Post post) {
        subject = create("email-event-expiring-subject", post);
        body = create("email-event-expiring-body", post);
    }

    @Override
    public void bookmarkReminder(Post post) {
        subject = create("email-bookmark-reminder-subject", post);
        body = create("email-bookmark-reminder-body", post);
    }

    @Override
    public void discussionPostNewMessage(TBLMessageDiscussionPublic post) {
        subject = create("email-participant-post-subject", post);
        body = create("email-participant-post-body", post);     
    }
    
    @Override
    public void eventRating() { 
        // TODO Auto-generated method stub
    }

    @Override
    public void postRenewed(Post post) {
        // TODO Auto-generated method stub

    }

	@Override
	public void invalidText() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void autoSearchAlert(Post post) {
        subject = create("email-autosearch-post-subject", post);
        body = create("email-autosearch-post-body", post);        
    }

    @Override
    public void pulicDiscussionForEvent(TBLDiscussionPublic post) {
        subject = create("email-discussion-message-event-subject", post);
        body =    create("email-discussion-message-event-body", post);       
    }

    @Override
    public void recommended(TBLUser user) {
        subject = create("email-recommended-subject", user);
        body =    create("email-recommended-body", user); 
    }

}
