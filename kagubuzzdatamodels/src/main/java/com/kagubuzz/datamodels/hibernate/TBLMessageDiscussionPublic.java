package com.kagubuzz.datamodels.hibernate;

import 	com.kagubuzz.datamodels.enums.MessageType;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.Post;

@Entity
@Table(name="tbl_messages_discussions_public")
public class TBLMessageDiscussionPublic extends TBLMessage implements Serializable, Ratable, EntityWithOwner, Post {
	private static final long serialVersionUID = 1L;

	@Override
	public MessageType messageType() { return MessageType.PublicDiscussion; }
	
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLDiscussionPublic discussion;

	public TBLDiscussionPublic getDiscussion() { return discussion; }
	public void setDiscussion(TBLDiscussionPublic discussion) { this.discussion = discussion;}

	@Override
	public JSPMessageRenderer getParent() { return this.discussion; }
	
    @Override    
    public TBLUser getOwner() { return this.getSender(); }
    
    @Override
    public Date getStartDate() {
        return createdDate;
    }
    @Override
    public Date getEndDate() {
        return createdDate;
    }
    @Override
    public void setRenewDate(int daysFromNow) {
 
    }
    
    // TODO: does this really need to be a post is is connected to a discussion which is a post
    
    @Override
    public void setActive(boolean active) { 
        setMarkedForDelete(active);  
    }
    
    @Override
    public void setLongitude(double longitude) {
    }
    @Override
    public void setLatitude(double latitude) {
  
    }
    @Override
    public void setTblKaguLocation(TBLKaguLocation location) {
    }
    @Override
    public TBLKaguLocation getTblKaguLocation() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TBLBallot getBallot() {
        return this.ballot;
    }
    @Override
    public void setBallot(TBLBallot ballot) {
        this.ballot = ballot;
    }
      
    public Set<LSTTag> getTags() { return null; }
}
