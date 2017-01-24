package com.kagubuzz.spring.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.CRUDDAO;
import com.kagubuzz.database.dao.SocialUserDAO;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class SocialUserServiceImpl implements SocialUserService {

	@Autowired 
	CRUDDAO crudDAO;
    @Autowired
    private SocialUserDAO socialUserDAO;
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;
    private TextEncryptor textEncryptor;
    //TODO: Fix encryption
    private String encryptionPassword = "password";

    private boolean encryptCredentials = true;

    @PostConstruct
    public void initializeTextEncryptor() {
        textEncryptor = Encryptors.text(encryptionPassword, KeyGenerators.string().generateKey());
    }

    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        return socialUserDAO.findUserIdsByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
    }

    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return new HashSet<String>(socialUserDAO.findUserIdsByProviderIdAndProviderUserIds(providerId, providerUserIds));
    }

    public ConnectionRepository createConnectionRepository(String kaguUserId) {
        if (kaguUserId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new SocialUserConnectionRepositoryImpl(
                kaguUserId,
                crudDAO,
                socialUserDAO,
                connectionFactoryLocator,
                (encryptCredentials ? textEncryptor : null)
        );
    }
}
