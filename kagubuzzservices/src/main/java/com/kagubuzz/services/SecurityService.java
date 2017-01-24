package com.kagubuzz.services;

import java.util.Calendar;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.enums.SpringRoles;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;


@Service
public class SecurityService {

    @Value("${recaptcha.publickey}")
    private String recaptchaPublicKey;
    @Value("${recaptcha.privatekey}")
    private String recaptchaPrivateKey;

    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired 
    UserDAO userDAO;
    
    public Object checkEntityOwner(long entityId, Class<? extends EntityWithOwner> clazz, TBLUser user) throws SecurityException {

        if (clazz == null)
            return null;
        if (user == null)
            return null;
        if (entityId < 0)
            return null;
 
        EntityWithOwner entity = crudDAO.getById(clazz, entityId);

        if(SpringSecurityUtilities.isAdmin(user)) {
            return entity;
        }
        
        entity = (entity.getOwner().equals(user)) ? entity : null;        
        
        if (entity == null) {
            throw new SecurityException("Enity attempting to be accessed does not belong to user " + user.getEmail());
        }              
        
        return entity;
    }
    
    public TBLUser checkUserCredentialsAndRole(String userEmail, String userPassword, SpringRoles springRole) {
        
        TBLUser user = userDAO.checkUserCredentials(userEmail, userPassword);
        
        if(user == null) return null;
        
        if(SpringSecurityUtilities.hasRole(user, springRole) == false) return null;
        
        return user;
    }
    

    public TBLUser checkSecurityCode(Long id, String securityCode) {

        if (id == null)
            return null;

        TBLUser user = crudDAO.getById(TBLUser.class, id);

        Calendar expires = Calendar.getInstance();

        expires.setTime(user.getSecurityCodeCreationDate());
        expires.add(Calendar.HOUR_OF_DAY, 24);

        if (!user.getSecurityCode().equals(securityCode)) {
            throw new SecurityException("User attempting to be accessed does not belong to user " + user.getEmail());
        }

        // Security code expires 24 hours after issuing it

        if (expires.before(Calendar.getInstance())) {
            throw new SecurityException("Security code expired");
        }

        return user;
    }

    public String getCaptchaHTML() {

        ReCaptchaImpl captcha = (ReCaptchaImpl) ReCaptchaFactory.newReCaptcha(recaptchaPublicKey, recaptchaPrivateKey, false);
        captcha.setIncludeNoscript(true);
        return captcha.createRecaptchaHtml(null, null);
    }

    public boolean checkCaptchaInput(String uresponse, String challenge, String remoteAddr) {

        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        
        reCaptcha.setPrivateKey(recaptchaPrivateKey);

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        return reCaptchaResponse.isValid();
    }

    public TBLDiscussionAd isTransactionParticipant(TBLUser user, long transactionId) {
        
        TBLDiscussionAd adDiscussion = crudDAO.getById(TBLDiscussionAd.class, transactionId);
        
        if(adDiscussion.getParticipants().contains(user)) return adDiscussion;
        
        return null;
    }

    public String getRecaptchaPublicKey() {
        return recaptchaPublicKey;
    }
}
