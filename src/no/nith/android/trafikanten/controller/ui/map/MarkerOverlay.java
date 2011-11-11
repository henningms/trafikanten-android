package no.nith.android.trafikanten.controller.ui.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();
	private Context context;

	public MarkerOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}

	@Override
	public int size() {
		return items.size();
	}
	
	@Override
	protected boolean onTap(int index) {		
		Toast.makeText(context, items.get(index).getTitle().toString() + ", " + 
				items.get(index).getSnippet(),
				Toast.LENGTH_LONG).show();
		//goToExternalMapApp(items.get(index));
		return super.onTap(index);
	}
	
	private void goToExternalMapApp(OverlayItem item) {
		String uri = "geo:"+ (item.getPoint().getLatitudeE6()/1E6) + "," + (item.getPoint().getLongitudeE6()/1E6) + "?z=13";
		context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
	}

	public void addOverlay(OverlayItem item) {
		items.add(item);
		setLastFocusedIndex(-1);
		populate();
	}
	
	public void addOverlay(List<OverlayItem> items) {
		this.items.addAll(items);
		setLastFocusedIndex(-1);
		populate();
	}

	public void clear() {
		items.clear();
		setLastFocusedIndex(-1);
		populate();
	}
}