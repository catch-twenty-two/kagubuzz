package com.kagubuzz.datamodels.enums;

import com.kagubuzz.datamodels.IntegrityValues;

public enum ExchangeRatingTypes implements IEnumExtendedValues {
    
    Good("Positive", false, IntegrityValues.ExchangeUpVote,"icon-thumbs-up","text-success"),   
    Neutral("Neutral", true, IntegrityValues.ExchangeNoOpinion,"icon-ok-circle", "muted"),  
	Bad("Negative", false, IntegrityValues.ExchangeDownVote,"icon-thumbs-down", "text-error");		
    
    private EnumExtendedValues values;
    int integrityPoints;
    String icon;
    String color;
    
    private ExchangeRatingTypes(String description, boolean defaultChoice, int integrityPoints, String icon, String color) {
        values = new EnumExtendedValues(description,defaultChoice);
        this.integrityPoints = integrityPoints;
        this.icon = icon;
        this.color = color;
    }
    
    private ExchangeRatingTypes(String description) {    
        values = new EnumExtendedValues(description);
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() { return values; }

    public int getIntegrityPoints() {
        return integrityPoints;
    }

    public void setRating(int rating) {
        this.integrityPoints = rating;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }   
}
