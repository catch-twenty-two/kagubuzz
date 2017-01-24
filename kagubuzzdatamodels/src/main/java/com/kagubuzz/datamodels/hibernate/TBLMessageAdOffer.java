 	package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_messages_ad_offers")
public class TBLMessageAdOffer extends TBLMessage implements Serializable, JSPMessageRenderer
{
	private static final long serialVersionUID = 1L;
	private Boolean offerActive = false;
	
	@Override
	public MessageType messageType() {	return MessageType.AdOffer;	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLDiscussionAd discussion;
	
	@Override
	public JSPMessageRenderer getParent() {return this.discussion;};
	    
	public TBLDiscussionAd getDiscussionAd() {
		return discussion;
	}

	public void setDiscussionAd(TBLDiscussionAd discussion) {
		this.discussion = discussion;
	}

    public Boolean getOfferActive() {
        return offerActive;
    }

    public void setOfferActive(Boolean offerActive) {
        this.offerActive = offerActive;
    }
}
