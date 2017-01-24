package com.kagubuzz.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.EventDAO;
import com.kagubuzz.datamodels.BookMarkedEventWithUser;
import com.kagubuzz.datamodels.enums.EventAgeAppropriate;
import com.kagubuzz.datamodels.enums.EventDuration;
import com.kagubuzz.datamodels.enums.EventPeriod;
import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.hibernate.LSTDiscussionCategory;
import com.kagubuzz.datamodels.hibernate.LSTEventCategory;
import com.kagubuzz.datamodels.hibernate.TBLDiscussionPublic;
import com.kagubuzz.datamodels.hibernate.TBLEvent;
import com.kagubuzz.datamodels.hibernate.TBLMessageUserEventFeedback;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.filesystem.FileService;
import com.kagubuzz.filesystem.UserSubfolder;
import com.kagubuzz.messagetemplates.discussions.PublicDiscussionMessage;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.utilities.KaguImage;

import de.svenjacobs.loremipsum.LoremIpsum;

@Service
public class EventServiceImpl implements EventService {

    @Value("${kagubuzz.event_post_expire}")
    private int eventRenewWindow;
    
    static Logger log = Logger.getLogger(EventServiceImpl.class);

    static int currentBookmarkReminderTimeZone = 0;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    CRUDDAO crudDAO;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    FileService fileService;
    
    @Autowired
    EventDAO eventDAO;
    
    @Autowired
    PostsService postsService;
    
    LoremIpsum loremIpsum = new LoremIpsum();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyyh:mm a");
    
    
    @Override
    @Transactional(readOnly = false)
    public void sendExpiringEventsNotices() {
        
        List<TBLEvent> expiringEvents = eventDAO.getExpiringEvents();
        
        for(TBLEvent event: expiringEvents) {
            
            SystemMessage systemEmailMessage = new  SystemMessage(event.getOwner(), userAccountService.getSystemUser());
            
            systemEmailMessage.postExpiring(event);
            messageDispatcher.queueMessage(systemEmailMessage);
            
            event.setReminderSent(new Date());
            
            crudDAO.update(event);
        }
        
        log.info("Done checking expiring events");
    }

    @Override
    @Transactional(readOnly = false)
    public void renewEvent(TBLEvent event) {
        event.setRenewDate(eventRenewWindow);
        event.setActive(true);
        
        crudDAO.update(event);
    }
    
    @Override
    public List<String> getEventListAsStringList(List<TBLEvent> eventList) {
        
        List<String> stringList = new ArrayList<String>();
        
        for(TBLEvent event : eventList) {
            stringList.add(String.format("<a href=\"%s\">%s</a>", 
                    event.getViewingURL(),
                    event.getTitle()));
        }
        
        return stringList;
    }
    
    @Override
    public void resetRepeatingEvents() {
        
        List<TBLEvent> events = eventDAO.getRepeatingEventsThatAreOver();
        
        for(TBLEvent event: events) {
            Date nextOccurance = event.getNextOccurance();
            event.setLastOccurance(event.getStartDate());
            event.setStartDate(nextOccurance);
            event.setEndDate(new Date(event.getStartDate().getTime() + TimeUnit.MINUTES.toMillis(event.getEventDuration().getMinutes())));
            
            crudDAO.update(event);
        }
        
        log.info("Done resetting repeating events");
    }
    
    // Called every hour
    
