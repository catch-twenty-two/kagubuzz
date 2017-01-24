package com.kagubuzz.services;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.kagubuzz.datamodels.hibernate.TBLAd;

public interface StringTemplateService {

    public abstract ST getTemplateModal(String name);

    public abstract ST getTemplateError(String name);
    
    public abstract ST getTemplateNotification(String name);

    STGroup getTemplateModalGroup(String name);

    String getTemplatePopver(String name);

    String getTemplateModals(String name);


    STGroup getTemplateGroupAds(TBLAd ad);
    
}