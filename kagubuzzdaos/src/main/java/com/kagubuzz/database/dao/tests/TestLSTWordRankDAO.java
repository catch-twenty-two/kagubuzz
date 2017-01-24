package com.kagubuzz.database.dao.tests;

import java.util.List;

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

import com.kagubuzz.database.dao.AdDAO;
import com.kagubuzz.database.dao.WordRankDAO;
import com.kagubuzz.datamodels.hibernate.TBLWordRank;

import org.apache.log4j.Logger; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-beans.xml", 
								   "classpath:spring-hibernate-test-config.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class TestLSTWordRankDAO extends AbstractTransactionalJUnit4SpringContextTests
{
	Logger logger = Logger.getLogger(TestLSTWordRankDAO.class);
	@Autowired
	WordRankDAO wordRankDAO;
	
	@Autowired
	AdDAO adDAO;
	
	@Before
	public void setUp() throws Exception
	{
		
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testWrdRank()
	{
		List<TBLWordRank> smsKeyword =
		wordRankDAO.GetWordRankForString("1973 Ford Mustang? (*&@#)*^&#@!)*^");
		
		for(TBLWordRank tblwr: smsKeyword) {
			
			logger.info(tblwr.getWord() + "(" + tblwr.getRank() + ")");
		}
		
		logger.info(smsKeyword);
	}
	
	@Test
	public void testAdCateogry()
	{		

	}
}
