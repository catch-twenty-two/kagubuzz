package com.kagubuzz.services;

import java.util.List;
import java.util.Set;

import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLBallot;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.utilities.KaguImage;

public interface PostsService {

    public abstract void renewPost(Post post);

    public abstract Notification deletePost(Post post);

    void setActive(Post post, boolean setActive);

    void setGeographicalCoordinates(Double longitude, Double latitude, Post post);

    void notifyFollowersOfNewPost(Post post);

    void notifyParticipantsOfNewDiscussionPost(TBLMessageDiscussionPublic message);

    void setAllNotificationsToRead(List<? extends TBLMessage> messages, TBLDiscussionBase discussion, TBLUser user, boolean delete);

    void setAllOwnerPostNotificationsToRead(Post post, List<? extends TBLMessage> messages, TBLUser user);

    void setAllOwnerPostNotificationsToRead(Post post, Set<? extends TBLMessage> messages, TBLUser user);

    void expireAllOldNotificationDiscussions();

    void sendAutoSearchAlertToUser(TBLUser user);

    Notification deletePost(Long id, Class<? extends EntityWithOwner> post, TBLUser user);

    KaguImage createBackgroundImage(String backgroundImage);

    KaguImage createSideBarImage(String sideBarImage);

    TBLBallot createPostBallotIfNeeded(Ratable post);

    Notification flagPost(Class<? extends Ratable> type, Long id, TBLUser user, FlagTypes flag);
}