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
import com.kagubuzz.messagetemplates.ads.offermessages.OfferMessage;
import com.kagubuzz.services.AdService;
import com.kagubuzz.services.UserAccountService;

@ContextConfiguration(locations = { "classpath:dao-beans.xml", "classpath:service-beans.xml", "classpath:hibernate-test-config.xml",
        "classpath:datasource-test-config.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class DiscussionAdExchangeTest extends AbstractTransactionalJUnit4SpringContextTests {
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

        TBLUser buyer = userAccountService.createGenericUser();
        crudDAO.create(buyer);
        
        TBLAd ad = adService.getGenericAd(seller);

        TBLDiscussionAd offer = adService.createOffer(buyer, ad);
        
        adService.acceptOffer(offer);
        
        OfferMessage message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.buyerInitialContact(); 
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.sellerInitialContact();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.buyerDeclined();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.buyerThinkOnIt();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.buyerAccepted();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.sellerAccepted();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.sellerAlreadyAccepted();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.sellerThinkOnIt();
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.sellerDeclined();

        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.offerReminder();

        DisplayMessages(message);
        
        /*message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.offerCanceled(seller);
        
        DisplayMessages(message);
        
        message = new OfferMessage(offer, userAccountService.getSystemUser());     
        message.exchangeFinalized(seller);

        DisplayMessages(message);*/
    }
    
    void DisplayMessages(OfferMessage message) {
        System.out.println("**************");
        System.out.println(message.getInstanceOfSMS().body);
        System.out.println("-------");
        System.out.println(message.getInstanceOfEmail().subject);
        System.out.println("-------");
        //System.out.println(message.getInstanceOfEmail().body);
        System.out.println("-------");
        System.out.println(message.getReceipt().getMessage());
        System.out.println("**************");
    }
}
