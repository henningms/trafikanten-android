package no.nith.android.trafikanten.controller.ui.map;

import no.nith.android.trafikanten.model.Stop;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class StopOverlayItem extends OverlayItem
{
	private Stop stop;
	
	public StopOverlayItem(GeoPoint point, Stop stop)
	{
		super(point, stop.getName(), "");
		
		this.stop = stop;
	}
	
	public Stop getStop()
	{
		return stop;
	}

}
