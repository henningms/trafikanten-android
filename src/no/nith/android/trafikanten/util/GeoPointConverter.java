package no.nith.android.trafikanten.util;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class GeoPointConverter
{

	public static GeoPoint convert(double latitude, double longitude)
	{
		return new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
	}
	
	public static GeoPoint convert(Location location)
	{
		return new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
	}
}
