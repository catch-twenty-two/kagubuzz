package com.kagubuzz.messagetemplates.privatemessage;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

class PrivateDiscussionMessageEmailTemplates extends PrivateDiscussionMessageTemplate implements PrivateDiscussionMessageTemplateFields {

    protected PrivateDiscussionMessageEmailTemplates(TBLUser recipient, TBLMessage message) {
        super(recipient, message);
    }

    @Override
    public void discussionNewPrivateMessage() {
        subject = create("email-private-message-new-subject");
        body = create("email-private-message-new-body");
    }
}