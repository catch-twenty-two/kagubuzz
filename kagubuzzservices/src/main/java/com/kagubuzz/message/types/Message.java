package com.kagubuzz.message.types;

import java.util.ArrayList;
import java.util.List;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionBase;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.messages.TemplateMessage;
import com.kagubuzz.services.notifications.Notification;

public abstract class Message {

    boolean saveHistory = true;
    protected DeliveryMethod deliveryMethod;
    
	protected TBLUser recipient;
	protected TBLDiscussionBase discussion = null;
	public TBLMessage receipt;
	
	public abstract TBLDiscussionBase getDiscussion();
	public abstract TBLMessage getReceipt();
    
    public boolean saveMessageHistory() {return saveHistory;}   
    public void setSaveMessageHistory(boolean saveDiscussion) { this.saveHistory = saveDiscussion;}
    
    CRUDDAO crudDAO;
    
    // Changes a list of messages into a list of notifications
    
    static public List<Notification> toNotificationList(List<TBLMessage> messages) {
        
        List<Notification> notificationList = new ArrayList<Notification>();
        
        for(TBLMessage message :messages) {
            
           String title =  String.format("<a href=\"%s\">RE: %s</a>", 
                                         message.getMessageViewingURL(),
                                         message.getTitle());
            
            String notificationBody = String.format("<strong>%s</strong> from <strong>%s</strong><br>%s", 
                                                     message.messageType().getName(), 
                                                     message.getSender().getFirstName(), 
                                                     message.getBodySummary());
            
            Notification notification = new Notification(title, notificationBody);
            
            notificationList.add(notification);
        }
        
        return notificationList;
    }
    
    public abstract TemplateMessage getInstanceOfSMS();
    public abstract TemplateMessage getInstanceOfEmail();
    
    @Override
    public int hashCode() {
       return (this.getReceipt().getTitle() + this.getReceipt().getRecipient().getEmail() + this.getReceipt().getSender().getEmail()).hashCode();                
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }
    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
    
    // If message is queued this is saved for later retrieval from db
    
    public MessageType messageType;
    public long messageId;
    
    public void saveMessageForDispatch() {
        this.messageType = receipt.messageType();
        this.messageId = receipt.getId();
    }
}
