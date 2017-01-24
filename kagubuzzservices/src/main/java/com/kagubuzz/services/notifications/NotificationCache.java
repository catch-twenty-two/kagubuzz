package com.kagubuzz.services.notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

@Service
public class NotificationCache {

    Cache<Object, Object> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<Notification> get(Long userId) throws ExecutionException {

        if (userId == null) {
            return null;
        }
        
        
        List<Notification> messages = (List<Notification>) cache.getIfPresent(userId);

        if (messages != null) {
            cache.invalidate(userId);
        }
        else {
            messages = new ArrayList<Notification>();
        }

        //System.out.println("Notifications Cache Size = " + cache.size());
        
        return messages;
    }

    @SuppressWarnings("unchecked")
    public void add(TBLMessage message) {
        
        if(message == null) {
            return;
        }
       
        Notification notification = Notification.getNotification(message);
        long userId = message.getRecipient().getId();
        
        List<Notification> notificationList = (List<Notification>) cache.getIfPresent(userId);

        if (notificationList == null) {
            notificationList = new ArrayList<Notification>();
            cache.put(userId, notificationList);
        }

        notificationList.add(notification);
        //System.out.println("Notifications Cache Size = " + cache.size());
    }
    
    @SuppressWarnings("unchecked")
    public void add(Notification notification, TBLUser user) {                
        
        if(notification == null || user == null) {
            return;
        }
        
        if(!user.isLoggedIn()) {
            return;
        }

        long userId = user.getId();
        
        List<Notification> notificationList = (List<Notification>) cache.getIfPresent(userId);

        if (notificationList == null) {
            
            notificationList = new ArrayList<Notification>();
            cache.put(userId, notificationList);
        }

        notificationList.add(notification);
        //System.out.println("Notifications Cache Size = " + cache.size());
    }
}