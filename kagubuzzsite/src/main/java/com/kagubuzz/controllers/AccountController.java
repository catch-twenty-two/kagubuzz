package com.kagubuzz.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;


import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.CategoryDAO;
import com.kagubuzz.database.dao.SocialUserDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.hibernate.TBLSocialUser;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.datamodels.hibernate.TBLUsersAutoSearchKeywordList;
import com.kagubuzz.exceptions.EmailInUseExecption;
import com.kagubuzz.exceptions.PhoneNumberInUseExecption;
import com.kagubuzz.exceptions.PhoneVerifyExecption;
import com.kagubuzz.exceptions.UnknownZipCodeExecption;
import com.kagubuzz.messagetemplates.systemmessages.SystemMessage;
import com.kagubuzz.services.SecurityService;
import com.kagubuzz.services.StringTemplateService;
import com.kagubuzz.services.UserAccountService;
import com.kagubuzz.services.UserSessionService;
import com.kagubuzz.services.dispatcher.MessageDispatcher;
import com.kagubuzz.services.notifications.Notification;
import com.kagubuzz.services.notifications.NotificationTypes;
import com.kagubuzz.services.sms.SMSService;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;
import com.kagubuzz.utilities.KaguImage;
import com.kagubuzz.utilities.KaguTextFormatter;

import org.apache.log4j.Logger;

@Controller
public class AccountController {

    @Autowired 
	UserDAO  userDAO;
    
    @Autowired
    UserAccountService userAccountService;
    
    @Autowired
    MessageDispatcher messageDispatcher;
    
    @Autowired
    CRUDDAO crudDAO;
    
    @Autowired
    SearchService userSearchService;

    @Autowired 
    CategoryDAO categoryDAO;
	
    @Autowired
    SecurityService securityService;
    
    @Autowired
    SocialUserDAO socialUserDAO;
    
    @Autowired
    UserSessionService sessionUserService;
    
    @Autowired
    StringTemplateService stringTemplateService;
    
    @Autowired 
    SpringSecurityUtilities springSecurityUtilities;
    
    @Autowired
    SMSService smsService;
    
    static Logger logger = Logger.getLogger(AccountController.class);       
    
    @InitBinder
    /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
	@RequestMapping(value = "/saveadsearch", method = RequestMethod.POST)
	public @ResponseBody Notification  saveAdSearchAjax(ModelMap modelMap,
						@RequestParam(required = false) String searchterm1,
						@RequestParam(required = false) String searchterm2) 
	{
	    TBLUser user = sessionUserService.getUser();
		
		TBLUsersAutoSearchKeywordList keyWordList = user.getSearchableKeyWordList();
		
		if(user.getSearchableKeyWordList() == null)
		{
			keyWordList = new TBLUsersAutoSearchKeywordList();
			keyWordList.setOwner(user);
			crudDAO.update(keyWordList);
			
			user.setSearchAbleKeyWordList(keyWordList);
		}
		
	    keyWordList.getAutoSearchAdsAlertSent().clear();
		keyWordList.getAutoSearchAds().clear();
		
		keyWordList.setAdKeyword1(searchterm1);
		keyWordList.setAdKeyword2(searchterm2);
		
		crudDAO.update(keyWordList);
		
	
		return new Notification("Ad Search Saved", Messages.getString("AccountController.0"));
	}
	
	@RequestMapping(value = "/saveeventsearch", method = RequestMethod.POST)
	public @ResponseBody Notification saveEventSearchAjax(ModelMap modelMap,
						@RequestParam(required = false) String searchterm1,
						@RequestParam(required = false) String searchterm2) 
	{
		
	    TBLUser user = sessionUserService.getUser();
		
		TBLUsersAutoSearchKeywordList keyWordList = user.getSearchableKeyWordList();
		
		if(user.getSearchableKeyWordList() == null)
		{
			keyWordList = new TBLUsersAutoSearchKeywordList();
			keyWordList.setOwner(user);
			crudDAO.update(keyWordList);
			
			user.setSearchAbleKeyWordList(keyWordList);
		}
		
		keyWordList.getAutoSearchEvents().clear();
		keyWordList.getAutoSearchEventsAlertSent().clear();
		
		keyWordList.setEventKeyword1(searchterm1);
		keyWordList.setEventKeyword2(searchterm2);
		
		crudDAO.update(keyWordList);
		
		return new Notification("Event Search Saved", Messages.getString("AccountController.1"));
	}
	
