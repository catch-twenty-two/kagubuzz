package com.kagubuzz.datamodels.hibernate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_event_rsvps")
public class TBLEventRSVP implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    TBLUser owner;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    TBLEvent event;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    protected TBLUser getOwner() {return owner;}
    protected void setOwner(TBLUser owner) {this.owner = owner;}
    
    protected TBLEvent getEvent() { return event;}
    protected void setEvent(TBLEvent event) {this.event = event;}
}
