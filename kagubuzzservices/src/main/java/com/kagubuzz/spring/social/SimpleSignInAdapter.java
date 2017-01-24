package com.kagubuzz.spring.social;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.SocialUserDAO;
import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLSocialUser;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

/**
 * Signs the user in by setting the currentUser property on the {@link SecurityContext}.
 * Remembers the sign-in after the current request completes by storing the user's id in a cookie.
 * This is cookie is read in {@link UserInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)} on subsequent requests.
 * @author Keith Donald
 * @see UserInterceptor
 */
@Service
public final class SimpleSignInAdapter implements SignInAdapter {

	@Autowired 
	UserDAO tblUserDao;
    @Autowired
    SocialUserDAO socialUserDAO;
    @Autowired 
    CRUDDAO crudDAO;
    
    @Autowired SpringSecurityUtilities springSecurityUtilities;
    @Autowired ConnectionRepository connectionRepository; 
    
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		
		ConnectionKey key = connection.getKey();
		
		TBLSocialUser socialUser = socialUserDAO.get(key.getProviderId(), key.getProviderUserId());
		

		
		springSecurityUtilities.signInUser(socialUser.getOwner(),
										  ((HttpServletRequest) request.getNativeRequest()).getSession());
	     // Update user on kagubuzz
        connectionRepository.updateConnection(connection);
        
		return null;
	}

}