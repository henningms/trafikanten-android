package no.nith.android.trafikanten.async;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	public static void findStations(String search, StationHandler handler)
	{
		if (httpClient == null) return;
		if (search == null || search == "") return;
		if (handler == null) return;
		
		String url = String.format("%s%s", BASE_URL, STATION_PATH, search);
		
		httpClient.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response)
			{
				JSONObject obj = response.optJSONObject(0);
				
				try
				{
					System.out.println(obj.getString("Name"));
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 * @param stationId
	 * @param handler
	 */
	public static void getDepartures(long stationId, DepartureHandler handler)
	{
		
	}
}
