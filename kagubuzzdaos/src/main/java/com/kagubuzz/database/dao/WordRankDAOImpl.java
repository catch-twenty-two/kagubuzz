package com.kagubuzz.database.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.datamodels.hibernate.TBLWordRank;

@Repository("wordRankDAO")
@Transactional(readOnly = true)
public class WordRankDAOImpl implements WordRankDAO{
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TBLWordRank> GetWordRankForString(String clause)
	{
		clause = clause.replaceAll("[^\\p{L}]", " ");
		
		String[] splitClause = clause.split(" ");
		
		Criteria criteria = sessionFactory.getCurrentSession()
	   			                          .createCriteria(TBLWordRank.class)
	   			                          .addOrder(Order.desc("rank"));
	   	
   		Disjunction disjunction = (Disjunction) Restrictions.disjunction();
   		
		for(String word: splitClause) {	
			disjunction = (Disjunction) disjunction.add(Restrictions.eq("word", word.toLowerCase().trim()));
		}

		List<TBLWordRank> wordRankList = (List<TBLWordRank>) criteria.add(disjunction).list();
		
		int cnt = 0;
		
		for(String word: splitClause) {
			
			if(word.equals("")) continue;
			
			TBLWordRank temp = new TBLWordRank();
			
			temp.setWord(word.toLowerCase().trim());
			
			if(!wordRankList.contains(temp))
			{
				temp.setRank(5000 + (cnt++));
				wordRankList.add(temp);
			}
		}
		
		Collections.sort(wordRankList);	
		
		return wordRankList;
	}
}