	@RequestMapping(value = "/updatesettings", method = RequestMethod.POST)
	public @ResponseBody Notification updateSettings(ModelMap modelMap,
								 @RequestParam(required = false, value="first_name") String firstName,
								 @RequestParam(required = false, value="last_name") String lastName,
								 String password,
								 @RequestParam(required = false, value="re_password") String repassword,
								 String email,
								 @RequestParam(required = false, value="zip_code") String zip,
								 @RequestParam(required = false, value="phone_number") String phoneNumber,
								 String avatarimage,
								 @RequestParam(required = false, value="sms_ad_notifications", defaultValue ="false") Boolean smsAdNotifications,
	                             @RequestParam(required = false, value="email_notifications", defaultValue ="false") Boolean emailNotifications,
	                             @RequestParam(required = false, value="on_screen_notifications", defaultValue ="false") Boolean showNotifications,
								 @RequestParam(required = false, value="swap_location_address") String swapLocation,
								 @RequestParam(required = false, value="swap_location_name") String swapLocationName)							 
	{
        TBLUser user = sessionUserService.getUser();

		try {
            userAccountService.updateUser(user,
                                          firstName,
                                          lastName,
                                          email,
            							  password, 
            							  null,
            							  zip,
            							  phoneNumber,
            							  user.isSocialAccount(), 
            							  avatarimage, 
            							  swapLocation, 
            							  swapLocationName, 
            							  smsAdNotifications, 
            							  emailNotifications, 
            							  showNotifications);
        }
        catch (EmailInUseExecption e) {
            return new Notification(stringTemplateService.getTemplateError("error_notification_title").render(),
                                    stringTemplateService.getTemplateError("email_in_use").render(), NotificationTypes.error);
        }
        catch (PhoneNumberInUseExecption e) {
            return new Notification(stringTemplateService.getTemplateError("error_notification_title").render(), 
                                    stringTemplateService.getTemplateError("phone_in_use").render(), NotificationTypes.error);
        }
        catch (UnknownZipCodeExecption e) {
            return new Notification(stringTemplateService.getTemplateError("error_notification_title").render(), 
                                    stringTemplateService.getTemplateError("unknown_zip_code").render(), NotificationTypes.error);
        }
		
		catch (PhoneVerifyExecption e) {
            return new Notification(stringTemplateService.getTemplateError("error_notification_title").render(),
                                    stringTemplateService.getTemplateError("phone_not_verified").render(), NotificationTypes.error);
        }
	        
		return new Notification("Settings Updated", "Your settings were updated. Thanks for keeping your info current!");
	}
	
