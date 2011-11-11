package no.nith.android.trafikanten.model;

import no.mesan.thomasp.location.util.LatLng;
import no.mesan.thomasp.location.util.UTMRef;
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
	
	public Stop()
	{
		this(0, null, null,0);
	}
	
	public Stop(long id, String name, String zone, int walkingDistance)
	{
		setId(id);
		setName(name);
		setZone(zone);
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

	public double getLatitude()
	{
		LatLng lat = new UTMRef(getX(), getY()).toLatLng();
		
		return lat.getLat();
	}


	public double getLongitude() 
	{
		LatLng lat = new UTMRef(getX(), getY()).toLatLng();
		
		return lat.getLng();
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
