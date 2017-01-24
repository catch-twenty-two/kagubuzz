package com.kagubuzz.database.dao;

import java.util.List;
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
import com.kagubuzz.datamodels.Bookmark;
import com.kagubuzz.datamodels.EventWithUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Repository("userDAO")
@Transactional(readOnly=true)
public class UserDAOImpl implements UserDAO {
    
    @Autowired
    SessionFactory sessionFactory;
    
    @Autowired
    StandardPasswordEncoder standardPasswordEncoder;
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Bookmark> getUserRatedAndBookMarkedEvents(TBLUser user) {
        
       String queryString = "select {tbl_events.*}, {tbl_message_user_event_feedback.*} from tbl_events " +  
                            "join tbl_users_bookmarked_events on " + 
                                "tbl_events.id = tbl_users_bookmarked_events.bookmarked_events and " +
                                "tbl_users_bookmarked_events.user_book_marks = :userId and " +
                                "tbl_events.active = true " +
                            "left join tbl_ballots on tbl_events.ballot = tbl_ballots.id " +
                            "left join tbl_message_user_event_feedback on " +
                                "tbl_ballots.id = tbl_message_user_event_feedback.ballot and " +
                                "tbl_message_user_event_feedback.sender = :userId " +
                            "order by tbl_events.start_date asc";
        
       SQLQuery sqlQuery =  sessionFactory.getCurrentSession().createSQLQuery(queryString);
       
       sqlQuery.setParameter("userId", user.getId());
       sqlQuery.addEntity("tbl_events", TBLEvent.class);
       sqlQuery.addEntity("tbl_message_user_event_feedback", TBLMessageUserEventFeedback.class);
       
       return sqlQuery.setResultTransformer(Transformers.aliasToBean(EventWithUserFeedback.class)).list();
    }
       
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLUser> getFollowers(TBLUser user) {
        
        if(user == null) return null;
        
        Query query = sessionFactory.getCurrentSession()
                      .createQuery("select follower from TBLUser as follower join follower.following where following = :user");

        query.setParameter("user", user);
        
        return (List<TBLUser>) query.list();  
    }  
    
    @Override
    public TBLUser checkUserCredentials(String userEmail, String password) {
        
        TBLUser user = (TBLUser)    sessionFactory.getCurrentSession()
                                    .createCriteria(TBLUser.class)
                                    .add(Restrictions.eq("email", userEmail))
                                    .uniqueResult();
        
        if(!standardPasswordEncoder.matches(password, user.getPassword())) return null;
        
        return user;
    }
    
    
    @Override
    public TBLUser getUserByEmail(String userEmail) {
        TBLUser user = (TBLUser)    sessionFactory.getCurrentSession()
                                    .createCriteria(TBLUser.class)
                                    .add(Restrictions.eq("email", userEmail))
                                    .uniqueResult();                
        return user;
    }
    
    @Override
    public TBLUser getUserBySecurityCode(String securityCode) {
        TBLUser user = (TBLUser)    sessionFactory.getCurrentSession()
                                    .createCriteria(TBLUser.class)
                                    .add(Restrictions.eq("securityCode", securityCode))
                                    .uniqueResult();                
        return user;
    }
    
    @Override
    public TBLUser getUserByUserAndProviderId(String providerUserId, String providerId)     {
        TBLUser user = (TBLUser) sessionFactory.getCurrentSession()
                .createCriteria(TBLUser.class)
                .add(Restrictions.eq("socialProviderUserId", providerUserId))
                .add(Restrictions.eq("socialProviderId", providerId))
                .uniqueResult();                
        return user;
    }
    
    @Override
    public TBLUser getUserById(long userId) {
        TBLUser user =  (TBLUser)   sessionFactory.getCurrentSession()
                                    .createCriteria(TBLUser.class)
                                    .add(Restrictions.eq("id", userId))
                                    .uniqueResult();            
        return user;
    }
    
