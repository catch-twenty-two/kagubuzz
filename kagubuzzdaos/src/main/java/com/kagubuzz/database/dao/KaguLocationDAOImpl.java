package com.kagubuzz.database.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kagubuzz.database.dao.utilities.SearchService;
import com.kagubuzz.datamodels.hibernate.LSTCategoryBase;
import com.kagubuzz.datamodels.hibernate.TBLKaguLocation;
import com.kagubuzz.services.cartography.Cartography;

@Repository("kaguLocationDAO")
@Transactional(readOnly = true)
public class KaguLocationDAOImpl implements KaguLocationDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public TBLKaguLocation getKaguLocationByZipCode(String zipCode) { 
        
        TBLKaguLocation kaguLocation = (TBLKaguLocation) sessionFactory.getCurrentSession()
                .createCriteria(TBLKaguLocation.class)
                .add(Restrictions.eq("zip", zipCode))
                .uniqueResult();

        return kaguLocation;
    }

    @Override
    public TBLKaguLocation getLocationWithSimilarZipCode(String originalZipCode) {

        String partialZipcode = originalZipCode.substring(0, originalZipCode.length() - 1);

        @SuppressWarnings("unchecked")
        List<TBLKaguLocation> locationList = sessionFactory.getCurrentSession()
                .createCriteria(TBLKaguLocation.class)
                .add(Restrictions.like("zip", partialZipcode + "%"))
                .addOrder(Order.desc("zip"))
                .list();

        List<String> zipList = new ArrayList<String>();
        
        if(zipList.size() == 0) return null;
        
        for (TBLKaguLocation kaguLocation : locationList) {
            zipList.add(kaguLocation.getZip());
        }

        zipList.add(originalZipCode);

        Collections.sort(zipList);

        int originalZipIndex = zipList.indexOf(originalZipCode);
        String similarZip = null;
        
        // At begging of list

        if (originalZipIndex == 0) {
            similarZip = zipList.get(1);
        }

        // At end of list

        if (originalZipIndex == zipList.size()) {
            similarZip = zipList.get(zipList.size() - 1);
        }
        
        if (originalZipIndex > 0 && originalZipIndex < zipList.size()) {

            String zipInc = zipList.get(originalZipIndex + 1);
            String zipDec =  zipList.get(originalZipIndex - 1);
            
            similarZip = (Math.abs((Integer.parseInt(zipInc) - Integer.parseInt(originalZipCode))) < 
                          Math.abs((Integer.parseInt(zipDec) - Integer.parseInt(originalZipCode)))) ?  zipInc : zipDec; 
             
        }

        return (TBLKaguLocation) sessionFactory.getCurrentSession()
                .createCriteria(TBLKaguLocation.class)
                .add(Restrictions.eq("zip", similarZip))
                .uniqueResult();
    }
    
    @Override
    public TBLKaguLocation getClosestZip(double latitude, double longitude) {
        
        Criteria zipCodeSearch = sessionFactory.getCurrentSession().createCriteria(TBLKaguLocation.class);
        TBLKaguLocation closestLocation = null;
        double shortestDistance = Double.MAX_VALUE;
        double distance = 0;

        TBLKaguLocation tempLocation = new TBLKaguLocation();
        
        tempLocation.setLatitude(latitude);
        tempLocation.setLongitude(longitude);
        
        SearchService.addRadiusCriteria(tempLocation, 1, zipCodeSearch);

        List<TBLKaguLocation> zipList = zipCodeSearch.list();
        
        for (TBLKaguLocation currentLocation : zipList) {

            double currentLatitude = currentLocation.getLatitude();
            double currentLongitude = currentLocation.getLongitude();
            
            distance = Cartography.getLocationDistanceWeight(currentLocation.getLatitude(), 
                                                             currentLocation.getLongitude(), 
                                                             currentLatitude,
                                                             currentLongitude);
            
            if (shortestDistance >= distance) closestLocation = currentLocation;
        }
        
        return closestLocation;      
    }
}
