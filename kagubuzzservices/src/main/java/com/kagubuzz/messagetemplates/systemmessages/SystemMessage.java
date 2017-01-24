package com.kagubuzz.messagetemplates.systemmessages;

import java.util.Date;


import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.Message;
import com.kagubuzz.message.types.SystemMessageTemplateFields;
import com.kagubuzz.messages.TemplateMessage;

public class SystemMessage extends Message implements SystemMessageTemplateFields{
    
    SystemMessageTemplates templates;   
    SystemMessageSMSTemplates smsMessage;
    SystemMessageEmailTemplates emailMessage;

    TBLUser systemUser;
    
	public SystemMessage(TBLUser recipient, TBLUser systemUser) {
	    
	    templates = new SystemMessageTemplates(recipient);
	    
		this.recipient = recipient;
		discussion = new TBLDiscussionPrivate();
		receipt = new TBLMessageDiscussionPrivate();
		this.systemUser = systemUser;
		
		getDiscussion().setActive(true);
		getDiscussion().addParticipant(recipient);
		getDiscussion().addParticipant(systemUser);
		((TBLDiscussionPrivate) getDiscussion()).addMessage((TBLMessageDiscussionPrivate) getReceipt());

		getReceipt().setRecipient(recipient);
		getReceipt().setSender(systemUser);
		getReceipt().setSystemMessage(true);
		getReceipt().setCreatedDate(new Date());
		getReceipt().setRecipientCanReply(false);
		((TBLMessageDiscussionPrivate) getReceipt()).setDiscussion( (TBLDiscussionPrivate) discussion);
		getReceipt().setFirstMessage(true);		
		
	    smsMessage = new SystemMessageSMSTemplates(recipient);
	    emailMessage = new SystemMessageEmailTemplates(recipient);
	}
	
	// Receipts
	
	public void welcome() {
		
		getReceipt().setSubject(templates.create("receipt-welcome-subject"));
		getReceipt().setMessage(templates.create("receipt-welcome-body"));	
		
		getReceipt().setDeleteAfterNotify(true);
		getReceipt().setRecipientCanReply(false);

		emailMessage.welcome();
		smsMessage.welcome();
	}
	
	public void appointment() {
		
		getReceipt().setSubject(templates.create("receipt-appointment-subject"));
		getReceipt().setMessage(templates.create("receipt-appointment-body"));

        emailMessage.appointment();
        smsMessage.appointment();			
	}
	
	public void passwordReset() {
        
        getReceipt().setSubject(templates.create("receipt-password-reset-subject"));
        getReceipt().setMessage(templates.create("receipt-password-reset-body"));     
        
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.passwordReset();
        smsMessage.passwordReset();
    }

    public void passwordChanged() {

        getReceipt().setSubject(templates.create("receipt-password-changed-subject"));
        getReceipt().setMessage(templates.create("receipt-password-changed-body"));
        
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.passwordChanged();
        smsMessage.passwordChanged();
    }
    
    public void beingFollowed() {

        getReceipt().setSubject(templates.create("receipt-being-followed-subject"));
        getReceipt().setMessage(templates.create("receipt-being-followed-body"));
        
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.beingFollowed();
        smsMessage.beingFollowed();
    }
    
    public void recommended(TBLUser user) {

        getReceipt().setSubject(templates.create("receipt-recommended-subject", user));
        getReceipt().setMessage(templates.create("receipt-recommended-body", user));
        
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.recommended(user);
        smsMessage.recommended(user);
    }
    
    public void eventRating() {

        getReceipt().setSubject(templates.create("receipt-new-rating-subject"));
        getReceipt().setMessage(templates.create("receipt-new-rating-body"));
        
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);
 
