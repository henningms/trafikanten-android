package no.nith.android.trafikanten.async;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import no.mesan.thomasp.location.util.*;
import no.nith.android.trafikanten.json.JsonParser;
import no.nith.android.trafikanten.model.Departure;
import no.nith.android.trafikanten.model.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TrafikantenApi
{
	private static final String BASE_URL = "http://api-test.trafikanten.no";
	
	private static final String STATION_PATH = "/Place/FindMatches/%s";
	private static final String DEPARTURE_PATH = "/RealTime/GetRealTimeData/%d";
	private static final String CLOSEST_STOPS_PATH = "/Places/GetClosestStopsByCoordinates/?coordinates=(X=%d,Y=%d)&proposals=7";
	
	private static AsyncHttpClient httpClient = new AsyncHttpClient();
	
	/**
	 * 
	 * @param search Search string
	 * @param handler
	 */
	public static void findStationsAsync(String search, final StationHandler handler)
	{
		if (httpClient == null) return;
		if (search == null || search == "") return;
		if (handler == null) return;
		
		String url = String.format(STATION_PATH, search);
		url = String.format("%s%s", BASE_URL, url);	
		
		httpClient.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response)
			{			
				handler.onParsed(JsonParser.ParseStations(response));
			}
			
			@Override
			public void onFailure(Throwable e)
			{
				Log.e("findStations", "Error retrieving JSON data");
				//
			}
		});
	}
	
	
	/**
	 * 
	 * @param stationId ID of station to retrieve departures
	 * @param handler
	 */
	public static void getDeparturesAsync(long stationId, final DepartureHandler handler)
	{
		if (httpClient == null) return;
		if (stationId == 0) return;
		if (handler == null) return;
		
		String url = String.format(DEPARTURE_PATH, stationId);
		url = String.format("%s%s", BASE_URL, url);	
		
		httpClient.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response)
			{			
				handler.onParsed(JsonParser.ParseDepartures(response));
			}
			
			@Override
			public void onFailure(Throwable e)
			{
				Log.e("getDepartures", "Error retrieving JSON data");
				handler.onParsed(null);
			}
		});
	}
	
	/**
	 * 
	 * @param latitude GPS Latitude
	 * @param longitude GPS Longitude
	 * @param handler 
	 */
	public static void getClosestStopsByCoordinates(double latitude, double longitude, final ClosestStopsHandler handler)
	{
		if (httpClient == null) return;
		if (latitude == 0 || longitude == 0)  return;
		if (handler == null) return;
		
		// Convert from GPS coordinates (Latitude, Longitude) to UTM (X, Y) coordinates
		UTMRef toXY = new LatLng(latitude, longitude).toUTMRef();
		
		String url = String.format(CLOSEST_STOPS_PATH, toXY.getEasting(), toXY.getNorthing());
		url = String.format("%s%s", BASE_URL, url);
		
		httpClient.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response)
			{
				handler.onParsed(JsonParser.ParseClosestStops(response));
			}
			
			@Override
			public void onFailure(Throwable e)
			{
				Log.e("getClosestStopsByCoordinates", "Error retrieveing JSON data");
				handler.onParsed(null);
			}
		});
	}
}
