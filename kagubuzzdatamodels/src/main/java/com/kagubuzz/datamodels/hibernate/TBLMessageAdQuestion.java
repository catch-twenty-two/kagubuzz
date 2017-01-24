 	package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_messages_ad_questions")
public class TBLMessageAdQuestion extends TBLMessage implements Serializable, Ratable, JSPMessageRenderer
{
	private static final long serialVersionUID = 1L;
	
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
	@Override
	public MessageType messageType() { return MessageType.AdQuestion;}
  
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLDiscussionAdQA discussion;
	
	public TBLDiscussionAdQA getDiscussion() { return discussion; }
	public void setDiscussion(TBLDiscussionAdQA discussion) { this.discussion = discussion; }

    @Override
    public JSPMessageRenderer getParent() { return this.discussion.getParent(); }
    
    @Override
    public TBLBallot getBallot() {
        return ballot;
    }
    
    @Override
    public void setBallot(TBLBallot ballot) {
        this.ballot = ballot;
    }
    
    @Override
    public void setActive(boolean active) {
       setMarkedForDelete(active);       
    }
}

