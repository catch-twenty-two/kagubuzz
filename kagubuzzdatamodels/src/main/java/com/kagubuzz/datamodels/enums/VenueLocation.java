package com.kagubuzz.datamodels.enums;

public enum VenueLocation implements IEnumExtendedValues{
    
	Outside("Outside", true),
	Inside("Inside");
	
    private EnumExtendedValues values;
    
    private VenueLocation(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private VenueLocation(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
