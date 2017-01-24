package com.kagubuzz.messagetemplates.qaadmessages;

import org.stringtemplate.v4.ST;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionAdQA;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.utilities.KaguTextFormatter;

public class ADQAMessageTemplates extends TemplateMessage {

    TBLDiscussionAdQA discussion;
    
    protected ADQAMessageTemplates(TBLUser recipient, TBLDiscussionAdQA discussion) {
        super(recipient);
        this.discussion = discussion;
    }
    
    protected String create(String templateName, TBLMessage receipt) {
        
        ST adQATemplate = getTemplate(templateName);
        KaguTextFormatter formatter = new KaguTextFormatter();
        
        adQATemplate.add("message", receipt.getMessage());
        adQATemplate.add("viewing_url", receipt.getMessageViewingURL());
        adQATemplate.add("ad_title", discussion.getAd().getTitle());
        adQATemplate.add("ad_owner", discussion.getAd().getOwner().getFirstName());
        adQATemplate.add("ad_id", discussion.getAd().getId());
        adQATemplate.add("sender", receipt.getSender().getFirstName());
        adQATemplate.add("recipient_name",  receipt.getRecipient().getFirstName());
        adQATemplate.add("server_url",  getServerURL());
        adQATemplate.add("post_summary",  receipt.getBodySummary());
        adQATemplate.add("post_sms_summary",  formatter.getSummary(receipt.getMessage(), 40));
        adQATemplate.add("title",  discussion.getAd().getTitle());
        
        adQATemplate = wrapInEmailIfAppropriate(adQATemplate);
        
        return adQATemplate.render(); 
     }

}