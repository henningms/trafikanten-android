package no.nith.android.trafikanten.json;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import no.mesan.thomasp.location.util.*;
import no.nith.android.trafikanten.model.*;

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
	
	public static ArrayList<Stop> ParseClosestStops(JSONArray toParse)
	{
		try
		{
			ArrayList<Stop> stopList = new ArrayList<Stop>();
			
			for (int i = 0; i < toParse.length(); i++)
			{
				JSONObject obj = toParse.optJSONObject(i);
				
				long x = obj.getLong("X");
				long y = obj.getLong("Y");
				
				LatLng latLng = new UTMRef(x,y).toLatLng();
				double latitude = latLng.getLat();
				double longitude = latLng.getLng();
				
				Stop tmpStop = new Stop(
						obj.getLong("ID"), obj.getString("Name"), obj.getString("Zone"),
						latitude, longitude, obj.getInt("WalkingDistance"));
				
				stopList.add(tmpStop);
				
			}
			
			return stopList;
		}
		catch (JSONException e)
		{
			return null;
		}
	}

}
