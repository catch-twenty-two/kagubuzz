package com.kagubuzz.database.dao.tests;
import java.util.List;

import java.util.Scanner;
 
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-beans.xml", 
								   "classpath:spring-hibernate-test-config.xml"})

@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class HibernateSearchTest {
    @Autowired 
    EventDAO eventDao;
    @Autowired
    SessionFactory sessionFactory;
    
    @Before
	public void setUp() throws Exception
	{
		
	}

	@After
	public void tearDown() throws Exception
	{
	}
	    
   @Test
   public void testLuceneSearch() throws InterruptedException {
       System.out.println("\n\n******Data stored in event table******\n");
       displayEventTableData();
        
       // Create an initial Lucene index for the data already present in the database
       doIndex();
        
       Scanner scanner = new Scanner(System.in);
       String consoleInput = null;
        
       while (true) {
           // Prompt the user to enter query string
           System.out.print("\n\nEnter search key (To exit type 'X')");            
           consoleInput = scanner.nextLine();
            
           if("X".equalsIgnoreCase(consoleInput)) {
               System.out.println("End");
               System.exit(0);
           }   
            
           List<TBLEvent> result = search(consoleInput);            
           System.out.println("\n\n>>>>>>Record found for '" + consoleInput + "'");
            
           for (TBLEvent event : result) {
               System.out.println(event.getMessage());
           }               
       }           
   }
   
    private void doIndex() throws InterruptedException {
         
        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
        fullTextSession.createIndexer(TBLAd.class).startAndWait();
        fullTextSession.createIndexer(TBLEvent.class).startAndWait();
         
        
    }
     
    @SuppressWarnings("unchecked")
    private List<TBLEvent> search(String queryString) {
        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
         
        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(TBLEvent.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("body").matching(queryString).createQuery();
 
        // wrap Lucene query in a javax.persistence.Query
        org.hibernate.Query fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, TBLEvent.class);
         
        
		List<TBLEvent> eventList = fullTextQuery.list();
         
        return eventList;
    }
     
    private void displayEventTableData() {
         
        try {
             
            // Fetching saved data
            @SuppressWarnings("unchecked")
			List<TBLEvent> eventList = sessionFactory.getCurrentSession().createQuery("from TBLEvent").list();
             
            for (TBLEvent event : eventList) {
                System.out.println(event.getTitle());
            }
             
        } catch (Exception ex) {
            ex.printStackTrace();
		}
	}
}