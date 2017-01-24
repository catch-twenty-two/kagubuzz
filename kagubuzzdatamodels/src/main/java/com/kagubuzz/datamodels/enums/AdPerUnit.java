package com.kagubuzz.datamodels.enums;

public enum AdPerUnit implements IEnumExtendedValues{
    
	PerHour("An Hour", true),	
	PerItem("Per Item"),
	PerJob("Per Job");
	
    private EnumExtendedValues values;
    
    private AdPerUnit(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private AdPerUnit(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
