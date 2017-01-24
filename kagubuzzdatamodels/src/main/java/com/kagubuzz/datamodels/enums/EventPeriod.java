package com.kagubuzz.datamodels.enums;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public enum EventPeriod implements IEnumExtendedValues{
    
	Once("Once", "Once", true),
	EveryDay("every day at TIME","Repeats Every Day", false),  // at TIME
	//EveryWeekDay("every weekday at TIME", "Repeats Every Weekday", false), // at TIME
	Weekly("every DAY at TIME", "Repeats Every Week", false), // On DAY at TIME
	BiMonthly("every other week on DAYs at TIME", "Repeats Every Other Week", false), // On DAY at TIME
	Monthly("every XXX DAY of the month at TIME", "Repeats Every Month", false), // On the X DAY of the month at TIME
	Yearly("once a year", "Repeats Once Per Year", false);
	
    private EnumExtendedValues values;
    private String formattedDescription;
    
    private EventPeriod(String formattedDescription, String description, boolean defaultChoice) {    
        
        values = new EnumExtendedValues(description, defaultChoice);
        
        this.formattedDescription = formattedDescription;
    }
    
    public String getFormattedRepeatString(Date startDate) {
   
        Calendar cal = Calendar.getInstance();
        int nthDayofTheMonth = 0;
        int day = 1;
        cal.setTime(startDate);
        Calendar calCompare = (Calendar) cal.clone();       
        
        for(; calCompare.before(cal) || calCompare.equals(cal); day++) {
            calCompare.set(Calendar.DAY_OF_MONTH, day);
            if(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).equals(calCompare.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,Locale.US))) {
                nthDayofTheMonth++;
            }
        }
        
        String description = this.formattedDescription;
    
        description = description.replace("TIME", MessageFormat.format("{0,time, short}", startDate));
        description = description.replace("DAY", cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,Locale.US));
        description = description.replace("XXX", numberToName(nthDayofTheMonth));
        
        return description;
    }
    
    private String numberToName(int number) {
        
        switch(number) {
        case 1:
            return "first";
        case 2:
            return "second";
        case 3:
            return "third";
        case 4:
            return "forth";
        }
        
        return "last";
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
