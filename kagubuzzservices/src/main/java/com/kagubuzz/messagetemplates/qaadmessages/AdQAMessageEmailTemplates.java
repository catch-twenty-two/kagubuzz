package com.kagubuzz.messagetemplates.qaadmessages;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

class AdQAMessageEmailTemplates extends ADQAMessageTemplates implements QAAdTemplateFields {

    public AdQAMessageEmailTemplates(TBLUser recipient, TBLDiscussionAdQA discussion) {
        super(recipient, discussion);
    }

    @Override
    public void answer(TBLMessage receipt) {
        body = create("email-ad-answer-body", receipt);
        subject = create("email-ad-answer-subject", receipt);
    }

    @Override
    public void question(TBLMessage receipt) {
        body = create("email-ad-question-body", receipt);
        subject = create("email-ad-question-subject", receipt);    
    }
}