	@RequestMapping(value="createfromsocial", method = RequestMethod.GET)
	public String createAccountFromSocial(WebRequest webRequest,
	                                      @CookieValue("time_zone") Integer timeZone,
	                                      ModelMap modelMap,
										  HttpSession httpSession) {	
	
		Connection<?> connection = ProviderSignInUtils.getConnection(webRequest);

		if(connection == null){
		    logger.warn("No social connection");
		    return "/jsp/error"; 
		}

		TBLUser newUser = null;
		KaguImage avatar = null;
		 
		try {
		    avatar = new KaguImage(new URL(connection.getImageUrl()));
		}catch (MalformedURLException e) {
            logger.error("REST Creating account" , e); 
            avatar = null;
        }
		
        try {
            newUser = userAccountService.createUser(connection.fetchUserProfile().getFirstName(), null, null, null, timeZone, "94612", true, avatar);
        }
        catch (EmailInUseExecption e) {
            modelMap.addAttribute("error_page_message", stringTemplateService.getTemplateError("email_in_use").render());
            logger.warn("Email is in use");
            return "/jsp/error";

        }
        catch (UnknownZipCodeExecption e) {
            modelMap.addAttribute("error_page_message", stringTemplateService.getTemplateError("unknown_zip_code").render());
            logger.warn("Unkown zip code");
            return "/jsp/error";

        }	
		
		ProviderSignInUtils.handlePostSignUp(newUser.getId().toString(), webRequest);
	    
		TBLSocialUser socialUser = socialUserDAO.findByUserId(newUser.getId()).get(0);	      
	    newUser.setSocialUserAccount(socialUser);
	    crudDAO.update(newUser);
	        
	    springSecurityUtilities.signInUser(newUser, httpSession);
		
        SystemMessage systemMessage = new SystemMessage(newUser, userAccountService.getSystemUser());
        systemMessage.welcome();
        messageDispatcher.sendMessage(systemMessage);
		
		return "redirect:/home/browse?new_account=true"; 
	}

	@RequestMapping(value="createaccount", method = RequestMethod.POST)
	public String createAccount(HttpSession httpSession,
	                          //                      @CookieValue("beta_test_code") String betaTestId,
	                                                @CookieValue("time_zone") Integer timeZone,
							                        ModelMap model,
							                        HttpServletRequest request,
							                        @RequestParam(required = true, value="first_name") String firstName,
							                        @RequestParam(required = true) String email,
							                        @RequestParam(required = true) String password,
							                        @RequestParam(required = true, value="re_password") String rePassword,
							                        @RequestParam(required = true, value="zip_code") String zip,
							                        @RequestParam(required = false) String recaptcha_challenge_field_submit,
				                                    @RequestParam(required = false) String recaptcha_response_field_submit)  {

	    
        if ((recaptcha_challenge_field_submit == null) || 
            (recaptcha_response_field_submit == null) || 
            !securityService.checkCaptchaInput(recaptcha_response_field_submit, recaptcha_challenge_field_submit, request.getRemoteAddr())) {
            model.addAttribute("captcha_error", new Notification(stringTemplateService.getTemplateError("captcha_text_wrong")).toJson());
            return "/jsp/landing_page";
        }        
        
        email = email.toLowerCase();
        
	    TBLUser newUser = userDAO.getUserByEmail(email);
	    
        if (newUser == null) {

            try {
                newUser = userAccountService.createUser(firstName, email, email, password, timeZone, zip, false, null);
            }
            catch (EmailInUseExecption e) {
                model.addAttribute("error_page_message", stringTemplateService.getTemplateError("email_in_use").render());
                logger.warn("Email is in use");
                return "/jsp/error";

            }
            catch (UnknownZipCodeExecption e) {
                model.addAttribute("error_page_message", stringTemplateService.getTemplateError("unknown_zip_code").render());
                logger.warn("Zip code unkown");
                return "/jsp/error";

            }
        }

	    if(newUser == null)  {
	        logger.warn("Wasn't able to create new user");
            return "/jsp/error";
         }
	    
	    if(newUser.getVerified())  {
            model.addAttribute("error_page_message", Messages.getString("AccountController.12"));
            logger.warn("User is already in use");
			return "/jsp/error";
         }
	    
		userAccountService.refreshSecurityCode(newUser);
		
        SystemMessage systemEmailMessage = new SystemMessage(newUser, userAccountService.getSystemUser());
        systemEmailMessage.verifyAccount();
        messageDispatcher.sendMessage(systemEmailMessage);
 
        model.addAttribute("message_title", "Thank you for signing up!");
        model.addAttribute("message_lead", stringTemplateService.getTemplateNotification("account_create").render());
        
		return "/jsp/info";
	}
	
	
    @RequestMapping(value ="/accountverify", method = RequestMethod.GET)
    public String activateAccount(HttpSession httpSession, 
                                  ModelMap modelMap, 
                                  String securityCode, 
                                  Long id) {     
        TBLUser user = null;
        
        try {       
            user = securityService.checkSecurityCode(id, securityCode);
        } catch(SecurityException e) {
            modelMap.addAttribute("error_page_message", Messages.getString("AccountController.16"));  //$NON-NLS-2$
            logger.warn("In account verify", e);
            return "/jsp/error"; 
        }
        
        if(user == null) {
            logger.warn("User not found, In account verify");
            return "/jsp/error"; 
        }
        
        userAccountService.refreshSecurityCode(user);
        
        springSecurityUtilities.signInUser(user, httpSession);
        
        user.setVerified(true);
        crudDAO.update(user);
        
        SystemMessage sytemMessage = new SystemMessage(user, userAccountService.getSystemUser());
        sytemMessage.welcome();
        messageDispatcher.sendMessage(sytemMessage);
       
        return "redirect:home/browse"; 
    }
    
