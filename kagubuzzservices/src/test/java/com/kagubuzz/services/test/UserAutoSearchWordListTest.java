package com.kagubuzz.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.CategoryUtilityService;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.datamodels.hibernate.TBLUsersAutoSearchKeywordList;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.utilities.JavaFileUtilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dao-beans.xml",
                                    "classpath:service-beans.xml", 
                                    "classpath:hibernate-test-config.xml", 
                                    "classpath:datasource-test-config.xml" })

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class UserAutoSearchWordListTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	UserDAO userDAO;

	@Autowired
	CategoryDAO categoryDAO;

	@Autowired
	CRUDDAO crudDAO;

	@Autowired
    UserAccountService userAccountService;
	
	@Before
	public void setUp() throws Exception {
	}

	List<String> userFolderPaths = new ArrayList<String>();
    
    @After
    public void tearDown() throws Exception
    {
        for(String userFolderPath: userFolderPaths) {
            JavaFileUtilities.deleteFile(userFolderPath);
        } 
    }

	public TBLUsersAutoSearchKeywordList getTestTBLUsersAutoSearchKeywordList1() {
		TBLUsersAutoSearchKeywordList searchableKeyWordList = new TBLUsersAutoSearchKeywordList();

		searchableKeyWordList.setAdCategory1((LSTAdCategory) categoryDAO.getCategoryByName("Books", LSTAdCategory.class));
		searchableKeyWordList.setAdKeyword1("Humpty Dumpty");

		searchableKeyWordList.setAdCategory2((LSTAdCategory) categoryDAO.getCategoryByName("Electronics", LSTAdCategory.class));
		searchableKeyWordList.setAdKeyword2("Nintendo");

		searchableKeyWordList.setEventCategory1((LSTEventCategory)categoryDAO.getCategoryByName("Science", LSTEventCategory.class));
		searchableKeyWordList.setEventKeyword1("Astronomy");

		searchableKeyWordList.setEventCategory2((LSTEventCategory)categoryDAO.getCategoryByName("Political", LSTEventCategory.class));
		searchableKeyWordList.setEventKeyword2("Occupy");

		return searchableKeyWordList;
	}
	
	public TBLUsersAutoSearchKeywordList getTestTBLUsersAutoSearchKeywordList2() {
		TBLUsersAutoSearchKeywordList searchableKeyWordList = new TBLUsersAutoSearchKeywordList();

		searchableKeyWordList.setAdCategory1((LSTAdCategory) categoryDAO.getCategoryByName("Furniture", LSTAdCategory.class));
		searchableKeyWordList.setAdKeyword1("desk");

		searchableKeyWordList.setAdCategory2((LSTAdCategory) categoryDAO.getCategoryByName("Tickets", LSTAdCategory.class));
		searchableKeyWordList.setAdKeyword2("beegees");

		searchableKeyWordList.setEventCategory1((LSTEventCategory)categoryDAO.getCategoryByName("Health Events & Fairs", LSTEventCategory.class));
		searchableKeyWordList.setEventKeyword1("fitness coach");

		searchableKeyWordList.setEventCategory2((LSTEventCategory)categoryDAO.getCategoryByName("Sports", LSTEventCategory.class));
		searchableKeyWordList.setEventKeyword2("football");

		return searchableKeyWordList;
	}

	@Test
	public void testAddKeyWordsToUserAccount() {
		
		TBLUser testUser1 = userAccountService.createGenericUser();
	    
		userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser1));
	    
		crudDAO.update(testUser1);
		
		TBLUsersAutoSearchKeywordList searchList = getTestTBLUsersAutoSearchKeywordList1();
		
		searchList.setOwner(testUser1);
		
		testUser1.setSearchAbleKeyWordList(searchList);
		
		crudDAO.update(testUser1);
	}

	@Test
	public void testDeleteKeyWordsFromUserAccount() {
		
	    TBLUser testUser = userAccountService.createGenericUser();
	    userFolderPaths.add(userAccountService.getUserFileDirectoryPath(testUser));
		
		Long userId = crudDAO.create(testUser);
		
		testUser.setSearchAbleKeyWordList(getTestTBLUsersAutoSearchKeywordList1());
		
		crudDAO.update(testUser);
		
		testUser = crudDAO.getById(TBLUser.class, userId);
		
		Assert.assertNotNull(testUser.getSearchableKeyWordList());
		
		testUser.setSearchAbleKeyWordList(null);
		
		crudDAO.update(testUser);
		
		testUser = crudDAO.getById(TBLUser.class, userId);
		
		Assert.assertNotNull(testUser);
		Assert.assertNull(testUser.getSearchableKeyWordList());
	}
}
