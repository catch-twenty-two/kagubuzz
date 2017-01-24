package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.kagubuzz.datamodels.JSPMessageRenderer;

@MappedSuperclass
public abstract class TBLMessageUserFeedback extends TBLMessage implements Serializable, JSPMessageRenderer {
    
	private static final long serialVersionUID = 1L;

	private Float rating = 0F;
	
	@Transient
	public static String EMPTY_REVIEW_CODE = "1jd8dh23193j0sa7009j";
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private TBLBallot ballot;

	public TBLBallot getBallot() {return ballot;}	    
	public void setBallot(TBLBallot ballot) { this.ballot = ballot; }
	
    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }
    
    public abstract String getIcon();
}
