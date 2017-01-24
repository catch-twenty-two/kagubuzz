package com.kagubuzz.spring.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;

import com.kagubuzz.database.dao.UserDAO;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.spring.utilities.SpringSecurityUtilities;

/**
 * Spring Social Configuration.
 */
@Configuration
public class SocialConfig {

	@Autowired
	UserDAO userDAO;

	@Value("${facebook.clientid}")
	String faceBookClientId;
	
	@Value("${facebook.clientsecret}")
	String faceBookClientSecret;

    @Value("${google.clientid}")
    String googleClientId;

    @Value("${google.clientsecret}")
    String googleClientSecret;
	    
	/**
	 * When a new provider is added to the app, register its {@link ConnectionFactory} here.
	 * @see FacebookConnectionFactory
	 */
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		
		// Register Facebook
		
		registry.addConnectionFactory(new FacebookConnectionFactory(faceBookClientId, faceBookClientSecret));
		
		// Register Google Plus
		
		 registry.addConnectionFactory(new GoogleConnectionFactory(googleClientId, googleClientSecret));
		
		return registry;
	}

	/**
	 * Singleton data access object providing access to connections across all users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return new SocialUserServiceImpl();
	}

	/**
	 * Request-scoped data access object providing access to the current user's connections.
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
	    return usersConnectionRepository().createConnectionRepository(SpringSecurityUtilities.getLoggedInUser().getUsername());
	}

	/**
	 * A proxy to a request-scoped object representing the current user's primary Facebook account.
	 * @throws NotConnectedException if the user is not connected to facebook.
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Facebook facebook() {
	    return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}

    /**
     * A proxy to a request-scoped object representing the current user's primary Google account.
     * @throws NotConnectedException if the user is not connected to Google.
     */
	
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google() {
        return connectionRepository().getPrimaryConnection(Google.class).getApi();
    }
}