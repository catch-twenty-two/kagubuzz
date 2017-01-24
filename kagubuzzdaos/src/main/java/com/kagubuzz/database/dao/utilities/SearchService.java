package com.kagubuzz.database.dao.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionPublicTag;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.LSTTag;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.datamodels.hibernate.TBLUsersAutoSearchKeywordList;

@Service
@Transactional(readOnly = true)
public class SearchService {

    public static final String[] ENGLISH_STOP_WORDS = {
        "a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it",
        "no", "not", "of", "on", "or", "such",
        "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with"
        };
    
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	CRUDDAO crudDAO;
	
	@Autowired
	EventDAO eventDAO;
	
	static final double EARTH_RADIUS_IN_MILES = 3959D;
	
	static Logger logger = Logger.getLogger(SearchService.class);
	
    public void indexLuceneSearch() throws InterruptedException {
		/*FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
		fullTextSession.createIndexer(TBLAd.class).startAndWait();
		fullTextSession.createIndexer(TBLEvent.class).startAndWait();*/
    }
    
    public List<TBLAd> searchAds(String queryString, AdGroup adGroup, TBLKaguLocation kaguLocation, Integer radius) {
       return this.searchAds(queryString, adGroup, null, kaguLocation, radius, null, true, null, null, new Date(), true, false, 0L).getList();
    }
    
    
    public Pagination<TBLAd>  searchAds(String queryString,
                                        AdGroup adGroup,
                                 Integer price,
                                 TBLKaguLocation kaguLocation,
                                 Integer radius,
                                 LSTAdCategory category,
                                 Boolean active,
                                 TBLUser owner,
                                 Date startDate,
                                 Date endDate,
                                 Boolean searchTitleOnly,
                                 Boolean acceptsTimebanking,
                                 Long page) {
        
        Criteria adSerachCriteria = sessionFactory.getCurrentSession().createCriteria(TBLAd.class); 
        
        adSerachCriteria = getAdSearchCriteria(queryString, adGroup, price, kaguLocation, radius, category, active, owner, startDate, endDate, searchTitleOnly, acceptsTimebanking);
        
        return new Pagination<TBLAd>(adSerachCriteria, page, Order.desc("postedDate"));
    }
    
    public List<TBLEvent> searchEvents(String queryString, TBLKaguLocation kaguLocation, Integer radius) {
        return this.searchEvents(queryString, null, kaguLocation, radius, null, null, null, true, null, null, new Date(), true, 0L).getList();
    }
    
    @Transactional
    public Long totalAdsInSearch(String queryString,
                                 AdGroup adGroup,
                                       Integer price,
                                       TBLKaguLocation kaguLocation,
                                       Integer radius,
                                       LSTAdCategory category,
                                       Boolean active,
                                       TBLUser owner,
                                       Date startDate,
                                       Date endDate,
                                       Boolean searchTitleOnly,
                                       Boolean acceptsTimebanking) {

        Criteria eventSerachCriteria = getAdSearchCriteria(queryString,adGroup, price, kaguLocation, radius, category, active, owner, startDate, endDate, searchTitleOnly,acceptsTimebanking);
        
        eventSerachCriteria = eventSerachCriteria.setProjection(Projections.rowCount());
        
        return (Long) eventSerachCriteria.uniqueResult();
    }
    
    public Criteria getAdSearchCriteria(String queryString,
                                        AdGroup adGroup,
                                        Integer price, 
                                        TBLKaguLocation kaguLocation, 
                                        Integer radius,
                                        LSTAdCategory category, 
                                        Boolean active, 
                                        TBLUser owner,                                        
                                        Date startDate,
                                        Date endDate, 
                                        Boolean searchTitleOnly,
                                        Boolean acceptsTimebanking) {

        Criteria adSerachCriteria = sessionFactory.getCurrentSession().createCriteria(TBLAd.class);
        
        adSerachCriteria = addKeyWordsCriteria(queryString, searchTitleOnly, adSerachCriteria, "body", "title");        
        
        if(acceptsTimebanking != null) {
           adSerachCriteria = adSerachCriteria.add(Restrictions.eq("acceptsTimebanking", acceptsTimebanking));
        }
        
        if(adGroup != null) {
            adSerachCriteria = adSerachCriteria.add(Restrictions.eq("adGroup", adGroup));
        }
        
        if(owner != null) {
            adSerachCriteria = adSerachCriteria.add(Restrictions.eq("owner", owner));
        }
        
        if(price != null) {
            adSerachCriteria = adSerachCriteria.add(Restrictions.le("price", price));
        }
        
        if(kaguLocation != null && radius != null) {
            adSerachCriteria =  addRadiusCriteria(kaguLocation, radius, adSerachCriteria);
        }
        
        if (active != null) {
            adSerachCriteria = adSerachCriteria.add(Restrictions.eq("active", active));
        }
        
        if (category != null) {
            adSerachCriteria = adSerachCriteria.add(Restrictions.eq("category", category));
        }
        
        return adSerachCriteria;
    }
	
