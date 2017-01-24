package com.kagubuzz.datamodels.enums;

public enum FlagTypes implements IEnumExtendedValues {
    
    Miscategorized("This seems like the wrong category for this post.", true),
	Spam("It's spam."),
	Offensive("I find it offensive."),
	Other("Other");		
    
    private EnumExtendedValues values;
    
    private FlagTypes(String description, boolean defaultChoice) {
        values = new EnumExtendedValues(description,defaultChoice);
    }
    
    private FlagTypes(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
