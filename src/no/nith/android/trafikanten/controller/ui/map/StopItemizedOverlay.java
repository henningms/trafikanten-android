package no.nith.android.trafikanten.controller.ui.map;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.controller.ui.dialog.StopDialog;
import no.nith.android.trafikanten.util.Toaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class StopItemizedOverlay extends ItemizedOverlay<StopOverlayItem>
{
	private ArrayList<StopOverlayItem> overlays = new ArrayList<StopOverlayItem>();
	private Context context;
	
	public StopItemizedOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		populate();
	}

	public void addOverlay(StopOverlayItem overlay)
	{
		overlays.add(overlay);
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	protected StopOverlayItem createItem(int i)
	{
		return overlays.get(i);
	}

	public void clearItems()
	{
		overlays.clear();
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	public int size()
	{
		return overlays.size();
	}
	
	@Override
	protected boolean onTap(int index)
	{
		StopOverlayItem item = overlays.get(index);
		
		StopDialog.showDialog(context, item.getStop());
		
		return super.onTap(index);
	}

}
