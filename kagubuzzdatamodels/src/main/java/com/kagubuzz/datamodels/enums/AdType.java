package com.kagubuzz.datamodels.enums;

public enum AdType implements IEnumExtendedValues {
    
    Request("Wanted"),
	Offered("Offered", true);
	
    private EnumExtendedValues values;
    
    private AdType(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private AdType(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
