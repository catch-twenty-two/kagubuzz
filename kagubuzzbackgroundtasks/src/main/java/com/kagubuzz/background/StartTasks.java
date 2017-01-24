package com.kagubuzz.background;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.EventService;
import com.kagubuzz.services.UserAccountService;

public class StartTasks {

    static Logger logger = Logger.getLogger(StartTasks.class);

    @Autowired
    SearchService searchService;

    @Autowired
    EventService eventService;
    
    @Autowired
    AdService adService;
    
    @Autowired
    EventDAO eventDAO;
    
    @Autowired
    UserAccountService userAccountService;
    
    // Every five minutes

    @Scheduled(fixedDelay = 60*1000*5)
    @Transactional
    public void everyFiveMinutes() {
       logger.info("Running 5 minute tasks");
       searchService.checkAutoSearchKeywords();
       eventDAO.removeExpiredEventsFromListings();
       eventService.resetRepeatingEvents(); 
       userAccountService.doUser5MinTasks();
    }
    
    // Every hour

    @Scheduled(cron="0 55 * * * ?")
    @Transactional
    public void everyHour() {
        logger.info("Running hour tasks");
        eventService.sendBookmarkedEventsNotices(); 
    }
    
    // Everyday at midnight
    
    @Scheduled(cron="0 0 0 * * ?")
    @Transactional
    public void everyDay() {
        logger.info("Running 24 hour tasks");
        eventService.sendExpiringEventsNotices();
        adService.sendAdResponseRequestedReminders();
        adService.sendExchangeRatingReminders();
    }
    
    
    void startUp() {

        try {
            searchService.indexLuceneSearch();
        }
        catch (InterruptedException e) {
            logger.error("Error Indexing", e);
        }

        logger.info("Done");
    }

    public static void main(final String[] args) {
        final ApplicationContext context = 
                new ClassPathXmlApplicationContext( "classpath:dao-beans.xml", 
                                                    "classpath:tasks-config.xml",
                                                    "classpath:hibernate-config.xml",
                                                    "classpath:service-beans.xml", 
                                                    "classpath:datasource-config.xml");

        final StartTasks tasks = context.getBean(StartTasks.class);

        ((AbstractApplicationContext) context).registerShutdownHook();

       // tasks.startUp();
    }
}