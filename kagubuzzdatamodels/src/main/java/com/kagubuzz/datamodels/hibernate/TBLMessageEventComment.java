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
@Table(name = "tbl_message_event_comments")
public class TBLMessageEventComment extends TBLMessage implements Serializable, Ratable {
    
	private static final long serialVersionUID = 1L;

	private Float rating = 0F;
	
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
	@Override
	public MessageType messageType() { return MessageType.EventComment;	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLEvent event;

	public TBLEvent getEvent() {return event;}
	
	@Override
	public JSPMessageRenderer getParent() {return this.event;}
	    
	public void setEvent(TBLEvent event) { this.event = event; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }

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
