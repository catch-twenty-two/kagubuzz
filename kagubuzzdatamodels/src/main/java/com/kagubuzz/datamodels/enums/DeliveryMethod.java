package com.kagubuzz.datamodels.enums;

import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.utilities.KaguTextFormatter;

public enum DeliveryMethod implements IEnumExtendedValues {
	
	KaguBuzz("abee.png", "Kagu Buzz"),
	Email("email.png", "E-Mail"),		
	Text("sms.png", "Text Message"),	
	Voice("abee.png", "A Phone Call");

	String iconName;
	private EnumExtendedValues values;
	   
	private DeliveryMethod(String iconName, String description) { 
		this.iconName = iconName;
		values = new EnumExtendedValues(description);
	}
	
	public String getIconName() {
		return iconName;
	}

    @Override
    public EnumExtendedValues getEnumExtendedValues() {
        return values;
    }
    
    static public String getPreferredDeliveryMethodDetails(DeliveryMethod contactMethod, TBLUser user) {
        
        switch(contactMethod) {
        
            case Email: return String.format("<a href='mailto:%s'>%s</a>",user.getEmail(), user.getEmail());
            case Text: return KaguTextFormatter.squashNull(user.getPhone());
            case Voice: return user.getPhone();
            case KaguBuzz: return "the messaging center";   
        }
        
        return null;
    }
}
