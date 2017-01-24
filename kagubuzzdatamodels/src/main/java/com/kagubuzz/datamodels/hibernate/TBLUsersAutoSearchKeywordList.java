package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;

import org.hibernate.annotations.GenericGenerator;

import com.kagubuzz.utilities.KaguTextFormatter;

@Entity
@Table(name = "tbl_users_autosearch_keyword_lists")
public class TBLUsersAutoSearchKeywordList extends KaguTextFormatter {

	@Id
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "owner"))
	@GeneratedValue(generator = "generator")
	Long id;

	String adKeyword1;
	String adKeyword2;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	LSTAdCategory adCategory1;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	LSTAdCategory adCategory2;

	Date lastAlertSent;
	Integer alertFrequency;
	
	String eventKeyword1;
	String eventKeyword2;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "lnk_ad_alerts_sent")
	private Set<TBLAd> autoSearchAdsAlertSent = new HashSet<TBLAd>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "lnk_event_alerts_sent")
	private Set<TBLEvent> autoSearchEventsAlertSent = new HashSet<TBLEvent>();
	   
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	LSTEventCategory eventCategory1;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	LSTEventCategory eventCategory2;
	
	//Creates a foreign key
	@OneToOne(fetch = FetchType.LAZY, optional=false)
    @PrimaryKeyJoinColumn
	private TBLUser owner;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "lnk_events_autosearch")
	private Set<TBLEvent> autoSearchEvents = new HashSet<TBLEvent>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "lnk_ads_autosearch")
	private Set<TBLAd> autoSearchAds = new HashSet<TBLAd>();

    public Set<TBLAd> getAutoSearchAdsAlertSent() { return autoSearchAdsAlertSent; }
    public void setAutoSearchAdsAlertSent(Set<TBLAd> autoSearchAdsAlertSent) { this.autoSearchAdsAlertSent = autoSearchAdsAlertSent; }
    public void addAutoSearchAdAlertSent(TBLAd ad) { this.autoSearchAdsAlertSent.add(ad); }
    
    public Set<TBLEvent> getAutoSearchEventsAlertSent() { return autoSearchEventsAlertSent; }
    public void setAutoSearchEventsAlertSent(Set<TBLEvent> autoSearchEventAlertSent) { this.autoSearchEventsAlertSent = autoSearchEventAlertSent; }
    public void addAutoSearchEventsAlertSent(TBLEvent event) { this.autoSearchEventsAlertSent.add(event); }
    
	public TBLUser getOwner() {	return owner;}
	public void setOwner(TBLUser owner) {this.owner = owner;}
	
	public Set<TBLAd> getAutoSearchAds() {	return autoSearchAds;}
	public void setAutoSearchAds(Set<TBLAd> autoSearchAds) { this.autoSearchAds = autoSearchAds; }
	public void addAutoSearchAd(TBLAd ad) {	this.autoSearchAds.add(ad); }
	public void removeAutoSearchAd(TBLAd ad) {	this.autoSearchAds.remove(ad);	}

	public Set<TBLEvent> getAutoSearchEvents() { return autoSearchEvents; }
	public void setAutoSearchEvents(Set<TBLEvent> autoSearchEvents) { this.autoSearchEvents = autoSearchEvents;	}
	public void addAutoSearchEvent(TBLEvent event) { this.autoSearchEvents.add(event); }
	public void removeAutoSearchEvent(TBLEvent event) {	this.autoSearchEvents.remove(event); }

	// Event search 
	
	public LSTEventCategory getEventCategory1() {	return eventCategory1; }
	public void setEventCategory1(LSTEventCategory eventCategory1) { this.eventCategory1 = eventCategory1; }
	
	public LSTEventCategory getEventCategory2() {	return eventCategory2; }
	public void setEventCategory2(LSTEventCategory eventCategory2) { this.eventCategory2 = eventCategory2; }
	
	public String getEventKeyword1() { return squashNull(eventKeyword1); }
	public void setEventKeyword1(String eventKeyword1) { this.eventKeyword1 = eventKeyword1; }
	
	public String getEventKeyword2() { return squashNull(eventKeyword2); }
	public void setEventKeyword2(String eventKeyword2) { this.eventKeyword2 = eventKeyword2;}
	
	// Ad search
	
	public LSTAdCategory getAdCategory1() { return adCategory1; }
	public void setAdCategory1(LSTAdCategory adCategory1) { this.adCategory1 = adCategory1; }
	
	public LSTAdCategory getAdCategory2() {return adCategory2; }
	public void setAdCategory2(LSTAdCategory adCategory2) { this.adCategory2 = adCategory2; }

	public String getAdKeyword1() {	return squashNull(adKeyword1); }
	public void setAdKeyword1(String adKeyword1) {	this.adKeyword1 = adKeyword1;}

	public String getAdKeyword2() {return squashNull(adKeyword2);}
	public void setAdKeyword2(String adKeyword2) {this.adKeyword2 = adKeyword2;}

	public List<String> getAdKeyWords() {
		List<String> adKeywords = new ArrayList<String>();

		if (adKeyword1 != null)
			adKeywords.add(adKeyword1);
		if (adKeyword2 != null)
			adKeywords.add(adKeyword2);

		return adKeywords;
	}

	public List<String> getEventKeyWords() {
		List<String> eventKeywords = new ArrayList<String>();

		if (eventKeyword1 != null)
			eventKeywords.add(eventKeyword1);
		if (eventKeyword2 != null)
			eventKeywords.add(eventKeyword2);
		
		return eventKeywords;
	}
	
	public int eventsFound() {
	    return getEventKeyWords().size();
	}
	
	public int adsFound() {
        return getAdKeyWords().size();
    }
}
