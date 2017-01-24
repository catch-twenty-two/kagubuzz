package com.kagubuzz.services.notifications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.stringtemplate.v4.ST;

import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.utilities.KaguTextFormatter;
// must add getters and setters for json conversion to work

public class Notification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    String message = null;
    String title = null;
    String alertType = NotificationTypes.info.name();
    int fadeIn = 1000;
    int fadeOut = 300;
    int timeOut = 20000;
    String position = NotificationPosition.topRight.toastrName;
    int senderId = 1;
    int id;
    boolean reload = false;
    boolean isModal = false;
    String modalTemplateName;
    
    public Notification(){}

    public String toJson() { return "JSON.parse('"+ new KaguTextFormatter().toJSON( this ) + "')";}
    
    public Notification sendReload() { reload = true; return this; };
    
    public Notification(ST stringTemplate) { 
        this.message = stringTemplate.render(); 
    }
    
    public Notification(String message) { 
        this.message = message; 
    }
    
    public Notification(String title, String message) { 
        this.message = message; 
        this.title = title; 
    }

    public Notification(String title, String message, NotificationTypes notificationType) { 
        this.message = message; 
        this.title = title;
        this.alertType = notificationType.name();
        id = message.hashCode();
    }
    
    public Notification(String title, 
                        String message, 
                        NotificationTypes notificationType, 
                        NotificationPosition position) { 
        this.message = message; 
        this.title = title;
        this.alertType = notificationType.name();
        this.position = position.toastrName;
        id = message.hashCode();
    }
    
    public Notification setResponseType(NotificationTypes alertType) { this.alertType = alertType.name(); return this;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public boolean getReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    };
    
    static public Notification getNotification(TBLMessage message) {
        
        String title =  String.format("<a style='color: #FFFFFF;' href=\"%s\">RE: %s</a>", 
                message.getMessageViewingURL(),
                message.getTitle());

        String notificationBody = String.format("<strong>%s</strong> from <strong>%s</strong><br>%s", 
                                                 message.messageType().getName(), 
                                                 message.getSender().getFirstName(), 
                                                 message.getBodySummary());

        return new Notification(title, notificationBody);
    }

    public boolean getIsModal() {
        return isModal;
    }
    
    public String getModalTemplateName() {
        return modalTemplateName;
    }
    
    public Notification setModalNotification(String modalTemplateName) {
        this.isModal = true;
        this.modalTemplateName = modalTemplateName;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notification other = (Notification) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        }
        else if (!message.equals(other.message))
            return false;
        return true;
    }
    
    
}