    @Override
    public void sendBookmarkedEventsNotices() {
        
        currentBookmarkReminderTimeZone = -1*java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        
        if(currentBookmarkReminderTimeZone < -12) {
            currentBookmarkReminderTimeZone = -1*(currentBookmarkReminderTimeZone + 12);
        }
        
        List<BookMarkedEventWithUser> bookmarkedEventWithUserList = eventDAO.getBookmarkedEventsHappeningTomorrow(currentBookmarkReminderTimeZone);

        for (BookMarkedEventWithUser bookmarkEventWithUser : bookmarkedEventWithUserList) {

            SystemMessage systemEmailMessage = new SystemMessage(bookmarkEventWithUser.getUser(), userAccountService.getSystemUser());
            
            systemEmailMessage.bookmarkReminder(bookmarkEventWithUser.getEvent());
            messageDispatcher.queueMessage(systemEmailMessage);
            
        }

        log.info("Done checking event bookmark notices for time zone " + currentBookmarkReminderTimeZone);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TBLEvent  createOrUpdateEvent(TBLEvent event,
                                    Boolean active,
                                    String title,
                                    String startDateTime, 
                                    String description,
                                    // optional parameters
                                    String sideBarImage, 
                                    String backgroundImage, 
                                    String price, 
                                    EventAgeAppropriate ages, 
                                    EventVenue takesPlace, 
                                    EventPeriod repeatsEvery,
                                    EventDuration duration,
                                    String address, 
                                    String keyWords, 
                                    TBLUser owner, 
                                    LSTEventCategory category) {

        Date eventStartDate;
        Date eventEndDate;
        
        if(event == null) {
            event = new TBLEvent();
        }
        else {
            event.setLastUpdated(new Date());
        }
            
 
        try {
            eventStartDate = sdf.parse(startDateTime);
;
            
            eventEndDate = sdf.parse(startDateTime);
        }
        catch(ParseException pe) {
            throw new IllegalArgumentException();
        }

        // change the start time to GMT
        
        java.util.Calendar eventDateCalStart = java.util.Calendar.getInstance();
        eventDateCalStart.setTime(eventStartDate);
        eventDateCalStart.add(java.util.Calendar.MINUTE, owner.getTimeZoneOffset() * -1);
        
        java.util.Calendar endCal = java.util.Calendar.getInstance();

        endCal.setTime(eventEndDate);
        endCal.add(java.util.Calendar.MINUTE, owner.getTimeZoneOffset() * -1);
        
        endCal.add(java.util.Calendar.MINUTE, (int) duration.getMinutes());
        
        eventEndDate = endCal.getTime();
        
        // Default 1 hour
       
        Dur vEventuration = new Dur(0,0,(int) duration.getMinutes(),0);
        VEvent vEvent = new VEvent(new net.fortuna.ical4j.model.Date(eventStartDate), vEventuration, title);

        UidGenerator ug = null;
        
        try {
            ug = new UidGenerator("1");
        }
        catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        vEvent.getProperties().add(ug.generateUid());
        
        Calendar icsCalendar = new Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Kagu Buzz Event//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);

        // Add the event and print
        icsCalendar.getComponents().add(vEvent);
        System.out.println(icsCalendar);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        
        try {
            outputter.output(icsCalendar, baos);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        event.setOwner(owner);
        
        if(event.getBallot() == null){
            event.setTitle(title);
        }
        
        event.setBody(description);
        event.setKeyWords(keyWords);
        
        if(price != null) {
            event.setPrice(Integer.parseInt(price));
        }
        else {
            event.setPrice(0);
        }
        
        if(active != null) {
            event.setActive(active);
        }
        
        event.setLastOccurance(eventStartDate);
        event.setAgeAppropriate(ages);
        event.setVenue(takesPlace);
        event.setAddress(address);
        event.setEndDate(eventEndDate);
        event.setStartDate(new Date(eventDateCalStart.getTimeInMillis()));
        event.setReminderSent(new Date());
        event.setCategory(category);
        event.setEventCycle(repeatsEvery);
        event.setEventDuration(duration);
        event.setEventICalender(baos.toByteArray());
        event.setActive(false);
        
        // resize and save the background image up the transparency first

        if (backgroundImage != null) {

            // Get the image uploaded by the user

            KaguImage kaguImage = postsService.createBackgroundImage(backgroundImage);
            
            String fileName = UUID.randomUUID().toString() + ".jpg";
            
            fileService.write(kaguImage.getOutputStream(), fileName, fileService.getUserPublicDirectoryPath(owner, UserSubfolder.Images));
            
            event.setBackgroundImageName(fileName);

            fileService.delete(backgroundImage, fileService.getTempFilePath());
        }

        // resize and save the side bar image

        if (sideBarImage != null) {

            KaguImage kaguImage = postsService.createSideBarImage(sideBarImage);

            String fileName = UUID.randomUUID().toString() + ".jpg";

            fileService.write(kaguImage.getOutputStream(), fileName, fileService.getUserPublicDirectoryPath(owner, UserSubfolder.Images));

            event.setSideBarImageName(fileName);

            fileService.delete(fileName, fileService.getTempFilePath());
        }

        URI uri;
        
        try {
            uri = new URI("www.kagubuzz.com");
      
            vEvent.getProperties().add(new net.fortuna.ical4j.model.property.Url(uri));
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(active = true) {
            renewEvent(event);
        }
        
        return event;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteEvent(TBLEvent event) {
        
        Set<TBLUser> users = event.getUserBookMarks();
        
        for(TBLUser user : users) {
            user.removeUserBookmarkedEvent(event);
            crudDAO.update(user);
        }
        
        fileService.delete(event.getSideBarImageName(), userAccountService.getUserFileDirectoryPath(event.getOwner()));
        fileService.delete(event.getBackgroundImageName(), userAccountService.getUserFileDirectoryPath(event.getOwner()));
        
        crudDAO.delete(event);
    }
    
    @Override
    public String getSidebarImageURL(TBLEvent event) {    
        if(event.getSideBarImageName() == null ) return null;
        return fileService.getUserPublicDirectoryURL(event.getOwner(), UserSubfolder.Images) + event.getSideBarImageName();
    }
    
    @Override
    public String getBackgroundImageURL(TBLEvent event) { 
        if(event.getBackgroundImageName() == null ) return null;
        
        return fileService.getUserPublicDirectoryURL(event.getOwner(),UserSubfolder.Images) + event.getBackgroundImageName();
    }
    
    @Override
    public TBLEvent getGenericEvent(TBLUser owner) {
        TBLEvent event = createOrUpdateEvent(null, 
                                             true,
                                             loremIpsum.getWords(3),                                        
                                             sdf.format(new Date()),
                                             loremIpsum.getParagraphs(),
                                             null,
                                             null,
                                             "0",
                                             EventAgeAppropriate.Adults,
                                             EventVenue.Indoors,
                                             EventPeriod.Weekly,
                                             EventDuration.OneHour,
                                             "4567 Elderberry Ln",
                                             loremIpsum.getWords(5),
                                             owner,
                                             (LSTEventCategory) categoryDAO.getCategoryByName("Theater", LSTEventCategory.class));
        return event;
    }
    
    @Override
    @Transactional(readOnly=false)
    public void removeEvent(long id) {
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        event.setActive(false);
        crudDAO.update(event);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TBLDiscussionPublic attachDiscussionToEvent(TBLEvent event, TBLUser user)  {
        
        String httpRef = "You can view the original event here <a href='" + event.getViewingURL() + "'>" + 
                         event.getTitle() + 
                         "</a><br><br>" + 
                         event.getSummary(event.getBody());

        TBLDiscussionPublic publicDiscussion = new TBLDiscussionPublic();
        
        publicDiscussion.setCategory((LSTDiscussionCategory) categoryDAO.getCategoryByName("Kagu Buzz Events", 
                                      LSTDiscussionCategory.class)); 
        
        new PublicDiscussionMessage(publicDiscussion, 
                                    user, 
                                    userAccountService.getSystemUser(), 
                                    userAccountService.getSystemUser(),
                                    httpRef, 
                                    "Kagu Buzz Event Posting -'" + event.getTitle() + "'");
        
        publicDiscussion.setActive(true);
        publicDiscussion.setCreatedDate(event.getCreatedDate());
        publicDiscussion.setEvent(event);
        publicDiscussion.addParticipant(event.getOwner());
        
        crudDAO.update(publicDiscussion);
        
        SystemMessage systemMessage = new SystemMessage(event.getOwner(), userAccountService.getSystemUser());
        
        systemMessage.pulicDiscussionForEvent(publicDiscussion);
        
        messageDispatcher.sendMessage(systemMessage);
        
        return publicDiscussion;
    }
    
    @Override
    public void duplicateEvent(TBLEvent event) {
        
        TBLEvent dupEvent = new TBLEvent();
        
        String destinationFileNameSideBar = UUID.randomUUID().toString() + ".jpg";
        String destinationFileNameBackGround = UUID.randomUUID().toString() + ".jpg";
        
        if(event.getBackgroundImageName() != null) {

            fileService.duplicate(event.getBackgroundImageName(), 
                                  destinationFileNameBackGround, 
                                  fileService.getUserPublicDirectoryPath(event.getOwner(), UserSubfolder.Images));
            
            dupEvent.setBackgroundImageName(destinationFileNameBackGround);
        }
        
        if(event.getSideBarImageName() != null) {
            
            fileService.duplicate(event.getSideBarImageName(), 
                                  destinationFileNameSideBar, 
                                  fileService.getUserPublicDirectoryPath(event.getOwner(), UserSubfolder.Images));
            
            dupEvent.setSideBarImageName(destinationFileNameSideBar);
        }
        
        dupEvent.setLatitude(event.getLatitude());
        dupEvent.setLongitude(event.getLongitude());
        
        createOrUpdateEvent(dupEvent, 
                false,
                event.getTitle() + " (Copy)", 
                getSdf(event.getStartDate()), 
                event.getBody(), 
                null,
                null, 
                event.getPrice().toString(), 
                event.getAgeAppropriate(), 
                event.getVenue(), 
                event.getEventCycle(), 
                event.getEventDuration(),
                event.getAddress(), 
                event.getKeyWords(), 
                event.getOwner(), 
                event.getCategory());
    }
      
    
    @Override
    @Transactional(readOnly = false)
    public void rateEvent(Long id, Float rating, TBLUser user, String review) {
        
        TBLEvent event = crudDAO.getById(TBLEvent.class, id);
        
        if(review == null) review = TBLMessageUserEventFeedback.EMPTY_REVIEW_CODE;
        
        TBLMessageUserEventFeedback newRating = new TBLMessageUserEventFeedback();       
        
        newRating.setSubject(event.getTitle());
        newRating.setCreatedDate(new Date());
        newRating.setMessage(review);
        newRating.setIsPrivate(true);
        newRating.setEvent(event);
        newRating.setSender(user);
        newRating.setRecipient(event.getOwner());
        newRating.setBallot(postsService.createPostBallotIfNeeded(event));
        newRating.setRating(rating);
        
        eventDAO.submitEventRating(newRating, event);
    }

    @Override
    public String getSdf(Date date) { return sdf.format(date); }
}
