package com.kagubuzz.services.dispatcher;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.message.types.Message;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.email.EmailerService;
import com.kagubuzz.services.notifications.NotificationCache;
import com.kagubuzz.services.sms.SMSService;
import com.kagubuzz.spring.hibernate.TransacatalRollbackOnAllExecptions;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

@Service
public class MessageDispatcher {
	@Autowired 
	SessionFactory sessionFactory;
    
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	EmailerService emailerService;
	
	@Autowired
	SMSService smsService;
	
	@Autowired
	UserAccountService userAccountService;
	
	@Autowired
	NotificationCache notificationCache;
	
	@Autowired
	CRUDDAO crudDAO;

    @Autowired
    SecurityService securityService;
    
    static Logger logger = Logger.getLogger(MessageDispatcher.class);
    
    HashSet<Message> messageQueue = new HashSet<Message>();
    
    // Set up a timer to send message queue every 10 minutes
     
    @PostConstruct
    public void StartMailSender() {
       new Timer().schedule(new SendQueue(), 0, TimeUnit.MINUTES.toMillis(15));
    }
    
    // queue up a message to keep from getting multiple emails or sms messages with same message
    
    @TransacatalRollbackOnAllExecptions
    public void queueMessage(Message message) {
        
        if(message.getReceipt().getDeliveryMethod() == null) {     
            message.getReceipt().setDeliveryMethod(message.getReceipt().getRecipient().getContactMethod());  
        } 

        if(message.saveMessageHistory()) {
            saveMessageToDataBase(message);
            message.saveMessageForDispatch();
            messageQueue.add(message); 
        }
        else {
            logger.warn("Cannot queue message id " + message.getReceipt() + " for sending later, receipt was not saved to db, sending now instead.");
            dispatchMessage(message);
        }
    }

    // send message immediately
    
    @TransacatalRollbackOnAllExecptions
    public void sendMessage(Message message) {
        
        if(message.getReceipt().getDeliveryMethod() == null) {     
            message.getReceipt().setDeliveryMethod(message.getReceipt().getRecipient().getContactMethod());  
        }
        
        if(message.saveMessageHistory()) {
            saveMessageToDataBase(message);
            message.saveMessageForDispatch();
        } 
        
        dispatchMessage(message);
    }
	
    protected void dispatchMessage(Message message) {
        
        logger.info("Sending message to:" + message.getReceipt().getRecipient().getFirstName() + " Via " + message.getReceipt().getDeliveryMethodName());
        
        if(message.getReceipt().getRecipient().showNotifications()) {
            if (message.saveMessageHistory()){
            	notificationCache.add(message.getReceipt());            	
            }
        }
        
        switch(message.getReceipt().getDeliveryMethod()) {
        
        case Email:
            
            // Check user preferences for email
            
            if(!message.getReceipt().getRecipient().getEmailNotifications()) {
                return;
            }
            
            if((message.getInstanceOfEmail() == null) || 
               (message.getInstanceOfEmail().getBody() == null) || 
               (message.getInstanceOfEmail().getSubject() == null)) {                        
                logger.error("Message, Body or Subject incomplete cannot send email of type " + message.getClass());
                break;
            }          
            
            emailerService.sendEmail(message.getReceipt().getRecipient(), 
                                     userAccountService.getSystemUser(), 
                                     message.getInstanceOfEmail().getBody(), 
                                     message.getInstanceOfEmail().getSubject());          
            break;
            
        case KaguBuzz:
            break;
            
        case Text:
            if((message.getInstanceOfSMS().getBody() == null)) {
                logger.error("Body incomplete cannot send SMS of type " + message.getClass());
                break;
            }
            smsService.sendSMSMessage(message.getReceipt().getRecipient().getPhone(), userAccountService.getSystemUser().getPhone(), message.getInstanceOfSMS().getBody());
            break;
            
        case Voice:
            break;
        }
        
        return;
    }
    
    @Transactional(readOnly = false, rollbackFor = { java.lang.RuntimeException.class })
    void saveMessageToDataBase(Message message) {         
        crudDAO.update(message.getDiscussion()); 
        crudDAO.update(message.getReceipt());  
    }
    
    // Message queue
    
    @Transactional(readOnly = true)
    class SendQueue extends TimerTask {

        public synchronized void run() {
            
            Session session = null;
            
            try {
                if (messageQueue.size() < 1) {
                    return;
                }
                
                session = sessionFactory.openSession();
                
                logger.info("Sending " + messageQueue.size() + " queued messages.");

                for (Message message : messageQueue) {
                    message.receipt = (TBLMessage) session.get(message.messageType.getClassType(), message.messageId);
                    logger.info("Dispatching " + message.messageType.getClassType().getName() + " id = " + message.messageId);
                    dispatchMessage(message);
                }
            }
            catch (Exception e) {
                logger.error("Queued task error" , e);               
            }
            finally {
                messageQueue.clear();
                if(session != null) session.close();
            }
        }
    }
}
