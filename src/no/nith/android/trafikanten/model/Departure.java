package no.nith.android.trafikanten.model;

import java.util.Date;

public class Departure
{
	private String lineRef, destinationName;
	private String departurePlatformName;
	private Date expectedDepartureTime;
	private String vehicleMode;
	
	public Departure()
	{
		this("","",null);
	}
	
	public Departure(String lineRef, String destinationName, Date expectedDepartureTime)
	{
		this(lineRef, destinationName, expectedDepartureTime, "", "");
	}
	
	public Departure(String lineRef, String destinationName, Date expectedDepartureTime,
				String departurePlatformName, String vehicleMode)
	{
		setLineRef(lineRef);
		setDestinationName(destinationName);
		setExpectedDepartureTime(expectedDepartureTime);
		setDeparturePlatformName(departurePlatformName);
		setVehicleMode(vehicleMode);
	}
	
	/**
	 * Getters
	 */
	
	public String getLineRef()
	{
		return lineRef;
	}
	
	public String getDestinationName()
	{
		return destinationName;
	}
	
	public String getDeparturePlatformName()
	{
		return departurePlatformName;
	}
	
	public Date getExpectedDepartureTime()
	{
		return expectedDepartureTime;
	}
	
	public String getVehicleMode()
	{
		return vehicleMode;
	}
	
	/*
	 * Setters
	 */
	
	public void setLineRef(String lineRef)
	{
		this.lineRef = lineRef;
	}
	
	public void setDestinationName(String destinationName)
	{
		this.destinationName = destinationName;
	}
	
	public void setDeparturePlatformName(String departurePlatformName)
	{
		this.departurePlatformName = departurePlatformName;
	}
	
	public void setExpectedDepartureTime(Date expectedDepartureTime)
	{
		this.expectedDepartureTime = expectedDepartureTime;
	}
	
	public void setVehicleMode(String vehicleMode)
	{
		this.vehicleMode = vehicleMode;
	}
}
