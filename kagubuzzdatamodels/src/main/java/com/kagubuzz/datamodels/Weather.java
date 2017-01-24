package com.kagubuzz.datamodels;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.kagubuzz.datamodels.enums.WeatherCondition;
import com.kagubuzz.utilities.KaguTextFormatter;

public class Weather extends KaguTextFormatter implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public Calendar reportDate;
    public String weatherDescription;
    public String coverageDescription;
    public float currentTemp;
    public float highTemp;
    public float lowTemp;
    public String descriptionIconName;
    public String stationName;
    public WeatherCondition weatherCondition;

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    public Calendar getReportDate() {
        return reportDate;
    }

    public void setReportDate(Calendar reportDate) {
        this.reportDate = reportDate;
    }

    public String getWeatherDescription() {
        return squashNull(weatherDescription);
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = capitalizeFirstLetter(weatherDescription);
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    }

    public float getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(float highTemp) {
        this.highTemp = highTemp;
    }

    public float getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(float lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getDescriptionIconName() {
        return descriptionIconName;
    }

    public void setDescriptionIconName(String descriptionIconName) {
        this.descriptionIconName = descriptionIconName;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEEE");
        return sdf.format(this.reportDate.getTime());
    }

    public String getCoverageDescription() {
        
        if (coverageDescription != null) {
            if (coverageDescription.contains("chance")) {
                return "A " + capitalizeFirstLetter(coverageDescription) + " Of";
            }
        }
        
        return "&nbsp;";
    }

    public void setCoverageDescription(String coverageDescription) {
        this.coverageDescription = coverageDescription;
    }
}
