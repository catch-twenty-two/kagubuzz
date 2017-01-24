package com.kagubuzz.database.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

@Repository("adDAO")
@Transactional(readOnly=true)
public class AdDAOImpl implements AdDAO {
    
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	CategoryDAO categoryDAO;
	
	@Autowired
	WordRankDAO wordRankDAO;
	
	@Autowired
	SearchService searchService;
	
	@Autowired
	CRUDDAO crudDAO;
	
	@Override
    public Pagination<TBLAd> browseAds(AdGroup group,
                                       AdType adType,
                                       LSTAdCategory adCategory,
                                       Calendar afterTime,
                                       boolean active, 
                                       long currentPage,
                                       int radius,
                                       TBLKaguLocation kaguLocation) {

        Criteria criteria = sessionFactory.getCurrentSession()
                            .createCriteria(TBLAd.class)
                            .add(Restrictions.eq("active", true)); 

        criteria = SearchService.addRadiusCriteria(kaguLocation, radius, criteria); 
        
        criteria = criteria.add(Restrictions.eq("adGroup", group));
        
        if(adType != null) {
            criteria = criteria.add(Restrictions.eq("adType", adType));
        }
        
        if(adCategory != null) {
            criteria = criteria.add(Restrictions.eq("category", adCategory));
        }
        
        return new Pagination<TBLAd>(criteria, currentPage, Order.desc("postedDate"));
    }
	
