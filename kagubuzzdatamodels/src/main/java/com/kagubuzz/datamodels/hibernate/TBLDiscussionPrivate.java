package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_discussions_private")
public class TBLDiscussionPrivate extends TBLDiscussionBase {

	private static final long serialVersionUID = 1L;
  
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "discussion")
    @OrderBy("createdDate")
    private List<TBLMessageDiscussionPrivate> messages = new ArrayList<TBLMessageDiscussionPrivate>();
	
	public List<TBLMessageDiscussionPrivate> getMessages() {	return messages;}
	public void setMessages(List<TBLMessageDiscussionPrivate> messages) {this.messages = messages;}
	public void removeMessage(TBLMessageDiscussionPrivate message) { messages.remove(message);}
	public void addMessage(TBLMessageDiscussionPrivate message) {messages.add(message);}
	
	@Override
	public String getViewingURL() { return "/discussion/private" + super.getViewingURL();};
	
	@Override
	public TBLMessage getFirstMessageInThread() {return getMessages().get(0);}
	
	@Override
	public MessageType messageType() {	return MessageType.PrivateDiscussion;}
	
    @Override
    public JSPMessageRenderer getParent() { return this;}
    
    @Override
    public TBLUser getOwner() { return this.getSender(); }
    @Override
    public Date getCreatedDateInUserTZ() {
        // TODO Auto-generated method stub
        return null;
    }
}