package no.nith.android.trafikanten.async;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	private static final String CLOSEST_STOPS_PATH = "/Places/GetClosestStopsByCoordinates/%s";
	
	private static AsyncHttpClient httpClient = new AsyncHttpClient();
	/**
	 * 
	 * @param search
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
				try
				{
					ArrayList<Station> stationList = new ArrayList<Station>();
					
					for (int i = 0; i < response.length(); i++)
					{
						JSONObject obj = response.optJSONObject(i);
						Station tmp = new Station(obj.getLong("ID"), obj.getString("Name"), obj.getString("Zone"));
						stationList.add(tmp);
					}
					
					handler.onParsed(stationList);
					
				} 
				catch (JSONException e)
				{
					
				}
				
				
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
	 * @param stationId
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
				try
				{
					ArrayList<Departure> departureList = new ArrayList<Departure>();
					
					for (int i = 0; i < response.length(); i++)
					{
						JSONObject obj = response.optJSONObject(i);
						Departure tmp = new Departure();
						
						String datee = obj.getString("ExpectedDepartureTime");
						datee = datee.replace("/Date(", "");
						datee = datee.replace(")/", "");
						datee = datee.substring(0, datee.indexOf('+'));
						
						Date date = new Date(Long.parseLong(datee) + 3600);

						
						tmp = new Departure(
								obj.getString("LineRef"), obj.getString("DestinationName"), 
								date,
								obj.getString("DeparturePlatformName"), obj.getString("VehicleMode")
								);
						departureList.add(tmp);
					}
					
					handler.onParsed(departureList);
					
				} 
				catch (JSONException e)
				{
					
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable e)
			{
				Log.e("getDepartures", "Error retrieving JSON data");
				//
			}
		});
	}
}
