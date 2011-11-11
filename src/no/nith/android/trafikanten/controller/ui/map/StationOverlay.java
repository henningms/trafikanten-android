package no.nith.android.trafikanten.controller.ui.map;

import no.nith.android.trafikanten.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class StationOverlay extends Overlay
{
	private double lat, lon;
	private String title, description;
	private GeoPoint p;
	private Context context;
	private Bitmap bmp;
	
	public StationOverlay(double lat, double lon, String title, String description, Context context, Bitmap bmp)
	{
		super();
		
		this.lat = lat;
		this.lon = lon;
		this.title = title;
		this.description = description;
		this.context = context;
		this.bmp = bmp;
		
		p = new GeoPoint((int) (lat * 1E6), (int)(lon * 1E6));
	}
	
	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView){
	        lat = geoPoint.getLatitudeE6(); 
	        lon = geoPoint.getLongitudeE6();
	        
	        Toast tst = Toast.makeText(context, title +"\n"+description, Toast.LENGTH_LONG);
	        tst.show();
	        
	        return true;
	        //return super.onTap(geoPoint,mapView);
	}
	 
	@Override
    public boolean draw(Canvas canvas, MapView mapView, 
    boolean shadow, long when) 
    {
        super.draw(canvas, mapView, shadow);                   

        //---translate the GeoPoint to screen pixels---
        Point screenPts = new Point();
        mapView.getProjection().toPixels(p, screenPts);

        //---add the marker---        
        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
        return true;
    }
}
