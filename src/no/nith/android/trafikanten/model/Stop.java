package no.nith.android.trafikanten.model;

public class Stop extends Station
{
	private int walkingDistance;
	private long x, y;
	
	public Stop()
	{
		this(0, null, null, 0,0,0);
	}
	
	public Stop(long id, String name, String zone, long x, long y, int walkingDistance)
	{
		super(id, name, zone);
		setX(x);
		setY(y);
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
	
	public long getX() 
	{
		return x;
	}
	
	public void setX(long x)
	{
		this.x = x;
	}
	
	public long getY() 
	{
		return y;
	}
	
	public void setY(long y) 
	{
		this.y = y;
	}
}
