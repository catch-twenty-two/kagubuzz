package com.kagubuzz.weather;

import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.ProductType;
import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.UnitType;
import gov.weather.graphical.xml.DWMLgen.schema.DWML_xsd.WeatherParametersType;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXML;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLLocator;
import gov.weather.graphical.xml.DWMLgen.wsdl.ndfdXML_wsdl.NdfdXMLPortType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.rpc.ServiceException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.kagubuzz.datamodels.Weather;
import com.kagubuzz.datamodels.enums.WeatherCondition;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.services.cartography.Cartography;
import com.kagubuzz.utilities.JavaFileUtilities;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

public class WeatherReport implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static Logger log = Logger.getLogger(WeatherReport.class);
    
	static Document stationsToZipCodeMapping;
	Weather currentConditions;
	List<Weather> currentForecast = null;
	int forecastDays;
	
	TBLKaguLocation kaguLocation;
	String weatherStation;
	TimeZone timeZone;
	String city;
	String state;
	
	public WeatherReport(TBLKaguLocation kaguLocation, 
	                     int timeZoneOffsetInMinutes, 
	                     String weatherStation, 
	                     int forecastDays)	{
	    
		String timeZone[] = TimeZone.getAvailableIDs(timeZoneOffsetInMinutes * 60 * 1000);

		this.timeZone = TimeZone.getTimeZone(timeZone[0]);
		this.kaguLocation = kaguLocation;
		this.weatherStation = weatherStation;
		this.forecastDays = forecastDays;
		
		currentConditions = currentConditions();
		currentForecast = forecast();
	}
	
	public long getAge () {
	    return Calendar.getInstance().getTimeInMillis() - this.getCurrentConditions().getReportDate().getTimeInMillis();
	}
	
	// gets current weather based on a weather station
	
	private Weather currentConditions() {
	    
		double shortestDistance = Double.MAX_VALUE;
		double distance = 0;
		double finalLongitude = 0;
		double finalLatitude = 0;
		Node finalStation = null;;
		
		Weather currentWeatherReport = new Weather();
		
		currentWeatherReport.reportDate = Calendar.getInstance();
		currentWeatherReport.stationName = this.weatherStation;
		
		Document allStationInfo = getStationToZipCodeMapping();
		Element rootElement = allStationInfo.getRootElement();
		Elements stations = rootElement.getChildElements("station");
		HttpResponse weatherInfoHTTPResponse= null;
		
		try	{
		    
			while (stations.size() > 0) {
			    
				shortestDistance = Double.MAX_VALUE;
				Element userStationNode = null;
				
				// find station closest to the zip given by the user
				
				if(weatherStation == null)	{
				    
					for (int i = 0; i < stations.size() ; i++) {
					    
						userStationNode = stations.get(i);
	
						double currentLatitude = Double.valueOf(userStationNode.getFirstChildElement("latitude").getValue());
						double currentLongitude = Double.valueOf(userStationNode.getFirstChildElement("longitude").getValue());
						
						distance = Cartography.getLocationDistanceWeight(kaguLocation.getLatitude(), 
																		 kaguLocation.getLongitude(), 
																		 currentLatitude,
																		 currentLongitude);
						if (shortestDistance >= distance) {
						    
							shortestDistance = distance;
							finalLatitude = currentLatitude;
							finalLongitude = currentLongitude;
							finalStation = userStationNode;
							weatherStation = userStationNode.getFirstChildElement("station_id").getValue();
						}
					}
				}

				// Make HTTPs call to NOAH current weather server

				// Build http request

				HttpClient httpclient = new DefaultHttpClient();
				URIBuilder builder = new URIBuilder();
				builder.setScheme("http").setHost("w1.weather.gov").setPath("/xml/current_obs/" + weatherStation + ".xml");
				URI uri = builder.build();
				HttpGet httpget = new HttpGet(uri);

				// Send HTTPs request
				try {				   	
				    weatherInfoHTTPResponse = httpclient.execute(httpget);
				}
				catch(UnknownHostException e) {
				     return null;
				}
				
				if (weatherInfoHTTPResponse.toString().contains("404 Not Found")) {
				    
					rootElement.removeChild(finalStation);
					stations = rootElement.getChildElements("station");

					log.info("Station Error with (not found)" + weatherStation);
					
					weatherStation = null;
					
					continue;
				}
				
				// Parse XML response
				InputStream responseStream = weatherInfoHTTPResponse.getEntity().getContent();
				
				Document weatherInfoXOM = new Builder().build(responseStream);
				Element weatherElement = weatherInfoXOM.getRootElement().getFirstChildElement("weather");
				
				if(weatherElement == null)	{
				    
					rootElement.removeChild(finalStation);
					stations = rootElement.getChildElements("station");
					
					log.info("Station Error with (No INFO)" + weatherStation);
					
					weatherStation = null;
					
					continue;
				}
				
                //log.info(weatherInfoXOM.toXML());
                
				log.info("Using (" + 
				                    kaguLocation.getLatitude() + 
				                    " , " +
				                    kaguLocation.getLongitude() +
				                    ") " +
				                    weatherStation + 
				                    " (" +  
				                    weatherInfoXOM.getRootElement().getFirstChildElement("location").getValue() + 
				                    ")");
				
				String location = weatherInfoXOM.getRootElement().getFirstChildElement("location").getValue();
				
				String parsed[] = location.split(",|/");
				
				state = parsed[parsed.length - 1];
				city = parsed[0];
				
				currentWeatherReport.weatherDescription = weatherElement.getValue();
				currentWeatherReport.currentTemp = Float.valueOf(weatherInfoXOM.getRootElement().getFirstChildElement("temp_f").getValue());
				
				break;
			}

		} catch (RemoteException e)	{
			log.error("While trying to get current coditions");
			return null;
		} catch (ValidityException e) {
		    log.error("While trying to get current coditions");
		    return null;
		} catch (ParsingException e) {
		    log.error("While trying to get current coditions");
		    return null;
		} catch (IOException e)	{
		    log.error("While trying to get current coditions");
		    return null;
		} catch (URISyntaxException e)	{
		    log.error("While trying to get current coditions");
		    return null;
		}

		this.setWeatherCondition(currentWeatherReport);

		return currentWeatherReport;
	}

	// gets weather based on zip code
	
	private List<Weather> forecast() {

	    List<Weather> weatherForecast = new ArrayList<Weather>(forecastDays);

		Calendar endDate = Calendar.getInstance(timeZone);
		endDate.add(Calendar.HOUR, forecastDays * 32);

		Calendar startDate = Calendar.getInstance(timeZone);
		startDate.set(startDate.get(Calendar.YEAR), 
		              startDate.get(Calendar.MONTH), 
		              startDate.get(Calendar.DATE), 
		              0, 
		              0);
		
		//log.info(startDate.getTime().toString());
		//log.info(endDate.getTime().toString());
		
		// Call NOAH service for forecast
		log.info("Latitude = " + kaguLocation.getLatitude() + "Longitude = " + kaguLocation.getLongitude());
		try	{
		    
			NdfdXML lctr = new NdfdXMLLocator();
			NdfdXMLPortType port = lctr.getndfdXMLPort();

			WeatherParametersType wp = new WeatherParametersType();

			wp.setMaxt(true);
			wp.setMint(true);
			wp.setWx(true);

			String weatherInfoHTTPResponse = port.NDFDgen(BigDecimal.valueOf(kaguLocation.getLatitude()),
														  BigDecimal.valueOf(kaguLocation.getLongitude()), 
														  ProductType.timeSeries, startDate, endDate, UnitType.e, wp);

			Document weatherInfoXOM = new Builder().build(weatherInfoHTTPResponse, null);
			
			//log.info(weatherInfoXOM.toXML());
			 
			Element parameters = weatherInfoXOM.getRootElement()
								.getFirstChildElement("data")
								.getFirstChildElement("parameters");

			Elements parameterChildren = parameters.getChildElements();

			for (int i = 0; i < forecastDays; i++) {

				Weather weather = new Weather();

				weather.reportDate = (Calendar) startDate.clone();
				weather.reportDate.add(Calendar.HOUR, 24 * i);
				weather.stationName = this.weatherStation;
				
				for (int child = 0; child < parameterChildren.size(); child++)	{
					
					Element parameterChild = parameterChildren.get(child);
					
					// parse temperature elements

					if (parameterChild.getLocalName().equalsIgnoreCase("temperature"))	{
					    
					    Calendar tempurateCheck = ((Calendar) startDate.clone());
					    tempurateCheck.add(Calendar.HOUR, 24 * i);
					    
						Element temperatureNode = getChildFromTimeLayout(parameterChild, startDate, tempurateCheck);
						
						if (parameterChild.getAttributeValue("type").equalsIgnoreCase("maximum")) {
							weather.highTemp = Integer.valueOf(temperatureNode.getValue());
						}

						if (parameterChild.getAttributeValue("type").equalsIgnoreCase("minimum")) {
							weather.lowTemp = Integer.valueOf(temperatureNode.getValue());
						}
					}

					// parse weather elements
					
					if (parameterChild.getLocalName().equalsIgnoreCase("weather"))	{
					    
					    Calendar tempurateCheck = ((Calendar) startDate.clone());
                        tempurateCheck.add(Calendar.HOUR, 20 * i);
                        
						Element weatherDescriptionNode = getChildFromTimeLayout(parameterChild,
						                                                        startDate, 
						                                                        tempurateCheck);
						
						weatherDescriptionNode = weatherDescriptionNode.getFirstChildElement("value");
						
 						if(weatherDescriptionNode != null) {						        						    				
					        weather.setCoverageDescription(weatherDescriptionNode.getAttributeValue("coverage"));
						    weather.setWeatherDescription(weatherDescriptionNode.getAttributeValue("weather-type"));
    					}
    						    
					}						
				}
				
				this.setWeatherCondition(weather);
				
				weatherForecast.add(weather);
			}

		} catch (ServiceException e) {
		    log.error("While trying to get forecast", e);
		} catch (RemoteException e) {
            log.error("While trying to get forecast", e);
		} catch (ValidityException e) {
	        log.error("While trying to get forecast", e);
		} catch (ParsingException e) {
	        log.error("While trying to get forecast", e);
		} catch (IOException e) {
	        log.error("While trying to get forecast", e);
		}

		return weatherForecast;
	}


	Element getChildFromTimeLayout(Element parentElement, Calendar dataPointsDateStart, Calendar dataPointsTimeOfInterest) {
	    
		int periodInHours = 0;
		int numberOfDataPoints = 0;
		int i;
		Calendar startDate = (Calendar) dataPointsDateStart.clone();
		
		String timeLayout = parentElement.getAttribute("time-layout").getValue();
		
		String sp[] = timeLayout.split("[A-Za-z-]");
		
		periodInHours = Integer.valueOf(sp[3]);
		numberOfDataPoints = Integer.valueOf(sp[6]);

		Elements childElements = parentElement.getChildElements();			
		
        for (i = 0; i < numberOfDataPoints; i++) {
            
            if (childElements.get(i).getLocalName().equalsIgnoreCase("value") || 
               (childElements.get(i).getFirstChildElement("value") != null)) {
                
                startDate.add(Calendar.HOUR, periodInHours);
               
            }
            if(startDate.compareTo(dataPointsTimeOfInterest) > 0) return childElements.get(i);            
        }
        
        return childElements.get(i);
	}

    synchronized private Document getStationToZipCodeMapping() {
        
        InputStream noahStationZips = new JavaFileUtilities().getResourceAsStream("noaa_station_zips.xml");

        if (stationsToZipCodeMapping == null) {
            try {
                stationsToZipCodeMapping = new Builder().build(noahStationZips);
            }
            catch (ValidityException e) {
                log.error("While trying to get station zipcode mapping", e);
            }
            catch (ParsingException e) {
                log.error("While trying to get station zipcode mapping", e);
            }
            catch (IOException e) {
                log.error("While trying to get station zipcode mapping", e);
            }
        }

        return stationsToZipCodeMapping;
    }

    private void setWeatherCondition(Weather weather) {

        String upperCaseDesc = weather.getWeatherDescription().toUpperCase();
        Calendar currentTime = Calendar.getInstance();

        Location location = new Location(kaguLocation.getLatitude(), kaguLocation.getLongitude());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timeZone.getID());
        
        Calendar officialSunset = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());
        Calendar officialSunrise = calculator.getOfficialSunriseCalendarForDate(Calendar.getInstance());

        for (WeatherCondition weatherCondition : WeatherCondition.values()) {
            
            weather.weatherCondition = weatherCondition;
            
            if (upperCaseDesc.contains(weatherCondition.toString())) {
                
                if ((officialSunrise.compareTo(currentTime) <= 0) && 
                    (officialSunset.compareTo(currentTime) >= 0)) {
                    
                    weather.setDescriptionIconName(weatherCondition.fileNameDay());

                    return;
                }

                weather.setDescriptionIconName(weatherCondition.fileNameNight());

                return;
            }
        }

        weather.descriptionIconName = "na.png";
    }

    public String getWeatherStation() {
        return weatherStation;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Weather getCurrentConditions() {
        return currentConditions;
    }

    public List<Weather> getForecast() {
        return currentForecast;
    }
}
