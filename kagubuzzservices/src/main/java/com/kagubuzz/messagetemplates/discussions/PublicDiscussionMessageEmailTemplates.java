package com.kagubuzz.messagetemplates.discussions;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

class PublicDiscussionMessageEmailTemplates extends PublicDiscussionMessageTemplate implements PublicDiscussionMessageTemplateFields {
    
	protected PublicDiscussionMessageEmailTemplates(TBLUser recipient, TBLMessage message) {
        super(recipient, message);
    }

    @Override
    public void discussionPostNewMessage() {
        subject = create("email-discussion-message-new-subject");
        body = create("email-discussion-message-new-body");     
    }
}