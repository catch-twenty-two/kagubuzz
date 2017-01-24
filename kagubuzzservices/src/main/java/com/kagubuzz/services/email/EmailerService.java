package com.kagubuzz.services.email;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.utilities.KaguTextFormatter;

@Service
public class EmailerService {
	
    @Value("${emailservice.blockoutgoingemail}")
    private boolean blockOutgoingEmail;
    
    static Logger log = Logger.getLogger(EmailerService.class);
    
	@Autowired
	JavaMailSender mailSender;

	public void sendEmail(TBLUser toUser, TBLUser fromUser, String body, String subject) {
		
	    KaguTextFormatter formatter = new KaguTextFormatter();
	    subject = formatter.getSummary(subject);
	    
	    if(blockOutgoingEmail){
	        log.info("Email message blocked to: " + toUser.getEmail());
	        log.info("from user: " + fromUser.getEmail());
	        //log.info("Body: " + body); 
	        log.info("subject:" + subject);
	        return;
	    }
	    
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom(fromUser.getEmail(), "Kagu Buzz");
			helper.setTo(toUser.getEmail());
			helper.setSubject(subject);
			helper.setText(body, true);
		} catch (MessagingException e) {			
			log.error("While trying to send an email", e);
		}
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		
		mailSender.send(mimeMessage); 
		
	}

}
