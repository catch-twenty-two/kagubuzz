package com.kagubuzz.services;

import java.util.List;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.ExchangeRatingTypes;
import com.kagubuzz.datamodels.enums.TransactionCancelTypes;
import com.kagubuzz.datamodels.enums.AdPerUnit;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface AdService {

    void renewAd(TBLAd ad);

    String getAdImageURL4(TBLAd ad);

    String getAdImageURL3(TBLAd ad);

    String getAdImageURL2(TBLAd ad);

    String getAdImageURL1(TBLAd ad);

    void setSMSKeyWord(TBLUser user, TBLAd newAd);

    List<String> getImagePathsURLS(TBLAd ad);

    TBLDiscussionAd createOffer(TBLUser user, TBLAd ad); 

    void rateTransaction(TBLDiscussionAd transaction, ExchangeRatingTypes exchangeRatingType, TBLUser user, String review);

    void notifyUsersOfClosedItem(TBLAd ad);

    boolean acceptOffer(TBLDiscussionAd transaction);

    TBLAd getGenericAd(TBLUser owner);

    void injectAdCategories(ModelMap model);

    String getAdImageIcon(TBLAd ad);

    void sendAdResponseRequestedReminders();
    
    void sendExchangeRatingReminders();

    TBLAd createOrUpdateAd(TBLAd ad, String title, String category, String description, Integer price, AdGroup adGroup, AdType adType, String zipCode,
            String adImage1, String adImage2, String adImage3, String adImage4, String address, TBLUser owner, Boolean perHour, Boolean firm, Boolean acceptsTimebanking);

    void makeOffer(Integer offer, Boolean timeBanking, DeliveryMethod buyerDelieveryMethod, TBLDiscussionAd adDiscussion);

    String getOfferSnippit(TBLDiscussionAd discussion);

}