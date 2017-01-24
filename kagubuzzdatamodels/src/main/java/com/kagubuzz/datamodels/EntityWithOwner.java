package com.kagubuzz.datamodels;

import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface EntityWithOwner {

    public abstract TBLUser getOwner();
    
}
