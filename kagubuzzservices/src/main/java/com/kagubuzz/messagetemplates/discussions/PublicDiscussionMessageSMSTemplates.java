package com.kagubuzz.messagetemplates.discussions;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

class PublicDiscussionMessageSMSTemplates extends PublicDiscussionMessageTemplate implements PublicDiscussionMessageTemplateFields {

        protected PublicDiscussionMessageSMSTemplates(TBLUser recipient, TBLMessage receipt) {
            super(recipient, receipt);
        }

        @Override
        public void discussionPostNewMessage() {
            // TODO Auto-generated method stub
            
        }
}