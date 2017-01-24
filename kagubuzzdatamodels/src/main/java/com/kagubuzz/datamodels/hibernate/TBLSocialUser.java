package com.kagubuzz.datamodels.hibernate;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.search.annotations.Similarity;

import java.util.Date;
import java.util.UUID;

/**
 * A social network user account.
 *
 * Implementation of Spring Social's expected UserConnection schema defined here:
 *
 * http://static.springsource.org/spring-social/docs/1.0.x/reference/html/serviceprovider.html
 *
 */
@Entity
@Table(name = "tbl_social_user",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "providerId", "providerUserId"}),
        @UniqueConstraint(columnNames = {"id", "providerId", "rank"})
})
public class TBLSocialUser {

    @Id
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "owner"))
    @GeneratedValue(generator = "generator")
    private Long id;
    
    @Column(nullable = false)
    private String providerId;

    private String providerUserId;
    
    @Column(nullable = false)
    private Integer rank;

    private String displayName;
    
    @Column(length = 500)
    private String profileUrl;
    
    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 1024)
    private String accessToken;

    @Column(length = 500)
    private String secret;

    @Column(length = 500)
    private String refreshToken;

    private Long expireTime;

    @Column(nullable = true)
    String email;
    
    private Date createDate = new Date();

    public Long getId() {  return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getKaguUserId() {
        return owner.getId().toString();
    }

    //Creates a foreign key
    @OneToOne(fetch = FetchType.LAZY, optional=true)
    @PrimaryKeyJoinColumn
    private TBLUser owner;
	
    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public TBLUser getOwner() {
		return owner;
	}

	public void setOwner(TBLUser owner) {
		this.owner = owner;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
