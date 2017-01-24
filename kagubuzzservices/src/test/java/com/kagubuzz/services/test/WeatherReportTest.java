package com.kagubuzz.services.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kagubuzz.datamodels.Weather;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.services.cartography.Cartography;
import com.kagubuzz.weather.WeatherReport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dao-beans.xml",
                                    "classpath:service-beans.xml", 
                                    "classpath:hibernate-test-config.xml", 
                                    "classpath:datasource-test-config.xml" })

public class WeatherReportTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    Cartography cartography;
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception   {

    }
    
    @Test
    public void testWeatherService() {
        
		/*TBLKaguLocation kaguLocation = cartography.getLongLatFromUSZipCode("94703");
		
		WeatherReport weatherReport = new WeatherReport(kaguLocation, -480, null, 10);
		
		Weather weather = weatherReport.getCurrentConditions();
		
		System.out.println("Current temp = " + weather.currentTemp);
		
		List<Weather> weatherForecast = weatherReport.getForecast();
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm aaa");
		
		  
		for(Weather w: weatherForecast)
		{
			System.out.println("Weather for " + sdf.format(w.reportDate.getTime()) + ". " + 
			                   " Hightemp: " + w.highTemp + 
			                   " Lowtemp: " + w.lowTemp + 
			                   " Description: " + w.weatherDescription);
		}*/
    }
}
