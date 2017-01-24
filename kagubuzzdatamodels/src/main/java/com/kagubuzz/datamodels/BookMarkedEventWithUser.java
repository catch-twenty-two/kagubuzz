package com.kagubuzz.datamodels;

import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public class BookMarkedEventWithUser{

    TBLUser  tbl_users;
    TBLEvent tbl_events;
    public TBLUser getUser() {
        return tbl_users;
    }
    public TBLEvent getEvent() {
        return tbl_events;
    }  
}