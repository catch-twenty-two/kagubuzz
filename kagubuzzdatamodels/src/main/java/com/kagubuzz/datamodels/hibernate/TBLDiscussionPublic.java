package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_discussions_public")
public class TBLDiscussionPublic extends TBLDiscussionBase implements Ratable {

	private static final long serialVersionUID = 1L;
    private Integer buzzRating;
    private Integer flags;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "discussion")
    @OrderBy("id")
    private List<TBLMessageDiscussionPublic> messages = new ArrayList<TBLMessageDiscussionPublic>();
	
    @OneToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<LSTDiscussionPublicTag> tags = new HashSet<LSTDiscussionPublicTag>();
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    TBLEvent event;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLUser> userBookMarks = new HashSet<TBLUser>();
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private LSTDiscussionCategory category;
    
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
    public LSTDiscussionCategory getCategory() { return category; }
	public void setCategory(LSTDiscussionCategory category) {this.category = category;}

    public Integer getBuzzRating() { return buzzRating; }
    public void setBuzzRating(Integer buzzRating) { this.buzzRating = buzzRating; }

    public Integer getFlags() { return flags; }
    public void setFlags(Integer flags) {this.flags = flags; }
    
	public List<TBLMessageDiscussionPublic> getMessages() {	return messages;}
	public void setMessages(List<TBLMessageDiscussionPublic> messages) {this.messages = messages;}
	public void addMessage(TBLMessageDiscussionPublic message) {this.messages.add(message);}
	public void removeMessage(TBLMessageDiscussionPublic message) {this.messages.remove(message);}
	
    public Set<LSTDiscussionPublicTag> getTags() { return tags; }
    public void setTags(Set<LSTDiscussionPublicTag> tags) { this.tags = tags; }
    public void addTag(LSTDiscussionPublicTag tag) {
        if(tag != null) { this.tags.add(tag); tag.popularity++; }
    }
    
    public void removeTag(LSTDiscussionPublicTag tag) {if(tag != null) this.tags.remove(tag); tag.popularity--;}
    
	@Override
	public MessageType messageType() {	return MessageType.PublicDiscussion;}
	
	@Override
	public TBLMessage getFirstMessageInThread() {return (getMessages().isEmpty()) ? null: getMessages().get(0);}
	
    @Override
    public JSPMessageRenderer getParent() { return this;}
    
    @Override
    public String getViewingURL() {return "/discussion/public" + super.getViewingURL();}
    
    @Override
    public TBLUser getOwner() { return this.getSender(); }
    
    @Override
    public TBLBallot getBallot() {
        return this.ballot;
    }
    
    @Override
    public void setBallot(TBLBallot ballot) {
        this.ballot = ballot;
    }
    public TBLEvent getEvent() {
        return event;
    }
    public void setEvent(TBLEvent event) {
        this.event = event;
    }
    public Set<TBLUser> getUserBookMarks() {
        return userBookMarks;
    }
    public void setUserBookMarks(Set<TBLUser> userBookMarks) {
        this.userBookMarks = userBookMarks;
    }
}