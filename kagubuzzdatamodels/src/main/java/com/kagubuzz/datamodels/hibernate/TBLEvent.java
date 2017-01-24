package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.annotations.Store;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.PaddedIntegerBridge;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventDuration;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.utilities.BookMarks;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.utilities.KaguTime;
import com.kagubuzz.utilities.SEOUtilities;

@Entity
@Indexed
@Table(name="tbl_events")
@Spatial(spatialMode = SpatialMode.GRID)
@NamedQueries({
@NamedQuery(name="events.bookMarkedReminder", 
            query="from TBLEvent as e inner join e.userBookMarks as u " +
                    "where " +
                    "u.timeZone = :timeZone " +
                    "and e.startDate >= :midnight1 " +
                    "and e.startDate <= :midnight2 " +
                    "and e.active = true "),
@NamedQuery(name="events.findRepeatingEvents", 
            query="select event as e, event.userBookMarks as user from TBLEvent event " +
                    "where " + 
                    "event.startDate >= :midnight1 " +
                    "and event.startDate <= :midnight2 " +
                    "and event.eventCycle != 'Once' "+
                    "and event.active = true ")
})
public class TBLEvent extends KaguTextFormatter implements Serializable, Post, Ratable, EntityWithOwner, Comparable<TBLEvent> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date lastUpdated;
    @Column(nullable = true)
    private Date reminderSent = new Date();
    @Column(nullable = false)
    private Date renewDate;
    private Date lastOccurance;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false, length = 30000)
    private String body;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false, length = 255)
    private String title;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String keyWords;
    @Column(nullable = false)
    private String address;
    @Field(index = Index.YES, store = Store.YES)
    @FieldBridge(impl = PaddedIntegerBridge.class)
    private Integer price;
    private Date createdDate = new Date();
    @Enumerated(EnumType.STRING)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private EventAgeAppropriate ageAppropriate;
    @Enumerated(EnumType.STRING)
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private EventVenue venue;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventPeriod eventCycle = EventPeriod.Once;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventDuration eventDuration = EventDuration.OneHour;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private boolean active = false;
	private String sideBarImagePath;
    private String backgroundImagePath;
    @Column(nullable = false)
    byte[] eventICalender;
    @Latitude
    Double latitude;
    @Longitude
    Double longitude;    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private TBLKaguLocation tblKaguLocation;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="event")
    Set<TBLEventRSVP> eventRSVPs;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category")
    @IndexedEmbedded
    private LSTEventCategory category; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TBLUser owner;

    @OneToOne(fetch = FetchType.LAZY, optional = true, mappedBy="event")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    TBLDiscussionPublic eventDiscussion;
    
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="event")
    @OrderBy("createdDate")
    private List<TBLMessageEventComment> comments = new ArrayList<TBLMessageEventComment>();
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy="bookmarkedEvents")
    private Set<TBLUser> userBookMarks = new HashSet<TBLUser>();
    
    public String getBackgroundImageName() {return backgroundImagePath; }
    public void setBackgroundImageName(String backgroundImagePath) {this.backgroundImagePath = backgroundImagePath;}

    public String getSideBarImageName() {return sideBarImagePath;}
    public void setSideBarImageName(String sideBarImagePath) { this.sideBarImagePath = sideBarImagePath; }

    public boolean isActive() {return active;}
	public void setActive(boolean active) {	this.active = active;}

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { 	this.startDate = startDate; }

    public Date getEndDate() { 	return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public TBLUser getOwner() { return owner; }
    public void setOwner(TBLUser owner) { this.owner = owner; }

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() {return this.address;  }
    public void setAddress(String address) {this.address = address;}

    public EventAgeAppropriate getAgeAppropriate() {return this.ageAppropriate; }
    public void setAgeAppropriate(EventAgeAppropriate ageAppropriate) {this.ageAppropriate = ageAppropriate; }

    public String getBody() {return StringEscapeUtils.unescapeHtml(this.body);}
    public void setBody(String body) { 	this.body = StringEscapeUtils.escapeHtml(body); }

    public Date getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate;}
    
    public Integer getPrice() {return this.price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public List<TBLMessageEventComment> getComments() { return comments; }
    public void setComments(ArrayList<TBLMessageEventComment> comments) { this.comments = comments; }
    
    public Double getLatitude() {return latitude;}
	public void setLatitude(double latitude) {this.latitude = latitude;}
	
	public Double getLongitude() {return longitude;}	
	public void setLongitude(double longitude) {this.longitude = longitude;}
	
	public String getKeyWords() {return keyWords;}
	public void setKeyWords(String keyWords) {	this.keyWords = keyWords;}
	
	public void setCategory(LSTEventCategory category) {	this.category = category;}
	public LSTEventCategory getCategory() {	return category; }
    
    public EventVenue getVenue() {return venue;}
    public void setVenue(EventVenue venue) {this.venue = venue;}
    
    public EventPeriod getEventCycle() { return eventCycle;}
    public void setEventCycle(EventPeriod eventCycle) { this.eventCycle = eventCycle;}
    
    public EventDuration getEventDuration() { return eventDuration; }
    public void setEventDuration(EventDuration eventDuration) { this.eventDuration = eventDuration; }
    
    public byte[] getEventICalender() { return eventICalender; }
    public void setEventICalender(byte[] eventICalender) { this.eventICalender = eventICalender; }
    
    public Set<TBLUser> getUserBookMarks() { return userBookMarks; }
    public void setUserBookMarks(Set<TBLUser> userBookMarks) {  this.userBookMarks = userBookMarks;    }
    
    public TBLBallot getBallot() { return ballot; }
    public void setBallot(TBLBallot eventFlags) { this.ballot = eventFlags; }
  
    // TODO: Implement this later
    
  //  public boolean isRSVPable() { return rsvpable; }
  //  public void setRSVPable(boolean rsvpable) { this.rsvpable = rsvpable; }
    
    public Date getRenewDate() { return renewDate; }    
    public void setRenewDate(Date renewDate) { this.renewDate = renewDate; }
    
    public void setRenewDate(int daysInFuture)  {

        Calendar currentTime = owner.getCalendarInUserTimeZone();        
        
        this.setRenewDate(new Date(currentTime.getTimeInMillis() + TimeUnit.DAYS.toMillis(daysInFuture)));
    }
    
    // Formatter calls
    
    public String getBookMarkCSS() { 
        
        Calendar eventCalendar = Calendar.getInstance();
        
        eventCalendar.setTime(this.getStartDate());
        
        return new BookMarks().getBookMarkState(eventCalendar, Calendar.getInstance()).getCSS();        
    }
    
    public long daysLeftUntilRenew() {
        
        long daysLeft = 0;
        
        Calendar currentTime = owner.getCalendarInUserTimeZone();
             
        Calendar eventCalReNewDate = Calendar.getInstance();
        
        eventCalReNewDate.setTime(getRenewDate());
        
        daysLeft = TimeUnit.MILLISECONDS.toDays(eventCalReNewDate.getTimeInMillis() - currentTime.getTimeInMillis());

        return (daysLeft < 0) ? 0 : daysLeft;
    }
    
    public boolean canBeRated() { 
        
        Calendar eventCalendar = Calendar.getInstance();
        
        eventCalendar.setTime(this.getLastOccurance());
      
        
        switch(new BookMarks().getBookMarkState(eventCalendar, Calendar.getInstance())) {
        case OccuredWithin7Days:
            return true;
            
        case OccuringNow:
        case OccursOver3DaysFromNow:
        case OccursWithin3DaysFromNow:
        case Occured:
            return false;
        }
        
        return true; 
    }
    
    public boolean startsAndEndsSameDay (int timeZoneOffsetInMinutes) {
        
        Calendar eventCalEnd = Calendar.getInstance();
        eventCalEnd.add(Calendar.MINUTE, timeZoneOffsetInMinutes);
        
        eventCalEnd.setTime(getEndDate());
        
        Calendar eventCalStart = Calendar.getInstance();
        
        eventCalStart.setTime(getStartDate());
        eventCalStart.add(Calendar.MINUTE, timeZoneOffsetInMinutes);
        
        return (TimeUnit.MILLISECONDS.toDays(eventCalEnd.getTimeInMillis() - eventCalStart.getTimeInMillis()) <= 0);
    }
    
    public boolean isExpired() { 
        
        if(this.getEventCycle() != EventPeriod.Once) return false;
        
        Calendar eventEnd = Calendar.getInstance();
        Calendar currentTime =  Calendar.getInstance();
        
        eventEnd.setTime(this.endDate);
        return currentTime.after(eventEnd);
    }
    
    public Date getNextOccurance() {
        
        KaguTime kaguTime = new KaguTime();
        
        if(!startDate.before(new Date())) return startDate;
        
        DateTime dt = new DateTime(startDate, DateTimeZone.UTC);
        
        switch(this.getEventCycle()) {
        case BiMonthly: 
            return kaguTime.nextBiMonthly(dt);
        case EveryDay:
            return kaguTime.nextEveryDay(dt);
        case Monthly:
            return kaguTime.nextMonthly(dt);
        case Once:
             return startDate;
        case Weekly:
            return kaguTime.nextWeekly(dt);
        case Yearly:    
        }
        
        return startDate;
    }

    public String getDate(){ return getDate(startDate); }
    
    public String getTime(){ return getTime(startDate); }
    
    public String getFormattedDateShortForm() {	return getFormattedDateShortForm(getStartDate());}
    public String getFormattedDateLongForm() {return getFormattedDateLongForm(getStartDate());}
    
    public String getTitleShortTitle() {return getSummary(this.title, 25); }
    
	@Override
	public MessageType messageType() {return MessageType.Event;};
	
	@Override
	public String getMessage() {return getSummary(this.getBody());}
	
	@Override
	public TBLUser getSender() {return this.getOwner();}
	
	@Override
	public Boolean recipientCanReply() {return null;}   
	
    @Override
    public JSPMessageRenderer getParent() { return this;}
    
    @Override
    public boolean isSystemMessage() { return false;}
    
    @Override
    public int compareTo(TBLEvent arg0) { 
        return this.getStartDate().compareTo(arg0.getStartDate());    
    }
    
    @Override
    public String getViewingURL() { return "/event/view/" + getId() + "/" + getFriendlyURL();  }
    
    @Override
    public String getFriendlyURL() { 
        return SEOUtilities.URLFriendly(getTitle() + "-" +  getTblKaguLocation().getCity() + "-" +  getTblKaguLocation().getState()); 
    }
    
    @Override
    public Date getCreatedDateInUserTZ() {
        
        Calendar cal = owner.getCalendarInUserTimeZone();
        cal.setTime(createdDate);
        
        return cal.getTime();
    }
    
    public Date getReminderSent() {  return reminderSent;   }
    public void setReminderSent(Date reminderSent) {  this.reminderSent = reminderSent; }
    public Date getLastOccurance() {
        return lastOccurance;
    }
    public void setLastOccurance(Date lastOccurance) {
        this.lastOccurance = lastOccurance;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override    
    public boolean equals(Object arg0) {
        return this.getId().equals(((TBLEvent) arg0).getId());
    }
    public TBLKaguLocation getTblKaguLocation() {
        return  ((tblKaguLocation == null) ? getOwner().getTblKaguLocation() : tblKaguLocation);
    }
    public void setTblKaguLocation(TBLKaguLocation tblKaguLocation) {
        this.tblKaguLocation = tblKaguLocation;
    }
    public TBLDiscussionPublic getEventDiscussion() {
        return eventDiscussion;
    }
    public void setEventDiscussion(TBLDiscussionPublic eventDiscussion) {
        this.eventDiscussion = eventDiscussion;
    }
    
    @Override
    public String getIconName() {
        return "attendit.png";
    }
    
    public Set<LSTTag> getTags() { 
        Set<LSTTag> set = new HashSet<LSTTag>(); 
        LSTDiscussionPublicTag tag = new LSTDiscussionPublicTag();
        tag.setName("Volunteer");
    set.add(tag);
    tag = new LSTDiscussionPublicTag();
    tag.setName("Fundraiser");
    tag = new LSTDiscussionPublicTag();
    tag.setName("Planting");
set.add(tag);
    return set; }
    
}
