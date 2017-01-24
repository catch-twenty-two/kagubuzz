package com.kagubuzz.services;

import java.util.Date;
import java.util.List;

import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventDuration;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.enums.FlagTypes;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface EventService {

	public abstract TBLEvent createOrUpdateEvent(TBLEvent eventId,
	        Boolean active,
			String title,
			String startDateTime,
			String description,
			//optional parameters									    
			String sideBarImage, String backgroundImage,
			String price,
            EventAgeAppropriate ages, 
            EventVenue takesPlace, 
            EventPeriod repeatsEvery,
            EventDuration eventDuration,
			String address, String keyWords,
			TBLUser owner,
			LSTEventCategory category);
	   
	public abstract TBLEvent getGenericEvent(TBLUser owner);

    public abstract void deleteEvent(TBLEvent event);

    public abstract String getBackgroundImageURL(TBLEvent event);

    public abstract String getSidebarImageURL(TBLEvent event);

    public abstract TBLDiscussionPublic attachDiscussionToEvent(TBLEvent event, TBLUser user);

    void rateEvent(Long id, Float rating, TBLUser user, String review);

    void sendExpiringEventsNotices();

    void sendBookmarkedEventsNotices();

    void resetRepeatingEvents();

    public abstract String getSdf(Date date);

    void removeEvent(long id);

    void duplicateEvent(TBLEvent event);

    void renewEvent(TBLEvent event);

    List<String> getEventListAsStringList(List<TBLEvent> eventList);

}