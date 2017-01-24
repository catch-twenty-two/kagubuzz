package com.kagubuzz.utilities;

import java.util.Calendar;

public class BookMarks {

    public enum BookMarkState {
        
        OccuringNow("text-error button hidden-content"),
        Occured("muted disabled"),
        OccuredWithin7Days("black"),
        OccursOver3DaysFromNow("muted"),
        OccursWithin3DaysFromNow("black");
        
        String css;
        
        private BookMarkState(String css) {          
            this.css = css;
        }

        public String getCSS() { return css; }
        public void setCSS(String css) { this.css = css; }
    }
    
    public BookMarkState getBookMarkState(Calendar endDate, Calendar timeZoneCalendar) {
        
        Calendar endDatePlus7Days = (Calendar) endDate.clone();
        
        endDatePlus7Days.add(Calendar.DAY_OF_MONTH, 7);
        
        Calendar endDateMinus3Days = (Calendar) endDate.clone();
        
        endDateMinus3Days.add(Calendar.DAY_OF_MONTH, -3);
            
        // Is it expired?

        if(timeZoneCalendar.after(endDatePlus7Days)) {
            return BookMarkState.Occured;
        }
        
        // Happening today!
        
        if((timeZoneCalendar.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) &&
           (timeZoneCalendar.get(Calendar.MONTH) == endDate.get(Calendar.MONTH)) &&
           (timeZoneCalendar.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH))) {
            return BookMarkState.OccuringNow;
        }
        
       // Can you vote on it ?
        
        if(timeZoneCalendar.after(endDate)) {
            return BookMarkState.OccuredWithin7Days;
        }
        
        // Happens over 3 days from now
        
        if(timeZoneCalendar.before(endDateMinus3Days)) {
            return BookMarkState.OccursOver3DaysFromNow;
        }
        
        // Happens within 3 days from now
        
        return BookMarkState.OccursWithin3DaysFromNow;
    }
}
