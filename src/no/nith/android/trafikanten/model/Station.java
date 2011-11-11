package no.nith.android.trafikanten.model;

import java.util.ArrayList;

import no.nith.android.trafikanten.json.annotation.JsonField;

public class Station
{
	@JsonField("ID")
	private long id;
	
	@JsonField("Name")
	private String name;
	
	@JsonField("Zone")
	private String zone;
	
	@JsonField("Stops")
	private ArrayList<Stop> stops;
	
	public Station()
	{
		this(0, "", "");
	}
	
	public Station(long id, String name, String zone)
	{
		setId(id);
		setName(name);
		setZone(zone);
	}
	
	/*
	 * Getters
	 */
	
	public long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getZone()
	{
		return zone;
	}
	
	public ArrayList<Stop> getStops()
	{
		return stops;
	}
	
	/*
	 * Setters
	 */
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setZone(String zone)
	{
		this.zone = zone;
	}
	
	public String toString()
	{
		return getName();
	}
}
