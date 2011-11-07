package no.nith.android.trafikanten.model;

import no.nith.android.trafikanten.json.annotation.JsonField;

public class Stop
{
	@JsonField("ID")
	private long id;
	
	@JsonField("Name")
	private String name;
	
	@JsonField("WalkingDistance")
	private int walkingDistance;
	
	@JsonField("X")
	private long x;
	
	@JsonField("Y")
	private long y;
	
	@JsonField("Zone")
	private String zone;
	
	private double latitude, longitude;
	
	public Stop()
	{
		this(0, null, null, 0,0,0);
	}
	
	public Stop(long id, String name, String zone, double latitude, double longitude, int walkingDistance)
	{
		setId(id);
		setName(name);
		setZone(zone);
		setLatitude(latitude);
		setLongitude(longitude);
		setWalkingDistance(walkingDistance);
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getZone()
	{
		return zone;
	}

	public void setZone(String zone)
	{
		this.zone = zone;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public int getWalkingDistance() 
	{
		return walkingDistance;
	}
	
	public void setWalkingDistance(int walkingDistance) 
	{
		this.walkingDistance = walkingDistance;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public long getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public long getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
}