	@Transactional
    public Pagination<TBLEvent> searchEvents(String queryString,
                                             Integer price,
                                             TBLKaguLocation kaguLocation,
                                             Integer radius,
                                             EventAgeAppropriate[] ageAppropriateFilters,
                                             EventVenue[] venues,
                                             LSTEventCategory category,
                                             Boolean active,
                                             TBLUser owner,
                                             Date startDate,
                                             Date endDate,
                                             Boolean searchTitleOnly,
                                             Long page) {

        Criteria eventSerachCriteria = getEventSearchCriteria(queryString, 
                                                              price, 
                                                              kaguLocation, 
                                                              radius, 
                                                              ageAppropriateFilters, 
                                                              venues, 
                                                              category, 
                                                              active, 
                                                              owner,
                                                              startDate, 
                                                              endDate, 
                                                              searchTitleOnly);        

        return new Pagination<TBLEvent>(eventSerachCriteria, page, Order.asc("startDate"));
    }
	
    public Criteria getEventSearchCriteria(String queryString, 
                                           Integer price, 
                                           TBLKaguLocation kaguLocation, 
                                           Integer radius,
                                           EventAgeAppropriate[] ageAppropriateFilters, 
                                           EventVenue[] venues, 
                                           LSTEventCategory category, 
                                           Boolean active, 
                                           TBLUser owner, 
                                           Date startDate,
                                           Date endDate, 
                                           Boolean searchTitleOnly) {
        
        Criteria eventSerachCriteria = sessionFactory.getCurrentSession()
                                       .createCriteria(TBLEvent.class);
        
        eventSerachCriteria = addKeyWordsCriteria(queryString, searchTitleOnly, eventSerachCriteria, "body", "title");
        
        if(startDate != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.gt("startDate", startDate));
        }