    @Override
    @SuppressWarnings("unchecked")
    public List<TBLAd> getAdsForUserByCreatedDate(int maxResults, 
                                                  int firstResult, 
                                                  TBLUser user) {

        Criteria criteria = sessionFactory.getCurrentSession()             
                                          .createCriteria(TBLAd.class)
                                          .addOrder(Order.desc("createdDate"))
                                          .setMaxResults(maxResults)
                                          .setFirstResult(firstResult)
                                          .add(Restrictions.eq("owner", user));

        List<TBLAd> list = (List<TBLAd>) criteria.list();

        return list;
    }

    
    @Override
	public TBLDiscussionAd getOfferAccepted(TBLAd ad) {
	
		TBLDiscussionAd transaction = (TBLDiscussionAd) sessionFactory.getCurrentSession()
											            .createCriteria(TBLDiscussionAd.class)
											            .add(Restrictions.eq("ad", ad))
											            .add(Restrictions.eq("adDiscussionState", AdDiscussionState.Accepted))
											            .uniqueResult();
		return transaction;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LSTAdCategory> getAdCategories() {

        List<LSTAdCategory> adCategoriesList = sessionFactory.getCurrentSession()
                                                                   .createCriteria(LSTAdCategory.class)
                                                                   //.add(Restrictions.not(Restrictions.eq("name", "Event")))
                                                                   .addOrder(Order.asc("name"))
                                                                   .list();

        return adCategoriesList;
    }
    
   	@Override
	@SuppressWarnings("unchecked")
    public List<TBLAd> getAds(int longitude, 
       						  int latitude, 
       						  LSTAdCategory category,
       						  int recordsToReturn,
       						  boolean active,
       						  long nextBrowseId) {
   		
	   	Criteria criteria = sessionFactory.getCurrentSession()
	   			.createCriteria(TBLAd.class)
	   			.addOrder(Order.desc("createdDate"))
	   			.add(Restrictions.eq("active", active))
	   			.setFirstResult((int) nextBrowseId)
	   			.setMaxResults(recordsToReturn);
	   	
	   	if(category != null)
   		{	
	   		Conjunction conjunction =  (Conjunction) Restrictions.conjunction();
	   		Disjunction disjunction = (Disjunction) Restrictions.disjunction();
	   		
   			for(LSTCategoryBase descendant: categoryDAO.getAllDescendantsForCategory(category, false))
   			{
   				disjunction = (Disjunction) disjunction.add(Restrictions.eq("category", descendant));
   			}

   			criteria = criteria.add(conjunction.add(disjunction));
   		}

		return criteria.list();
   	}

   	@Override
    public TBLDiscussionAd getUserTransactionForAd(TBLAd ad, TBLUser buyer)  {
   	 
        Query q = sessionFactory
                .getCurrentSession()
                .createQuery("from TBLDiscussionAd where buyer = :buyer and ad = :ad");
        
        q.setEntity("buyer", buyer);
        q.setEntity("ad", ad);
        
        return (TBLDiscussionAd) q.uniqueResult();
   	 
    }
   	
    @Override
    @Transactional(readOnly = false)
    public void saveTransactionRating(TBLMessageUserTransactionFeedback newRating, TBLDiscussionAd ad, String mappingName) {
        
        TBLMessageUserTransactionFeedback currentRating = (TBLMessageUserTransactionFeedback) sessionFactory.getCurrentSession()
                                                            .createCriteria(TBLMessageUserEventFeedback.class)
                                                            .add(Restrictions.eq("sender", newRating.getSender()))
                                                            .uniqueResult();
        if(currentRating != null) {
            currentRating.setMessage(newRating.getMessage());
            currentRating.setRating(newRating.getRating());
        }
        else {
            currentRating = newRating;
        }
        
        crudDAO.update(currentRating);
    }

    @Override
    @Transactional(readOnly = false)
    public void setAllOffersInactiveForTransaction(TBLDiscussionAd discussion)  {

        String hqlUpdate = "update TBLMessageAdOffer set offerActive = false " +
                           "where discussion = :discussion";
        
                            sessionFactory.getCurrentSession()
                            .createQuery(hqlUpdate)
                            .setEntity("discussion", discussion)
                            .executeUpdate();
    }
    
    @Override
	public TBLAd getAdFromSMSKeyword(String smsKeyword, TBLUser user)  {
    	return  (TBLAd) sessionFactory.getCurrentSession()
    					.createCriteria(TBLAd.class)							
						.add(Restrictions.eq("smsKeyword", smsKeyword))
						.add(Restrictions.eq("active", true))
						.add(Restrictions.eq("owner", user))
						.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLMessageAdOffer> getAdMessagesForUser(TBLDiscussionAd ad, TBLUser user)  {
     
        Criteria criteria =  sessionFactory
                .getCurrentSession()
                .createCriteria(TBLMessageAdOffer.class)
                .addOrder(Order.asc("createdDate"))               
                .add(Restrictions.or(
                        Restrictions.and(Restrictions.eq("discussion", ad),Restrictions.eq("recipient", user)),
                        Restrictions.and(Restrictions.and(Restrictions.eq("sender", user), 
                                         Restrictions.eq("sentBySystem", false)), 
                                         Restrictions.and(Restrictions.eq("discussion", ad)))));

                
        return (List<TBLMessageAdOffer>) criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Pagination<TBLAd>  getAutoSearchAds(TBLUser owner, 
                                               long currentPage) {
        
        Query query = sessionFactory.getCurrentSession()
                                .createQuery(
                                             "select autoSearchAds from " +
                                             "TBLUsersAutoSearchKeywordList a " +
                                             "where a.owner = :owner order by posted_date asc"
                                             )                                                    
                                .setEntity("owner", owner);
        
        Query count = sessionFactory.getCurrentSession()
                                    .createQuery(
                                                 "select count(a) from " +
                                                 "TBLUsersAutoSearchKeywordList a " +
                                                 "where a.owner = :owner"
                                                )                                                    
                                    .setEntity("owner", owner);
        
        return new Pagination<TBLAd>(query, count, currentPage);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int  getAutoSearchAdsCount(TBLUser owner) {
        
        String hqlUpdate = "select count(ads) from TBLUsersAutoSearchKeywordList as a inner join a.autoSearchAds as ads where a.owner = :owner";
        
        Query q = sessionFactory.getCurrentSession().createQuery(hqlUpdate)                                                    
                                                    .setEntity("owner", owner);
        
        return (Integer)  ((Long)q.uniqueResult()).intValue();
    }
    
    @Override
    public int  getOffersForActiveAds(TBLUser owner) {
        
        Criteria q = sessionFactory.getCurrentSession()
                     .createCriteria(TBLAd.class)
                     .add(Restrictions.eq("owner", owner))
                     .add(Restrictions.eq("active", true))
                     .setProjection(Projections.rowCount());
        
        return (Integer)  ((Long)q.uniqueResult()).intValue();
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public long getAdCount() {
		
		long adCount = (Long) sessionFactory.getCurrentSession()
    		  					.createCriteria(TBLAd.class)
    		  					.setProjection(Projections.rowCount())
    		  					.uniqueResult();
		
		return adCount;
	}
	
    @Override
    public LSTAdCategory getAdCategoryByNameAndGroup(String name, AdGroup group) {

        return (LSTAdCategory) sessionFactory.getCurrentSession()
                .createCriteria(LSTAdCategory.class)
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("adGroup", group))
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LSTAdCategory> getAdCategoryByGroup(AdGroup group) {

        return (List<LSTAdCategory>) sessionFactory.getCurrentSession()
                .createCriteria(LSTAdCategory.class)
                .add(Restrictions.eq("adGroup", group))
                .addOrder(Order.asc("name"))
                .list();
    }
    
    // Only call one per day or will send multiple messages
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLDiscussionAd> getResponseRequestedReminders() {

        Calendar calendar = Calendar.getInstance();
        
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(3) * -1);
        Date expiryDateCeiling = calendar.getTime();
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(4) * -1);
        Date expiryDateFloor = calendar.getTime();
        
        // Get ads that have not been updated for 3 days but not more than 4 days ago
        
        return (List<TBLDiscussionAd>) sessionFactory.getCurrentSession()                                       
                                       .createCriteria(TBLDiscussionAd.class)
                                       .add(Restrictions.lt("updatedDate", expiryDateCeiling))
                                       .add(Restrictions.gt("updatedDate", expiryDateFloor))
                                       .add(Restrictions.or(Restrictions.eq("adDiscussionState", AdDiscussionState.WaitingForResponse), 
                                                            Restrictions.eq("adDiscussionState", AdDiscussionState.ThinkingAboutIt)))
                                       .list();
    }
    
    // Only call one per day or will send multiple messages
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLDiscussionAd> getExchangeRatingReminders() {

        Calendar calendar = Calendar.getInstance();
        
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(13) * -1);
        Date expiryDateCeiling = calendar.getTime();
        
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(14) * -1);
        Date expiryDateFloor = calendar.getTime();
        
        // Get ads that haven't been rated but transaction is marked as complete, or accepted.
        
        return (List<TBLDiscussionAd>) sessionFactory.getCurrentSession()                                       
                                       .createCriteria(TBLDiscussionAd.class)
                                       .add(Restrictions.lt("updatedDate", expiryDateCeiling))
                                       .add(Restrictions.gt("updatedDate", expiryDateFloor))
                                       .add(Restrictions.or(
                                               Restrictions.eq("adDiscussionState", AdDiscussionState.Complete), Restrictions.eq("adDiscussionState", AdDiscussionState.Accepted)))
                                       .add(Restrictions.or(
                                               Restrictions.isNull("sellerFeedback"), Restrictions.isNull("buyerFeedback"))
                                           )
                                       .list();
    }
    
}
