package com.kagubuzz.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.KaguLocationDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.exceptions.EmailInUseExecption;
import com.kagubuzz.exceptions.PhoneNumberInUseExecption;
import com.kagubuzz.exceptions.PhoneVerifyExecption;
import com.kagubuzz.exceptions.UnknownZipCodeExecption;
import com.kagubuzz.services.cartography.Cartography;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.JavaFileUtilities;
import com.kagubuzz.utilities.KaguImage;
import com.kagubuzz.weather.WeatherReport;
import org.apache.log4j.*;
import org.apache.lucene.search.CachingWrapperFilter.DeletesMode;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Value("${path.userfiles}")
	private String userfilesRoot;
	
	@Value("${kagubuzz.iphistory}")
    private int ipHistoryLimit;
     
    @Value("${nexmo.sms_phone_number}")
    private String smsPhoneNumber;
    
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	KaguLocationDAO kaguLocationDAO;
	
	@Autowired
	CRUDDAO crudDAO;
	
	@Autowired
	Cartography cartography;
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	PostsService postsService;
	   
	Logger log = Logger.getLogger(UserAccountServiceImpl.class);
	
	 TBLUser systemUser;
	
	@Override
    public String getUserFileDirectoryPath(TBLUser user) {
		return String.format("%s%s%s%s%s", File.separatorChar, userfilesRoot,File.separatorChar,user.getUserFileStoreDirectory(),File.separatorChar); 
	}
	
    @Override
    @PostConstruct
    @Transactional
    public void init() {
        TBLUser systemUser = userDAO.getUserByEmail("info@kagubuzz.com");

        if (systemUser != null)
            return;
        
        systemUser = new TBLUser();
        JavaFileUtilities jfu = new JavaFileUtilities();
        KaguImage avatar = new KaguImage(jfu.getResourceAsStream("system_avatar.jpg"));

        systemUser.setAvatarImage(avatar.getBytes());
        systemUser.setFirstName("Kagu Buzz");
        systemUser.setEmail("info@kagubuzz.com");
        systemUser.setPassword(null);
        systemUser.setPhone(smsPhoneNumber);
        systemUser.setWeatherStationId("none");
        systemUser.setTimeZoneOffset(-420);
        systemUser.setZipCode("94702");
        systemUser.setSocialAccount(false);
        TBLKaguLocation kbLo = new TBLKaguLocation();
        kbLo.setCity("Berkeley");
        kbLo.setFullState("California");
        kbLo.setLatitude(37.865026);
        kbLo.setLongitude(-122.2859);
        kbLo.setState("Ca");
        kbLo.setZip("94702");
        
        systemUser.setTblKaguLocation(kbLo);
        
        crudDAO.update(systemUser);
    }
	
	@Override
	public TBLUser getSystemUser()	{
		return systemUser = userDAO.getUserByEmail("info@kagubuzz.com");
	}
	
	//TODO: check real person?
	//TDOD: check zip code
	//TODO: check password strength/cross check pw
	
	@Override
    public TBLUser updateUser(TBLUser user,
                              String firstName,
                              String lastName,
							  String email,
							  String password,
						  	  Integer timeZone,
						   	  String zip,
						   	  String phoneNumber,
						   	  boolean socialAccount,
						   	  String avatarImageName, 
						   	  String swapLocation, 
						   	  String swapLocationName, 
						   	  Boolean smsAdNotifications,
						   	  Boolean showNotifications,
						   	  Boolean emailNotifications) throws EmailInUseExecption, PhoneNumberInUseExecption, UnknownZipCodeExecption, PhoneVerifyExecption
	{
	    KaguImage avatarImage = null;
	    
	    if(firstName != null) {
	        user.setFirstName(firstName);
	    }
	    
	    if(firstName != null) {
            user.setLastName(lastName);
        }
	    
	    if(email != null) {
	        TBLUser emailUser = userDAO.getUserByEmail(email);
	        
	        if(emailUser == user || 
	           emailUser == null) {
	            user.setEmail(email);
	        }
	        else {
	            throw new EmailInUseExecption();
	        }
	    }
	    
        if (phoneNumber != null) {       
            
            TBLUser phoneUser = userDAO.getUserByPhoneNumber(phoneNumber);
            
            if(phoneUser != null && !user.equals(phoneUser)) {
                throw new PhoneNumberInUseExecption();
            }
            
            if (!phoneNumber.equals(user.getPhone())) {
                user.setPhone(phoneNumber);
                user.setPhoneVerified(false);
                user.setSmsAdNotifications(false);
            }
        }
	    
	    if(password != null) {
	        user.setPassword(new StandardPasswordEncoder().encode(password));
	    }
	    
	    if(timeZone != null) {
	        user.setTimeZoneOffset(timeZone);
	    }    
	       
        if(swapLocation != null) {
           user.setSwapLocation(swapLocation);
        }
        
        if(swapLocationName != null) {
            user.setSwapLocationName(swapLocationName);
        }
        
        if(smsAdNotifications != null) {
            user.setSmsAdNotifications(smsAdNotifications);
        }
        
        if(showNotifications != null) {
            user.setEmailNotifications(emailNotifications);
        }
        
        if(emailNotifications != null) {
            user.setSmsAdNotifications(smsAdNotifications);
        }
	    
	    if(zip != null) {
	        TBLKaguLocation kaguLocation = kaguLocationDAO.getKaguLocationByZipCode(zip);
	        
	        if(kaguLocation == null) {
	            
	            log.info("Couldn't find zip, finding closest match");
	            kaguLocation = kaguLocationDAO.getLocationWithSimilarZipCode(zip);
	        }
	        
	        if(kaguLocation == null) {
	            throw new UnknownZipCodeExecption();
	        }
	        
	        user.setZipCode(zip);
	        user.setTblKaguLocation(kaguLocation);
	    }
		
	    user.setSocialAccount(socialAccount);
	    
	    if(avatarImageName != null) {

            try {
                avatarImage = new KaguImage(new URL(avatarImageName));
            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }       
            avatarImage.resize(100, 0);
            
            user.setAvatarImage(avatarImage.getBytes());
        }
	    
	    if(user.isLoggedIn()) {
	        crudDAO.update(user);
	    }
	    
		return user;
	}
	
	@Override
    public TBLUser createUser(String name,
			  		   String currentEmail,
		  		 	   String email,
		  		 	   String password,
		  		  	   int timeZone,
		  		   	   String zip,
		  		  	   boolean socialAccount,
		  		 	   KaguImage avatar) throws EmailInUseExecption, UnknownZipCodeExecption
	{	
		TBLKaguLocation kaguLocation = kaguLocationDAO.getKaguLocationByZipCode(zip);
		
		if(kaguLocation == null) {	    
		    log.info("Couldn't find zip, finding closest match");
		    kaguLocation = kaguLocationDAO.getLocationWithSimilarZipCode(zip);		
		}
		
		if(kaguLocation == null) { return null; }
		
		WeatherReport weatherReport = new WeatherReport(kaguLocation, timeZone, null, 1);
		
		weatherReport.getCurrentConditions();

		TBLUser user = userDAO.getUserByEmail(currentEmail);
		
		// brand new user (user not being updated)
		
		if(user == null)  {
		    
			user = new TBLUser();
            
            // create a place for the user to store images files
            
            if(new File(getUserFileDirectoryPath(user)).mkdirs()) {
                log.info("New folder created for user" + currentEmail);
            }
		}
		
		if(avatar == null) { 
		    JavaFileUtilities jfu = new JavaFileUtilities();
            avatar = new KaguImage(jfu.getResourceAsStream("blank_avatar.jpg"));
		}
		
	    user.setAvatarImage(avatar.getBytes());
		user.setFirstName(name);
		if(!socialAccount) {
		    user.setEmail(email);		
		}
		user.setWeatherStationId(weatherReport.getWeatherStation());
		user.setTimeZoneOffset(timeZone);
		user.setZipCode(zip);
		user.setSocialAccount(socialAccount);
		user.setTblKaguLocation(kaguLocation);
		user.setContactMethod(DeliveryMethod.Email);
		
		if(!socialAccount) { 
		    user.setPassword(new StandardPasswordEncoder().encode(password));		
		}

        crudDAO.create(user);
        
		return user;
	}
	
	static long uniqueEmailcnt;
	
	@Override
    public TBLUser createGenericUser()
	{
		uniqueEmailcnt++;
		TBLUser user = null;
		
		try {
            user = createUser("john", 
            				  "john.smith" + uniqueEmailcnt + "@yahoo.com", 
            		   		  "john.smith" + uniqueEmailcnt + "@yahoo.com",
            		   		  "12345",            		   		  
            		   		  -480,
            		   		  "94702",
            		   		  false,
            		   		  null);
        }
        catch (EmailInUseExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (UnknownZipCodeExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
        return user;
	}
	
	@Override
    @Transactional(readOnly = false)	
	public void refreshSecurityCode(TBLUser user) {
	    
	    user.setSecurityCode(UUID.randomUUID().toString());
	    user.setSecurityCodeCreationDate(new Date());
	    
	    crudDAO.update(user);
	}
	
	@Override
    @Transactional(readOnly = false)
    public void saveIPToUser(String ip, TBLUser user) {
       
       ObjectMapper mapper = new ObjectMapper(); 
       List<String> stringSet = getKnownIPsForUser(user);
       
       if(stringSet.contains(ip)) return;
       
       if(stringSet.size() > ipHistoryLimit) stringSet.remove(0);
       
       stringSet.add(ip);
       
       try {
           user.setLastKnownIPAddresses(mapper.writeValueAsString(stringSet));
       }
       catch (JsonGenerationException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       catch (JsonMappingException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       
        crudDAO.update(user);
    }
	
   @Override
   @SuppressWarnings("unchecked")
   public List<String> getKnownIPsForUser(TBLUser user) {
           
           ObjectMapper mapper = new ObjectMapper();
           
           try {
               return mapper.readValue(user.getLastKnownIPAddresses(), new TypeReference<List<String>>(){});
           }
           catch (JsonParseException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           catch (JsonMappingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           
        return null;
   }
   
   @Override
   public void doUser5MinTasks() {
       List<TBLUser> users = userDAO.getAllKaguBuzzUsers();
       
       for(TBLUser user: users) {
           // TODO: Optimize SQl inner join only with users that have unread messages
           postsService.expireAllOldNotificationDiscussions();
           postsService.sendAutoSearchAlertToUser(user);
       }
       
   }
   
   @Override
   public int getTrustLevel(TBLUser user) {
       
    float rep = 1;
    
    if(user.isSocialAccount()) {
        rep += 1;
    }
    
    if(user.getPhoneVerified()) {
       rep += 1;
    }
    
    if(SpringSecurityUtilities.isAdmin(user)) {
       rep = 5;
    }
    
    return (int) (rep * 20);       
   }
   
   @Override
   public long getUserReputation(TBLUser user) {
       
       return userDAO.getUserIntegrityRating(user) + getUserRecommendationCount(user) * 5;
   }
   
   @Override
   public long getUserRecommendationCount(TBLUser user) {
       
       return userDAO.getUserReccomendationCount(user);
   }
   
   @Override
   public void injectUserProfile(ModelMap modelMap, TBLUser user, TBLUser userInquiry) {
       List<TBLUser> following = userDAO.getFollowers(userInquiry);
       
       modelMap.addAttribute("user_profile", userInquiry);
       modelMap.addAttribute("following", following.contains(user));
       modelMap.addAttribute("reccomended", userDAO.hasRecomended(user, userInquiry));
       modelMap.addAttribute("is_admin", SpringSecurityUtilities.isAdmin(userInquiry));
       modelMap.addAttribute("user_service", this);
   }
   
}
