package no.nith.android.trafikanten.model;

public class Stop extends Station
{
	private int walkingDistance;
	private double latitude, longitude;
	
	public Stop()
	{
		this(0, null, null, 0,0,0);
	}
	
	public Stop(long id, String name, String zone, double latitude, double longitude, int walkingDistance)
	{
		super(id, name, zone);
		setLatitude(latitude);
		setLongitude(longitude);
		setWalkingDistance(walkingDistance);
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
	
}
