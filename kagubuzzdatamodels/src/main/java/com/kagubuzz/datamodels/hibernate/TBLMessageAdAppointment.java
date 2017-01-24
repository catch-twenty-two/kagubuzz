 	package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_messages_ad_appointments")
public class TBLMessageAdAppointment extends TBLMessage implements Serializable, JSPMessageRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	public MessageType messageType() {
		return MessageType.AdAppointment;
	}
	
	@Override
	public JSPMessageRenderer getParent() {return this.discussion;};

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLDiscussionAd discussion;
	
	public TBLDiscussionAd getDiscussionAd() {
		return discussion;
	}

	public void setDiscussionAd(TBLDiscussionAd discussion) {
		this.discussion = discussion;
	}
}
