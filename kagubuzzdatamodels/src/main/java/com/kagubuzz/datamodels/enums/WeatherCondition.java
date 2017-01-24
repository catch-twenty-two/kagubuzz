package com.kagubuzz.datamodels.enums;

public enum WeatherCondition  {
	
	CLOUDY("overcast_128.png", "Night_Clear_Few_Clouds.png", "and it's cloudy"), // Mostly Cloudy 
	PARTLY("partly_sunny_128.png", "Night_Clear_Few_Clouds.png", "and it's partly cloudy"), // Partl
	OVERCAST("overcast_128.png", "overcast_128.png", "and it's overcast"), // Overcast 
	DRIZZLE("Rain_Occasional.png", "Rain_Occasional.png", "and it's drizzling"), // Overcast 
	RAIN("rain_128.png", "night_rain_128.png", "and there's a chance of rain"),
	FREEZING("sleet_128.png", "Night_Hail.png", "it's freezing"), // Freezing Rain
	FOG("fog_128.png", "fog_128.png", "but, it should clear up"), // Fog
	ICE("ice_128.png", "ice_128.png", "and there might be ice"), 
	SHOWERS("raining_128.png", "night_rain_128.png", "and there's a chance of rain"), // Rain
	THUNDERSTORM("raining_128.png", "Night_Thunderstorm.png","and there's a chance of thunderstorms"), 
	SNOW("Snow_Heavy.png", "Night_Snow.png", "and there's a chance of snow"), 
	WINDY("wind_flag_storm_128.png", "wind_flag_storm_128.png", "and it's windy"), 
	FUNNEL("Funnel_Cloud.png", "Funnel_Cloud.png", "and it's dangerous"), 
	DUST("Dust.png", "Dust.png", "and it's windy"), 
	HAZE("haze.png", "haze.png", "and it's hazy"),
	FEW("Sunny_Few_Clouds.png", "Night_Clear_Few_Clouds.png", "with a few clouds in the sky"), // A Few Clouds
    FAIR("sunny_128.png", "moon_phase_full_128.png", "without a cloud in the sky"), // Fair
    CLEAR("sunny_128.png", "moon_phase_full_128.png", "with clear skies"); // Clear
	

	private final String fileNameDay;
	private final String fileNameNight;
	private final String readableDescription;
	
	WeatherCondition(String fileNameDay, String fileNameNight, String readableDescription)	{
		this.fileNameDay = fileNameDay;
		this.fileNameNight = fileNameNight;
		this.readableDescription = readableDescription;
	}
	
	public String fileNameDay()	{
		return fileNameDay;
	}

	public String fileNameNight() {
		return fileNameNight;
	}

    public String getReadableDescription() {
        return readableDescription;
    }
}
