package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.datamodels.enums.NotificationGroups;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface MessageDAO {

    void markMessageReadAndDelete(long id, Class<? extends TBLMessage> clazz);

    public abstract TBLMessage getMessage(long id, Class<? extends TBLMessage> clazz);

    TBLMessageUserTransactionFeedback getUserTransactionFeedback(TBLUser user, TBLDiscussionAd disucsisonAd);

    List<TBLMessage> getUnreadMessagesInGroupForUser(TBLUser user, NotificationGroups group, int maxMessages, int messageToStartAt);

    List<TBLMessage> getUnreadMessagesForUser(TBLUser user, int maxMessages, int messageToStartAt);
    
    List<TBLMessageDiscussionPrivate> getExpiredNotificationsForUser(TBLUser user);

    List<TBLDiscussionPrivate> getExpiredNotificationDiscussions();

    List<TBLDiscussionBase> getExpiredDiscussionsForUser(TBLUser user, Class<? extends TBLDiscussionBase> discussion, TBLUser systemUser);

    List<TBLMessageUserTransactionFeedback> getAllFeedbackForUser(TBLUser user);

}