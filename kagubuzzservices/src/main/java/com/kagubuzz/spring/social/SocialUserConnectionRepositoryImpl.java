package com.kagubuzz.spring.social;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import com.kagubuzz.datamodels.hibernate.TBLSocialUser;
import com.kagubuzz.datamodels.hibernate.TBLUser;
import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.SocialUserDAO;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class SocialUserConnectionRepositoryImpl implements ConnectionRepository {

    private String kaguUserId;
    private SocialUserDAO socialUserDAO;
    private ConnectionFactoryLocator connectionFactoryLocator;
    private TextEncryptor textEncryptor;
    private CRUDDAO crudDAO;

    public SocialUserConnectionRepositoryImpl(String kaguUserId,
    										  CRUDDAO crudDAO,
                                              SocialUserDAO socialUserDAO,
                                              ConnectionFactoryLocator connectionFactoryLocator,
                                              TextEncryptor textEncryptor) {
        this.kaguUserId = kaguUserId;
        this.socialUserDAO = socialUserDAO;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.crudDAO = crudDAO;
    }

    public MultiValueMap<String, Connection<?>> findAllConnections() {
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();

        List<TBLSocialUser> allSocialUsers = socialUserDAO.findByUserId(Long.parseLong(kaguUserId));
        for (TBLSocialUser socialUser : allSocialUsers) {
            ConnectionData connectionData = toConnectionData(socialUser);
            Connection<?> connection = createConnection(connectionData);
            connections.add(connectionData.getProviderId(), connection);
        }

        return connections;
    }

    public List<Connection<?>> findConnections(String providerId) {
        List<Connection<?>> connections = new ArrayList<Connection<?>>();

        List<TBLSocialUser> socialUsers = socialUserDAO.findByUserIdAndProviderId(kaguUserId, providerId);
        for (TBLSocialUser socialUser : socialUsers) {
            ConnectionData connectionData = toConnectionData(socialUser);
            Connection<?> connection = createConnection(connectionData);
            connections.add(connection);
        }

        return connections;
    }

    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();

        // do some lame stuff to make the casting possible
        List<?> connections = findConnections(providerId);
        return (List<Connection<A>>) connections;
    }

    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();

        List<TBLSocialUser> allSocialUsers = socialUserDAO.findByUserIdAndProviderUserIds(kaguUserId, providerUserIds);
        for (TBLSocialUser socialUser : allSocialUsers) {
            ConnectionData connectionData = toConnectionData(socialUser);
            Connection<?> connection = createConnection(connectionData);
            connections.add(connectionData.getProviderId(), connection);
        }

        return connections;
    }

    public Connection<?> getConnection(ConnectionKey connectionKey) {
        TBLSocialUser socialUser = socialUserDAO.get(kaguUserId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        if (socialUser == null) {
            throw new NoSuchConnectionException(connectionKey);
        }
        return createConnection(toConnectionData(socialUser));
    }

    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
        TBLSocialUser socialUser = socialUserDAO.get(kaguUserId, providerId, providerUserId);
        if (socialUser == null) {
            throw new NoSuchConnectionException(new ConnectionKey(providerId, providerUserId));
        }
        return (Connection<A>) createConnection(toConnectionData(socialUser));
    }

    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        Connection<A> connection = findPrimaryConnection(apiType);
        if (connection == null) {
            String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();

        List<TBLSocialUser> socialUsers = socialUserDAO.findPrimaryByUserIdAndProviderId(kaguUserId, providerId);
        Connection<A> connection = null;
        if (socialUsers != null && !socialUsers.isEmpty()) {
            connection = (Connection<A>) createConnection(toConnectionData(socialUsers.get(0)));
        }

        return connection;
    }

    @Transactional(readOnly = false)
    public void addConnection(Connection<?> connection) {
        ConnectionData connectionData = connection.createData();
        
        // check if this social account is already connected to a local account
        List<String> userIds 
        			= socialUserDAO.findUserIdsByProviderIdAndProviderUserId(connectionData.getProviderId(), 
        																	 connectionData.getProviderUserId());
        
        if (!userIds.isEmpty()) {
            throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
        }
        //check if this user already has a connected account for this provider
        List<TBLSocialUser> socialUsers = socialUserDAO.findByUserIdAndProviderId(kaguUserId, connectionData.getProviderId());
        if (!socialUsers.isEmpty()) {
            throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
        }

        Integer maxRank = socialUserDAO.selectMaxRankByUserIdAndProviderId(kaguUserId, connectionData.getProviderId());
        int nextRank = (maxRank == null ? 1 : maxRank + 1);

        TBLSocialUser socialUser = new TBLSocialUser();

        socialUser.setProviderId(connectionData.getProviderId());
        socialUser.setProviderUserId(connectionData.getProviderUserId());
        socialUser.setRank(nextRank);
        socialUser.setDisplayName(connectionData.getDisplayName());
        socialUser.setProfileUrl(connectionData.getProfileUrl());
        socialUser.setImageUrl(connectionData.getImageUrl());
        socialUser.setOwner(crudDAO.getById(TBLUser.class, Long.parseLong(kaguUserId)));
        socialUser.setEmail(connection.fetchUserProfile().getEmail());
        // encrypt these values
       // socialUser.setAccessToken(encrypt(connectionData.getAccessToken()));
       // socialUser.setSecret(encrypt(connectionData.getSecret()));
       // socialUser.setRefreshToken(encrypt(connectionData.getRefreshToken()));

       socialUser.setAccessToken(connectionData.getAccessToken());
       socialUser.setSecret(connectionData.getSecret());
       socialUser.setRefreshToken(connectionData.getRefreshToken());
        
        socialUser.setExpireTime(connectionData.getExpireTime());

        try {
            crudDAO.update(socialUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
        }
    }

    @Transactional(readOnly = false)
    public void updateConnection(Connection<?> connection) {
        ConnectionData connectionData = connection.createData();
        TBLSocialUser socialUser = socialUserDAO.get(kaguUserId, connectionData.getProviderId(), connectionData.getProviderUserId());
        if (socialUser != null) {
            socialUser.setDisplayName(connectionData.getDisplayName());
            socialUser.setProfileUrl(connectionData.getProfileUrl());
            socialUser.setImageUrl(connectionData.getImageUrl());

            //socialUser.setAccessToken(encrypt(connectionData.getAccessToken()));
            //socialUser.setSecret(encrypt(connectionData.getSecret()));
           // socialUser.setRefreshToken(encrypt(connectionData.getRefreshToken()));

            socialUser.setAccessToken(connectionData.getAccessToken());
            socialUser.setSecret(connectionData.getSecret());
            socialUser.setRefreshToken(connectionData.getRefreshToken());
            socialUser.setEmail(connection.fetchUserProfile().getEmail());
            socialUser.setExpireTime(connectionData.getExpireTime());
            crudDAO.update(socialUser);
        }
    }

    @Transactional(readOnly = false)
    public void removeConnections(String providerId) {
        // TODO replace with bulk delete HQL
        List<TBLSocialUser> socialUsers = socialUserDAO.findByUserIdAndProviderId(kaguUserId, providerId);
        for (TBLSocialUser socialUser : socialUsers) {
            crudDAO.delete(socialUser);
        }
    }

    @Transactional(readOnly = false)
    public void removeConnection(ConnectionKey connectionKey) {
        TBLSocialUser socialUser = socialUserDAO.get(kaguUserId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        if (socialUser != null) {
            crudDAO.delete(socialUser);
        }
    }

    private ConnectionData toConnectionData(TBLSocialUser socialUser) {
        return new ConnectionData(socialUser.getProviderId(),
                socialUser.getProviderUserId(),
                socialUser.getDisplayName(),
                socialUser.getProfileUrl(),
                socialUser.getImageUrl(),

                //decrypt(socialUser.getAccessToken()),
                //decrypt(socialUser.getSecret()),
                //decrypt(socialUser.getRefreshToken()),

                socialUser.getAccessToken(),
                socialUser.getSecret(),
                socialUser.getRefreshToken(),
                
                convertZeroToNull(socialUser.getExpireTime()));
    }

    private Connection<?> createConnection(ConnectionData connectionData) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
        return connectionFactory.createConnection(connectionData);
    }

    private Long convertZeroToNull(Long expireTime) {
        return (expireTime != null && expireTime == 0 ? null : expireTime);
    }

    private String decrypt(String encryptedText) {
        return (textEncryptor != null && encryptedText != null) ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

    private String encrypt(String text) {
        return (textEncryptor != null && text != null) ? textEncryptor.encrypt(text) : text;
    }
}
