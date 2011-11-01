package no.nith.android.trafikanten.json;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.nith.android.trafikanten.model.Departure;
import no.nith.android.trafikanten.model.Station;

public class JsonParser
{
	
	public static ArrayList<Station> ParseStations(JSONArray toParse)
	{
		try
		{
			ArrayList<Station> stationList = new ArrayList<Station>();
			
			for (int i = 0; i < toParse.length(); i++)
			{
				JSONObject obj = toParse.optJSONObject(i);
				Station tmp = new Station(obj.getLong("ID"), obj.getString("Name"), obj.getString("Zone"));
				stationList.add(tmp);
			}
			
			return stationList;
			
		} 
		catch (JSONException e)
		{
			return null;
		}
	}
	
	public static ArrayList<Departure> ParseDepartures(JSONArray toParse)
	{
		try
		{
			ArrayList<Departure> departureList = new ArrayList<Departure>();
			
			for (int i = 0; i < toParse.length(); i++)
			{
				JSONObject obj = toParse.optJSONObject(i);
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
			
			return departureList;
			
		} 
		catch (JSONException e)
		{
			return null;
		}
	}

}
