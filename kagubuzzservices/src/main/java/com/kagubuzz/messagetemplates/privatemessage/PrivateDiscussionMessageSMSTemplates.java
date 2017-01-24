package com.kagubuzz.messagetemplates.privatemessage;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

class PrivateDiscussionMessageSMSTemplates extends PrivateDiscussionMessageTemplate implements PrivateDiscussionMessageTemplateFields {

        protected PrivateDiscussionMessageSMSTemplates(TBLUser recipient, TBLMessage receipt) {
            super(recipient, receipt);
        }

        @Override
        public void discussionNewPrivateMessage() {
            body = "temp";//create("sms-private-message-new-body");  
        }
}