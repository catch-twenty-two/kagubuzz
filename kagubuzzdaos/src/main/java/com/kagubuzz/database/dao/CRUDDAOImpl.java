package com.kagubuzz.database.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("crudDAO")
@Transactional
public class CRUDDAOImpl implements CRUDDAO {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@Transactional(readOnly=false)
	public <HibernateEntity> Long create(final HibernateEntity hibernateEntity)
	{
		Long id = (Long)sessionFactory.getCurrentSession().save(hibernateEntity);
		return id;
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(final Object object)
	{
		 sessionFactory.getCurrentSession().delete(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public <HibernateEntity> HibernateEntity getById(final Class<HibernateEntity> type, final Long id)
	{
	    if(id == null) return null;
	    
		HibernateEntity hibernateEntity = (HibernateEntity)  sessionFactory.getCurrentSession().get(type, id);

		return hibernateEntity;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=false)
	public <HibernateEntity> HibernateEntity merge(HibernateEntity hibernateEntity)
	{
		hibernateEntity = (HibernateEntity)  sessionFactory.getCurrentSession().merge(hibernateEntity);

		return hibernateEntity;
	}

	@Override
	@Transactional(readOnly=false)
	public <HibernateEntity> void update(final HibernateEntity hibernateEntity)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(hibernateEntity);
	}

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
