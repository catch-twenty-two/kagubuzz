package com.kagubuzz.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.WordRankDAO;
import com.kagubuzz.database.dao.utilities.CategoryUtilityService;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.ExchangeRatingTypes;
import com.kagubuzz.datamodels.enums.AdPerUnit;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.datamodels.hibernate.TBLWordRank;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessage;
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessageTemplates;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.utilities.KaguImage;

import de.svenjacobs.loremipsum.LoremIpsum;

@Service
public class AdServiceImpl implements AdService {
    @Value("${kagubuzz.ad_post_expire}")
    private int adRenewWindow;

    @Autowired
    WordRankDAO wordRankDAO;
    @Autowired
    AdDAO adDAO;
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    KaguLocationDAO kaguLocationDAO;
    @Autowired
    FileService fileService;
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    UserDAO userDAO;

    @Autowired
    PostsService postsService;

    @Autowired
    UserAccountService accountService;

    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    CategoryUtilityService categoryUtilityService;

    static Logger logger = Logger.getLogger(AdServiceImpl.class);
        
    static String catJobsList = null;
    static String catForSaleList = null;
    static String catServicesList = null;
    static String catHousingList = null;

    @Override
    public void setSMSKeyWord(TBLUser user, TBLAd newAd) {

        // Get a 'ranked list' of words from the ad title

        List<TBLWordRank> wordRankList = wordRankDAO.GetWordRankForString(newAd.getTitle());
        Set<TBLAd> tblAdList = user.getUserAds();
        String setSMSKeyword = null;

        // shorten the words in the list to something that sms can deal with

        for (TBLWordRank wordRank : wordRankList) {

            if (wordRank.getWord()
                    .length() > 7) {
                wordRank.setWord(wordRank.getWord()
                        .substring(0, 6));
            }
        }

        // Get all ads and remove sms keywords already used from ranked word
        // list

        if (!tblAdList.isEmpty()) {

            List<TBLWordRank> removeList = new ArrayList<TBLWordRank>();

            for (TBLAd tblWordRank : tblAdList) {

                TBLWordRank wordRank = new TBLWordRank();
                wordRank.setRank(0);
                wordRank.setWord(tblWordRank.getSMSKeyword());

                removeList.add(wordRank);
            }

            wordRankList.removeAll(removeList);
        }

        // If all the words are used, substitute a random number for the keyword

        if (wordRankList.isEmpty()) {
            setSMSKeyword = Integer.toString((int) (Math.random() % 300 * 100));
        }
        else {
            setSMSKeyword = wordRankList.get(wordRankList.size() - 1)
                    .getWord();
        }

        newAd.setSMSKeyword(setSMSKeyword);
    }

    @Override
    public String getAdImageIcon(TBLAd ad) { return (ad.getAdGroup() == AdGroup.CommunityServices) ? this.getAdImageURL2(ad) : this.getAdImageURL1(ad);}
    
    @Override
    public String getAdImageURL1(TBLAd ad) {
        if (ad == null)
            return null;
        return (ad.getImagePath1() == null) ? null : fileService.getUserPublicDirectoryURL(ad.getOwner(), UserSubfolder.Images) + ad.getImagePath1();
    }

    @Override
    public String getAdImageURL2(TBLAd ad) {
        if (ad == null)
            return null;
        return (ad.getImagePath2() == null) ? null : fileService.getUserPublicDirectoryURL(ad.getOwner(), UserSubfolder.Images) + ad.getImagePath2();
    }

    @Override
    public String getAdImageURL3(TBLAd ad) {
        if (ad == null)
            return null;
        return (ad.getImagePath3() == null) ? null : fileService.getUserPublicDirectoryURL(ad.getOwner(), UserSubfolder.Images) + ad.getImagePath3();
    }

    @Override
    public String getAdImageURL4(TBLAd ad) {
        if (ad == null)
            return null;
        return (ad.getImagePath4() == null) ? null : fileService.getUserPublicDirectoryURL(ad.getOwner(), UserSubfolder.Images) + ad.getImagePath4();
    }

    @Override
    @Transactional(readOnly = false)
    public void renewAd(TBLAd ad) {
        ad.setRenewDate(adRenewWindow);
        ad.setActive(true);

        crudDAO.update(ad);
    }

