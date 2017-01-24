package com.kagubuzz.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.MessageDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLBallot;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.datamodels.hibernate.TBLUsersAutoSearchKeywordList;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguImage;

@Service
public class PostsServiceImpl implements PostsService {
    
    @Value("${kagubuzz.event_post_expire}")
    private int eventRenewWindow;
    
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    UserAccountService accountService;
    
    @Autowired
    MessageDAO messageDAO;
    
    @Autowired
    SecurityService securityService;
    
    @Autowired
    KaguLocationDAO kaguLocationDAO;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    static Logger logger = Logger.getLogger(PostsService.class);

    @Override  
    public Notification deletePost(Long id, Class<? extends EntityWithOwner> post, TBLUser user) { 
        EntityWithOwner entity = null;
        
        try {
            entity =   (EntityWithOwner) securityService.checkEntityOwner(id, post, user);
        }
        catch (SecurityException e) {
            return new Notification("Error", "Error", NotificationTypes.error);
        }

        crudDAO.delete(crudDAO.getById(entity.getClass(), id)); 
        
        return new Notification("Post was deleted.");  
    }

    @Override   
    @Transactional(readOnly = false)
    public  Notification deletePost(Post post) {   
        String title = post.getTitle();
        String type = post.messageType().getName();
        
        crudDAO.delete(post);   
        return new Notification("Post Deleted", type + "Post &ldquo;" + title + "&rdquo; was deleted!");  
    }

