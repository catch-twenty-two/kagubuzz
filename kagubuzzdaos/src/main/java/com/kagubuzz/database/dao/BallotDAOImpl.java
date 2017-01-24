package com.kagubuzz.database.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.datamodels.hibernate.TBLBallot;

@Repository("ballotDAO")
@Transactional(readOnly = true)
public class BallotDAOImpl implements BallotDAO {

    @Autowired
    SessionFactory sessionFactory;
    
    @Override
    public double getBallotEventRating(TBLBallot ballot) {
        
        if(ballot == null) return 0D;
        
        Query query = sessionFactory.getCurrentSession()
                      .createQuery("select avg(feedback.rating) from TBLMessageUserEventFeedback feedback " +
                      		       "where feedback.ballot = :ballot");

        query.setParameter("ballot", ballot);
        
        if(query.uniqueResult() == null) return 0D;
        
        return (Double) query.uniqueResult();
    }
    
    @Override
    public long getBallotEventRatingCount(TBLBallot ballot) {
        
        if(ballot == null) return 0;
        
        Query query = sessionFactory.getCurrentSession()
                      .createQuery("select count(feedback.rating) from TBLMessageUserEventFeedback feedback " +
                                   "where feedback.ballot = :ballot");

        query.setParameter("ballot", ballot);
        
        if(query.uniqueResult() == null) return 0;
        
        return (Long) query.uniqueResult();
    }

}
