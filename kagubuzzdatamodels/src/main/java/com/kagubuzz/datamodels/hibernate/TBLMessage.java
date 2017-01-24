 	package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.management.RuntimeErrorException;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.utilities.SEOUtilities;
        
@MappedSuperclass
public abstract class TBLMessage extends KaguTextFormatter implements Serializable, JSPMessageRenderer, EntityWithOwner,  Comparable<TBLMessage>
{
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Date createdDate = new Date();
    
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    DeliveryMethod deliveryMethod = null;
    
    boolean markedForDelete = false;
	boolean isPrivate = true;
	
	@Column(nullable = false, length = 140)
	String subject;
	
	@Column(nullable = false, length = 10000)
	String body;
	
	boolean readByRecipient = false;
	boolean sentBySystem = false;
	
	@Column(nullable=true)
	boolean deleteAfterNotify = false;
	
	// Reply Options
	boolean publicCanReply = false;
	boolean recipientCanReply = false;

	boolean isFirstMessage = false;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	TBLUser recipient;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	TBLUser sender;
	
	public String getSubject() {return subject;}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public boolean isFirstMessage() { return isFirstMessage;}
	public void setFirstMessage(boolean isFirstMessage) { this.isFirstMessage = isFirstMessage; }
	
	@Override
	public String getMessage() { return body; }
	public void setMessage(String body) throws RuntimeErrorException {
		
		if(body.length() > messageType().getMaxLength()) {
			throw new RuntimeErrorException(new Error("String for body to long for message type."));
		}	
		
		this.body = body;	
	}
	
	@Override
	public String getTitle() {return this.subject;}
	
	@Override
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id;}
	
	public TBLUser getRecipient() { return recipient; }
	public void setRecipient(TBLUser recipient) {this.recipient = recipient; }
	
	@Override
	public TBLUser getSender() { return sender;}
	public void setSender(TBLUser sender) { this.sender = sender;}
	
	public boolean getIsPrivate() { return isPrivate; }
	public void setIsPrivate(boolean publiclyViewable) { this.isPrivate = publiclyViewable;	}

	public boolean getrecipientCanReply() {return recipientCanReply;}
	public void setRecipientCanReply(boolean recipientCanReply) {	this.recipientCanReply = recipientCanReply;}
	
	public boolean isReadByRecipient() { return readByRecipient;}
	
	public void setReadByRecipient(boolean readByRecipient) {	
	    this.readByRecipient = readByRecipient;
	}
	
	public DeliveryMethod getDeliveryMethod() {	return deliveryMethod; }	
	public void setDeliveryMethod(DeliveryMethod deliveryMethod) {	this.deliveryMethod = deliveryMethod; }

	@Override
	public Date getCreatedDate() {return createdDate;}
	public void setCreatedDate(Date createdDate) { this.createdDate = createdDate;}
	
	@Override
	public Boolean recipientCanReply() { return recipientCanReply; };
	
    @Override
    public Date getCreatedDateInUserTZ() {
        
        // created dates are stored in the server in gmt
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);
        
        cal.add(Calendar.MINUTE, sender.getTimeZoneOffset());
        
        return cal.getTime();
    }
    
	// This is to allow the system to send messages to a user on behalf of another user
	
	public boolean isSystemMessage() {return sentBySystem; }
	public void setSystemMessage(boolean sentBySystem) {this.sentBySystem = sentBySystem;}
	
	@Override
    public int compareTo(TBLMessage arg0) {
	    return arg0.getCreatedDate().compareTo(this.getCreatedDate());
    }
	
    @Override
    public String getFriendlyURL() { 
        return SEOUtilities.URLFriendly(getTitle() + "-" + getSender().getTblKaguLocation().getCity()); 
    }

	public String getMessageViewingURL() { 
	        return getParent().getViewingURL()  + ((getId() != null) ? "?scroll_id=" + getId() : "");
	}
	
	@Override
    public String getViewingURL() { return getParent().getViewingURL(); }
	
	@Override
	public TBLUser getOwner() {return this.sender;};
	
	@Override
	public String getFormattedDateLongForm() {	
	    return null;//getFormattedDateLongForm(createdDate, sender.getTimeZoneObject());
	    }
	public String getFormattedDateShortForm() {	return null;//getFormattedDateShortForm(createdDate, sender.getTimeZoneObject());
	}	
	public String getDeliveryMethodName() { return deliveryMethod.name(); }
	public String getDeliveryMethodIconName() { return deliveryMethod.getIconName();}	
	public String timeSinceCreated() {return timeSinceCreated(getCreatedDate());}	
	public String timeSinceCreatedShortForm() {return timeSinceCreatedShortForm(getCreatedDate());}	
	public String getSubjectSummary(){ return getSummary(this.getSubject(), 70);}
	public String getBodySummary(){ return getSummary(this.getMessage(),100);}
	public boolean getPublicCanReply() {return publicCanReply;}
	public void setPublicCanReply(boolean publicCanReply) {this.publicCanReply = publicCanReply;}
    public boolean isDeleteAfterNotify() {
        return deleteAfterNotify;
    }
    public void setDeleteAfterNotify(boolean deleteAfterNotify) {
        this.deleteAfterNotify = deleteAfterNotify;
    }
    public boolean isMarkedForDelete() {
        return markedForDelete;
    }
    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
    
    @Override
    public String getIconName() {
        return "discussion.png";
    }
}