    @Override
    public TBLUser getUserByPhoneNumber(String phoneNumber) {
        
        if(!phoneNumber.startsWith("1")) {
            phoneNumber = "1" + phoneNumber;
        }
        
        TBLUser user =  (TBLUser)   sessionFactory.getCurrentSession()
                                    .createCriteria(TBLUser.class)
                                    .add(Restrictions.eq("phone", phoneNumber))
                                    .uniqueResult();            
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TBLMessage> getCommentsForUserPosts(Long userId,
                                                    int maxMessages, 
                                                    int messageToStartAt) {
        List<TBLMessage>  messages = sessionFactory.getCurrentSession()
                                    .getNamedQuery("User.getAllMessagesForEventsAndDiscussions")
                                    .setMaxResults(maxMessages)
                                    .setString("userId", userId.toString()).list();     
        return messages;
    }
    
    //  Grabs the latest comments for events and ads posted by the userID
    //  
    //  SELECT TBLMessages.Message, TBLMessages.SenderId, TBLMessages.ID... 
    //  FROM TBLMessages INNER JOIN TBLEvents ON 
    //  TBLEvents.ID = TBLMessages.RecipientID 
    //  WHERE TBLEvents.OwnerID = 1;
    
    @Override
    @SuppressWarnings("unchecked")
    public List<TBLMessage> getUserComments(long userID) {              
        
        List<TBLMessage> comments = (List<TBLMessage>) sessionFactory.getCurrentSession()
                                    .createCriteria(TBLMessage.class)                               
                                    .createAlias("events.comments","comments")
                                    .add(Restrictions.eq("user.id", 1L)).list();                        
        return comments;
    }
    
    @Override               
    public int getUnreadMessageCountForUser(TBLUser user) {
        
        int totals = 0;
        
        @SuppressWarnings("unchecked")
        List<Number> counts = sessionFactory.getCurrentSession()
                              .createCriteria(TBLMessage.class)
                              .add(Restrictions.eq("recipient", user))
                              .add(Restrictions.eq("readByRecipient", false))
                              .setProjection(Projections.rowCount()).list();
        
        for(Number count: counts) {
            totals += count.intValue();
        }
        
        return totals;
    }
    

    @Override
    @SuppressWarnings("unchecked")
    public Pagination<TBLUser> getListOfUsers(long page) {
        
     Criteria userLists =  sessionFactory.getCurrentSession()
                                .createCriteria(TBLUser.class);             
    
        
     return new Pagination <TBLUser> (userLists, page, Order.asc("firstName"));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public long getUserCount() {
        
        long userCount =  (Long) sessionFactory.getCurrentSession()
                                .createCriteria(TBLUser.class)
                                .setProjection(Projections.rowCount())
                                .uniqueResult();
        
        return userCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TBLUser> getAllKaguBuzzUsers() {
        return (List<TBLUser>) sessionFactory.getCurrentSession().createCriteria(TBLUser.class).list();
    }
    
    @Override
    public long getUserIntegrityRating(TBLUser user) {
        
        if(user == null) return 0L;
        
        Query query = sessionFactory.getCurrentSession()
                      .createQuery("select sum(feedback.rating) from TBLMessageUserTransactionFeedback feedback " +
                                   "where feedback.recipient = :user");

        query.setParameter("user", user);
        
        if(query.uniqueResult() == null) return 0L;
        
        return ((Double) query.uniqueResult()).longValue();
    }
    
    @Override
    public long getUserReccomendationCount(TBLUser user) {
        
        if(user == null) return 0L;
        
        Query query = sessionFactory.getCurrentSession()
                      .createSQLQuery("select count(tbl_users) as total from tbl_users_recommended where recommended = :user");

        query.setParameter("user", user);
        
        Number number = (Number) query.list().get(0);
        
        if(number == null) return 0L;
        
        return number.intValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean hasRecomended(TBLUser user, TBLUser reccomendedUser) {

        if(user == null || reccomendedUser == null) return false;
        
        Query query = sessionFactory.getCurrentSession()
                      .createSQLQuery("select * from tbl_users_recommended where tbl_users = :user and recommended = :reccomendedUser");

        query.setParameter("user", user);
        query.setParameter("reccomendedUser", reccomendedUser);
        
        return (!query.list().isEmpty());
    }  
}
