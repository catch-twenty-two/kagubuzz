package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.datamodels.hibernate.TBLDiscussionAd;
import com.kagubuzz.datamodels.hibernate.TBLMessage;

public interface DiscussionAdDAO {

	public abstract TBLDiscussionAd getTransactionByIDAndSecurityCode(long id, String securityCode);


}