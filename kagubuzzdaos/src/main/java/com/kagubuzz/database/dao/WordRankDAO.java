package com.kagubuzz.database.dao;

import java.util.List;

import com.kagubuzz.datamodels.hibernate.TBLWordRank;

public interface WordRankDAO {

	@SuppressWarnings("unchecked")
	public abstract List<TBLWordRank> GetWordRankForString(String clause);

}