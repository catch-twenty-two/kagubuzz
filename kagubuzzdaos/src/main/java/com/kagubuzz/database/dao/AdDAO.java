package com.kagubuzz.database.dao;

import java.util.Calendar;
import java.util.List;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface AdDAO {

	@SuppressWarnings("unchecked")
	public abstract List<TBLAd> getAds(int longitude, int latitude, LSTAdCategory category, int recordsToReturn, boolean active, long nextBrowseId);

	public abstract TBLAd getAdFromSMSKeyword(String smsKeyword, TBLUser user);

    List<TBLAd> getAdsForUserByCreatedDate(int maxResults, int firstResult, TBLUser user);

    List<LSTAdCategory> getAdCategories();

    TBLDiscussionAd getUserTransactionForAd(TBLAd ad, TBLUser buyer);

    TBLDiscussionAd getOfferAccepted(TBLAd ad);

    List<TBLMessageAdOffer> getAdMessagesForUser(TBLDiscussionAd ad, TBLUser user);

	int getAutoSearchAdsCount(TBLUser owner);

	long getAdCount();

    int getOffersForActiveAds(TBLUser owner);

    void setAllOffersInactiveForTransaction(TBLDiscussionAd discussion);

    void saveTransactionRating(TBLMessageUserTransactionFeedback newRating, TBLDiscussionAd ad, String mappingName);

    Pagination<TBLAd> getAutoSearchAds(TBLUser owner, long currentPage);

    LSTAdCategory getAdCategoryByNameAndGroup(String name, AdGroup group);

    List<LSTAdCategory> getAdCategoryByGroup(AdGroup group);

    Pagination<TBLAd> browseAds(AdGroup group, AdType adType, LSTAdCategory adCategory, Calendar afterTime, boolean active, long currentPage, int radius,
            TBLKaguLocation kaguLocation);

    List<TBLDiscussionAd> getResponseRequestedReminders();

    List<TBLDiscussionAd> getExchangeRatingReminders();


}