    @RequestMapping(value = "/verify_phone", method = RequestMethod.POST)
    public @ResponseBody Notification verifyPhone(ModelMap modelMap, 
                                                  HttpSession httpSession, 
                                                  @RequestParam(required = true, value = "phone_number") String phoneNumber) {

        TBLUser user = sessionUserService.getUser();
        
        phoneNumber = "1" + phoneNumber;        
        
        // Number is already being used by user and is verified

        if (user.getPhoneVerified() && (user.getPhone().equals(phoneNumber))) {
            return new Notification().setModalNotification("account_phone_already_verified").setResponseType(NotificationTypes.success);
        }

        TBLUser phoneUser = userDAO.getUserByPhoneNumber(phoneNumber);

        if (phoneUser == null || (user.getPhone().equals(phoneNumber))) {
            
            // Number is available or not verified yet

            user.setPhone(phoneNumber);
            user.setPhoneVerified(false);

            crudDAO.update(user);

            SystemMessage sytemMessage = new SystemMessage(user, userAccountService.getSystemUser());

            sytemMessage.verifyPhone();
            messageDispatcher.sendMessage(sytemMessage);
            
            return new Notification().setModalNotification("account_verify_phone");
        }

        // Number is taken
        
        return new Notification(stringTemplateService.getTemplateError("error_notification_title")
                                .render(), stringTemplateService.getTemplateError("phone_in_use")
                                .render(), NotificationTypes.error);
    }
    
    @RequestMapping(value = "/verifying_phone", method = RequestMethod.POST)
    public @ResponseBody Notification verifyingPhone(HttpSession httpSession,
                                                     ModelMap modelMap) {
        
        TBLUser user = sessionUserService.getUser();

        if (!user.getPhoneVerified()) {
            return new Notification("Still Waiting For A Response...", 
                                    "Please reply back with '#kagubuzz' when you get a text message from " +
                                     KaguTextFormatter.getPhoneFormatted(smsService.getSmsPhoneNumber()) +
                                     ". (This process may take up to 10 minutes)", NotificationTypes.info);
        }
        
        return new Notification().setModalNotification("account_phone_verified").setResponseType(NotificationTypes.success);
    }
      
    @RequestMapping(value ="/reset_password_request", method = RequestMethod.GET)
    public String resetUserPassword(ModelMap modelMap) {
        return "/jsp/reset_password";
    }
    
    @RequestMapping(value ="/reset_password_send", method = RequestMethod.POST)
    public String resetUserPassword(HttpSession httpSession,
                                    ModelMap modelMap,
                                    @RequestParam("userId") String userName) 
    {   
        TBLUser user = userDAO.getUserByEmail(userName);
        
        if(user != null) {             
            
            userAccountService.refreshSecurityCode(user);
            
            SystemMessage systemEmailMessage = new SystemMessage(user, userAccountService.getSystemUser());
            systemEmailMessage.passwordReset();
            messageDispatcher.sendMessage(systemEmailMessage);
        }
        
        modelMap.addAttribute("message_title", "Good News!");
        modelMap.addAttribute("message_lead", stringTemplateService.getTemplateNotification("reset_password").render());
        
        return "/jsp/info";
    }
    
