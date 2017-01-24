package com.kagubuzz.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.utilities.JavaFileUtilities;

@ContextConfiguration(locations = {"classpath:dao-beans.xml", 
								   "classpath:service-beans.xml", 
		   						   "classpath:hibernate-test-config.xml",
		   					  	   "classpath:datasource-test-config.xml"})

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class UserAccountTest extends AbstractTransactionalJUnit4SpringContextTests
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
	
	
	@Before
	public void setUp() throws Exception
	{
	    
	}

	List<String> userFolderPaths = new ArrayList<String>();
	
	@After
	public void tearDown() throws Exception
	{
	    for(String userFolderPath: userFolderPaths) {
	        JavaFileUtilities.deleteFile(userFolderPath);
	    } 
	}
	
	@Test
	public void testCreateUser()
	{
		TBLUser testUser = userAccountService.createGenericUser();
		userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser));
		crudDAO.create(testUser);
	}
	
	@Test
	public void deleteUser()
	{
		TBLUser testUser = userAccountService.createGenericUser();
		
		userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser));
		
		crudDAO.create(testUser);
		crudDAO.delete(testUser);
	}
	
	@Test 
	public void testAttachDiscussionToUsers()
	{

	}
	
	@Test
	public void testAttachEventToUser()
	{
		TBLUser testUser = userAccountService.createGenericUser();
		
	    userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser));
	      
		TBLEvent event = eventService.getGenericEvent(testUser);
		
		testUser.addUserEvent(event);
		
		crudDAO.update(testUser);
		
		TBLEvent tblEvent = (TBLEvent) testUser.getUserEvents().toArray()[0];
		
		Assert.assertNotNull(tblEvent);
		
		testUser.removeUserEvent(tblEvent);
		
		crudDAO.update(testUser);
		
		Assert.assertTrue(testUser.getUserEvents().size() == 0);
		
		testUser.addUserEvent(event);
		
		Long eventId = event.getId();
		
		crudDAO.update(testUser);
		
	}
	
	@Test
	public void testUserBookmarks()
	{
		TBLUser testUser1 = userAccountService.createGenericUser();
		
	    userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser1));
	      
		TBLUser testUser2 = userAccountService.createGenericUser();
		
		userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser2));
	      
		TBLEvent event = eventService.getGenericEvent(testUser1);

		crudDAO.create(testUser1);

		testUser2.addToBookmarkedEvents(event);
		
		crudDAO.create(testUser2);
	}
	
	@Test
	public void testUpdateUser()
	{
		TBLUser testUser = userAccountService.createGenericUser();
		
		userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser));
		
		String gericUserEmail = testUser.getEmail();
		
		crudDAO.create(testUser);
		
		TBLUser savedUser = userDAO.getUserByEmail(gericUserEmail);
		
		savedUser.setEmail("newemail");
		crudDAO.update(savedUser);
		testUser = userDAO.getUserByEmail("newemail");
		
		Assert.assertEquals(savedUser.getEmail(), "newemail");
		
	}

	@Test
	public void testGetCommentsToUser()
	{
		
	}

	@Test
	public void testGetUserById()
	{
		
	}

	@Test
	public void testGetUserSavedEvents()
	{
		
	}

	@Test
	public void testAddUserSavedEvent()
	{
		
	}

}
