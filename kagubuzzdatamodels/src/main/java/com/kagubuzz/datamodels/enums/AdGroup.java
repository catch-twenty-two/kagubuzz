package com.kagubuzz.datamodels.enums;

public enum AdGroup implements IEnumExtendedValues {
    

    //Housing("Housing and Real Estate"),
     CommunityServices("Community Services", true, "community_services"),
     ForSale("For Sale", false, "for_sale");//,
	//Jobs("Jobs");		
    
    private EnumExtendedValues values;
    
    private String underScoreName;
    
    private AdGroup(String description, boolean defaultChoice, String underScoreName) {
        values = new EnumExtendedValues(description,defaultChoice);
        this.underScoreName = underScoreName;
    }
    
    private AdGroup(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }
    
    public String getUnderScoreName() {
        return underScoreName;
    }
}