    @Override
    public List<String> getImagePathsURLS(TBLAd ad) {
        ArrayList<String> imagePaths = new ArrayList<String>();

        String url = this.getAdImageURL1(ad);

        if (url != null) {
            imagePaths.add(url);
        }

        url = this.getAdImageURL2(ad);

        if (url != null) {
            imagePaths.add(url);
        }

        url = this.getAdImageURL3(ad);

        if (url != null) {
            imagePaths.add(url);
        }

        url = this.getAdImageURL4(ad);

        if (url != null) {
            imagePaths.add(url);
        }

        return imagePaths;
    }

    @Override
    @Transactional(readOnly = false)
    public TBLAd createOrUpdateAd(TBLAd ad, 
                                String title, 
                                String category, 
                                String description, 
                                Integer price, 
                                AdGroup adGroup, 
                                AdType adType, 
                                String zipCode, 
                                String adImage1,
                                String adImage2, 
                                String adImage3, 
                                String adImage4, 
                                String address, 
                                TBLUser owner,
                                Boolean perHour,
                                Boolean firm,
                                Boolean acceptsTimebanking) {
        if (ad == null) {
            ad = new TBLAd();
        }

        ad.setFirm(firm);
        ad.setLastUpdated(new Date());

        if (ad.getQuestionsAndAnswers() == null) {
            ad.getQuestionsAndAnswers(new TBLDiscussionAdQA());
            ad.getQuestionsAndAnswers().setAd(ad);
        }

        ad.setPostedDate(new Date());
        ad.setTitle(title);
        ad.setCategory(adDAO.getAdCategoryByNameAndGroup(category, adGroup));
        ad.setBody(description);
        ad.setActive(false);
        
        // Set per item dependent on ad group type
        
        if(adGroup == AdGroup.ForSale) {
            ad.setPerUnit(AdPerUnit.PerItem);           
        }
        
        if(adGroup == AdGroup.CommunityServices) {
            ad.setPerUnit((perHour) ? AdPerUnit.PerHour : AdPerUnit.PerJob); 
        }
        
        if(acceptsTimebanking != null) {
            ad.setAcceptsTimebanking(acceptsTimebanking);
        }
        
        // Price is set to zero if empty
        
        if (price == null) {
            price = 0;
        }

        ad.setAdType(adType);
        ad.setPrice(price);
        ad.setAddress(address);
        ad.setTblKaguLocation(kaguLocationDAO.getKaguLocationByZipCode(zipCode));
        ad.setCreatedDate(new Date());
        ad.setOwner(owner);
        ad.setAdGroup(adGroup);
        DeliveryMethod deliveryMethod = (owner.isSmsAdNotifications()) ? DeliveryMethod.Text : DeliveryMethod.Email;

        ad.setContactMethod(deliveryMethod);
        ad.setZipCode(zipCode);
        ad.setRenewDate(adRenewWindow);
        ad.setAddress(zipCode);

        // Copy from the tmp directory into the users image folder and save the
        // path as the image

        if (adImage1 != null) {
            ad.setImagePath1(saveAdImage(adImage1, owner, AdImageType.ForSale));
        }

        if (adImage2 != null) {
            ad.setImagePath2(saveAdImage(adImage2, owner, (ad.getAdGroup().equals(AdGroup.CommunityServices)) ? AdImageType.SideBar : AdImageType.ForSale));
        }

        if (adImage3 != null) {
            ad.setImagePath3(saveAdImage(adImage3, owner, (ad.getAdGroup().equals(AdGroup.CommunityServices)) ? AdImageType.BackGround : AdImageType.ForSale));
        }

        if (adImage4 != null) {
            ad.setImagePath4(saveAdImage(adImage4, owner, AdImageType.ForSale));
        }

        if (ad.getSmsKeyword() == null) {
            setSMSKeyWord(owner, ad);
        }

        crudDAO.update(ad);

        return ad;
    }

    public enum AdImageType{
        ForSale,
        SideBar,
        BackGround,
    }
    
