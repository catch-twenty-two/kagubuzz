package com.kagubuzz.datamodels.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_kagu_locations")
public class TBLKaguLocation implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String zip;
    private String state;
    private double latitude;
    private double longitude;
    private String city;
    private String fullState;

    public String getZip() {
	return this.zip;
    }

    public void setZip(String zip) {
	this.zip = zip;
    }

    public String getState() {
	return this.state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public double getLatitude() {
	return this.latitude;
    }

    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    public double getLongitude() {
	return this.longitude;
    }

    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }

    public String getCity() {
	return this.city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getFullState() {
	return this.fullState;
    }

    public void setFullState(String fullState) {
	this.fullState = fullState;
    }

    public String getCityState()   {
    	return this.city + ", " + this.state;
    }
}
