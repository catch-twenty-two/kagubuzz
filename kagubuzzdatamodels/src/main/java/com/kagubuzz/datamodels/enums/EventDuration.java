package com.kagubuzz.datamodels.enums;

import java.util.concurrent.TimeUnit;

public enum EventDuration implements IEnumExtendedValues{
    
    OneHalfHour("1/2 hour", TimeUnit.MINUTES.toMinutes(30)),
	OneHour("1 hour", true, TimeUnit.HOURS.toMinutes(1)),
	TwoHours("2 hours", TimeUnit.HOURS.toMinutes(2)),	
	ThreeHours("3 hours", TimeUnit.HOURS.toMinutes(3)),
	FourHours("4 hours", TimeUnit.HOURS.toMinutes(4)),   
	FiveHours("5 hours", TimeUnit.HOURS.toMinutes(5)),   
	SixHours("6 hours", TimeUnit.HOURS.toMinutes(6)),
	SevenHours("7 hours", TimeUnit.HOURS.toMinutes(7)),
	EightHours("8 hours", TimeUnit.HOURS.toMinutes(8)),
	NineHours("9 hours", TimeUnit.HOURS.toMinutes(9)),
	TenHours("10 hours", TimeUnit.HOURS.toMinutes(10)),
	ElevenHours("11 hours", TimeUnit.HOURS.toMinutes(11)),
	OneHalfDay("12 hours", TimeUnit.HOURS.toMinutes(12)),
	OneDay("the rest of the day", TimeUnit.HOURS.toMinutes(12)),
	TwoDays("2 days",  TimeUnit.DAYS.toMinutes(2)),
	ThreeDays("3 days",  TimeUnit.DAYS.toMinutes(3)),         
	FourDays("4 days",  TimeUnit.DAYS.toMinutes(4)),
	FiveDays("5 days",  TimeUnit.DAYS.toMinutes(5)),
	SixDays("6 days",  TimeUnit.DAYS.toMinutes(6)),
	OneWeek("1 week",  TimeUnit.DAYS.toMinutes(7)),
	TwoWeeks("2 weeks", TimeUnit.DAYS.toMinutes(14));
	
    private EnumExtendedValues values;
    private long minutes;
    
    public long getMinutes() { return minutes; }
    
    private EventDuration(String description, boolean defaultChoice, long minutes) { 

        this.minutes = minutes;
        
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private EventDuration(String description, long minutes) {
        this.minutes = minutes;
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
