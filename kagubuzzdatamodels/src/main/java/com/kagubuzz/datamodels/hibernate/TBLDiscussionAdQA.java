package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name="tbl_discussions_ad_qa")
public class TBLDiscussionAdQA extends TBLDiscussionBase {
    
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "discussion")
    @OrderBy("id")
	private List<TBLMessageAdQuestion> messages = new ArrayList<TBLMessageAdQuestion>();
	 
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    TBLAd ad;
    
    public List<TBLMessageAdQuestion> getMessages() { return messages;}
    public void setMessages(List<TBLMessageAdQuestion> messages) {this.messages = messages;}
    public void addMessage(TBLMessageAdQuestion message) {this.messages.add(message);}
    public void removeMessage(TBLMessageAdQuestion message) {this.messages.remove(message);}

    @Override
    public TBLUser getOwner() { return ad.getOwner(); }
    
    @Override
    public JSPMessageRenderer getParent() { return this.ad;}
    
    @Override
    public TBLMessage getFirstMessageInThread() {return (getMessages().isEmpty()) ? null: getMessages().get(0);}
    
    @Override
    public MessageType messageType() { return MessageType.AdQuestion;}
     
    @Override
    public Date getCreatedDateInUserTZ() {
        // TODO Auto-generated method stub
        return null;
    }
    public TBLAd getAd() {
        return ad;
    }
    public void setAd(TBLAd ad) {
        this.ad = ad;
    }
}