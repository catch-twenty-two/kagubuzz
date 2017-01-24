package com.kagubuzz.datamodels;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLAd;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public class AdBookmark implements Bookmark , Comparable<Bookmark>{

    TBLAd ad;
    
    @Override
    public TBLMessageUserFeedback getUserFeedback(TBLUser user) {        
        return null;
    }
    
    public AdBookmark(TBLAd ad) {
        this.ad = ad;
    }

    @Override
    public Date bookmarkRelevantDate() {       
        return ad.getEndDate();
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
        return ad.getViewingURL();
    }

    @Override
    public String getTitle() {
        return ad.getTitle();
    }

    @Override
    public Long getId() {       
        return ad.getId();
    }

    @Override
    public String deleteBookMarkURL() {
        return ad.messageType().getViewingURL();
    }
}
