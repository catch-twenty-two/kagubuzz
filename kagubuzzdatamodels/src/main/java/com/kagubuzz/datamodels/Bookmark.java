package com.kagubuzz.datamodels;

import java.util.Date;

import com.kagubuzz.datamodels.hibernate.TBLMessageUserFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface Bookmark {
    public abstract String getViewingURL();
    public abstract String getTitle();
    public abstract Long getId();
    public abstract TBLMessageUserFeedback getUserFeedback(TBLUser user);
    public abstract Date bookmarkRelevantDate();
    public abstract boolean canBeRated();
    public abstract String getBookMarkCSS();
    public abstract String getDetail();
    public abstract boolean bookmarkCanBeDeleted();
    public abstract String deleteBookMarkURL();
    public abstract String bookmarkIcon();
}
