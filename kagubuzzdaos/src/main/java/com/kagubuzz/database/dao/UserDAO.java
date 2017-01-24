package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.Bookmark;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface UserDAO {

    public abstract TBLUser getUserByEmail(String userEmail);

    public abstract TBLUser getUserByUserAndProviderId(String providerUserId, String providerId);

    public abstract TBLUser getUserById(long userId);

    public abstract TBLUser getUserByPhoneNumber(String phoneNumber);

    public abstract List<TBLMessage> getCommentsForUserPosts(Long userId, int maxMessages, int messageToStartAt);

    public abstract List<TBLMessage> getUserComments(long userID);

    public int getUnreadMessageCountForUser(TBLUser user);

    public abstract List<Bookmark> getUserRatedAndBookMarkedEvents(TBLUser user);

    public abstract List<TBLUser> getFollowers(TBLUser user);

    Pagination<TBLUser> getListOfUsers(long page);

    long getUserCount();

    long getUserIntegrityRating(TBLUser user);

    List<TBLUser> getAllKaguBuzzUsers();

    TBLUser getUserBySecurityCode(String securityToken);

    TBLUser checkUserCredentials(String userEmail, String password);

    long getUserReccomendationCount(TBLUser user);

    boolean hasRecomended(TBLUser user, TBLUser recommendedUser);

}