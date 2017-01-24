 	package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_messages_discussions_private")
public class TBLMessageDiscussionPrivate extends TBLMessage implements Serializable, JSPMessageRenderer
{
	private static final long serialVersionUID = 1L;

	@Override
	public MessageType messageType() {	return MessageType.PrivateDiscussion;  }
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = false)
	private TBLDiscussionPrivate discussion;
	
	public TBLDiscussionPrivate getDiscussion() {return discussion;	}

	public void setDiscussion(TBLDiscussionPrivate discussion) {this.discussion = discussion;}

	@Override
	public JSPMessageRenderer getParent() {return this.discussion;}

}
