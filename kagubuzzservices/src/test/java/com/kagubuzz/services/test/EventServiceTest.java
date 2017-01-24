package com.kagubuzz.services.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLBallot;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.PostsService;
import com.kagubuzz.services.UserAccountService;

@ContextConfiguration(locations = {"classpath:dao-beans.xml", 
								   "classpath:service-beans.xml", 
		   						   "classpath:hibernate-test-config.xml",
		   					  	   "classpath:datasource-test-config.xml"})

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class EventServiceTest extends AbstractTransactionalJUnit4SpringContextTests
{
    
	@Autowired 
	EventService eventService;
	
	@Autowired
	UserDAO  userDAO;
	
	@Autowired
	EventDAO eventDAO;
	
	@Autowired
	UserAccountService userAccountService;
	
	@Autowired 
	CRUDDAO crudDAO;
	
	@Autowired
	PostsService postsService;
	
	@After
	public void tearDown() throws Exception	{
	    
	}
	
	@Test
	public void testFlagEvent()	{
	    
	    TBLUser user1 = userAccountService.createGenericUser();
	    TBLUser user2 = userAccountService.createGenericUser();
        
	    TBLEvent event = eventService.getGenericEvent(user1);	    
	       
        TBLBallot ballot = new TBLBallot();
        ballot.setOwner(event.getOwner());
        event.setBallot(ballot);
	   
	    crudDAO.create(user1);
	    crudDAO.create(user2);
	    
	    crudDAO.create(ballot);
	    crudDAO.create(event);
	    
	    postsService.flagPost(TBLEvent.class, event.getId(), user2, FlagTypes.Spam);
	    
	    crudDAO.update(event);
	    
	    Long flagId = ballot.getId();
	    
	    System.out.println("user flags = " + user1.getUserFlags().size());	    
	    
	   // crudDAO.delete(user1);
  
	}

    @Test
    public void testExpiringEvent() {

        TBLUser user = userAccountService.createGenericUser();
        TBLEvent event = eventService.getGenericEvent(user);

        crudDAO.create(user);
        crudDAO.create(event);

    }
    
	
	@Test
	public void deleteFlagEvent()    {
	    crudDAO.delete(crudDAO.getById(TBLUser.class, 1L));
	}

}
