package com.kagubuzz.messagetemplates.qaadmessages;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

// System text messgaes

public class AdQAMessageSMSTemplates extends ADQAMessageTemplates implements QAAdTemplateFields {

    protected AdQAMessageSMSTemplates(TBLUser recipient, TBLDiscussionAdQA discussion) {
        super(recipient, discussion);
    }

    @Override
    public void answer(TBLMessage receipt) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void question(TBLMessage receipt) {
        body = create("sms-ad-question-body", receipt);    
    }
}
