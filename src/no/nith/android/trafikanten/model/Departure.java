package no.nith.android.trafikanten.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import no.nith.android.trafikanten.json.annotation.JsonField;

public class Departure
{
	@JsonField("LineRef")
	private String lineRef;
	
	@JsonField("DestinationName")
	private String destinationName;
	
	@JsonField("DeparturePlatformName")
	private String departurePlatformName;
	
	@JsonField("ExpectedDepartureTime")
	private Date expectedDepartureTime;
	
	@JsonField("VehicleMode")
	private String vehicleMode;
	
	private enum Vehicle {
		Bus, Boat
	};
	
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
	
	public String getHourMinute()
	{
		if (getExpectedDepartureTime() == null) return "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(getExpectedDepartureTime()).toString();
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
