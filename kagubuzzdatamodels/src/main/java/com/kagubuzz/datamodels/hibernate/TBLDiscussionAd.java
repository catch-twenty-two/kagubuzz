package com.kagubuzz.datamodels.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.kagubuzz.datamodels.Bookmark;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.AdDiscussionState;
import com.kagubuzz.datamodels.enums.DeliveryMethod;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name = "tbl_discussions_ad")
public class TBLDiscussionAd extends TBLDiscussionBase implements Bookmark {

    private static final long serialVersionUID = 1L;

    private Boolean timeBanking = false;
    private Integer offerAmount = 0;
    @Enumerated(EnumType.STRING)
    private DeliveryMethod buyerContactMethod = DeliveryMethod.Email;
    @Enumerated(EnumType.STRING)
    AdDiscussionState adDiscussionState = AdDiscussionState.WaitingForResponse;
    private String uuid = UUID.randomUUID().toString();
    private Date lastOfferDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    TBLMessageUserTransactionFeedback buyerFeedback;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
    TBLMessageUserTransactionFeedback sellerFeedback;

    // Grabs all the messages that are marked as an add offer request for this
    // add

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "discussion")
    @OrderBy("createdDate")
    private List<TBLMessageAdOffer> offerMessageInquiries = new ArrayList<TBLMessageAdOffer>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(updatable = false)
    TBLAd ad;

    @ManyToOne(optional = false)
    TBLUser buyer;

    @ManyToOne(optional = false)
    TBLUser seller;
    
    public void addMessage(TBLMessageAdOffer offerMessage) {
        this.offerMessageInquiries.add(offerMessage);
    }

    public void removeMessage(TBLMessageAdOffer offerMessage) {
        this.offerMessageInquiries.remove(offerMessage);
    }

    public TBLUser getParty(TBLUser user) {
        return (user != getBuyer()) ? getSeller() : getBuyer();
    }

    public TBLUser getOppositeParty(TBLUser user) {
        return (user.getId().equals(getBuyer().getId())) ? getSeller() : getBuyer();
    }

    public TBLAd getAd() {
        return ad;
    }

    public void setAd(TBLAd ad) {
        this.ad = ad;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public DeliveryMethod getBuyerContactMethod() {
        return this.buyerContactMethod;
    }

    public void setBuyerContactMethod(DeliveryMethod buyerContactMethod) {
        this.buyerContactMethod = buyerContactMethod;
    }

    public TBLUser getBuyer() {
        return buyer;
    }

    public void setBuyer(TBLUser buyer) {
        this.buyer = buyer;
    }

    public TBLUser getSeller() {
        return seller;
    }

    public void setSeller(TBLUser seller) {
        this.seller = seller;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(Integer offerAmount) {
        this.offerAmount = offerAmount;
    }

    public int getInquiryOrderNumber() {
        return (this.getAd()
                .getOffers().indexOf(this));
    }

    public AdDiscussionState getAdDiscussionState() {
        return adDiscussionState;
    }

    public void setAdDiscussionState(AdDiscussionState discussionState) {
        this.adDiscussionState = discussionState;
    }

    @Override
    public MessageType messageType() {
        return MessageType.AdOffer;
    }

    @Override
    public TBLMessage getFirstMessageInThread() {
        return offerMessageInquiries.get(0);
    }

    @Override
    public JSPMessageRenderer getParent() {
        return this;
    }

    @Override
    public TBLUser getOwner() {
        return this.getSeller();
    }

    @Override
    public String getTitle() {
        return getAd().getTitle();
    }

    public void refreshSecurityCode() {
        this.setUuid(UUID.randomUUID()
                .toString());
    }

    @Override
    public Date getCreatedDateInUserTZ() {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getLastOfferDate() {
        return lastOfferDate;
    }

    public void setLastOfferDate(Date lastOfferDate) {
        this.lastOfferDate = lastOfferDate;
    }

    public List<TBLMessageAdOffer> getOfferMessageInquiries() {
        return offerMessageInquiries;
    }

    public void setOfferMessageInquiries(List<TBLMessageAdOffer> offerMessageInquiries) {
        this.offerMessageInquiries = offerMessageInquiries;
    }

    @Override
    public String getViewingURL() {
        return "/transaction" + super.getViewingURL();
    }

    public TBLMessageUserTransactionFeedback getBuyerFeedback() {
        return buyerFeedback;
    }

    public void setBuyerFeedback(TBLMessageUserTransactionFeedback buyerFeedback) {
        this.buyerFeedback = buyerFeedback;
    }

    public TBLMessageUserTransactionFeedback getSellerFeedback() {
        return sellerFeedback;
    }

    public void setSellerFeedback(TBLMessageUserTransactionFeedback sellerFeedback) {
        this.sellerFeedback = sellerFeedback;
    }

    @Override
    public TBLMessageUserFeedback getUserFeedback(TBLUser user) {

        if (user == this.buyer) {
            return buyerFeedback;
        }

        return sellerFeedback;
    }

    @Override
    public Date bookmarkRelevantDate() {
        return getUpdatedDate();
    }

    @Override
    public boolean canBeRated() {

        if(sellerFeedback != null || buyerFeedback != null) return true;
        
        switch (this.getAdDiscussionState()) {
        case Accepted:
        case Canceled:
        case Complete:
            return true;
        }

        return false;
    }

    @Override
    public String getBookMarkCSS() {
        return "darkgrey";
    }

    @Override
    public String getDetail() {
        return this.adDiscussionState.getEnumExtendedValues().getDescription();
    }

    @Override
    public boolean bookmarkCanBeDeleted() {
        return false;
    }

    @Override
    public String bookmarkIcon() {
        return "icon-tags";
    }

    @Override
    public String deleteBookMarkURL() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean getTimeBanking() {
        return timeBanking;
    }

    public void setTimeBanking(Boolean timeBanking) {
        this.timeBanking = timeBanking;
    }
}