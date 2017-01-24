package com.kagubuzz.messagetemplates.discussions;

import org.stringtemplate.v4.ST;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;

public class PublicDiscussionMessageTemplate extends TemplateMessage {

    TBLMessage message;

    protected PublicDiscussionMessageTemplate(TBLUser recipient, TBLMessage message) {
        super(recipient);

        this.message = message;
    }

    protected String create(String templateName) {

        ST publicDiscussionTP = getTemplate(templateName);

        publicDiscussionTP.add("post_id", message.getId());
        publicDiscussionTP.add("message", message.getMessage());
        publicDiscussionTP.add("post_owner", message.getSender().getFirstName());
        publicDiscussionTP.add("viewing_url", message.getMessageViewingURL());
        publicDiscussionTP.add("recipient_name", message.getRecipient().getFirstName());
        publicDiscussionTP.add("recipient_id", message.getRecipient().getId());
        publicDiscussionTP.add("server_url", getServerURL());
        publicDiscussionTP.add("post_summary", message.getBodySummary());
        publicDiscussionTP.add("post_title", message.getTitle());

        publicDiscussionTP = wrapInEmailIfAppropriate(publicDiscussionTP);

        return publicDiscussionTP.render();
    }

}