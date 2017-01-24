package com.kagubuzz.database.dao;

import com.kagubuzz.datamodels.hibernate.TBLBallot;

public interface BallotDAO {

    public abstract double getBallotEventRating(TBLBallot ballot);

    public abstract long getBallotEventRatingCount(TBLBallot ballot);

}