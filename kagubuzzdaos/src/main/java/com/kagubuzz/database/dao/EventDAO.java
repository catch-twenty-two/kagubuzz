package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.database.dao.utilities.Pagination;
import com.kagubuzz.datamodels.BookMarkedEventWithUser;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;

public interface EventDAO {
    
	public abstract List<LSTEventCategory> getEventCategories();
	public abstract void submitEventRating(TBLMessageUserEventFeedback newRating, TBLEvent event);
	public abstract TBLMessageUserEventFeedback getUserRatingForEvent(TBLEvent event, TBLUser user);
    public abstract List<TBLEvent> getExpiringEvents();    
    public abstract void removeExpiredEventsFromListings();
    public abstract List<TBLEvent> getRepeatingEventsThatAreOver();
    public abstract List<TBLEvent> getAutoSearchEvents(TBLUser owner, int recordsToReturn, int nextBrowseId);
    public abstract List<BookMarkedEventWithUser> getBookmarkedEventsHappeningTomorrow(int timeZone);
    public abstract Pagination<TBLEvent> browseEvents(LSTEventCategory category, long currentPage, boolean active, int radius, TBLKaguLocation kaguLocation);
    
	long getEventCount();
    public abstract Pagination<TBLEvent> getAutoSearchEvents(TBLUser owner, long currentPage);
    List<TBLEvent> getSuggestedEventsForUser(TBLUser user, EventVenue[] locations);
}
