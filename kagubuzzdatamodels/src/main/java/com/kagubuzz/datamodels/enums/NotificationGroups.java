package com.kagubuzz.datamodels.enums;

public enum NotificationGroups implements IEnumExtendedValues {
    
    Everything("Everything", "icon-th-list", true),
    Posts("Posts", "icon-pencil"),
    Exchanges("Exchanges", "icon-tags"),
    Discussions("Discussions", "icon-comment"),
    KaguBuzz("Kagu Buzz","abee.png");

    private EnumExtendedValues values;
    String icon;
    
    private NotificationGroups(String description, String icon) {
        values = new EnumExtendedValues(description);
        this.icon = icon;
    }

    private NotificationGroups(String description, String icon, boolean defaultChoice) {
        values = new EnumExtendedValues(description, defaultChoice);
        this.icon = icon;
    }
    
    @Override
    public EnumExtendedValues getEnumExtendedValues() {  return values;  }

    public String getIcon() {
        return icon;
    }
}
