package com.kagubuzz.messagetemplates.qaadmessages;

import com.kagubuzz.datamodels.hibernate.TBLMessage;

public interface QAAdTemplateFields {   
    public abstract void answer(TBLMessage receipt);
    public abstract void question(TBLMessage receipt);
}

