package com.kagubuzz.message.types;

import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLMessageDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface SystemMessageTemplateFields {
      
    abstract void welcome();
    abstract void appointment();
    abstract void passwordReset();
    abstract void passwordChanged();    
    abstract void beingFollowed();    
    abstract void verifyAccount();
    abstract void verifyPhone();    
    abstract void eventRating();    
    abstract void followingPost(Post post);    
    abstract void postExpiring(Post post);   
    abstract void autoSearchAlert(Post post);  
    abstract void bookmarkReminder(Post post);    
    abstract void postRenewed(Post post);    
    abstract void discussionPostNewMessage(TBLMessageDiscussionPublic post);    
    abstract void welcomePhoneVerify();    
    abstract void invalidText();
    abstract void pulicDiscussionForEvent(TBLDiscussionPublic post);
    abstract void recommended(TBLUser user);
}
