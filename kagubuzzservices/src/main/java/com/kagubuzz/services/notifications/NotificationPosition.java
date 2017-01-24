package com.kagubuzz.services.notifications;
public enum NotificationPosition {   
    
    topRight("toast-top-right"),
    bottomRight("toast-bottom-right"),
    bottomLeft("toast-bottom-left"),
    topLeft("toast-top-right");
    
    public String toastrName;
    
    private NotificationPosition(String toastrName) {
        this.toastrName = toastrName;
    }
    
}