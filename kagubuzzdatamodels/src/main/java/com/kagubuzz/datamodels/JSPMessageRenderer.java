package com.kagubuzz.datamodels;

import java.util.Date;

import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface JSPMessageRenderer {

	public abstract Long getId();

	public abstract String getMessage();

	public abstract Date getCreatedDateInUserTZ();
	   
	public abstract Date getCreatedDate();

	public abstract TBLUser getSender();

	public abstract String getFormattedDateLongForm();

	public abstract Boolean recipientCanReply();
	
	public abstract String getTitle();
	
	public abstract MessageType messageType();
	
	public abstract JSPMessageRenderer getParent();
	
	public abstract boolean isSystemMessage();
	
	public abstract String getFriendlyURL();
	
	public abstract String getViewingURL();

    public String getIconName();
}