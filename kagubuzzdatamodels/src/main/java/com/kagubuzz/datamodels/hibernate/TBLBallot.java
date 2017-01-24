package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.kagubuzz.datamodels.enums.FlagTypes;

@Entity
@Table(name="tbl_ballots")
public class TBLBallot implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int miscategorizedCount;
    private int spamCount;
    private int offensiveCount;
    private int otherCount;
    private int buzzUpsCount;
    private int ratingCount;
    private boolean adminFlagged = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    TBLUser owner;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
    Set<TBLUser> voters = new HashSet<TBLUser>();
    
    // Keeps track of all ratings given to an event even when event is deleted all ratings are still attached to owner
    
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="ballot")
    Set<TBLMessageUserEventFeedback> eventRatings = new HashSet<TBLMessageUserEventFeedback>(); 
    
    public Set<TBLMessageUserEventFeedback> getEventRatings() { return eventRatings; }
    public void setEventRatings(Set<TBLMessageUserEventFeedback> ratings) { this.eventRatings = ratings; }
    public void addEventRating(TBLMessageUserEventFeedback rating) { this.eventRatings.add(rating); }
    public void removeEventRating(TBLMessageUserEventFeedback rating) { this.eventRatings.remove(rating); }
    
    // Add ratings for a certain transaction
    
    public void incFlagCount(FlagTypes flag, TBLUser user) {
        
        if(voters.contains(user)) return;        
        
        voters.add(user);
        
        switch(flag) {
  
            case Miscategorized:
                miscategorizedCount += 1;
                break;
                
            case Offensive:
                offensiveCount += 2;
                break;
                
            case Spam:
                spamCount += 5;
                break;
                
            default:
                otherCount += 1;
                break;     
        }        
    }
    
    // If admin flagged, set inactive immediately
    // take hash of content admin flag not removed until hash changes
    
    public int getTotals() { return miscategorizedCount + offensiveCount + spamCount + otherCount; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getMiscategorizedCount() { return miscategorizedCount;}
    public void setMiscategorizedCount(int miscategorizedCount) { this.miscategorizedCount = miscategorizedCount;}

    public int getSpamCount() { return spamCount; }
    public void setSpamCount(int spamCount) { this.spamCount = spamCount; }

    public int getOffensiveCount() { return offensiveCount; }
    public void setOffensiveCount(int offensiveCount) { this.offensiveCount = offensiveCount; }

    public int getOtherCount() { return otherCount; }
    public void setOtherCount(int otherCount) { this.otherCount = otherCount; }

    public TBLUser getOwner() { return owner; }
    public void setOwner(TBLUser owner) { this.owner = owner; }

    public int getBuzzUpsCount() { return buzzUpsCount;}
    public void setBuzzUpsCount(int buzzUpsCount) { this.buzzUpsCount = buzzUpsCount;}

    public int getRatingCount() { return ratingCount;}
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public float getRating() { return buzzUpsCount; }
    
    public boolean isAdminFlagged() {
        return adminFlagged;
    }
    public void setAdminFlagged(boolean adminFlagged) {
        this.adminFlagged = adminFlagged;
    }
}
