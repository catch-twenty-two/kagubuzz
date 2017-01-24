package com.kagubuzz.datamodels.enums;

import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessage;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdAppointment;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdOffer;
import com.kagubuzz.datamodels.hibernate.TBLMessageAdQuestion;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPrivate;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageEventComment;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserTransactionFeedback;

public enum MessageType {
    
	PrivateDiscussion("Private Message","/discussion/private", true ,3000, TBLMessageDiscussionPrivate.class, NotificationGroups.KaguBuzz), // Connects private messages to their discussions
	PublicDiscussion("Public Discussion","/discussion/public", true, 3000, TBLMessageDiscussionPublic.class, NotificationGroups.Discussions),
	EventComment("Event Comment","event", false, 250, TBLMessageEventComment.class, NotificationGroups.Posts),       
	EventRating("Event Rating","event", false, 250, TBLMessageUserEventFeedback.class, NotificationGroups.Posts),       
	AdAppointment("Appointment Request","undef", false, 500, TBLMessageAdAppointment.class, NotificationGroups.Exchanges),
	AdQuestion("Ad Question", "ad", true, 500, TBLMessageAdQuestion.class, NotificationGroups.Posts),
	Event("Event","/event", false, 250, TBLMessageEventComment.class, NotificationGroups.Everything), 
	Ad("Ad","/ad", false, 250, TBLMessageAdQuestion.class, NotificationGroups.Everything),
	AdOffer("Offer Message", "transaction", true, 3000, TBLMessageAdOffer.class, NotificationGroups.Exchanges),
	TransactionRating("Ad Rating","transaction", false, 250, TBLMessageUserTransactionFeedback.class, NotificationGroups.Exchanges);

	int maxLength;
	String url;
	String name;
	Class<? extends TBLMessage> mType;
	NotificationGroups group;
	
	private MessageType(String name, String url, Boolean canReply, int maxLength, Class<? extends TBLMessage> mType, NotificationGroups grouping) {
	    this.mType = mType;
		this.maxLength = maxLength;
		this.url = url;
		this.name = name;
		this.group = grouping;
	}
	
	public int getMaxLength() {	return maxLength; }
	
	public String getViewingURL()
	{
		return url;
	}
	
	public String getName()
	{
		return name;
		
	}

    public Class<? extends TBLMessage> getClassType() {
        return mType;
    }

    public NotificationGroups getGroup() {
        return group;
    }
}
