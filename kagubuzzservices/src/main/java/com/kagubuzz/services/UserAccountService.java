package com.kagubuzz.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.exceptions.EmailInUseExecption;
import com.kagubuzz.exceptions.PhoneNumberInUseExecption;
import com.kagubuzz.exceptions.PhoneVerifyExecption;
import com.kagubuzz.exceptions.UnknownZipCodeExecption;
import com.kagubuzz.utilities.KaguImage;

public interface UserAccountService {
    
    public abstract String getUserFileDirectoryPath(TBLUser user);

    @Transactional(readOnly = true)
    public abstract TBLUser getSystemUser();

    public abstract TBLUser createUser(String name, String currentEmail, String email, String password, int timeZone, String zip,  boolean socialAccount,
            KaguImage avatar) throws EmailInUseExecption, UnknownZipCodeExecption;

    public abstract TBLUser createGenericUser();

    @Transactional(readOnly = false)
    public abstract void refreshSecurityCode(TBLUser user);

    @Transactional(readOnly = false)
    public abstract void saveIPToUser(String ip, TBLUser user);

    @SuppressWarnings("unchecked")
    public abstract List<String> getKnownIPsForUser(TBLUser user);

    TBLUser updateUser(TBLUser user, 
            String firstName, 
            String lastName, 
            String email, 
            String password, 
            Integer timeZone, 
            String zip, 
            String phoneNumber,
            boolean socialAccount, 
            String avatarImageName, 
            String swapLocation, 
            String swapLocationName, 
            Boolean smsAdNotifications, 
            Boolean showNotifications, Boolean emailNotifications) throws EmailInUseExecption, PhoneNumberInUseExecption, UnknownZipCodeExecption, PhoneVerifyExecption;

    void init();

    void doUser5MinTasks();

    int getTrustLevel(TBLUser user);

    long getUserReputation(TBLUser user);

    long getUserRecommendationCount(TBLUser user);

    void injectUserProfile(ModelMap modelMap, TBLUser user, TBLUser userInquiry);

}