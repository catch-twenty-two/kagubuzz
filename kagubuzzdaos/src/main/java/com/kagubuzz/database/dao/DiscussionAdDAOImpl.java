package com.kagubuzz.database.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;

@Repository("discussionAdDAO")
@Transactional(readOnly=true)
public class DiscussionAdDAOImpl implements DiscussionAdDAO {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public TBLDiscussionAd getTransactionByIDAndSecurityCode(long id, String securityCode)
	{
		return (TBLDiscussionAd)	sessionFactory.getCurrentSession()
								.createCriteria(TBLDiscussionAd.class)
								.add(Restrictions.eq("uuid", securityCode))
								.uniqueResult();
	}

}