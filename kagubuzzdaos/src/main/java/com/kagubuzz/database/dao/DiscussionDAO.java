package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface DiscussionDAO {

    public Pagination<TBLDiscussionBase> getLatestPublicDiscussions(LSTDiscussionCategory category, Long discussionToStartAt);

    List<TBLDiscussionPublic> getLatestPublicDiscussionTrends(int maxResults);

    List<LSTDiscussionCategory> getPublicDiscussionCategories();

    void removeUserFromAllDiscussions(TBLUser user);

    List<TBLDiscussionBase> getAllDiscussionsForUser(TBLUser user);

}