package com.kagubuzz.datamodels.hibernate;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.kagubuzz.datamodels.JSPMessageRenderer;
import com.kagubuzz.datamodels.enums.ExchangeRatingTypes;
import com.kagubuzz.datamodels.enums.MessageType;

@Entity
@Table(name = "tbl_message_user_transaction_feedback")
public class TBLMessageUserTransactionFeedback extends TBLMessageUserFeedback implements Serializable, JSPMessageRenderer {
    
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private TBLDiscussionAd transaction;
	
    @Enumerated(EnumType.STRING)
	ExchangeRatingTypes exchangeRatingType;
    
	@Override
	public MessageType messageType() { return MessageType.TransactionRating;}

    public TBLDiscussionAd getTransaction() {return transaction;}
    public void setTransaction(TBLDiscussionAd transaction) {this.transaction = transaction;}
    
    @Override
    public JSPMessageRenderer getParent() {return transaction;}

    public ExchangeRatingTypes getExchangeRatingType() {
        return exchangeRatingType;
    }

    public void setExchangeRatingType(ExchangeRatingTypes exchangeRatingType) {
        this.exchangeRatingType = exchangeRatingType;
    }
    
    public String getIcon() {        
     return  this.getExchangeRatingType().getIcon();
    }
    
    @Override
    public String getSubjectSummary() {
        return this.getExchangeRatingType().getEnumExtendedValues().getDescription();
    }    
}
