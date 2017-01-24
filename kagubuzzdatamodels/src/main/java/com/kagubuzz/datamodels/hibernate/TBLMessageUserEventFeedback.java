package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name = "tbl_message_user_event_feedback")
public class TBLMessageUserEventFeedback extends TBLMessageUserFeedback implements Serializable, JSPMessageRenderer {
    
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLEvent event;
	
	@Override
	public MessageType messageType() { return MessageType.EventRating;	}

    public TBLEvent getEvent() {return event;}
    public void setEvent(TBLEvent event) {this.event = event;}
    
    @Override
    public JSPMessageRenderer getParent() {return event;}

    @Override
    public String getIcon() {
        // TODO Auto-generated method stub
        return null;
    }   
    
}
