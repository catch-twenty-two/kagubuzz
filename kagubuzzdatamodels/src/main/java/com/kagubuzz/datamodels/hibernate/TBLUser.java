package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.utilities.KaguTextFormatter;

@Entity
@Table(name = "tbl_users")
public class TBLUser extends KaguTextFormatter implements Serializable, Comparable<TBLUser> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = true, unique = true)
    String email;
    @Column(nullable = false)
    String firstName;
    String lastName;
    Integer integrity = 0;
    @Column(nullable = false)
    byte[] avatarImage;
    Boolean verified = false;
    Boolean phoneVerified = false;
    Date creationDate = new Date();
    Date lastLogin = new Date();
    String phone;
    @Column(nullable = false)
    Integer timeZone;
    @Column(nullable = false)
    String zipCode;
    @Column(nullable = false)
    String weatherStationId;
    String password;
    @Enumerated(EnumType.STRING)
    DeliveryMethod contactMethod = DeliveryMethod.Email;
    String swapLocation;
    String swapLocationName;
    Boolean socialAccount = false;
    @Column(nullable = false)
    String userFileStoreDirectory = UUID.randomUUID().toString();
    @Column(length= 2500)
    String lastKnownIPAddresses;
    Date securityCodeCreationDate;
    String  securityCode;
    @Column(nullable = false)
    String springSecurityRole = "ROLE_USER";
    
    // Notification Prefs
    
    @Column(nullable = true)
    Boolean smsAdNotifications = false;
    @Column(nullable = true)
    Boolean showNotifications = true;
    @Column(nullable = true)
    Boolean emailNotifications = true;
    
    @Transient 
    boolean loggedIn = false;
    
	@OneToOne(fetch = FetchType.LAZY, optional = true, orphanRemoval = true, mappedBy="owner", cascade=CascadeType.ALL)
    private TBLUsersAutoSearchKeywordList searchableKeyWordList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="owner")
    private Set<TBLBallot> userFlags = new HashSet<TBLBallot>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="owner")
	private Set<TBLEvent> userEvents = new HashSet<TBLEvent>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="owner")
	private Set<TBLAd> userAds = new HashSet<TBLAd>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLEvent> bookmarkedEvents  = new HashSet<TBLEvent>();

	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLAd> bookmarkedAds = new HashSet<TBLAd>();
	
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLDiscussionPublic> bookmarkedDiscussions = new HashSet<TBLDiscussionPublic>();
	   
    @ManyToOne(fetch = FetchType.LAZY)
    private TBLKaguLocation tblKaguLocation;
	
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLUser> following = new HashSet<TBLUser>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<TBLUser> recommended = new HashSet<TBLUser>();
    
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="buyer")
	private List<TBLDiscussionAd> roleBuyerTransactions = new ArrayList<TBLDiscussionAd>();
	
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private List<TBLDiscussionAd> roleSellerTransactions = new ArrayList<TBLDiscussionAd>();
	
	@OneToOne(fetch = FetchType.LAZY, optional = true, orphanRemoval = true, mappedBy="owner", cascade=CascadeType.ALL)
	private TBLSocialUser socialUserAccount;
	
    public TBLKaguLocation getTblKaguLocation() { return tblKaguLocation; }
	public void setTblKaguLocation(TBLKaguLocation tblKaguLocation) { this.tblKaguLocation = tblKaguLocation; }

	public String getWeatherStationId() { return weatherStationId; }
    public void setWeatherStationId(String weatherStationId) { this.weatherStationId = weatherStationId; }
    
    public Integer getTimeZoneOffset() { return timeZone; }
    public void setTimeZoneOffset(Integer timeZone) { this.timeZone = timeZone; } 
    
    public Calendar getCalendarInUserTimeZone() {
        TimeZone timeZone =  TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.add(Calendar.MINUTE, getTimeZoneOffset());
        return  cal;     
    }
    
    public Integer getTimeZoneInMilliseconds() { return timeZone*60*1000; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public byte[] getAvatarImage() { return avatarImage; }
    public void setAvatarImage(byte[] avatarImage) { this.avatarImage = avatarImage; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { 
        if(!isSocialAccount() && email != null) {
            return email; 
        }
        
        return this.getSocialUserAccount().getEmail();
    }
    
    public void setEmail(String email) {
        if(!isSocialAccount()) {
            this.email = email; 
            return;
        }
        
        this.getSocialUserAccount().setEmail(email);
    }
    
    public int getIntegrity() { return integrity; }
    public void setIntegrity(Integer integrity) { this.integrity = integrity; }

    public int getIntegrityHistory() { return integrity; }
    public void setIntegrityHistory(Integer integrity) { this.integrity = integrity; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) {	this.verified = verified;}

    public Boolean getPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(Boolean phoneVerified) {	this.phoneVerified = phoneVerified;}
    
    public Date getCreationDate() {	return creationDate;  }
    public void setCreationDate(Date creationDate) {	this.creationDate = creationDate;    }

    public String getPhone() {	return phone;    }
    public void setPhone(String phone) {	
        
        if(!phone.startsWith("1")) {
            phone = "1" + phone;
        }
        this.phone = phone;    
    }

    public String getFirstName() {	return firstName;    }
    public void setFirstName(String firstName) {	this.firstName = firstName;    }

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {	this.lastName = lastName; }

    public Set<TBLUser> getFollowing() { return following; }
    public void setFollowing(Set<TBLUser> following) { this.following = following; }
    public void removeFollowing(TBLUser user) { this.following.remove(user); }
    public void addFollowing(TBLUser user) { this.following.add(user); }
    
    public Set<TBLUser> getRecommended() { return recommended; }
    public void setRecommended(Set<TBLUser> recommended) { this.recommended = recommended; }
    public void removeRecommended(TBLUser user) { this.recommended.remove(user); }
    public void addRecommended(TBLUser user) { this.recommended.add(user); }
    
    public Set<TBLEvent> getBookmarkedEvents() {return bookmarkedEvents;}
    public void addToBookmarkedEvents(TBLEvent event) {	this.bookmarkedEvents.add(event);}
    public void removeUserBookmarkedEvent(TBLEvent userEvent) { this.bookmarkedEvents.remove(userEvent); }
	public void addUserBookmarkedEvent(TBLEvent userEvent) { this.bookmarkedEvents.add(userEvent); }

    public Set<TBLAd> getBookmarkedAds() {return bookmarkedAds;}
    public void addToBookmarkedAds(TBLAd ad) {	this.bookmarkedAds.add(ad);}
    public void removeUserBookmarkedAd(TBLAd ad) { this.bookmarkedAds.remove(ad); }
	public void addUserBookmarkedAd(TBLAd ad) { this.bookmarkedAds.add(ad); }
	
	public Set<TBLEvent> getUserEvents() {	return userEvents;}	
	public void setUserEvents(Set<TBLEvent> userEvents) {this.userEvents = userEvents;}	
	public void addUserEvent(TBLEvent userEvent) { this.userEvents.add(userEvent); }
	public void removeUserEvent(TBLEvent userEvent) { this.userEvents.remove(userEvent); }
	
	public Set<TBLAd> getUserAds() { return userAds;}	
	public void setUserAds(Set<TBLAd> userAds) {this.userAds = userAds;}	
	public void addUserAd(TBLAd userAd) { this.userAds.add(userAd); }
	public void removeUserAd(TBLAd userAd) { this.userAds.remove(userAd); }
	
	public TBLSocialUser getSocialUserAccount() {	return socialUserAccount;	}
	public void setSocialUserAccount(TBLSocialUser socialUserAccounts) {	this.socialUserAccount = socialUserAccounts;	}
	
	public List<TBLDiscussionAd> getRoleBuyerTransactions() {return roleBuyerTransactions;}
	public void setRoleBuyerTransactions(List<TBLDiscussionAd> roleBuyerTransactions) {this.roleBuyerTransactions = roleBuyerTransactions;}
	
	public List<TBLDiscussionAd> getRoleSellerTransactions() { return roleSellerTransactions; }	
	public void setRoleSellerTransactions(List<TBLDiscussionAd> roleSellerTransactions) { this.roleSellerTransactions = roleSellerTransactions;	}
	
	public DeliveryMethod getContactMethod() {	return contactMethod;}
	public void setContactMethod(DeliveryMethod contactMethod) { this.contactMethod = contactMethod;}

    public boolean showNotifications() {  return showNotifications; }
    public void setShowNotifications(boolean showNotifictions) { this.showNotifications = showNotifictions;}
    
    public Set<TBLBallot> getUserFlags() {  return userFlags;  }
    public void setUserFlags(Set<TBLBallot> userFlags) { this.userFlags = userFlags;}
    
	// Helper functions
	
	public String getFullName()	{ return this.getFirstName() + " " + this.getLastName(); }
	
	public String getPreferredDeliveryMethodDetails() {	    
	    return DeliveryMethod.getPreferredDeliveryMethodDetails(this.getContactMethod(), this);
	}
	
    public boolean isSocialAccount() { return socialAccount; }
    public void setSocialAccount(boolean socialAccount) { this.socialAccount = socialAccount; }
    
    public String getUserFileStoreDirectory() { return userFileStoreDirectory; }
    public void setUserFileStoreDirectory(String userFileStoreDirectory) { this.userFileStoreDirectory = userFileStoreDirectory; }
	
    public String getLastKnownIPAddresses() { return lastKnownIPAddresses == null ? "[]": lastKnownIPAddresses; }    
    public void setLastKnownIPAddresses(String lastKnownIPAddresses) { this.lastKnownIPAddresses = lastKnownIPAddresses; }
    
    public TBLUsersAutoSearchKeywordList getSearchableKeyWordList() { return searchableKeyWordList; }
    public void setSearchAbleKeyWordList(TBLUsersAutoSearchKeywordList searchableKeyWordList) { this.searchableKeyWordList = searchableKeyWordList; }
    
    public void setSecurityCodeCreationDate(Date securityCodeCreationDate) {  this.securityCodeCreationDate = securityCodeCreationDate; }
    public String getSecurityCode() {return securityCode; }
    
    public void setSecurityCode(String securityCode) { this.securityCode = securityCode;}
    public boolean isShowNotifications() { return showNotifications; }
    
	// Text Formatter
	
	public String getFormattedCreatedDate() {return super.getFormattedDateLongForm(creationDate);}
	
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	
	public String getSwapLocation() { return swapLocation; }
	public void setSwapLocation(String swapLocation) { this.swapLocation = swapLocation; }
	
	public String getSwapLocationName() { return swapLocationName; }
	public void setSwapLocationName(String swapLocationName) { this.swapLocationName = swapLocationName; }

	public Date getLastLogin() { return lastLogin;}
	public void setLastLogin(Date lastLogin) {	this.lastLogin = lastLogin;	}
    public Date getSecurityCodeCreationDate() { return securityCodeCreationDate; }
    
    public boolean isLoggedIn() {return loggedIn;}
    public TBLUser setLoggedIn(boolean loggedIn) {this.loggedIn = loggedIn; return this;}
    
    public boolean isSmsAdNotifications() { return smsAdNotifications; }
    public void setSmsAdNotifications(boolean smsAdNotifications) { this.smsAdNotifications = smsAdNotifications; }
    public boolean getEmailNotifications() {
        return emailNotifications;
    }
    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
	@Override
	public int compareTo(TBLUser arg0) {
        return this.getFirstName().compareTo(arg0.getFirstName());
	}
    public String getSpringSecurityRole() {
        return springSecurityRole;
    }
    public void setSpringSecurityRole(String springSecurityRole) {
        this.springSecurityRole = springSecurityRole;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        TBLUser other = (TBLUser) obj;
        
        if (this.getId() == null) {
            return this == other;
        } else {
            return this.getId() == other.getId();
        }
    }
    
    public Set<TBLDiscussionPublic> getBookmarkedDiscussions() {
        return bookmarkedDiscussions;
    }
    
    public void addToBookmarkedDiscussions(TBLDiscussionPublic discussion) {  
        this.bookmarkedDiscussions.add(discussion);
    }
    
    public void removeUserBookmarkedDiscussion(TBLDiscussionPublic discussion) { 
        this.bookmarkedDiscussions.remove(discussion); 
        discussion.removeParticipant(this);
    }
    
    public void addUserBookmarkedDiscussion(TBLDiscussionPublic discussion) { 
        this.bookmarkedDiscussions.add(discussion);
        discussion.addParticipant(this);
    }
}
