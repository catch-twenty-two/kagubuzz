package com.kagubuzz.messagetemplates.systemmessages;

import java.text.SimpleDateFormat;
import org.stringtemplate.v4.ST;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.utilities.KaguTextFormatter;

public class SystemMessageTemplates extends TemplateMessage {
    
    protected SystemMessageTemplates(TBLUser recipient) {
        super(recipient);
    }
    
    protected String create(String templateName, TBLUser user) {
        return create(templateName, null, user);
    }
    
    protected String create(String templateName) {
        return create(templateName, null, null);
    }
    
    protected String create(String templateName, Post post) {
        return create(templateName, post, null); 
    }
    
    protected String create(String templateName, Post post, TBLUser referencedUser) {
        
        ST systemMessage = getTemplate(templateName);

        systemMessage
        .add("server_url", getServerURL())
        .add("recipient_name", recipient.getFirstName())
        .add("recipient_id", recipient.getId())
        .add("recipient_security_code", recipient.getSecurityCode())
        .add("email", recipient.getEmail());
        
        if(referencedUser != null) {
            systemMessage.add("referenced_user", referencedUser.getFirstName());
        }
        
        if(post != null) {
            systemMessage.add("post_summary", post.getMessage());
            systemMessage.add("post_type", post.messageType().name());
            systemMessage.add("url_address_head",post.messageType().name().toLowerCase());
            systemMessage.add("viewing_url", post.getViewingURL());
            systemMessage.add("post_title", post.getTitle());
            systemMessage.add("post_owner", post.getSender().getFirstName());
            systemMessage.add("post_id", post.getParent().getId());
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
            String startDate = sdf.format(new KaguTextFormatter().gmtDateToTzDate(post.getStartDate(), post.getSender().getTimeZoneOffset()));
            String endDate = sdf.format(new KaguTextFormatter().gmtDateToTzDate(post.getEndDate(), post.getSender().getTimeZoneOffset()));        	  
            systemMessage.add("start_date", startDate);
            systemMessage.add("end_date", endDate);
        }

        systemMessage = wrapInEmailIfAppropriate(systemMessage);
  
        return systemMessage.render(); 
    }
    
}