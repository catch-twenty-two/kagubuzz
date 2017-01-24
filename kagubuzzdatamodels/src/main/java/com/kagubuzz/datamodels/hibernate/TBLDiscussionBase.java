package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cascade;
import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.utilities.SEOUtilities;

@MappedSuperclass
public abstract class TBLDiscussionBase extends KaguTextFormatter implements Serializable, Post, EntityWithOwner, Comparable<TBLDiscussionBase> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdDate = new Date();
    @Column(nullable = true)
    private Date updatedDate = new Date();
    private boolean active = false;
    boolean sticky = false;
	
    @ManyToMany(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLUser> participants = new HashSet<TBLUser>();
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private TBLKaguLocation tblKaguLocation;
    
	public Set<TBLUser> getParticipants() {return participants;}
	public void setParticipants(Set<TBLUser> participants) {this.participants = participants;}
	public void addParticipant(TBLUser user) {this.participants.add(user);}
	public void removeParticipant(TBLUser user) {this.participants.remove(user);}
	
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
        
	public abstract TBLMessage getFirstMessageInThread();

	public boolean isActive() {return active;}
	public void setActive(boolean active) {	this.active = active;}

    public Date getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate;}

    public String getFormattedDateShortForm() { return  getFormattedDateShortForm(createdDate); }
    public String getFormattedDateLongForm() { return  getFormattedDateLongForm(createdDate); }
	
    public String getLastUpdated(int timeZoneOffset) { return getFormattedDateLongForm(gmtDateToTzDate(getUpdatedDate(), timeZoneOffset)); }
    
	public abstract MessageType messageType();
	
	public String getMessage() {return this.getFirstMessageInThread().getMessage();}
	
	public TBLUser getSender() { return this.getFirstMessageInThread().getSender();	}
	
	public Boolean recipientCanReply() { return this.getFirstMessageInThread().recipientCanReply();	}

	public String getTitle() { return this.getFirstMessageInThread().getTitle(); }
	
    public boolean getSticky() {
        return sticky;
    }
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }
	
    @Override
    public JSPMessageRenderer getParent() { return this;}
    
    @Override
    public String getViewingURL() { return  "/view/" + getId() + "/" + getFriendlyURL(); };
    
    @Override
    public boolean isSystemMessage(){ return false;}
    
    @Override
    public String getFriendlyURL() { 
        return SEOUtilities.URLFriendly(getTitle() + "-" + getTblKaguLocation().getCity() + "-" +  getTblKaguLocation().getState()); 
    }
    
    @Override
    public int compareTo(TBLDiscussionBase discussion) {
        return (this.getUpdatedDate().before(discussion.getUpdatedDate())) ? 1 : 0;
    }
    public Date getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }	
    
    @Override
    public Date getCreatedDateInUserTZ() {        
        return this.gmtDateToTzDate(this.getCreatedDate(), this.getOwner().getTimeZoneOffset());
    }
    
    @Override
    public Date getStartDate() {
        return updatedDate;
    }
    
    @Override
    public Date getEndDate() {
        return updatedDate;
    }       
    
    @Override
    public void setRenewDate(int daysFromNow) {
    }
    
    @Override
    public void setLongitude(double longitude) {  
    }
    
    @Override
    public void setLatitude(double latitude) {  
    }
    
    @Override
    public void setTblKaguLocation(TBLKaguLocation location) {this.tblKaguLocation = location;}

    @Override
    public TBLKaguLocation getTblKaguLocation() {         
        return  ((tblKaguLocation == null) ? getOwner().getTblKaguLocation() : tblKaguLocation);
    }
    
    @Override
    public String getIconName() {
        return "discussion.png";
    }
    
    public Set<LSTDiscussionPublicTag> getTags() { return null; }
}