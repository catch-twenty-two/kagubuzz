package com.kagubuzz.database.dao;

import org.hibernate.SessionFactory;

public interface CRUDDAO {

	public abstract <HibernateEntity> Long create(final HibernateEntity hibernateEntity);

	public abstract void delete(final Object object);

	@SuppressWarnings("unchecked")
	public abstract <HibernateEntity> HibernateEntity getById(final Class<HibernateEntity> type, final Long id);

	@SuppressWarnings("unchecked")
	public abstract <HibernateEntity> HibernateEntity merge(HibernateEntity hibernateEntity);

	public abstract <HibernateEntity> void update(final HibernateEntity hibernateEntity);

    SessionFactory getSessionFactory();

}