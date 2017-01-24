package com.kagubuzz.services.cartography;

import org.springframework.stereotype.Service;


// TODO: move this into location dao

@Service
public class Cartography
{
    /*
	public static double distanceFrom(TBLKaguLocation kaguLocation1, TBLKaguLocation kaguLocation2)
	{
		return distanceFrom(kaguLocation1.getLatitude(), kaguLocation1.getLongitude(), kaguLocation2.getLatitude(), kaguLocation2.getLongitude());
	}

	public static double distanceFrom(TBLKaguLocation kaguLocation1, double latitude, double longitude)
	{
		return distanceFrom(kaguLocation1.getLatitude(), kaguLocation1.getLongitude(), latitude, longitude);
	}
*/
	public static double distanceFrom(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(latitude2 - latitude1);
		double dLng = Math.toRadians(longitude2 - longitude1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(latitude1) * Math.cos(latitude2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}
	
	public static double getLocationDistanceWeight(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		return Math.abs(latitude1 - latitude2) + Math.abs(longitude1 - longitude2);
	}
}
