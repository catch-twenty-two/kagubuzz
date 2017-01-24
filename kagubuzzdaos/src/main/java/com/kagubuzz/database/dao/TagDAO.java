package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.datamodels.hibernate.LSTTag;

public interface TagDAO {

    @SuppressWarnings("unchecked")
    public abstract <HibernateEntityTag> List<LSTTag> getTags(Class<HibernateEntityTag> tag, int maxResults, int firstResult);

    @SuppressWarnings("unchecked")
    public abstract <HibernateEntityTag> List<LSTTag> getTagsByPartialMatch(String partialMatch, Class<HibernateEntityTag> tag);

    public abstract <HibernateEntityTag> LSTTag getTagByName(String name, Class<HibernateEntityTag> tag);

}