    public String saveAdImage(String adImage, TBLUser owner, AdImageType imageType) {
        
        String fileName = null;
        KaguImage kaguImage = null;

        try {        
            switch(imageType) {
            case BackGround:
                kaguImage = postsService.createBackgroundImage(adImage);
                break;
            case SideBar:
                kaguImage = postsService.createSideBarImage(adImage);
                break;
            case ForSale:
            default:
                kaguImage = new KaguImage(new URL(adImage));
                kaguImage.resize(400, -1);
                break;
            }
           
            fileName = UUID.randomUUID().toString() + ".jpg";
            fileService.write(kaguImage.getOutputStream(), fileName, fileService.getUserPublicDirectoryPath(owner, UserSubfolder.Images));
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    @Transactional(readOnly = false)
    public TBLDiscussionAd createOffer(TBLUser user, TBLAd ad) {

        TBLDiscussionAd adDiscussion = new TBLDiscussionAd();
        adDiscussion.setAd(ad);
        adDiscussion.addParticipant(ad.getOwner());
        adDiscussion.setSeller(ad.getOwner());
        adDiscussion.addParticipant(user);
        adDiscussion.setBuyer(user);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, -20);
        adDiscussion.setLastOfferDate(calendar.getTime());
        adDiscussion.setCreatedDate(new Date());

        return adDiscussion;
    }

    @Override
    @Transactional(readOnly = false)
    public void makeOffer(Integer offer, Boolean timeBanking, DeliveryMethod buyerDelieveryMethod, TBLDiscussionAd adDiscussion) {

        adDiscussion.setBuyerContactMethod(buyerDelieveryMethod);
        adDiscussion.setLastOfferDate(new Date());
        adDiscussion.setOfferAmount(offer);
        adDiscussion.setTimeBanking(timeBanking);
        
        crudDAO.create(adDiscussion);

        adDAO.setAllOffersInactiveForTransaction(adDiscussion);
    }

    private TBLMessageUserTransactionFeedback createTransactionFeedBackIfNeeded(TBLDiscussionAd transaction, TBLUser user) {

        TBLMessageUserTransactionFeedback feedBack = new TBLMessageUserTransactionFeedback();

        if (user == transaction.getBuyer()) {

            if (transaction.getBuyerFeedback() == null) {
                transaction.setBuyerFeedback(feedBack);
            }
            else {
                return transaction.getBuyerFeedback();
            }
        }
        else {
            if (transaction.getSellerFeedback() == null) {
                transaction.setSellerFeedback(feedBack);
            }
            else {
                return transaction.getSellerFeedback();
            }
        }

        return feedBack;
    }

    @Override
    public void notifyUsersOfClosedItem(TBLAd ad) {

        List<TBLDiscussionAd> offers = ad.getOffers();

        for (TBLDiscussionAd offer : offers) {
            // TODO: make this join query
            adDAO.setAllOffersInactiveForTransaction(offer);

            switch (offer.getAdDiscussionState()) {
            case Accepted:
            case Canceled:
            case Declined:
            case Complete:

                break;

            case WaitingForResponse:
            case ThinkingAboutIt:
                offer.setActive(false);
                offer.setAdDiscussionState(AdDiscussionState.Declined);
                crudDAO.update(offer);

                OfferMessage declinedMessage = new OfferMessage(offer, accountService.getSystemUser());

                declinedMessage.buyerDeclined();

                messageDispatcher.queueMessage(declinedMessage);
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void rateTransaction(TBLDiscussionAd transaction, ExchangeRatingTypes exchangeRatingType, TBLUser user, String review) {

        TBLMessageUserTransactionFeedback feedBack = createTransactionFeedBackIfNeeded(transaction, user);

        if (review == null) {
            review = TBLMessageUserFeedback.EMPTY_REVIEW_CODE;
        }

        feedBack.setSubject(transaction.getTitle());
        feedBack.setCreatedDate(new Date());
        feedBack.setMessage(review);
        feedBack.setIsPrivate(true);
        feedBack.setTransaction(transaction);
        feedBack.setSender(user);
        feedBack.setRecipient(transaction.getOppositeParty(user));
        feedBack.setRating((float) exchangeRatingType.getIntegrityPoints());
        feedBack.setExchangeRatingType(exchangeRatingType);
        feedBack.setDeliveryMethod(DeliveryMethod.KaguBuzz);

        crudDAO.update(feedBack);
        crudDAO.update(transaction);
        
        // TODO: optimize SQL
        
        user.setIntegrity(((Long)userDAO.getUserIntegrityRating(user)).intValue());
        
        crudDAO.update(user);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, timeout=10)
    public boolean doAcceptOffer(TBLDiscussionAd transaction) {

        TBLAd ad = (TBLAd) crudDAO.getSessionFactory().getCurrentSession().get(TBLAd.class, transaction.getAd().getId(), LockOptions.UPGRADE);

        if (ad.getAdState() == AdState.OfferAccepted) {
            return false;
        }

        ad.setAdState(AdState.OfferAccepted);
        crudDAO.update(ad);

        return true;
    }

    @Transactional(readOnly = false)
    public boolean acceptOffer(TBLDiscussionAd transaction) {
        
        if (transaction.getAdDiscussionState() != AdDiscussionState.WaitingForResponse &&
            transaction.getAdDiscussionState() != AdDiscussionState.ThinkingAboutIt) { 
            return false; 
        }
         
        try {
            if(doAcceptOffer(transaction) == false) {
                return false;
            }
        }
        catch (org.hibernate.StaleObjectStateException e) {
            logger.warn("Race condition for offer accepted offer ignored.");
            return false;
        }
        catch (Exception e) {
            logger.error("Error with offer " + transaction.getTitle(), e);
            return false;
        }

        transaction.refreshSecurityCode();
        transaction.setAdDiscussionState(AdDiscussionState.Accepted);
        crudDAO.update(transaction);

        return true;
    }

    @Override
    public TBLAd getGenericAd(TBLUser owner) {

        LoremIpsum loremIpsum = new LoremIpsum();

        TBLAd ad = createOrUpdateAd(null, 
                                    loremIpsum.getWords(3), 
                                    "Automobiles", loremIpsum.getParagraphs(), 
                                    new Integer(0), 
                                    AdGroup.CommunityServices, 
                                    AdType.Offered, 
                                    "94702", 
                                    null,
                                    null, 
                                    null, 
                                    null, 
                                    "4567 Elderberry Ln", 
                                    owner,
                                    true,
                                    true,
                                    true);
        return ad;
    }

    @Override
    public void injectAdCategories(ModelMap model) {
        
        if(catForSaleList == null) {    
            catForSaleList = categoryUtilityService.javascriptCategoryDecendentArray(adDAO.getAdCategoryByGroup(AdGroup.ForSale)); 
        }       
        
        if(catServicesList == null) {   
            catServicesList =  categoryUtilityService.javascriptCategoryDecendentArray(adDAO.getAdCategoryByGroup(AdGroup.CommunityServices)); 
        }       
        
        if(catHousingList == null)  {   
            catHousingList =  null;//categoryUtilityService.javascriptCategoryDecendentArray(adDAO.getAdCategoryByGroup(AdGroup.Housing)); 
        }
        
        if(catJobsList == null) {   
            catJobsList =  null;//categoryUtilityService.javascriptCategoryDecendentArray(adDAO.getAdCategoryByGroup(AdGroup.)); 
        }
       
        model.addAttribute("ad_types", AdType.values());
        model.addAttribute("adGroups", AdGroup.values());
        model.addAttribute("jobcategories", catJobsList);
        model.addAttribute("forsalecategories", catForSaleList);
        model.addAttribute("servicescategories", catServicesList);
        model.addAttribute("housingcategories", catHousingList);
    }
    
    // This is only sent out once after 3 days of inactivity on ads that have users waiting for responses
    // Important ONLY CALL ONCE PER DAY
    
    @Override
    public void sendAdResponseRequestedReminders () {
        
        List<TBLDiscussionAd> expiringEvents = adDAO.getResponseRequestedReminders();
        
        for(TBLDiscussionAd discussion: expiringEvents) {
            
            OfferMessage reminderMessage = new  OfferMessage(discussion, accountService.getSystemUser());
            
            reminderMessage.offerReminder();
            logger.info("Sending reminder for discussion on ad " + discussion.getAd().getTitle());
            messageDispatcher.queueMessage(reminderMessage);
            
            crudDAO.update(discussion);
        }
    }
    
    // This is only sent out once after 2 weeks of inactivity on ads that have users waiting for responses
    // Important ONLY CALL ONCE PER DAY
    
    @Override
    public void sendExchangeRatingReminders() {
        
        List<TBLDiscussionAd> expiringEvents = adDAO.getExchangeRatingReminders();
        
        for(TBLDiscussionAd discussion: expiringEvents) {
            
            if(discussion.getSellerFeedback() == null) {
                sendExchangeRateReminderToUser(discussion, discussion.getSeller());
            }
            
            if(discussion.getBuyerFeedback() == null)  {
                sendExchangeRateReminderToUser(discussion, discussion.getBuyer());
            }
            
            crudDAO.update(discussion);
        }
    }

    private void sendExchangeRateReminderToUser(TBLDiscussionAd discussion, TBLUser user) {
        
        OfferMessage reminderMessage = new  OfferMessage(discussion, accountService.getSystemUser());        
        reminderMessage.exchangeRateReminder(user);        
        messageDispatcher.queueMessage(reminderMessage);
        
        logger.info("Queued ratings reminder for discussion on ad " + discussion.getAd().getTitle() + "to user" + user.getFirstName());
    }
    
    @Override
    public String getOfferSnippit(TBLDiscussionAd discussion) {        
       return OfferMessageTemplates.getOfferSnippit(discussion);
    }

}