        if(endDate != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.gt("endDate", endDate));
        }
        
        if(owner != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.eq("owner", owner));
        }
        
        if((kaguLocation != null) && (radius != null)) {
            eventSerachCriteria = addRadiusCriteria(kaguLocation, radius, eventSerachCriteria);
        }

        if (active != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.eq("active", active));
        }

        if (venues != null) {
            
            Conjunction conjunction = (Conjunction) Restrictions.conjunction();
            Disjunction disjunction = (Disjunction) Restrictions.disjunction();
            
            for (EventVenue venue : venues) {
                disjunction = (Disjunction) disjunction.add(Restrictions.eq("venue", venue));
            }
            
            eventSerachCriteria = eventSerachCriteria.add(conjunction.add(disjunction));
        }

        if ((ageAppropriateFilters != null) && (ageAppropriateFilters.length != 0)) {

            Conjunction conjunction = (Conjunction) Restrictions.conjunction();
            Disjunction disjunction = (Disjunction) Restrictions.disjunction();

            for (EventAgeAppropriate ageAppropriate : ageAppropriateFilters) {
                disjunction = (Disjunction) disjunction.add(Restrictions.eq("ageAppropriate", ageAppropriate));
            }

            eventSerachCriteria = eventSerachCriteria.add(conjunction.add(disjunction));
        }

        if (price != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.le("price", price));
        }

        if (category != null) {
            eventSerachCriteria = eventSerachCriteria.add(Restrictions.eq("category", category));
        }
        
        return eventSerachCriteria;
    }

    public Criteria addKeyWordsCriteria(String queryString, Boolean searchTitleOnly, Criteria serachCriteria, String bodyColumn, String titleColumn) {
        
        if (queryString != null) {

            String splitKeyWords[] = queryString.split(" ,.");

            ArrayList<String> keywordList = new ArrayList<String>();
            ArrayList<String> stopWordList = new ArrayList<String>();

            Collections.addAll(keywordList, splitKeyWords);
            Collections.addAll(stopWordList, ENGLISH_STOP_WORDS);

            keywordList.removeAll(stopWordList);

            // switch to lucene for text search?

            Disjunction bodyKeyWords = Restrictions.disjunction();
            Disjunction titleKeyWords = Restrictions.disjunction();
            
            for (String keyword : splitKeyWords) {

                if (!searchTitleOnly) {
                    bodyKeyWords = (Disjunction) bodyKeyWords.add(Restrictions.ilike(bodyColumn, "%" + keyword + "%"));               
               
                    titleKeyWords = (Disjunction) titleKeyWords.add(Restrictions.ilike(titleColumn, "%" + keyword + "%"));
               
                    serachCriteria = serachCriteria.add(Restrictions.or(bodyKeyWords, titleKeyWords));
                }
                else {
                    serachCriteria = serachCriteria.add(Restrictions.ilike(titleColumn, "%" + keyword + "%"));
                }
            }     
        }
        
        return serachCriteria;
    }
	
	@SuppressWarnings("unchecked")
    public <HibernateEntity> List<HibernateEntity> searchDistance(String queryString, final Class<HibernateEntity> kaguSearchable) {
    	// serach distance
        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
        
        QueryBuilder builder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity( kaguSearchable ).get();

    		double centerLatitude= 24;
    		double centerLongitude= 31.5;

    		org.apache.lucene.search.Query luceneQuery = builder.spatial().onCoordinates( "location" )
    		   .within( 50, Unit.KM ).ofLatitude( centerLatitude ).andLongitude( centerLongitude )
    		   .createQuery();

    		List<HibernateEntity> posts = fullTextSession.createFullTextQuery(luceneQuery, kaguSearchable).list();
    		
			return posts;
    }
    
    public void checkAutoSearchKeywords()
    {
    	@SuppressWarnings("unchecked")
        List<TBLUsersAutoSearchKeywordList> userKeyWordTables = (List<TBLUsersAutoSearchKeywordList>) sessionFactory.getCurrentSession()
    													  										      .createCriteria(TBLUsersAutoSearchKeywordList.class)
    													  										      .list();
    	//TODO: change to SQL join change to search multiple words at a time
    	
    	for(TBLUsersAutoSearchKeywordList keyWordTable : userKeyWordTables)
    	{
    	    // remove ads and events that are no longer active
    	    List<TBLEvent> removeEventList = new ArrayList<TBLEvent>();
    	    
    	    for(TBLEvent foundEvent: keyWordTable.getAutoSearchEvents()) {
    	        
    	      if(!foundEvent.isActive()) {
    	          removeEventList.add(foundEvent);
    	      }
    	    }
    	    
    	    keyWordTable.getAutoSearchEvents().removeAll(removeEventList);
    	    
    	    List<TBLAd> removeAdList = new ArrayList<TBLAd>();
    	    
    	    for(TBLAd foundAd: keyWordTable.getAutoSearchAds()) {
                if(!foundAd.isActive()) {
                    removeAdList.add(foundAd);
                }
              }
    	    
    	    keyWordTable.getAutoSearchAds().removeAll(removeAdList);
    	    
    	    // find ads and events that match the user search
    	    
    		for(String keyWord: keyWordTable.getEventKeyWords()) {    			
    		    
    			List<TBLEvent> foundEvents = (List<TBLEvent>)  this.searchEvents(keyWord, keyWordTable.getOwner().getTblKaguLocation(), 30);    		

    			for(TBLEvent tblEvent: foundEvents) { 
    			    keyWordTable.addAutoSearchEvent(tblEvent);     			    
    			}
    			    			
    		}
    		
    		for(String keyWord: keyWordTable.getAdKeyWords())
    		{
    			List<TBLAd> foundAds = (List<TBLAd>) this.searchAds(keyWord, null,keyWordTable.getOwner().getTblKaguLocation(), 30);			  
    		
    			for(TBLAd tblAd: foundAds) keyWordTable.addAutoSearchAd(tblAd);
    		}
    		
    		logger.info("Done Checking Keywords for user " + keyWordTable.getOwner().getFirstName());
    		
    		crudDAO.update(keyWordTable);

    	}
    }
    
    public static Criteria addRadiusCriteria(TBLKaguLocation kaguLocation,
                                             Integer radius,
                                             Criteria criteria) {
        
        if (kaguLocation == null || radius == null) return criteria;
        
        double maxLat = 0;
        double minLat = 0;
        
        double lat1 = kaguLocation.getLatitude() + Math.toDegrees(radius.doubleValue()/EARTH_RADIUS_IN_MILES);
        double lat2 = kaguLocation.getLatitude() - Math.toDegrees(radius.doubleValue()/EARTH_RADIUS_IN_MILES);
        
        if(Math.abs(lat1) > Math.abs(lat2)) {
            maxLat = lat1;
            minLat = lat2;
        }
        else {
            maxLat = lat2;
            minLat = lat1;
        }
        
        // compensate for degrees longitude getting smaller with increasing latitude
        
        double maxLon = 0;
        double minLon = 0;
        
        double lon1 = kaguLocation.getLongitude() + Math.toDegrees(radius.doubleValue()/EARTH_RADIUS_IN_MILES/Math.cos(Math.toDegrees(kaguLocation.getLatitude())));
        double lon2 = kaguLocation.getLongitude() - Math.toDegrees(radius.doubleValue()/EARTH_RADIUS_IN_MILES/Math.cos(Math.toDegrees(kaguLocation.getLatitude())));
        
        if(Math.abs(lon1) < Math.abs(lon2)) {
            maxLon = lon1;
            minLon = lon2;
        }
        else {
            maxLon = lon2;
            minLon = lon1;
        }
        
       //System.out.println("Search Coords (" + minLon + " < Lon < " + maxLon + " and "  + minLat + " < Lat < " + maxLat);
        
        return criteria.add(Restrictions.and(Restrictions.between("latitude", minLat, maxLat), Restrictions.between("longitude", minLon, maxLon)));
    }
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<TBLDiscussionPublic> searchDiscussions(String queryString,
                                                       String tagsList[],
                                                       TBLKaguLocation kaguLocation,
                                                       Integer radius,
                                                       TBLUser owner,
                                                       LSTDiscussionCategory category,
                                                       Boolean searchTitleOnly,
                                                       Integer nextBrowseId,
                                                       Integer maxResults) {

        Criteria discussionSerachCriteria = sessionFactory.getCurrentSession()
                                       .createCriteria(TBLDiscussionPublic.class, "discussion")                                      
                                       .createAlias("messages", "m")
                                       .setProjection(Projections.distinct(Projections.property("m.discussion")));

        if((nextBrowseId != null) && (maxResults != null)) {
           discussionSerachCriteria = discussionSerachCriteria.setFirstResult(nextBrowseId).setMaxResults(maxResults);
        } 
         
        if(owner != null) {
            discussionSerachCriteria = discussionSerachCriteria.add(Restrictions.eq("owner", owner));
        }
       
        if((kaguLocation != null) && (radius != null)) {
            discussionSerachCriteria = addRadiusCriteria(kaguLocation, radius, discussionSerachCriteria);
        }

        if (category != null) {
            discussionSerachCriteria = discussionSerachCriteria.add(Restrictions.eq("category", category));
        }
       
        discussionSerachCriteria = discussionSerachCriteria.add(Restrictions.eq("active", true));
           
        if(queryString != null) {
            discussionSerachCriteria =  addKeyWordsCriteria(queryString, searchTitleOnly, discussionSerachCriteria, "m.body", "m.subject");    
        }

        if(tagsList != null) {
            discussionSerachCriteria =  discussionSerachCriteria.createCriteria("tags").add(Restrictions.in("name", tagsList)); 
        }     
        
       return discussionSerachCriteria.list();
    }
    
	public void saveKeyWordSearch(String searchterm1, String searchterm2, TBLUser user){
		TBLUsersAutoSearchKeywordList keyWordList = new TBLUsersAutoSearchKeywordList();
		
		user.setSearchAbleKeyWordList(keyWordList);
		keyWordList.setOwner(user);
		
		keyWordList.setAdKeyword1(searchterm1);
		keyWordList.setAdKeyword2(searchterm2);
		
		crudDAO.update(user);
	}
}
