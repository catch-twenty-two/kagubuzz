package com.kagubuzz.messagetemplates.privatemessage;

import org.stringtemplate.v4.ST;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;

public class PrivateDiscussionMessageTemplate extends TemplateMessage {

    TBLMessage message;

    protected PrivateDiscussionMessageTemplate(TBLUser recipient, TBLMessage message) {
        super(recipient);
        this.message = message;
    }

    protected String create(String templateName) {

        ST privateMessageTemplate = getTemplate(templateName);

        privateMessageTemplate.add("message", message.getMessage());
        privateMessageTemplate.add("sender_name", message.getSender().getFirstName());
        privateMessageTemplate.add("viewing_url", message.getMessageViewingURL());
        privateMessageTemplate.add("recipient_name", message.getRecipient().getFirstName());
        privateMessageTemplate.add("recipient_id", message.getRecipient().getId());
        privateMessageTemplate.add("server_url", getServerURL());
        privateMessageTemplate.add("message_title", message.getTitle());

        privateMessageTemplate = wrapInEmailIfAppropriate(privateMessageTemplate);

        return privateMessageTemplate.render();
    }

}