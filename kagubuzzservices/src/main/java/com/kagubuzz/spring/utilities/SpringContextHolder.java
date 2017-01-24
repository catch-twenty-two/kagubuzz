package com.kagubuzz.spring.utilities;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringContextHolder implements ApplicationContextAware {

		static ApplicationContext ctx= null;

		@Override
		public void setApplicationContext(ApplicationContext arg0) throws BeansException {
			if (arg0!= null)ctx= arg0;
		}
		
		public synchronized static ApplicationContext getApplicationDAOContext() {
			if (ctx!=null)	return ctx;
			ctx = new ClassPathXmlApplicationContext("dao-beans.xml");
			return ctx;
		}
}
