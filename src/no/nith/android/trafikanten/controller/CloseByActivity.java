package no.nith.android.trafikanten.controller;

import java.util.ArrayList;
import java.util.List;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.api.TrafikantenApi;
import no.nith.android.trafikanten.async.ClosestStopsHandler;
import no.nith.android.trafikanten.controller.ui.map.MarkerOverlay;
import no.nith.android.trafikanten.controller.ui.map.StationOverlay;
import no.nith.android.trafikanten.controller.ui.map.StopItemizedOverlay;
import no.nith.android.trafikanten.controller.ui.map.StopOverlayItem;
import no.nith.android.trafikanten.model.Stop;
import no.nith.android.trafikanten.util.GeoPointConverter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CloseByActivity extends MapActivity
{
	private MapView mapView;
	private MapController mapController;
	private StopItemizedOverlay stopOverlays;
	private MyLocationOverlay myLocationOverlay;
	private Context context;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location lastLocation;
	
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.closeby);
		
		context = this;
		
		mapView = (MapView) findViewById(R.id.mapView);
		
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		
		Drawable drawable = getResources().getDrawable(R.drawable.map_pin);
		stopOverlays = new StopItemizedOverlay(drawable, context);
		
		mapView.getOverlays().add(stopOverlays);
		initMyLocation();
	}
	
	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onResume() 
	{
		refreshMyLocation();
		initLocationListening();
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		disableMyLocation();
		locationManager.removeUpdates(locationListener);
		super.onResume();
	}
	
	private void refreshMyLocation()
	{
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		
		myLocationOverlay.runOnFirstFix(new Runnable() {
		    public void run()
		    {
		        myLocationOverlay.getMyLocation();
		    }
		});
	}
	
	private void disableMyLocation()
	{
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}
	
	private void initMyLocation() 
	{
		myLocationOverlay = new MyLocationOverlay(context, mapView);
		mapView.getOverlays().add(myLocationOverlay);		
	}
	
	private void initLocationListening() 
	{

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				lastLocation = location;
				mapController.animateTo(GeoPointConverter.convert(location.getLatitude(), location.getLongitude()));
				mapController.setZoom(17);
				createStationOverlays(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 30, locationListener);
	}
	
	private void createStationOverlays(Location location)
	{
		TrafikantenApi.getClosestStopsByCoordinates(location.getLatitude(), location.getLongitude(),
				new ClosestStopsHandler()
				{
					@Override
					public void onParsed(ArrayList<Stop> closestStops)
					{
						for (Stop stop : closestStops)
						{
							StopOverlayItem item = new StopOverlayItem(GeoPointConverter.convert(stop.getLatitude(),
									stop.getLongitude()), stop);
							stopOverlays.addOverlay(item);
						}
						
					}

					@Override
					public void onError(String message)
					{
						Toast errorToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
						errorToast.show();
						
					}
				});
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    menu.clear();
	    inflater.inflate(no.nith.android.trafikanten.R.menu.closeby_menu, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.closeby_menu_showAsList:
	    	showAsList();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void showAsList()
	{
		if (lastLocation == null) return;

		Intent intent = new Intent().setClass(this, CloseByListActivity.class);
		
		intent.putExtra("lat", lastLocation.getLatitude());
		intent.putExtra("lon", lastLocation.getLongitude());
		
		this.startActivity(intent);
		
	}

}
