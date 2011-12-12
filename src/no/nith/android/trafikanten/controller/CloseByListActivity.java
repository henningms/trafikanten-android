package no.nith.android.trafikanten.controller;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.api.TrafikantenApi;
import no.nith.android.trafikanten.async.ClosestStopsHandler;
import no.nith.android.trafikanten.controller.ui.adapter.CloseByListAdapter;
import no.nith.android.trafikanten.controller.ui.map.StopOverlayItem;
import no.nith.android.trafikanten.model.Stop;
import no.nith.android.trafikanten.util.GeoPointConverter;
import no.nith.android.trafikanten.util.Toaster;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CloseByListActivity extends BaseActivity
{

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void controller()
	{
		final ListView lstStops = (ListView) findListView(R.id.lstCloseByAsList);
		final Context context = this;
		
		Intent intent = this.getIntent();
		
		double latitude = intent.getDoubleExtra("lat", -1);
		double longitude = intent.getDoubleExtra("lon", -1);
		
		if (latitude != -1 && longitude != -1)
		{
			final ProgressDialog progress = ProgressDialog.show(context, "", "" +
					getResources().getString(R.string.loading_closest_stops));
			
			TrafikantenApi.getClosestStopsByCoordinates(latitude, longitude,
					new ClosestStopsHandler()
					{
						@Override
						public void onParsed(ArrayList<Stop> closestStops)
						{
							progress.cancel();
							lstStops.setAdapter(new CloseByListAdapter(context, closestStops));
							
						}

						@Override
						public void onError(String message)
						{
							progress.cancel();
							Toaster.show(context, message);
							
						}
					});
		}
		
		lstStops.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				Intent intent = new Intent().setClass(context, DeparturesFullActivity.class);
				
				Stop stop = (Stop)arg0.getAdapter().getItem(arg2);
				
				if (stop == null) return;
				
				intent.putExtra("name", stop.getName());
				intent.putExtra("id", stop.getId());
				intent.putExtra("zone", stop.getZone());
				
				context.startActivity(intent);

				
			}
		});
		
	}

	@Override
	public void view()
	{
		// TODO Auto-generated method stub
		setLayout("closeby_list");
	}

}
