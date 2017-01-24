package com.kagubuzz.datamodels;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public class EventWithUserFeedback implements Comparable<Bookmark> , Bookmark {
    
    TBLEvent tbl_events;
    TBLMessageUserEventFeedback tbl_message_user_event_feedback;
    
    public TBLEvent getEvent() { return tbl_events;}

    @Override
    public int compareTo(Bookmark arg0) { return arg0.bookmarkRelevantDate().compareTo(this.bookmarkRelevantDate());}
    
    @Override
    public String getViewingURL() {
        return (tbl_message_user_event_feedback == null) ? (tbl_events.getViewingURL()) : tbl_message_user_event_feedback.getViewingURL();
    }
    
    @Override
    public String getTitle() {
        return tbl_events.getTitle();
    }
    
    @Override
    public TBLMessageUserFeedback getUserFeedback(TBLUser user) {
        return this.tbl_message_user_event_feedback;
    }
    
    @Override
    public Date bookmarkRelevantDate() {        
        return tbl_events.getStartDate();
    }

    @Override
    public boolean canBeRated() {
        return tbl_events.canBeRated();
    }

    @Override
    public Long getId() {        
        return tbl_events.getId();
    }

    @Override
    public String getBookMarkCSS() {
       return tbl_events.getBookMarkCSS();
    }

    @Override
    public String getDetail() {
        return null;
    }

    @Override
    public boolean bookmarkCanBeDeleted() {
        return (tbl_message_user_event_feedback == null);
    }

    @Override
    public String bookmarkIcon() {
        return "icon-glass";
    }

    @Override
    public String deleteBookMarkURL() {
        return tbl_events.messageType().getViewingURL();
    }
}
