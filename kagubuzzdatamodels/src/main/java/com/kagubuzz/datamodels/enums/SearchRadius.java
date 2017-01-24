package com.kagubuzz.datamodels.enums;

public enum SearchRadius implements IEnumExtendedValues{
    
	R1("1"),
	R2("5"),
	R3("10"),			
	R4("30",true),
	R5("60");
	
    private EnumExtendedValues values;
    
    private SearchRadius(String description, boolean defaultChoice) {    
        values = new EnumExtendedValues(description, defaultChoice);
    }

    private SearchRadius(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
