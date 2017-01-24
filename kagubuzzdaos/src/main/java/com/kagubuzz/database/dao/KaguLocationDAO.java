package com.kagubuzz.database.dao;

import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;

public interface KaguLocationDAO {

	TBLKaguLocation getKaguLocationByZipCode(String zipCode);

    TBLKaguLocation getLocationWithSimilarZipCode(String zipCode);

    TBLKaguLocation getClosestZip(double latitude, double longitude);

}