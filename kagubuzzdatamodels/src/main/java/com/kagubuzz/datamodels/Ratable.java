package com.kagubuzz.datamodels;

import com.kagubuzz.datamodels.hibernate.TBLBallot;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface Ratable {
    
    public abstract TBLBallot getBallot();
    public abstract void setBallot(TBLBallot ballot);
    public abstract void setActive(boolean active);
    public TBLUser getOwner();
    public String getTitle();
}
