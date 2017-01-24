package com.kagubuzz.datamodels.enums;

public class EnumExtendedValues {
    String description;
    boolean defaultChoice = false;
    
    public EnumExtendedValues(String description)   {
        this.description = description;
    }
    
    public EnumExtendedValues(String description, boolean defaultChoice)
    {
        this.description = description;
        this.defaultChoice = defaultChoice;
    }
    
    public String getDescription() {  return description;  }
    public void setDescription(String description) {  this.description = description;  }
    
    public boolean isDefaultChoice() { return defaultChoice; }
    public void setDefaultChoice(boolean defaultChoice) { this.defaultChoice = defaultChoice; }
    
    public String isChecked() { return (defaultChoice) ? "checked=\"checked\"" : ""; }
    public String isActive()  { return (defaultChoice) ? "active" : ""; }
}

