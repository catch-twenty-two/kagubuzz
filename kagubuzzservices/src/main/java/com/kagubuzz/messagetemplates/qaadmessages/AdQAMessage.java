package com.kagubuzz.messagetemplates.qaadmessages;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdQuestion;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.Message;
import com.kagubuzz.messages.TemplateMessage;

public class AdQAMessage extends Message {
    
    TBLDiscussionAdQA discussion;
    
    AdQAMessageEmailTemplates adQAMessageEmailTemplate;
    AdQAMessageSMSTemplates adQAMessageSMSTemplate;
    
    public AdQAMessage(TBLDiscussionAdQA discussion) {
        
        this.discussion = discussion;
        getDiscussion().setSticky(true);
        
        adQAMessageEmailTemplate = new AdQAMessageEmailTemplates(null, (TBLDiscussionAdQA) discussion);
        adQAMessageSMSTemplate = new AdQAMessageSMSTemplates(null, (TBLDiscussionAdQA) discussion);
    }
    
    public void answer(TBLMessageAdQuestion question, String answer) {
        
        receipt =  new TBLMessageAdQuestion();
        
        receipt.setRecipient(question.getSender());
        receipt.setSender(discussion.getOwner());
        receipt.setRecipientCanReply(false);
        
        createAdMessage(answer, getDiscussion().getAd().getOwner());
        
        adQAMessageEmailTemplate.answer(receipt);
        adQAMessageSMSTemplate.answer(receipt);
    }
    
    public void question(String question, TBLUser from) {
        
        receipt =  new TBLMessageAdQuestion();
        
        receipt.setRecipient(discussion.getOwner());
        receipt.setRecipientCanReply(true);
        
        receipt.setDeliveryMethod(getDiscussion().getAd().getContactMethod());
        
        createAdMessage(question, from); 
        
        adQAMessageEmailTemplate.question(receipt);
        adQAMessageSMSTemplate.question(receipt);
    }

    public void createAdMessage(String message, TBLUser user) {
        
        getReceipt().setDiscussion(discussion);
        
        if(getDiscussion().getMessages().isEmpty()) {
            getReceipt().setFirstMessage(true);           
        }
        
        getDiscussion().addParticipant(user);
        getDiscussion().addMessage(getReceipt());
        
        receipt.setCreatedDate(new Date());
        receipt.setMessage(message);
        receipt.setSubject(discussion.getAd().getTitle());
        receipt.setIsPrivate(false);
        receipt.setSender(user);
    }
	
	@Override
	public TBLDiscussionAdQA getDiscussion() {return (TBLDiscussionAdQA) discussion;};
	
	@Override
	public TBLMessageAdQuestion getReceipt() {	return (TBLMessageAdQuestion) receipt;	}

    @Override
    public TemplateMessage getInstanceOfSMS() { return adQAMessageSMSTemplate; }

    @Override
    public TemplateMessage getInstanceOfEmail() { return adQAMessageEmailTemplate; }
}