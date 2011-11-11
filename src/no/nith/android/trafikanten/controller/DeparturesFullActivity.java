package no.nith.android.trafikanten.controller;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.api.TrafikantenApi;
import no.nith.android.trafikanten.async.DepartureHandler;
import no.nith.android.trafikanten.controller.ui.adapter.DepartureListAdapter;
import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.DBManager.Mode;
import no.nith.android.trafikanten.model.Departure;
import no.nith.android.trafikanten.model.FavoriteStation;
import no.nith.android.trafikanten.util.Toaster;

public class DeparturesFullActivity extends BaseActivity
{
	private long id;
	private String name;
	private String zone;
	private Context context;
	
	private boolean favorited;
	
	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void controller()
	{
		final TextView lblName = (TextView)findTextView(R.id.lblFullDepartureStationName);
		final TextView lblZone = (TextView)findTextView(R.id.lblFullDepartureZone);
		final ListView lstStations = (ListView)findListView(R.id.lstFullDepartureList);
		
		Intent intent = this.getIntent();
		
		id = intent.getLongExtra("id",-1);
		
		zone = intent.getStringExtra("zone");
		name = intent.getStringExtra("name");
		
		lblName.setText(intent.getStringExtra("name"));
		
		if (zone == null)
			lblZone.setText(getString(R.string.departure_zone_unknown));
		else
			lblZone.setText(String.format(getString(R.string.departure_zone), zone));
		
		final ProgressDialog progress = ProgressDialog.show(context, name,
				getResources().getString(R.string.loading_departures));
		
		TrafikantenApi.getDeparturesAsync(id, new DepartureHandler()
		{
			
			@Override
			public void onParsed(ArrayList<Departure> departures)
			{
				progress.cancel();
				
				lstStations.setAdapter(new DepartureListAdapter(context, departures));
			}

			@Override
			public void onError(String message)
			{
				progress.cancel();
				
				Toaster.showToast(context, message);
				
			}
		});

	}

	@Override
	public void view()
	{
		setLayout("departures_full");
		
		context = this;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    menu.clear();
	    inflater.inflate(no.nith.android.trafikanten.R.menu.station_full_menu, menu);
	    
	    checkIfStationIsFavorite();
	    
	    int addRemoveFavorite = getItemFromMenu(menu, R.string.add_to_favorite);
	    
	    if (addRemoveFavorite != -1)
	    {
	    	if (favorited)
	    	{
	    		menu.getItem(addRemoveFavorite).setTitle(R.string.remove_from_favorites);
	    	}
	    }
	   
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.station_full_menu_refresh:
	        refreshDepartures();
	        return true;
	    case R.id.station_full_menu_favorite:
	        addRemoveFavorite();
	        return true;

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void addRemoveFavorite()
	{
		if (name == null || name.equals("")) return;
		
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE, FavoriteStation.class);
			
			if (favorited == true)
			{
				db.delete(new FavoriteStation((int)id, name, zone));
				setFavorite(false);
				
				Toaster.showToast(this, R.string.favorite_removed, 
						name);
			}
			else
			{
				// Example of inserting an entity:
				db.insert(new FavoriteStation((int)id, 
						name, zone));
				
				setFavorite(true);
				
				Toaster.showToast(this, R.string.favorite_added, name);
			}
			
			// Close the database connection after use.
			db.close();
			
		} catch (Exception e) {
			
			String message = getResources().getString(R.string.error_db_general);
			
			Toaster.showToast(this, message);
			
			String output = e.getClass().getName() + " ";
			for (StackTraceElement se : e.getStackTrace()) {
				output += se.toString();
			}
			Log.e("DBMANAGER", output);
		}
		
	}
	
	private void setFavorite(boolean favorited)
	{
		this.favorited = favorited;
	}

	private void refreshDepartures()
	{
		if (name == null) return;
		
		final ListView lstStation = findListView("lstStations");
		
		final ProgressDialog progress = ProgressDialog.show(context, name,
				getResources().getString(R.string.loading_departures));
		
		TrafikantenApi.getDeparturesAsync(id, new DepartureHandler()
		{
			
			@Override
			public void onParsed(ArrayList<Departure> departures)
			{
				progress.cancel();
			 
				lstStation.setAdapter(new DepartureListAdapter(context, departures));
			}

			@Override
			public void onError(String message)
			{
				progress.cancel();
				
				Toaster.showToast(context, message);
				
			}
		});
	}
	
	private void checkIfStationIsFavorite()
	{
		if (name == null || name == "") return;
		
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE, FavoriteStation.class);
			
			// Example of inserting an entity:
			ArrayList<FavoriteStation> stations = db.select();
			
			setFavorite(false);
			
			for (FavoriteStation fs : stations)
			{
				if (fs.getId() == (int)id)
				{
					setFavorite(true);
					break;
				}
			}
			
			// Close the database connection after use.
			db.close();
			
		} catch (Exception e) {
			
			Toaster.showToast(this, R.string.error_db_general);
			
			String output = e.getClass().getName() + " ";
			for (StackTraceElement se : e.getStackTrace()) {
				output += se.toString();
			}
			Log.e("DBMANAGER", output);
		}
		
	}
	
	private int getItemFromMenu(Menu menu, int id)
	{
		String title = getString(id);
		
		if (menu == null) return -1;
		if (title == null || title.equals("")) return -1;
		
		for (int i = 0; i < menu.size(); i++)
		{
			if (menu.getItem(i).getTitle().equals(title))
			{
				return i;
			}
		}
		
		return -1;
		
	}

}
