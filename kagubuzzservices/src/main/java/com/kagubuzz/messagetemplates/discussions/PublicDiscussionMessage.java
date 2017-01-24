package com.kagubuzz.messagetemplates.discussions;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.message.types.Message;
import com.kagubuzz.messages.TemplateMessage;

public class PublicDiscussionMessage extends Message {

    PublicDiscussionMessageEmailTemplates discussionMessageEmailTemplates;
    PublicDiscussionMessageSMSTemplates discussionMessageSMSTemplates;
    
    public PublicDiscussionMessage(TBLDiscussionPublic publicDisucssion,
                                   TBLUser sender,
                                   TBLUser recipient,
                                   TBLUser systemUser,
                                   String message,
                                   String subject) {

        
        discussion = publicDisucssion;        
        receipt = new TBLMessageDiscussionPublic();
        
        if(getDiscussion().getMessages().isEmpty()) {
            getReceipt().setFirstMessage(true);
            getDiscussion().addParticipant(systemUser);
        }
        
        getDiscussion().addParticipant(sender);
        getDiscussion().addMessage(getReceipt());
        getDiscussion().setUpdatedDate(new Date());
        getReceipt().setRecipient(recipient);
        getReceipt().setSubject(subject);
        getReceipt().setMessage(message);
        getReceipt().setSender(sender);
        getReceipt().setSystemMessage(false);
        getReceipt().setCreatedDate(new Date());
        getReceipt().setRecipientCanReply(true);
        getReceipt().setReadByRecipient(false);
        getReceipt().setIsPrivate(false);
        getReceipt().setDiscussion(getDiscussion());
        getReceipt().setPublicCanReply(true);
        
        
        discussionMessageEmailTemplates = new PublicDiscussionMessageEmailTemplates(recipient, getReceipt());
        discussionMessageSMSTemplates = new PublicDiscussionMessageSMSTemplates(recipient, getReceipt());
        
        // Only one type of email response so far no need for multiple methods
        
        discussionMessageEmailTemplates.discussionPostNewMessage();
        discussionMessageSMSTemplates.discussionPostNewMessage();
    }
 
    @Override
    public TBLDiscussionPublic getDiscussion() { return (TBLDiscussionPublic) this.discussion; }

    @Override
    public TBLMessageDiscussionPublic getReceipt() { return (TBLMessageDiscussionPublic) this.receipt; }
    
    @Override
    public TemplateMessage getInstanceOfSMS() {
        return discussionMessageSMSTemplates;
    }

    @Override
    public TemplateMessage getInstanceOfEmail() {
        return discussionMessageEmailTemplates;
    }
}
