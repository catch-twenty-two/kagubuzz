package com.kagubuzz.database.dao.tests;

import org.junit.After;
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
import com.kagubuzz.database.dao.utilities.CategoryUtilityService;
import com.kagubuzz.datamodels.hibernate.LSTAdCategory;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import org.apache.log4j.Logger; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dao-beans.xml", 
                                    "classpath:hibernate-test-config.xml",
                                    "classpath:datasource-test-config.xml" })

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class TestLSTCategoryDAO extends AbstractTransactionalJUnit4SpringContextTests
{
	Logger logger = Logger.getLogger(TestLSTCategoryDAO.class);
	
	@Autowired
	CategoryDAO categoryDAO;
	
	@Autowired
	CategoryUtilityService categoryUtilityBean;
	
	@Autowired
	CRUDDAO crudDAO;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception	{
	}

	@Test
	public void testBuildCategories() {
		categoryUtilityBean.createInitialCategoriesAds();
	}
	
	@Test
	public void testBuildEventCategories()	{
		categoryUtilityBean.createInitialCategoriesEvents();
	}
	
	@Test
    public void testBuildDiscussionCategories()   {
        categoryUtilityBean.createInitialCategoriesDiscussions();
    }
	
	@Test
	public void testAdCateogry() {		
	    
		LSTAdCategory lstAD = crudDAO.getById(LSTAdCategory.class, 4L);

		for(LSTCategoryBase category: categoryDAO.getAllDescendantsForCategory(lstAD, false)) {
			logger.info(category.getName());
		}
		
		lstAD = (LSTAdCategory) categoryDAO.getCategoryByName("Tools", LSTAdCategory.class);
		
		for(LSTCategoryBase category: categoryDAO.getAllAncestorsForCategory(lstAD, true)) {
			logger.info(category.getName());
		}
		
		lstAD = (LSTAdCategory) categoryDAO.getCategoryByName("Cars", LSTAdCategory.class);
		
		for(LSTCategoryBase category: categoryDAO.getAllDescendantsForCategory(lstAD, false)) {
			logger.info(category.getName());
		}
	}
}
