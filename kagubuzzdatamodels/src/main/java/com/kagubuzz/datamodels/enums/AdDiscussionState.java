package com.kagubuzz.datamodels.enums;

public enum AdDiscussionState implements IEnumExtendedValues {
    
	WaitingForResponse("Waiting For A Reponse"),
	Declined("Declined"),
	Accepted("Accepted"),
	ThinkingAboutIt("Thinking On It"),
	Canceled("Canceled"),
	Complete("Complete");

    private EnumExtendedValues values;
    
    private AdDiscussionState(String description, boolean defaultChoice) {
        values = new EnumExtendedValues(description,defaultChoice);
    }
    
    private AdDiscussionState(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
}