        emailMessage.eventRating();
        smsMessage.eventRating();
    }

    public void followingPost(Post post) {
        
        getReceipt().setSubject(templates.create("receipt-following-post-subject", post));
        getReceipt().setMessage(templates.create("receipt-following-post-body", post));

        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.followingPost(post);
        smsMessage.followingPost(post);
    }
    
    public void postExpiring(Post post) {
        
        getReceipt().setSubject(templates.create("receipt-post-expiring-subject", post));
        getReceipt().setMessage(templates.create("receipt-post-expiring-body", post));

        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.postExpiring(post);
        smsMessage.postExpiring(post);
    }
    
    public void bookmarkReminder(Post post) {
        
        getReceipt().setSubject(templates.create("receipt-bookmark-reminder-subject", post));
        getReceipt().setMessage(templates.create("receipt-bookmark-reminder-body", post));

        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.bookmarkReminder(post);
        smsMessage.bookmarkReminder(post);
    }
    
    public void postRenewed(Post post) {
        

        getReceipt().setSubject(templates.create("receipt-post-renewed-subject", post));
        getReceipt().setMessage(templates.create("receipt-post-renewed-body", post));

        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.postRenewed(post);
        smsMessage.postRenewed(post);
    }
    
    public void verifyAccount() {

        // No message history generated for this message type
        
        setSaveMessageHistory(false);
        
        emailMessage.verifyAccount();
        smsMessage.verifyAccount();
    }
    
    public void verifyPhone() {
        getReceipt().setSubject(templates.create("receipt-verify-phone-subject"));
        getReceipt().setMessage(templates.create("receipt-verify-phone-body"));
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);
    	getReceipt().setDeliveryMethod(DeliveryMethod.Text);
    	setSaveMessageHistory(true);
        smsMessage.verifyPhone();
    }
    
 
    public void welcomePhoneVerify() {
        getReceipt().setSubject(templates.create("receipt-welcome-verify-subject"));
        getReceipt().setMessage(templates.create("receipt-welcome-verify-body"));
        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);
    	getReceipt().setDeliveryMethod(DeliveryMethod.Text);
    	setSaveMessageHistory(true);
        smsMessage.welcomePhoneVerify();
    }
    
    public void invalidText() {
    	getReceipt().setDeliveryMethod(DeliveryMethod.Text);
    	setSaveMessageHistory(false);
        smsMessage.invalidText();
    }
    
    @Override
    public void autoSearchAlert(Post post) {
        getReceipt().setSubject(templates.create("receipt-autosearch-post-subject", post));
        getReceipt().setMessage(templates.create("receipt-autosearch-post-body", post));

        getReceipt().setRecipientCanReply(false);
        getReceipt().setDeleteAfterNotify(true);

        emailMessage.autoSearchAlert(post);
        smsMessage.autoSearchAlert(post);   
    }
    
    public void discussionPostNewMessage(TBLMessageDiscussionPublic post) {               
        
        getReceipt().setSubject(templates.create("receipt-participant-post-subject",post));
        getReceipt().setMessage(templates.create("receipt-participant-post-body", post));        
        
        receipt.setRecipientCanReply(false);
        receipt.setDeleteAfterNotify(true);        
        
        emailMessage.discussionPostNewMessage(post);
        smsMessage.discussionPostNewMessage(post);
    }
    
    @Override
    public void pulicDiscussionForEvent(TBLDiscussionPublic discussion) {
        
        getReceipt().setSubject(templates.create("receipt-discussion-post-event-subject",discussion));
        getReceipt().setMessage(templates.create("receipt-discussion-post-event-body", discussion));        
        
        receipt.setRecipientCanReply(false);
        receipt.setDeleteAfterNotify(true);    
        
        emailMessage.pulicDiscussionForEvent(discussion);
        smsMessage.pulicDiscussionForEvent(discussion);
    }
 
	
	@Override
	public TBLDiscussionBase getDiscussion() {return discussion;};
	
	@Override
	public TBLMessage getReceipt() {	return receipt;	}

    @Override
    public TemplateMessage getInstanceOfSMS() { return smsMessage; }

    @Override
    public TemplateMessage getInstanceOfEmail() { return emailMessage; }



}
