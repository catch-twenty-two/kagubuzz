package com.kagubuzz.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;

public class KaguTime {
    
    public Date nextMonthly(DateTime startTime) {
        
        int nthDay = 0;
        
        DateTime startTimeCopy = new DateTime(startTime);
        DateTime monthScan = new DateTime(startTime).dayOfMonth().setCopy(1);
        
        while(startTimeCopy.isAfter(monthScan)) { 
            monthScan = monthScan.plusDays(1); 
            if(startTimeCopy.getDayOfWeek() == monthScan.getDayOfWeek()) nthDay++;
        }
        
        int monthsBetween = Months.monthsBetween(startTimeCopy, DateTime.now().plusMonths(1)).getMonths();
        
        startTimeCopy = startTimeCopy.plusMonths(monthsBetween);
        
        monthScan = startTimeCopy.dayOfMonth().setCopy(1);
        startTimeCopy = new DateTime(startTime);
        
        while(nthDay > 0) { 
            monthScan = monthScan.plusDays(1); 
            if(startTimeCopy.getDayOfWeek() == monthScan.getDayOfWeek()) nthDay--;
        }
            
       return monthScan.toDate();  
    }
    
    public Date nextBiMonthly(DateTime startTime) {
        
        int weeksBetween = Weeks.weeksBetween(startTime, DateTime.now().plusWeeks(2)).getWeeks();
        
        return startTime.plusWeeks(weeksBetween%2 + weeksBetween).toDate();
    }
    
    public Date nextWeekly(DateTime startTime) {
        
        int weeksBetween = Weeks.weeksBetween(startTime, DateTime.now().plusWeeks(1)).getWeeks();
        
        return startTime.plusWeeks(weeksBetween + 1).toDate();
    }
    
    
    public Date nextEveryDay(DateTime startTime) {
        
        int weeksBetween = Days.daysBetween(startTime, DateTime.now().plusDays(1)).getDays();
        
        return startTime.plusDays(weeksBetween).toDate();
    }
}

