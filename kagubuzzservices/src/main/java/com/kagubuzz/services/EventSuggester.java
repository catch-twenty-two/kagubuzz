package com.kagubuzz.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.hsqldb.rights.User;

import com.kagubuzz.datamodels.enums.EventVenue;
import com.kagubuzz.datamodels.enums.VenueLocation;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.weather.WeatherReport;

public class EventSuggester {
    
    final String COLD = "chilly"; 
    final String HOT = "hot"; 
    final String WARM = "warm"; 
    
    String climateOutside;
    String timeOfDay;
    
    TBLUser user;
    
    WeatherReport weatherReport;
    VenueLocation venueLocation;
    
    float outDoorsPercent;
    float inDoorsPercent;
    
    int indoorsMeter;
    int outdoorsMeter;
    
    int timeZoneOffset;
    
    public EventSuggester(WeatherReport weatherReport, TBLUser user) {
        this.weatherReport = weatherReport;
        this.timeZoneOffset = user.getTimeZoneOffset();
        this.user = user;
        
        if(weatherReport.getCurrentConditions() == null) {
            return;
        }
        
        evaluateTempurature();
        evaluateWeather();
        evaluateTimeOfDay();
        evaluateTimeOfYear();
        // 
        float total = indoorsMeter + outdoorsMeter;
        
        outDoorsPercent = outdoorsMeter/total;
        inDoorsPercent = indoorsMeter/total;
    }
    
    void evaluateTimeOfYear() {
        Calendar time = user.getCalendarInUserTimeZone();
        Calendar timeCompare = user.getCalendarInUserTimeZone();
        
        timeCompare.set(time.get(Calendar.YEAR), 
                3, 
                28, 
                9, 
                0);

        if(time.before(timeCompare)) {
            indoorsMeter++;
            return;        
        }
        
        timeCompare.set(time.get(Calendar.YEAR), 
                6, 
                28, 
                9, 
                0);

        if(time.before(timeCompare)) {
            outdoorsMeter++;
            return;        
        }
        
        timeCompare.set(time.get(Calendar.YEAR), 
                9, 
                28, 
                9, 
                0);

        if(time.before(timeCompare)) {
            outdoorsMeter++;
            return;        
        }
        
        timeCompare.set(time.get(Calendar.YEAR), 
                12, 
                31, 
                9, 
                0);

        if(time.before(timeCompare)) {
            indoorsMeter++;
            return;        
        }
        
        indoorsMeter++;
    }
    
    void evaluateTimeOfDay() {
             
        Calendar currentTime = user.getCalendarInUserTimeZone();
        Calendar timeCompare = user.getCalendarInUserTimeZone();
        
        timeOfDay = "during the day";
        
        timeCompare.set(currentTime.get(Calendar.YEAR), 
                        currentTime.get(Calendar.MONTH), 
                        currentTime.get(Calendar.DATE), 
                        9, 
                        0);
        
        if(currentTime.before(timeCompare)) {
            outdoorsMeter++;
            return;
        }

        
        timeCompare.set(currentTime.get(Calendar.YEAR), 
                currentTime.get(Calendar.MONTH), 
                currentTime.get(Calendar.DATE), 
                15, 
                0);
        
        if(currentTime.before(timeCompare)) {
            indoorsMeter++;
            outdoorsMeter++;
            return;
        }
        
        timeOfDay = "during the evening";
        
        timeCompare.set(currentTime.get(Calendar.YEAR), 
                        currentTime.get(Calendar.MONTH), 
                        currentTime.get(Calendar.DATE), 
                        16, 
                        0);
        
        if(currentTime.before(timeCompare)) {
            indoorsMeter++;
            return;
        }
        
        timeCompare.set(currentTime.get(Calendar.YEAR), 
                currentTime.get(Calendar.MONTH), 
                currentTime.get(Calendar.DATE), 
                19, 
                0);

        if(currentTime.before(timeCompare)) {
            indoorsMeter++;
            return;
        }
        
        indoorsMeter++;
    }
    
    void evaluateTempurature()  {
        
        // Super Hot outside temp above 100
        
        climateOutside = HOT;
        
        if(weatherReport.getCurrentConditions().currentTemp > 100) {
            indoorsMeter++;
            return;
        }
        
        // Hot outside temp between 90 and 100
        
        if(weatherReport.getCurrentConditions().currentTemp > 85) {
            indoorsMeter++;
            outdoorsMeter++;
            return;
        }
        
        // Nice outside temp between 70 and 85
        climateOutside = WARM;
        
        if(weatherReport.getCurrentConditions().currentTemp > 70 ) {
            outdoorsMeter++;

            return;
        }
        
        climateOutside = "a little chilly";
        
        
        if(weatherReport.getCurrentConditions().currentTemp >= 55 ) {
            indoorsMeter++;
            outdoorsMeter++;
            return;
        }
        
        // Nice outside temp between 55 and 70
        
        climateOutside = "chilly";
        
        if(weatherReport.getCurrentConditions().currentTemp > 50 ) {
            outdoorsMeter++;
            indoorsMeter++;
            return;
        }
        
        // Cold outside
        
        climateOutside = "cold";
        
        if(weatherReport.getCurrentConditions().currentTemp > 32 ) {
            indoorsMeter++;
            return;
        }
        
        // Cold outside
        
        climateOutside = "freezing";
        
        indoorsMeter++;
        
        // Okay outside
        
    }
    
    void evaluateWeather()  {
        
        switch(weatherReport.getCurrentConditions().weatherCondition)
        {
        // outdoors okay
        case CLEAR:
        case FEW:
        case FAIR:
            outdoorsMeter += 2;
            return;
        case CLOUDY:
            outdoorsMeter++;
            return;
        case OVERCAST:
            outdoorsMeter++;
            indoorsMeter++;
            return;
        case FOG:
            return;
            // Dangerous Conditions
        case THUNDERSTORM:
        case DUST:
        case HAZE:
        case FUNNEL:
            indoorsMeter++;
            return;
            
        case WINDY:
        case RAIN:
        case FREEZING:
        case ICE:
        case SNOW:
        case SHOWERS:
            indoorsMeter++;
            return;
        
        case PARTLY:
            indoorsMeter++;
            return;
        }
    }

    public EventVenue[] getEvaluatedLocation() {
        
        List<EventVenue> eventVenue = new ArrayList<EventVenue>();
        
        if(outDoorsPercent >= .60 ) {
            eventVenue.add(EventVenue.Outdoors);
        }
        
        if(inDoorsPercent >= .60 ) {
            eventVenue.add(EventVenue.Indoors);
        }
        
        if(eventVenue.size() == 0) {
            eventVenue.add(EventVenue.Outdoors);
            eventVenue.add(EventVenue.Indoors);
        }

        return (EventVenue[]) eventVenue.toArray(new EventVenue[eventVenue.size()]);
    }
    
    public String getClimateOutsideMessage() {
       String message = "It's <strong>" + climateOutside + " out </strong> " +  weatherReport.getCurrentConditions().weatherCondition.getReadableDescription();
       
       if(outDoorsPercent >= .60 ) {
           return message += ", try to get outside! Here's some stuff to do this week.";
       }
       
       if(inDoorsPercent >= .60 ) {
           return message += ". Here's some stuff to do indoors around <strong>" +  user.getTblKaguLocation().getCity() + "</strong> this week.";
       }    
       
       return message += ". Here's some stuff to do around <strong>" +  user.getTblKaguLocation().getCity() + "</strong> this week.";

    }

    public TBLUser getUser() {
        return user;
    }

    public void setUser(TBLUser user) {
        this.user = user;
    }
}