    @RequestMapping(value ="/account_create", method = RequestMethod.GET)
    public String accountCreate(ModelMap modelMap) { 
        modelMap.addAttribute("message_title", "Thank you for signing up!");
        modelMap.addAttribute("message_lead", stringTemplateService.getTemplateNotification("account_create").render());
        return "/jsp/account_create";
    }


    @RequestMapping(value ="/reset_password_step_1", method = RequestMethod.GET)
    public String resetPasswordStep1(HttpSession httpSession, 
                                     ModelMap modelMap, 
                                     String securityCode, 
                                     Long id, 
                                     @RequestParam(required = false) boolean captchaError) { 
        
        TBLUser user = null;
        
        try {
            user = securityService.checkSecurityCode(id, securityCode);
        } catch(SecurityException e) {
            logger.warn(e.getMessage());
            return "/jsp/error"; 
        }
        
        if(user == null) {
            logger.warn("In password reset user not found");
            return "/jsp/error"; 
        }
        
        modelMap.addAttribute("reset_user", user); 
        modelMap.addAttribute("recaptcha", securityService.getCaptchaHTML()); 
        
        if(captchaError) {
            modelMap.addAttribute("captcha_error", "Please make sure you&rsquo;re entering the captcha text correctly.");  //$NON-NLS-2$
        }
        
        return "/jsp/reset_password_step_1"; 
    }
    
    @RequestMapping(value ="/reset_password_step_2", method = RequestMethod.GET)
    public String resetPasswordStep2(HttpSession httpSession, 
                                     ModelMap modelMap, 
                                     HttpServletRequest request,
                                     String securityCode,
                                     @RequestParam(value = "password", required = true) String newPassword1,
                                     @RequestParam(value = "re_password", required = true) String newPassword2,
                                     @RequestParam(required = false) String recaptcha_challenge_field,
                                     @RequestParam(required = false) String recaptcha_response_field,
                                     Long id) {
        TBLUser user = null;
        
        String captchaErrorRedirect = "redirect:reset_password_step_1?id=" +id + "&securityCode=" + securityCode + "&captchaError=true";
        
        if((recaptcha_challenge_field == null) ||
            (recaptcha_response_field == null)) {
            return captchaErrorRedirect;
        }
        
        if(!securityService.checkCaptchaInput(recaptcha_response_field, recaptcha_challenge_field, request.getRemoteAddr())) {
            return captchaErrorRedirect; 
        }
        
        try {
            user = securityService.checkSecurityCode(id, securityCode);
        } catch(SecurityException e) {
            logger.warn("In password reset step 2", e);
            return "/jsp/error"; 
        }
        
        if(user == null) {
            logger.warn("User not found");
            return "/jsp/error"; 
        }
        
        if(!newPassword1.equals(newPassword2)) {
            logger.warn("old password didn't equal new password");
            return "/jsp/error"; 
        }

        try {
            userAccountService.updateUser(user,
                                          null, 
                                          null, 
                                          null, 
                                          newPassword1, 
                                          null,
                                          null,
                                          null,
                                          false, 
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null);
        }
        catch (EmailInUseExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (PhoneNumberInUseExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (UnknownZipCodeExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (PhoneVerifyExecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        springSecurityUtilities.signInUser(user, httpSession);
        
        userAccountService.refreshSecurityCode(user);
        
        SystemMessage systemEmailMessage = new SystemMessage(user, userAccountService.getSystemUser());
        systemEmailMessage.passwordChanged();
        messageDispatcher.sendMessage(systemEmailMessage);
        
        return "redirect:/home/browse"; 
    }
}
