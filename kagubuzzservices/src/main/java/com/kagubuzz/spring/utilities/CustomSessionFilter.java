package com.kagubuzz.spring.utilities;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;

public class CustomSessionFilter extends OpenSessionInViewFilter{
	
	/*@Override
	protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		try {
			Session session = SessionFactoryUtils.openSession(sessionFactory);
			session.setFlushMode(FlushMode.AUTO);
			return session;
		}
		catch (HibernateException ex) {
			throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
		}
	}*/

}
