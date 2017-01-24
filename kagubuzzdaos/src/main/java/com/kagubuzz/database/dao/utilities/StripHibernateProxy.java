package com.kagubuzz.database.dao.utilities;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class StripHibernateProxy {

	// This function takes the hibernate entiy and forces it to be a real java
	// object instead
	// of a hibernate entity

	@SuppressWarnings("unchecked")
    public static <T> T initializeAndUnproxy(T entity) {
		
		if (entity == null) {
			throw new NullPointerException("Entity passed for initialization is null");
		}

		Hibernate.initialize(entity);
		
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		return entity;
	}
}