    @Override
    @Transactional(readOnly = false)
    public void renewPost(Post post) {
        post.setRenewDate(eventRenewWindow);
        post.setActive(true);   
        crudDAO.update(post);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void setActive(Post ad, boolean setActive) {
        ad.setActive(setActive);
        crudDAO.update(ad);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void setGeographicalCoordinates(Double longitude, Double latitude, Post post) {
        post.setLatitude(latitude);
        post.setLongitude(longitude);
        
        post.setTblKaguLocation(kaguLocationDAO.getClosestZip(latitude, longitude));
        
        crudDAO.update(post);
    }
    
    @Override
    public void notifyFollowersOfNewPost(Post post) {

        List<TBLUser> followers =  userDAO.getFollowers(post.getSender());
        
        for(TBLUser follower: followers) {
            
            SystemMessage systemMessage = new SystemMessage(follower, accountService.getSystemUser());
            
            systemMessage.followingPost(post);
            messageDispatcher.queueMessage(systemMessage);
        }
    }
    
    // TODO: Optimize SQL get all ads/events not in users alert sent table
    
    @Override
    public void sendAutoSearchAlertToUser(TBLUser user) {

        TBLUsersAutoSearchKeywordList keyWordList = user.getSearchableKeyWordList();
                       
        if(keyWordList == null) return;
        
        for(TBLAd ad: keyWordList.getAutoSearchAds()) {
            
            if(!keyWordList.getAutoSearchAdsAlertSent().contains(ad)) {
                
                keyWordList.getAutoSearchAdsAlertSent().add(ad);
                
                SystemMessage systemMessage = new SystemMessage(user, accountService.getSystemUser());            
                systemMessage.autoSearchAlert(ad);
                messageDispatcher.queueMessage(systemMessage);
            }
        }
        
        for(TBLEvent event: keyWordList.getAutoSearchEvents()) {
            
            if(!keyWordList.getAutoSearchEventsAlertSent().contains(event)) {
                
                keyWordList.getAutoSearchEventsAlertSent().add(event);
                
                SystemMessage systemMessage = new SystemMessage(user, accountService.getSystemUser());            
                systemMessage.autoSearchAlert(event);
                messageDispatcher.queueMessage(systemMessage);
            }
        }
        
        crudDAO.update(keyWordList);
    }
    
    @Override
    public void notifyParticipantsOfNewDiscussionPost(TBLMessageDiscussionPublic message) {

        Set<TBLUser> participants =  message.getDiscussion().getParticipants();
        
        Iterator<TBLUser> iter = participants.iterator(); 
        
        while(iter.hasNext()){
            
            TBLUser user = iter.next();
            
            if(message.getSender() == user) {
                continue;
            }
            
            if(message.getRecipient() == user) {
                continue;
            }
            
            if(accountService.getSystemUser() == user) {
                continue;
            }
            
            SystemMessage systemMessage = new SystemMessage(user, accountService.getSystemUser());
            
            systemMessage.discussionPostNewMessage(message);
            messageDispatcher.queueMessage(systemMessage);
        }
    }
    
    @Override
    public void setAllOwnerPostNotificationsToRead(Post post, Set<? extends TBLMessage> messages, TBLUser user) {
        
        if(post.getSender() != user) return;
        
        List<TBLMessage> messagesList = new ArrayList<TBLMessage>();
        
        messagesList.addAll(messages);
        
        setAllOwnerPostNotificationsToRead(post, messagesList, user);
    }
    
    @Override
    public void setAllOwnerPostNotificationsToRead(Post post, List<? extends TBLMessage> messages, TBLUser user) {
        
        if(post.getSender() != user) return;
        
        for(TBLMessage message: messages) {
            if(message.getRecipient() == user) { 
                message.setReadByRecipient(true);
            }            
        }     
        
        crudDAO.update(post);
    }
    
    // TODO: Optimize SQL
    
    @Override
    public void setAllNotificationsToRead(List<? extends TBLMessage> messages, TBLDiscussionBase discussion, TBLUser user, boolean deleteEntireDiscussion) {       
        
        for(TBLMessage message: messages) {
            if(message.getRecipient() == user) { 
                message.setReadByRecipient(true);
            }
        }       
        
        crudDAO.update(discussion);
        
        if(deleteEntireDiscussion == false) {
            return;
        }
        
        if(discussion.getSticky() == false) {
            logger.info("Deleting discussion " + discussion.getTitle());
            crudDAO.delete(discussion);
        }
    }
    
    // TODO: Change to HQL batch delete
    @Override
    public void expireAllOldNotificationDiscussions() {
        
        List<TBLDiscussionPrivate> expiredDiscussions = messageDAO.getExpiredNotificationDiscussions();
        
        for(TBLDiscussionBase discussion: expiredDiscussions) {
            try {
                logger.info("Deleting discussion " + discussion.getTitle());
            }
            catch(Exception e) {
                logger.error("Error while getting title", e);
            }
            crudDAO.delete(discussion);
        }       
        
    }
    
    @Override
    public KaguImage createBackgroundImage(String backgroundImageURL) {

        KaguImage kaguImage = null;

        try {
            kaguImage = new KaguImage(new URL(backgroundImageURL));
            kaguImage.resize(-1, 645);
            kaguImage.lighten(.10F); 
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return kaguImage;
    }
    
    @Override
    public KaguImage createSideBarImage(String sideBarImage) {
        
        KaguImage kaguImage = null;

        try {
            kaguImage = new KaguImage(new URL(sideBarImage));
            kaguImage.resize(-1, 290);
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return kaguImage;
    }
    
    @Override
    public TBLBallot createPostBallotIfNeeded(Ratable post) {
        
        TBLBallot ballot = post.getBallot();
        
        if(ballot == null) {
            ballot = new TBLBallot();
            ballot.setOwner(post.getOwner());
            post.setBallot(ballot);
            crudDAO.create(ballot);
            crudDAO.update(post);
        }
        
        return ballot;
    }
    
    @Override
    @Transactional(readOnly = false)
    public Notification flagPost(Class<? extends Ratable> clazz, Long id, TBLUser user, FlagTypes flag)  {
        
        Ratable post = crudDAO.getById(clazz, id);
        Boolean isAdmin = SpringSecurityUtilities.isAdmin(user);
        
        if(post == null) return null;
        
        TBLBallot ballot = createPostBallotIfNeeded(post);

        ballot.setAdminFlagged(isAdmin);
        
        if(isAdmin) {
            post.setActive(false);
        }
        
        ballot.incFlagCount(flag, user);       
        logger.info("Post flagged");
        crudDAO.update(ballot);
        
        return new Notification(((isAdmin) ? "Administrator" : "") + " Flagged", stringTemplateService.getTemplateNotification("post_flagged").add("post_title", post.getTitle()).render());
   }
}
