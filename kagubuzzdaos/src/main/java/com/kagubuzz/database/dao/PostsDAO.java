package com.kagubuzz.database.dao;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface PostsDAO {

   public abstract <T extends Comparable<T>> Pagination<T> getPostsForUser(Long page, TBLUser user, Class<T> postType);
}
