package com.kagubuzz.database.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.enums.NotificationGroups;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

@Repository("messageDAO")
@Transactional(readOnly = true)
public class MessageDAOImpl implements MessageDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    CRUDDAO crudDAO;
    
    // this returns all messages for all entities inheriting tblmessage class only limiting each query to maxMessages so
    // the final list may be longer than maxMessages
    
    @Override
    public List<TBLMessage> getUnreadMessagesForUser(TBLUser user, int maxMessages, int messageToStartAt) {

        @SuppressWarnings("unchecked")
        List<TBLMessage> messages = (List<TBLMessage>) sessionFactory.getCurrentSession()
                .createCriteria(TBLMessage.class)
                .add(Restrictions.eq("recipient", user))
                .addOrder(Order.desc("createdDate"))
                .add(Restrictions.eq("readByRecipient", false))
                .setMaxResults(messageToStartAt + maxMessages)
                .list();

        Collections.sort(messages);
        
        if (messages.size() <  maxMessages){
            return messages;
        }
        
        
        if (messages.size() <  messageToStartAt + maxMessages){
            messages.clear();
            return messages;
        }
        
        return (messages.size() > maxMessages) ? messages.subList(messageToStartAt, messageToStartAt + maxMessages) : messages;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLMessageDiscussionPrivate> getExpiredNotificationsForUser(TBLUser user) {

        Calendar calendar = user.getCalendarInUserTimeZone();
        
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(21) * (-1));
        Date expiryDate = calendar.getTime();
        
        return (List<TBLMessageDiscussionPrivate>) sessionFactory.getCurrentSession()
                                                  .createCriteria(TBLMessageDiscussionPrivate.class)
                                                  .add(Restrictions.eq("readByRecipient", false))
                                                  .add(Restrictions.eq("recipient", user))  
                                                  .add(Restrictions.lt("createdDate", expiryDate))
                                                  .list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLDiscussionPrivate> getExpiredNotificationDiscussions() {

        Calendar calendar = Calendar.getInstance();
        
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(14) * (-1));
        Date expiryDate = calendar.getTime();
        
        return (List<TBLDiscussionPrivate>) sessionFactory.getCurrentSession()
                                            .createCriteria(TBLDiscussionPrivate.class)
                                            .add(Restrictions.eq("sticky", false))
                                            .add(Restrictions.lt("updatedDate", expiryDate))
                                            .list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLDiscussionBase> getExpiredDiscussionsForUser(TBLUser user, Class<? extends TBLDiscussionBase> discussion, TBLUser systemUser) {

        Calendar calendar = user.getCalendarInUserTimeZone();
        
        calendar.add(Calendar.HOUR, (int) TimeUnit.DAYS.toHours(14));
        Date expiryDate = calendar.getTime();
        
        return (List<TBLDiscussionBase>) sessionFactory.getCurrentSession()
                                         .createCriteria(TBLDiscussionBase.class)
                                         .add(Restrictions.lt("createdDate", expiryDate))
                                         .createCriteria("participants", "p")
                                         .add(Restrictions.and(Restrictions.eq("p.id", user.getId()),Restrictions.eq("p.id", systemUser.getId())))
                                         .list();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLMessage> getUnreadMessagesInGroupForUser(TBLUser user, NotificationGroups group, int maxMessages, int messageToStartAt) {

        if(group == NotificationGroups.Everything) {
            return this.getUnreadMessagesForUser(user, maxMessages, messageToStartAt);
        }
        
        List<TBLMessage> messages = new ArrayList<TBLMessage>();

        for (MessageType messageType : MessageType.values()) {
            
            if (messageType.getGroup() == group) {
                messages.addAll((List<TBLMessage>) sessionFactory.getCurrentSession()
                        .createCriteria(messageType.getClassType())
                        .add(Restrictions.eq("recipient", user))
                        .addOrder(Order.desc("createdDate"))
                        .add(Restrictions.eq("readByRecipient", false))
                        .setMaxResults(messageToStartAt + maxMessages)
                        .list());                
            }
        }

        Collections.sort(messages);

        if (messages.size() < maxMessages) {
            return messages;
        }

        if (messages.size() < messageToStartAt + maxMessages) {
            messages.clear();
            return messages;
        }

        return (messages.size() > maxMessages) ? messages.subList(messageToStartAt, messageToStartAt + maxMessages) : messages;
    }
    

    @Override
    public TBLMessage getMessage(long id, Class<? extends TBLMessage> clazz) {

        TBLMessage message = (TBLMessage) sessionFactory.getCurrentSession()
                .createCriteria(clazz)
                .add(Restrictions.eq("id", id))
                .uniqueResult();

        return message;
    }
    
    @Override
    public TBLMessageUserTransactionFeedback getUserTransactionFeedback(TBLUser user, TBLDiscussionAd disucsisonAd) {

        TBLMessageUserTransactionFeedback message = (TBLMessageUserTransactionFeedback) sessionFactory.getCurrentSession()
                                                    .createCriteria(TBLMessageUserTransactionFeedback.class)
                                                    .add(Restrictions.eq("transaction", disucsisonAd))
                                                    .add(Restrictions.eq("sender", user))
                                                    .uniqueResult();

        return message;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<TBLMessageUserTransactionFeedback> getAllFeedbackForUser(TBLUser user) {

        return (List<TBLMessageUserTransactionFeedback>) sessionFactory.getCurrentSession()
               .createCriteria(TBLMessageUserTransactionFeedback.class)                                                    
               //.createCriteria("recipient")               
               .add(Restrictions.eq("recipient", user))
               .addOrder(Order.desc("createdDate"))
               .list();
    }
    
    @Override
    @Transactional(readOnly = false)
    public void markMessageReadAndDelete(long id, Class<? extends TBLMessage> clazz) {

        TBLMessage message = (TBLMessage) sessionFactory.getCurrentSession()
                .createCriteria(clazz)
                .add(Restrictions.eq("id", id))
                .uniqueResult();

        if(message.isDeleteAfterNotify()) {
            crudDAO.delete(message.getParent());
            return;
        }
        else {
            message.setReadByRecipient(true);
        }

        crudDAO.update(message);
    }
}
