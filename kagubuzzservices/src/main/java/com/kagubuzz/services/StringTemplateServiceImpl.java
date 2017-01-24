package com.kagubuzz.services;

import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;
import org.stringtemplate.v4.STRawGroupDir;

import com.kagubuzz.datamodels.hibernate.TBLAd;

@Service
public class StringTemplateServiceImpl implements StringTemplateService {
    
    STRawGroupDir errors = new STRawGroupDir("stringtemplates/errors",'$','$');
    STRawGroupDir modals = new STRawGroupDir("stringtemplates/modal",'$','$');
    STRawGroupDir notifications = new STRawGroupDir("stringtemplates/notifications",'$','$');
    STRawGroupDir stringTemplates = new STRawGroupDir("stringtemplates",'$','$');
    
    @Override
    public ST getTemplateError(String name) {
        return errors.getInstanceOf(name);
    }
    
    @Override
    public ST getTemplateModal(String name) {
        return modals.getInstanceOf(name);
    }
    
    @Override
    public String getTemplatePopver(String name) {
        STGroup group = new STGroupString(stringTemplates.getInstanceOf("pop-overs").render());
        group.delimiterStartChar = '$';
        group.delimiterStopChar ='$';
        
        return group.getInstanceOf(name).render();
    }

    @Override
    public String getTemplateModals(String name) {
        STGroup group = new STGroupString(stringTemplates.getInstanceOf("modals").render());
        group.delimiterStartChar = '$';
        group.delimiterStopChar ='$';
        
        return group.getInstanceOf(name).render();
    }
    
    @Override
    public STGroup getTemplateModalGroup(String name) {
        STGroup group = new STGroupString(modals.getInstanceOf(name).render());
        group.delimiterStartChar = '$';
        group.delimiterStopChar ='$';
        
        return group;
    }
    
    @Override
    public STGroup getTemplateGroupAds(TBLAd ad) {
        ST st = stringTemplates.getInstanceOf("ads");
        
        st.add("ad_owner", ad.getOwner().getFirstName());
        
        STGroup group = new STGroupString(st.render());
        group.delimiterStartChar = '$';
        group.delimiterStopChar ='$';
        
        return group;
    }
    
    @Override
    public ST getTemplateNotification(String name) {
        ST template = notifications.getInstanceOf(name);
        
        if(template == null) {
            System.out.print("Warning template "+name+" not found.");          
        }
        
        return template;
    }
}