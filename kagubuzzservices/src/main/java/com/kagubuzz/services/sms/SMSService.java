package com.kagubuzz.services.sms;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.TextMessage;

@Service("smsService")
public class SMSService {
	KaguTextFormatter formatter = new KaguTextFormatter();
	
	static Logger logger = Logger.getLogger(SMSService.class);
	
    @Value("${nexmo.username}")
    private String nexmoUserName;
    @Value("${nexmo.password}")
    private String nexmoPassword;
	@Value("${nexmo.blockoutgoingsms}")
	private boolean blockOutgoingSMS;
	@Value("${nexmo.sms_phone_number}")
	private String smsPhoneNumber;
	   
	BlockingQueue<SMSMessage> smsBlockedQueue = new LinkedBlockingQueue<SMSMessage>(100);
	
	class SMSMessage {
	    public String toNumber;
	    public String fromNumber;
	    public String message;
	    
        private SMSMessage(String toNumber, String fromNumber, String message) {
            this.toNumber = toNumber;
            this.fromNumber = fromNumber;
            this.message = message;
        }
	}
	
    @PostConstruct
    public void StarSMSSender() {
        SendSMSQueue smsSendThread = new SendSMSQueue();
        smsSendThread.start();
    }
	
    class SendSMSQueue extends Thread {
        
        public void run() {

            while (true) {

                SMSMessage smsMessage = null;

                try {
                    smsMessage = smsBlockedQueue.take();
                }
                catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                NexmoSmsClient client = null;

                smsMessage.message = formatter.getSummary(smsMessage.message, 137);

                try {
                    logger.info("Sending '" + smsMessage.message + "' to " + smsMessage.toNumber + " total chars = " + smsMessage.message.length());

                    if (blockOutgoingSMS) {
                        logger.info("Message Blocked");
                        return;
                    }

                    client = new NexmoSmsClient(nexmoUserName, nexmoPassword);

                    TextMessage textMessage = new TextMessage(smsPhoneNumber, smsMessage.toNumber, smsMessage.message);

                    // Use the Nexmo client to submit the Text Message ...

                    SmsSubmissionResult[] results = null;

                    results = client.submitMessage(textMessage);

                    // Evaluate the results of the submission attempt ...

                    logger.info("... Message submitted in [ " + results.length + " ] parts");

                    for (int i = 0; i < results.length; i++) {

                        logger.info("--------- part [ " + (i + 1) + " ] ------------");

                        logger.info("Status [ " + results[i].getStatus() + " ] ...");

                        logger.info("Message-Id [ " + results[i].getMessageId() + " ] ...");

                        if (results[i].getStatus() == SmsSubmissionResult.STATUS_OK) {
                            logger.info("SUCCESS");
                        }

                        if (results[i].getTemporaryError()) {
                            logger.info("TEMPORARY FAILURE - PLEASE RETRY");
                        }
                        else if (results[i].getStatus() != SmsSubmissionResult.STATUS_OK) {
                            logger.info("SUBMISSION FAILED!");
                        }

                        if (results[i].getErrorText() != null) {
                            logger.info("Error-Text [ " + results[i].getErrorText() + " ] ...");
                        }

                        if (results[i].getMessagePrice() != null) {
                            logger.info("Message-Price [ " + results[i].getMessagePrice() + " ] ...");
                        }

                        if (results[i].getRemainingBalance() != null)
                            logger.info("Remaining-Balance [ " + results[i].getRemainingBalance() + " ] ...");
                    }

                }
                catch (Exception e) {
                    logger.error("Failed to communicate with the Nexmo Client", e);
                    return;
                }

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
	
	public void sendSMSMessage(String toNumber, String fromNumber, String message) {
	    
	    smsBlockedQueue.offer(new SMSMessage(toNumber, fromNumber, message));
	}
	
	public static String getPhoneDashedFormat(TBLUser user) {
		
		String prefix = (String) user.getPhone().subSequence(0, 1);
		String areaCode = (String) user.getPhone().subSequence(1, 4);
		String exchange = (String) user.getPhone().subSequence(4, 7);
		String lastfour = (String) user.getPhone().subSequence(7, 11);
		
		return prefix + " (" + areaCode + ") " + exchange + "-" + lastfour;
	}
	
	public String getNexmousername() {
		return nexmoUserName;
	}

	public void setNexmousername(String nexmousername) {
		this.nexmoUserName = nexmousername;
	}

	public String getNexmopassword() {
		return nexmoPassword;
	}

	public void setNexmopassword(String nexmopassword) {
		this.nexmoPassword = nexmopassword;
	}

	public Boolean getBlockoutgoingsms() {
		return blockOutgoingSMS;
	}

	public void setBlockoutgoingsms(Boolean blockoutgoingsms) {
		this.blockOutgoingSMS = blockoutgoingsms;
	}

    public String getSmsPhoneNumber() {
        return smsPhoneNumber;
    }

    public void setSmsPhoneNumber(String smsPhoneNumber) {
        this.smsPhoneNumber = smsPhoneNumber;
    }
}
