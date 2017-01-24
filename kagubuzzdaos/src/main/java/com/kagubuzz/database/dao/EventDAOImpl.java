package com.kagubuzz.database.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.BookMarkedEventWithUser;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.enums.VenueLocation;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

@Repository("eventDAO")
@Transactional(readOnly = true)
public class EventDAOImpl implements EventDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    CategoryDAO categoryDAO;
    
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired 
    SearchService searchService;
    
    static Logger log = Logger.getLogger(EventDAOImpl.class);

    @Override
    public Pagination<TBLEvent> browseEvents(LSTEventCategory category,
                                             long currentPage,
                                             boolean active, 
                                             int radius,
                                             TBLKaguLocation kaguLocation) {
     
        Criteria criteria = sessionFactory.getCurrentSession()
                                          .createCriteria(TBLEvent.class)
                                          .add(Restrictions.eq("active", true))    
                                          .add(Restrictions.gt("endDate", new Date()));
        
        criteria = SearchService.addRadiusCriteria(kaguLocation, radius, criteria);    
        
        criteria = categoryDAO.addCriteriaLimitedToCategoryAndDescendants(criteria, category);
        
        return new Pagination<TBLEvent>(criteria, currentPage, Order.asc("startDate"));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLEvent> getExpiringEvents() {
        
         Calendar currentTime = Calendar.getInstance();        
        
        // Checks to see if the renew date is within a week
        // and if the renew sent notice was sent more than 2 weeks ago
        
        Date expireWindowDate = new Date(currentTime.getTimeInMillis() + TimeUnit.DAYS.toMillis(3));
        Date reminderSendWindow = new Date(currentTime.getTimeInMillis() - TimeUnit.DAYS.toMillis(14));
        
        // if the last reminder sent is over 2 weeks old and the expire date is in less than a week, send the notice
        
        Query q = sessionFactory.getCurrentSession().createQuery("select event from TBLEvent event " +
                                                                 "where " + 
                                                                     "event.renewDate <= :expireWindowDate " +
                                                                     "and " +
                                                                     "event.reminderSent <= :reminderSendWindow " +
                                                                     "and " +
                                                                     "event.active = true " +
                                                                     "and " +
                                                                     "event.eventCycle != 'Once'");
        
        q.setDate( "expireWindowDate", expireWindowDate);
        q.setDate( "reminderSendWindow", reminderSendWindow);

        return (List<TBLEvent>) q.list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLEvent> getRepeatingEventsThatAreOver() {
        
        Calendar currentTime = Calendar.getInstance();
        
        List<TBLEvent> eventsList = (List<TBLEvent>) sessionFactory.getCurrentSession()
                                                     .createCriteria(TBLEvent.class)
                                                     .add(Restrictions.lt("endDate", new Date(currentTime.getTimeInMillis())))
                                                     .add(Restrictions.eq("active", true))
                                                     .add(Restrictions.ne("eventCycle", EventPeriod.Once)).list();
                                                      
        return (List<TBLEvent>) eventsList;
    }
    
    @Override
    public void removeExpiredEventsFromListings() {
        
        Calendar currentTime = Calendar.getInstance();
        Date currentDate = currentTime.getTime();
        
        String hqlUpdate = "update TBLEvent e set e.active = false " +
        		           "where " +
        		               "e.renewDate <= :currentDate and e.eventCycle != 'Once') " +
        		               "and " +
        		               "e.active = true";

       int count=  sessionFactory.getCurrentSession()
                                 .createQuery(hqlUpdate)
                                 .setTimestamp("currentDate", currentDate)
                                 .executeUpdate();
       
       log.info("Removed " + count + " events from the listings");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BookMarkedEventWithUser> getBookmarkedEventsHappeningTomorrow(int timeZone) {
        
        Calendar cal = Calendar.getInstance();
        
        // set it to the time is would be in greenwich time at midnight of the timezone
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.HOUR_OF_DAY, timeZone*-1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date begginingOfDayTomorrow = new Date(cal.getTimeInMillis());
        
        cal = Calendar.getInstance();
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.HOUR_OF_DAY, timeZone*-1);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        
        Date endOfDayTomorrow = new Date(cal.getTimeInMillis()  + TimeUnit.HOURS.toMillis(23));
        
        log.info(begginingOfDayTomorrow);
        log.info(endOfDayTomorrow);
        
        String queryString =    "select " +
                                    "{tbl_events.*}, {tbl_users.*} " +
                                "from " +
                                    "tbl_events " +
                                "inner join " +
                                    "tbl_users_bookmarked_events bookmarks " +
                                        "on tbl_events.id=bookmarks.bookmarked_events " +
                                "inner join " +
                                    "tbl_users tbl_users " +
                                        "on bookmarks.user_book_marks=tbl_users.id " +
                                "where " +
                                    "tbl_users.time_zone=:timeZone " +
                                    "and tbl_events.start_date>=:midnight1 " +
                                    "and tbl_events.active = true " +
                                    "and tbl_events.start_date<=:midnight2";

        SQLQuery sqlQuery =  sessionFactory.getCurrentSession().createSQLQuery(queryString);
        sqlQuery.addEntity("tbl_events", TBLEvent.class);
        sqlQuery.addEntity("tbl_users", TBLUser.class);
        
        sqlQuery.setTimestamp("midnight1", begginingOfDayTomorrow);
        sqlQuery.setTimestamp( "midnight2", endOfDayTomorrow);
        sqlQuery.setInteger("timeZone", (int) TimeUnit.HOURS.toMinutes(timeZone));
        
        // find repeating events happening tomorrow
        
        List<BookMarkedEventWithUser> events = (List<BookMarkedEventWithUser>) sqlQuery.setResultTransformer(Transformers.aliasToBean(BookMarkedEventWithUser.class)).list();
        
        return events;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LSTEventCategory> getEventCategories() {

        List<LSTEventCategory> eventCategoriesList = sessionFactory.getCurrentSession()
                                                                   .createCriteria(LSTEventCategory.class)
                                                                   .add(Restrictions.not(Restrictions.eq("name", "Event")))
                                                                   .addOrder(Order.asc("name"))
                                                                   .list();

        return eventCategoriesList;
    }
    
    @Override
    public TBLMessageUserEventFeedback getUserRatingForEvent(TBLEvent event, TBLUser user) {
        
        if(event.getBallot() == null) return null;
        
        return (TBLMessageUserEventFeedback) sessionFactory.getCurrentSession()
                                                            .createCriteria(TBLMessageUserEventFeedback.class)
                                                            .add(Restrictions.eq("ballot", event.getBallot()))
                                                            .add(Restrictions.eq("sender", user))
                                                            .uniqueResult();
    }
    
    @Override
    @Transactional(readOnly = false)
    public void submitEventRating(TBLMessageUserEventFeedback newRating, TBLEvent event) {
        
        TBLMessageUserEventFeedback currentRating = (TBLMessageUserEventFeedback) sessionFactory.getCurrentSession()
                                                            .createCriteria(TBLMessageUserEventFeedback.class)
                                                            .add(Restrictions.eq("ballot", event.getBallot()))
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

    @SuppressWarnings("unchecked")
    @Override
    public List<TBLEvent>  getAutoSearchEvents(TBLUser owner, 
                                               int recordsToReturn, 
                                               int nextBrowseId) {
        
        String hqlUpdate = "select autoSearchEvents from TBLUsersAutoSearchKeywordList a where a.owner = :owner order by start_date asc";
        
        Query q = sessionFactory.getCurrentSession().createQuery(hqlUpdate)                                                    
                                                    .setEntity("owner", owner)
                                                    .setMaxResults(recordsToReturn)
                                                    .setFirstResult(nextBrowseId);
        
        return (List<TBLEvent>)  q.list();
    }
    
    @Override
    public Pagination<TBLEvent>  getAutoSearchEvents(TBLUser owner, 
                                                     long currentPage) {
        
        Query query = sessionFactory.getCurrentSession()
                                .createQuery(
                                             "select autoSearchEvents from " +
                                             "TBLUsersAutoSearchKeywordList a " +
                                             "where a.owner = :owner order by end_date asc"
                                             )                                                    
                                .setEntity("owner", owner);
        
        Query count = sessionFactory.getCurrentSession()
                                    .createQuery(
                                                 "select count(a) from " +
                                                 "TBLUsersAutoSearchKeywordList a " +
                                                 "where a.owner = :owner"
                                                )                                                    
                                    .setEntity("owner", owner);
        
        return new Pagination<TBLEvent>(query, count, currentPage);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<TBLEvent> getSuggestedEventsForUser(TBLUser user, EventVenue[] locations) {
        
        Calendar cal = Calendar.getInstance();
        Date today = new Date(cal.getTimeInMillis() + TimeUnit.DAYS.toMillis(7));
        
        Criteria criteria = searchService.getEventSearchCriteria(null, null, user.getTblKaguLocation(), 20, null, locations, null, true, null, new Date(), null, false);
        
        criteria = criteria.setMaxResults(5).add(Restrictions.lt("startDate", today));                
        
        return criteria.list();
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public long getEventCount() {

		return  (Long) sessionFactory.getCurrentSession()
    		  					.createCriteria(TBLEvent.class)
    		  					.add(Restrictions.eq("active", true)) 
    		  					.add(Restrictions.gt("endDate", new Date()))
    		  					.setProjection(Projections.rowCount())
    		  					.uniqueResult();
	}
}
