package no.nith.android.trafikanten.model;

public class Station
{
	private long id;
	private String name, zone;
	
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
}
