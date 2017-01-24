package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.spatial.Coordinates;

import com.kagubuzz.datamodels.Bookmark;
import com.kagubuzz.datamodels.EntityWithOwner;
import com.kagubuzz.datamodels.Ratable;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.Post;
import com.kagubuzz.datamodels.enums.AdState;
import com.kagubuzz.datamodels.enums.AdGroup;
import com.kagubuzz.datamodels.enums.AdType;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.MessageType;
import com.kagubuzz.datamodels.enums.AdPerUnit;
import com.kagubuzz.utilities.KaguTextFormatter;
import com.kagubuzz.utilities.SEOUtilities;


@Entity
@Indexed
@Table(name="tbl_ads")
@Spatial(name = "location", spatialMode = SpatialMode.GRID)
public class TBLAd extends KaguTextFormatter implements Serializable, EntityWithOwner, Post, Ratable, KaguSearchable, Coordinates, Comparable<TBLAd> {

    private static final long serialVersionUID = 1L;
    
    @Version
    long optimisticLocking;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false, length = 30000)
    private String body;
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false)
    private String title;
    private String zipCode;
	private String address;
    private Integer price;
    private int buzzRating;
    private int flags;
    private Boolean active;
    private String imagePath1;
	private String imagePath2;
    private String imagePath3;
    private String imagePath4;
    @Enumerated(EnumType.STRING)
    private AdState adState = AdState.NoActivity;
    private String smsKeyword;
    @Enumerated(EnumType.STRING)
    private DeliveryMethod contactMethod;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdGroup adGroup;
    @Enumerated(EnumType.STRING)
    private AdType adType; 
    private boolean Firm; 
    private Date lastUpdated;
    @Column(nullable = true)
    private Date reminderSent = new Date();
    @Column(nullable = false)
    private Date renewDate;
    private Date postedDate;
    private Date createdDate;
    private boolean acceptsTimebanking = false; 
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    AdPerUnit perUnit;
    
    @Latitude
    Double latitude;
    @Longitude
    Double longitude;
    
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    TBLBallot ballot;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TBLUser owner;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy="bookmarkedAds", cascade = CascadeType.ALL)
    private Set<TBLUser> userBookMarks = new HashSet<TBLUser>();
    
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate")
    TBLDiscussionAdQA questionsAndAnswers;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
    @OrderBy("createdDate ASC")
    private List<TBLDiscussionAd> inquiries = new ArrayList<TBLDiscussionAd>();
    
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    private TBLKaguLocation tblKaguLocation;
    
	@ManyToOne(fetch = FetchType.LAZY)
	private LSTAdCategory category;
	 
    public TBLKaguLocation getTblKaguLocation() { return  ((tblKaguLocation == null) ? getOwner().getTblKaguLocation() : tblKaguLocation);}
	public void setTblKaguLocation(TBLKaguLocation tblKaguLocation) {this.tblKaguLocation = tblKaguLocation;}
	
	public Boolean isActive() { return active;  }
    public void setActive(boolean active) { this.active = active; }

    public int getBuzzRating() { return buzzRating; }
    public void setBuzzRating(int buzzRating) { this.buzzRating = buzzRating; }

    public int getFlags() { return flags; }
    public void setFlags(int flags) {this.flags = flags; }

    public TBLUser getOwner() { return owner; }
    public void setOwner(TBLUser owner) { this.owner = owner; }

    public Long getId() { return this.id; }
    public void setId(long id) { this.id = id; }

    public String getAddress() {return this.address;  }
    public void setAddress(String address) {this.address = address;}

    public String getBody() {return this.body;}
    public void setBody(String body) { this.body = body;}

    public Date getCreatedDate() { return this.createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate;}

    public Integer getPrice() {return this.price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getZipCode() {return zipCode;}
	public void setZipCode(String zipCode) {this.zipCode = zipCode;}
	
	public String getImagePath1() {	return imagePath1;}
	public void setImagePath1(String imagePath1) {this.imagePath1 = imagePath1;	}
	
	public String getImagePath2() {	return imagePath2;}
	public void setImagePath2(String imagePath2) {this.imagePath2 = imagePath2;}
	
	public String getImagePath3() {return imagePath3;}
	public void setImagePath3(String imagePath3) {this.imagePath3 = imagePath3;}
	
	public String getImagePath4() {return imagePath4;}
	public void setImagePath4(String imagePath4) {this.imagePath4 = imagePath4;}
	
    public List<TBLDiscussionAd> getOffers() { return inquiries; }
	public void setOffers(List<TBLDiscussionAd> offers) { this.inquiries = offers; }
	
	public AdState getAdState() { return adState; }	
	public void setAdState(AdState adState) {	this.adState = adState; }

	public LSTAdCategory getCategory() {	return category; }
	public void setCategory(LSTAdCategory category) { this.category = category; }
	
	public DeliveryMethod getContactMethod() {	return contactMethod; }
	public void setContactMethod(DeliveryMethod contactMethod) { this.contactMethod = contactMethod; }
	
	public Set<TBLUser> getUserBookMarks() { return userBookMarks; }
	public void setUserBookMarks(Set<TBLUser> userBookMarks) {  this.userBookMarks = userBookMarks;    }
    
	public boolean isExpired() { return (renewDate.getTime() < Calendar.getInstance().getTimeInMillis()); }
	
	// Formatted text
	
	public String getFormattedDateShortForm(){	return getFormattedDateShortForm(createdDate); }
	
	public String getFormattedDateLongForm() {return getFormattedDateLongForm(createdDate);}
	
	public String getBodySummary()	{return getSummary(this.getBody());}
	
	public String getTitleSummary()	{return getSummary(this.getTitle(),20);}
	
	@Override
	public String getMessage() {return this.getBodySummary();}
	
	@Override
	public TBLUser getSender() {return this.getOwner();}
	
	@Override
	public Boolean recipientCanReply() {return null;}
	
	@Override
	public MessageType messageType() {return MessageType.Ad;};
	
    @Override
    public JSPMessageRenderer getParent() { return this;}
    
	public String getSMSKeyword() {	return smsKeyword;}
	
	public void setSMSKeyword(String sMSKeyword) {smsKeyword = sMSKeyword;}
	
	public String getSMSKeywordShort() { return smsKeyword.substring(0,7); }
	
	public long daysLeftUntilRenew() {
	        
	        long daysLeft = 0;
	        
	        Calendar currentTime = owner.getCalendarInUserTimeZone();
	        Calendar eventCalReNewDate = Calendar.getInstance();
	        
	        eventCalReNewDate.setTime(getRenewDate());
	        
	        daysLeft = TimeUnit.MILLISECONDS.toDays(eventCalReNewDate.getTimeInMillis() - currentTime.getTimeInMillis());

	        return (daysLeft < 0) ? 0 : daysLeft;
	}
	
    public void setRenewDate(int daysInFuture) {

        Calendar currentTime = owner.getCalendarInUserTimeZone();

        this.setRenewDate(new Date(currentTime.getTimeInMillis() + TimeUnit.DAYS.toMillis(daysInFuture)));
    }
	
	public List<TBLDiscussionAd> getInquiries() { return inquiries;	}
	public void setInquiries(List<TBLDiscussionAd> inquiries) {	this.inquiries = inquiries;	}
	
	@Override
	public Double getLatitude() {
		return tblKaguLocation.getLatitude();
	}
	@Override
	public Double getLongitude() {
		return tblKaguLocation.getLongitude();
	}
	
    @Override
    public int compareTo(TBLAd arg0) {
        return arg0.getPostedDate().compareTo(this.getPostedDate());        
    }
    
    @Override
    public Date getCreatedDateInUserTZ() {
        // TODO Auto-generated method stub
        return null;
    }
    public String getSmsKeyword() {
        return smsKeyword;
    }
    public void setSmsKeyword(String smsKeyword) {
        this.smsKeyword = smsKeyword;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Date getReminderSent() {
        return reminderSent;
    }
    public void setReminderSent(Date reminderSent) {
        this.reminderSent = reminderSent;
    }
    public Date getRenewDate() {
        return renewDate;
    }
    public void setRenewDate(Date renewDate) {
        this.renewDate = renewDate;
    }
    public TBLBallot getAdBallot() {
        return ballot;
    }
    public void setAdBallot(TBLBallot adBallot) {
        this.ballot = adBallot;
    }
    public TBLDiscussionAdQA getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }
    public void getQuestionsAndAnswers(TBLDiscussionAdQA questionsAndAnswers) {
        this.questionsAndAnswers = questionsAndAnswers;
    }
    @Override
    public boolean isSystemMessage() {
        return false;
    }
    
    public Date getPostedDate() {
        return postedDate;
    }
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }
    
    @Override
    public String getFriendlyURL() { 
      return SEOUtilities.URLFriendly(getTitle() + "-" +  getTblKaguLocation().getCity() + "-" +  getTblKaguLocation().getState());
    }
    
    @Override
    public String getViewingURL() {
        return "/ad/view/" + getId() + "/" + getFriendlyURL(); 
    }
    
	@Override
	public Date getStartDate() {
		return postedDate;
	}
	@Override
	public Date getEndDate() {
		return renewDate;
	}
    public AdGroup getAdGroup() {
        return adGroup;
    }
    public void setAdGroup(AdGroup adType) {
        this.adGroup = adType;
    }
    public AdType getAdType() {
        return adType;
    }
    public void setAdType(AdType adType) {
        this.adType = adType;
    }
    public boolean isFirm() {
        return Firm;
    }
    public void setFirm(boolean firm) {
        Firm = firm;
    }
    @Override
    public TBLBallot getBallot() {
        return this.ballot;
    }
    @Override
    public void setBallot(TBLBallot ballot) {
       this.ballot = ballot;
    }
    public AdPerUnit getPerUnit() {
        return perUnit;
    }
    public void setPerUnit(AdPerUnit perUnit) {
        this.perUnit = perUnit;
    }
    
    public boolean isPerHour() {
        return (this.perUnit.equals(AdPerUnit.PerHour));
    }
    public boolean acceptsTimebanking() {
        return acceptsTimebanking;
    }
    public void setAcceptsTimebanking(boolean acceptTimebanking) {
        this.acceptsTimebanking = acceptTimebanking;
    }
    @Override
    public String getIconName() {
        return "exchange.png";
    }
    
    public Set<LSTTag> getTags() { 
        Set<LSTTag> set = new HashSet<LSTTag>(); 
        LSTDiscussionPublicTag tag = new LSTDiscussionPublicTag();
        tag.setName("Organic");
    set.add(tag);
    tag = new LSTDiscussionPublicTag();
    tag.setName("Orchards");
    tag = new LSTDiscussionPublicTag();
    tag.setName("Apples");
set.add(tag);
    return set; }
}