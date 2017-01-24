package com.kagubuzz.datamodels.enums;

public enum EventVenue implements IEnumExtendedValues {
    
    Indoors("Indoors", true),
	Outdoors("Outdoors");
	
    private EnumExtendedValues values;
    
    private EventVenue(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private EventVenue(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
