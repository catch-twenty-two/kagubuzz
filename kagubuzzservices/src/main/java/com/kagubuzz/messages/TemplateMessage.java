package com.kagubuzz.messages;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

import com.kagubuzz.datamodels.hibernate.TBLUser;

public abstract class TemplateMessage {
    
    private STRawGroupDir group = new STRawGroupDir("stringtemplates",'$','$');
    
    public String body;
    public String subject;
    
    static String serverURL = System.getenv("SERVER_URL");
    
    protected TBLUser recipient;
    
    protected TemplateMessage(TBLUser recipient) {
        this.recipient = recipient;
    }
    
    /**
     * This will only render a template message as an email if it has email and body in the name
     * so any message can be sent to this function
     * 
     * @param templateMessage
     * @return
     */
    protected ST wrapInEmailIfAppropriate(ST templateMessage) {
        
        if(templateMessage.getName().contains("email") &&
           templateMessage.getName().contains("body")) {    
            templateMessage = group.getInstanceOf("email-template-body").add("kagubuzzurl", serverURL).add("standardcontent", templateMessage.render());  
        }
                
        return templateMessage;
    }

    public static String getServerURL() {
        return serverURL;
    }


    public STRawGroupDir getGroup() {
        return group;
    }
    
    public ST getTemplate(String name) {
        return group.getInstanceOf(name);
    }
    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
