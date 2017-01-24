package com.kagubuzz.database.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.datamodels.hibernate.LSTTag;

@Repository("tagDAO")
@Transactional(readOnly=true)
public class TagDAOImpl implements TagDAO {
	
	@Autowired
	SessionFactory sessionFactory;
	
    @Override
    @SuppressWarnings("unchecked")
    public <HibernateEntityTag> List<LSTTag> getTags(Class<HibernateEntityTag> tag, int maxResults, int firstResult) {
        List<LSTTag> list = (List<LSTTag>) sessionFactory.getCurrentSession()
                .createCriteria(tag)
                .addOrder(Order.asc("name"))
                .setMaxResults(maxResults)
                .setFirstResult(firstResult)
                .list();

        return list;
    }
	
	@Override
    @SuppressWarnings("unchecked")
	public <HibernateEntityTag> List<LSTTag> getTagsByPartialMatch(String partialMatch, Class<HibernateEntityTag> tag)
	{
		List<LSTTag> partialMatchList = 
				(List<LSTTag>) sessionFactory.getCurrentSession()
				.createCriteria(tag)
				.add(Restrictions.ilike("name", "%" + partialMatch + "%"))
				.setMaxResults(8)
				.addOrder(Order.desc("name"))
				.list();
		
		return partialMatchList;				
	}
	
	@Override
    public <HibernateEntityTag> LSTTag getTagByName(String name, Class<HibernateEntityTag> tag) {
		
		return	(LSTTag) sessionFactory.getCurrentSession()
				.createCriteria(tag)
				.add(Restrictions.eq("name", name))
				.uniqueResult();
	}
	
}
