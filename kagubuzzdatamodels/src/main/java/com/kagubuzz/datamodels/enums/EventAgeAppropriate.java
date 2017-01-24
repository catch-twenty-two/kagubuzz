package com.kagubuzz.datamodels.enums;

public enum EventAgeAppropriate implements IEnumExtendedValues {
    
	Adults("Adults"),
	Kids("Kids"),
	Seniors("Seniors"),
	Teens("Teens");
    
    private EnumExtendedValues values;
    
    private EventAgeAppropriate(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private EventAgeAppropriate(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
    
}
