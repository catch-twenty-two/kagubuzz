package com.kagubuzz.datamodels;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public class PublicDiscussionBookmark implements Bookmark , Comparable<Bookmark>{

    TBLDiscussionPublic discussion;
    
    @Override
    public TBLMessageUserFeedback getUserFeedback(TBLUser user) {        
        return null;
    }
    
    public PublicDiscussionBookmark(TBLDiscussionPublic discussion) {
        this.discussion = discussion;
    }

    @Override
    public Date bookmarkRelevantDate() {       
        return discussion.getUpdatedDate();
    }
    @Override
    public boolean canBeRated() {        
        return false;
    }
    @Override
    public String getBookMarkCSS() {
        return "text-danger";
    }
    @Override
    public String getDetail() {
        return null;
    }
    
    @Override
    public boolean bookmarkCanBeDeleted() {        
        return true;
    }
    @Override
    public String bookmarkIcon() {        
        return "icon-bookmark";
    }

    @Override
    public int compareTo(Bookmark bookmark) {
        return bookmark.bookmarkRelevantDate().compareTo(this.bookmarkRelevantDate());   
    }

    @Override
    public String getViewingURL() {        
        return discussion.getViewingURL();
    }

    @Override
    public String getTitle() {
        return discussion.getTitle();
    }

    @Override
    public Long getId() {       
        return discussion.getId();
    }

    @Override
    public String deleteBookMarkURL() {
        return discussion.messageType().getViewingURL();
    }
}
