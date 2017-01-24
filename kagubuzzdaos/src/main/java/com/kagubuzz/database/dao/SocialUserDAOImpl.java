package com.kagubuzz.database.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.kagubuzz.datamodels.hibernate.TBLSocialUser;

/**
 * Implements SocialUserDAO using Hibernate and its Criteria API.
 * 
 * Based on this class:
 * 
 * https://github.com/mschipperheyn/spring-social-jpa/blob/master/spring-social-jpa/src/main/java/org/springframework/social/connect/jpa/JpaConnectionRepository.java
 */

@SuppressWarnings("unchecked")

@Repository("socialUserDAO")
@Transactional(readOnly=true)
public class SocialUserDAOImpl implements SocialUserDAO {

	@Autowired
	SessionFactory sessionFactory;
	
    private static final String USER_ID = "id";
    private static final String PROVIDER_ID = "providerId";
    private static final String PROVIDER_USER_ID = "providerUserId";
    private static final String RANK = "rank";

    private Criteria createCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(TBLSocialUser.class);
    }

    public List<TBLSocialUser> findByProviderId(String providerId) {
        return (List<TBLSocialUser>) createCriteria().add(Restrictions.eq(PROVIDER_ID, providerId)).list();
    }

    public List<TBLSocialUser> findByUserId(Long userId) {
        return (List<TBLSocialUser>) createCriteria().add(Restrictions.eq(USER_ID, userId)).list();
    }

    public List<TBLSocialUser> findByUserIdAndProviderId(String userId, String providerId) {
        return (List<TBLSocialUser>) createCriteria()
                .add(Restrictions.eq(USER_ID, Long.valueOf(userId)))
                .add(Restrictions.eq(PROVIDER_ID, providerId))
                .list();
    }

    public List<TBLSocialUser> findByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq(USER_ID, Long.valueOf(userId)));
        Disjunction or = Restrictions.disjunction();
        for (String providerId : providerUserIds.keySet()) {
            or.add(
                    Restrictions.and(
                            Restrictions.eq(PROVIDER_ID, providerId),
                            Restrictions.in(PROVIDER_USER_ID, providerUserIds.get(providerId))
                    )
            );
        }
        return (List<TBLSocialUser>) criteria.list();
    }

    public TBLSocialUser get(String userId, String providerId, String providerUserId) {
        return (TBLSocialUser) createCriteria()
                .add(Restrictions.eq(USER_ID, Long.valueOf(userId)))
                .add(Restrictions.eq(PROVIDER_ID, providerId))
                .add(Restrictions.eq(PROVIDER_USER_ID, providerUserId))
                .uniqueResult();
    }

    public List<TBLSocialUser> findPrimaryByUserIdAndProviderId(String userId, String providerId) {
        return (List<TBLSocialUser>) createCriteria()
                .add(Restrictions.eq(USER_ID, Long.valueOf(userId)))
                .add(Restrictions.eq(PROVIDER_ID, providerId))
//        .add(Restrictions.eq("rank", 1))
                .addOrder(Order.asc(RANK))
                .list();
    }

    public Integer selectMaxRankByUserIdAndProviderId(String userId, String providerId) {
        return (Integer) createCriteria()
                .add(Restrictions.eq(USER_ID, Long.valueOf(userId)))
                .add(Restrictions.eq(PROVIDER_ID, providerId))
                .setProjection(Projections.max(RANK))
                .uniqueResult();
    }

    public List<String> findUserIdsByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        List<TBLSocialUser> socialUsers = (List<TBLSocialUser>) createCriteria()
                .add(Restrictions.eq(PROVIDER_ID, providerId))
                .add(Restrictions.eq(PROVIDER_USER_ID, providerUserId))
                .list();
        List<String> userIds = new ArrayList<String>(socialUsers.size());
        for (TBLSocialUser tblSocialUser : socialUsers) {
            userIds.add(tblSocialUser.getId().toString());
        }
        return userIds;
    }

    public List<String> findUserIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds) {
        List<TBLSocialUser> socialUsers = (List<TBLSocialUser>) createCriteria()
                .add(Restrictions.eq(PROVIDER_ID, providerId))
                .add(Restrictions.in(PROVIDER_USER_ID, providerUserIds))
                .list();
        List<String> userIds = new ArrayList<String>(socialUsers.size());
        for (TBLSocialUser tblSocialUser : socialUsers) {
            userIds.add(tblSocialUser.getId().toString());
        }
        return userIds;
    }

	@Override
	public TBLSocialUser get(String providerId, String providerUserId) {
	      return (TBLSocialUser) createCriteria()
	                .add(Restrictions.eq(PROVIDER_ID, providerId))
	                .add(Restrictions.eq(PROVIDER_USER_ID, providerUserId))
	                .uniqueResult();
	}
}
