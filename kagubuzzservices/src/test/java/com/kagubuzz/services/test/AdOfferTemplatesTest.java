package com.kagubuzz.services.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.UserAccountService;

@ContextConfiguration(locations = { "classpath:dao-beans.xml", "classpath:service-beans.xml", "classpath:hibernate-test-config.xml",
        "classpath:datasource-test-config.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class AdOfferTemplatesTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    AdService adService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    EventDAO eventDAO;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    CRUDDAO crudDAO;

    @Test
    public void testMakeOffer() {
        TBLUser seller = userAccountService.createGenericUser();
        crudDAO.create(seller);

        TBLUser buyer1 = userAccountService.createGenericUser();
        crudDAO.create(buyer1);

        TBLUser buyer2 = userAccountService.createGenericUser();
        crudDAO.create(buyer2);
        
        TBLAd ad = adService.getGenericAd(seller);

        TBLDiscussionAd offer1 = adService.createOffer(buyer1, ad);
        TBLDiscussionAd offer2 = adService.createOffer(buyer2, ad);
        
        adService.acceptOffer(offer1);
        adService.acceptOffer(offer2);
    }
}
