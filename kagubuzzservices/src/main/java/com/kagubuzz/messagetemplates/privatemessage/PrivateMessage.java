package com.kagubuzz.messagetemplates.privatemessage;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.Message;
import com.kagubuzz.messages.TemplateMessage;

public class PrivateMessage extends Message  {
    
    PrivateDiscussionMessageSMSTemplates smsTemplates;
    PrivateDiscussionMessageEmailTemplates emailTemplates;
    
    public PrivateMessage(TBLDiscussionBase discussion,
            TBLUser sender,
            TBLUser recipient,
            TBLMessage receipt,
            String message) {
        
        this.receipt = receipt;
        this.discussion = discussion;
        
        getDiscussion().setSticky(true);
        getDiscussion().setUpdatedDate(new Date());
        receipt.setRecipient(recipient);
        receipt.setRecipientCanReply(true);
        receipt.setSystemMessage(false);        

        receipt.setCreatedDate(new Date());
        receipt.setMessage(message);
        receipt.setSubject(getDiscussion().getTitle());
        receipt.setIsPrivate(true);
        receipt.setSender(sender);
        
        smsTemplates = new PrivateDiscussionMessageSMSTemplates(recipient, receipt);
        emailTemplates = new PrivateDiscussionMessageEmailTemplates(recipient, receipt);
        
        smsTemplates.discussionNewPrivateMessage();
        emailTemplates.discussionNewPrivateMessage();
    }

    @Override
    public TBLDiscussionBase getDiscussion() {  return this.discussion; }

    @Override
    public TBLMessage getReceipt() { return this.receipt; }

    @Override
    public TemplateMessage getInstanceOfSMS() { return smsTemplates; }

    @Override
    public TemplateMessage getInstanceOfEmail() { return emailTemplates; }
   
}

