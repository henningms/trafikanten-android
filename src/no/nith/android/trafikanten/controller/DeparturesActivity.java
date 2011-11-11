package no.nith.android.trafikanten.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.api.TrafikantenApi;
import no.nith.android.trafikanten.async.DepartureHandler;
import no.nith.android.trafikanten.async.StationHandler;
import no.nith.android.trafikanten.controller.ui.adapter.DepartureListAdapter;
import no.nith.android.trafikanten.controller.ui.adapter.StationArrayAdapter;
import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.DBManager.Mode;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import no.nith.android.trafikanten.model.*;
import no.nith.android.trafikanten.util.SimpleTimer;
import no.nith.android.trafikanten.util.SimpleTimerTick;
import no.nith.android.trafikanten.util.TimeSpan;
import no.nith.android.trafikanten.util.Toaster;

public class DeparturesActivity extends BaseActivity
{
	private long bseconds = 0;

	private Station currentStation = null;
	private Context context;
	
	private boolean favorited = false;
	private SimpleTimer timer;
	
	private ProgressDialog progress;
	
	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void controller()
	{
		context = this;
		
		final AutoCompleteTextView txtSearch = (AutoCompleteTextView)findViewById(no.nith.android.trafikanten.R.id.txtSearch);
		
		timer = new SimpleTimer(new TimeSpan(0,0,2,0), new SimpleTimerTick()
		{
			
			@Override
			public void onTick()
			{
				timer.stop();
				
				if (currentStation != null)
				{
					if (txtSearch.getText().toString().toLowerCase().equals(currentStation.getName().toLowerCase()))
							return;
				}
				
				progress = ProgressDialog.show(context, "", 
						getResources().getString(R.string.loading_stations));
				try
				{
					TrafikantenApi.findStationsAsync(txtSearch.getText().toString(), 
							new StationHandler(){

								@Override
								public void onParsed(ArrayList<Station> arrayList)
								{
									progress.cancel();
									txtSearch.setAdapter(new StationArrayAdapter(context, arrayList));
									txtSearch.showDropDown();
								}

								@Override
								public void onError(String message)
								{
									progress.cancel();
									Toast errorToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
									errorToast.show();
									
								}
						
					});
				} catch (URISyntaxException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
		txtSearch.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				timer.stop();
				timer.start();
				
			}
		});
		
		txtSearch.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				currentStation = (Station) arg0.getItemAtPosition(arg2);
				checkIfStationIsFavorite();
				refreshDepartures();
				
			}
		});
	}

	@Override
	public void view()
	{
		setLayout("departures");
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    menu.clear();
	    inflater.inflate(no.nith.android.trafikanten.R.menu.station_menu, menu);
	    
	    int addRemoveFavorite = getItemFromMenu(menu, R.string.add_to_favorite);
	    int moreInfo = getItemFromMenu(menu, R.string.more_info);
	    
	    if (addRemoveFavorite != -1)
	    {
	    	if (favorited)
	    	{
	    		menu.getItem(addRemoveFavorite).setTitle(R.string.remove_from_favorites);
	    	}
	    }
	    
	    if (moreInfo != 1)
	    {
	    	if (currentStation != null && currentStation.getName() != null && !currentStation.getName().equals(""))
	    	{
	    		menu.getItem(moreInfo).setEnabled(true);
	    	}
	    }
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.station_menu_refresh:
	        refreshDepartures();
	        return true;
	    case R.id.station_menu_favorite:
	        addRemoveFavorite();
	        return true;
	    case R.id.station_menu_moreInfo:
	    	launchMoreInfo();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void launchMoreInfo()
	{		
		if (currentStation == null) return;
		if (currentStation.getName() == null | currentStation.getName().equals("")) return;
		
		Intent launchFull = new Intent(context, DeparturesFullActivity.class);
		
		launchFull.putExtra("name", currentStation.getName());
		launchFull.putExtra("id", currentStation.getId());
		launchFull.putExtra("zone", currentStation.getZone());
		
		this.startActivity(launchFull);
	}

	private void addRemoveFavorite()
	{
		if (currentStation == null) return;
		if (currentStation.getName() == null || currentStation.getName() == "") return;
		
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE);
			
			// Set the entity that the database should map its results to.
			db.setEntity(FavoriteStation.class);
			
			if (favorited == true)
			{
				db.delete(new FavoriteStation((int)currentStation.getId(), currentStation.getName()));
				setFavorite(false);
				
				Toaster.showToast(this, R.string.favorite_removed, 
						currentStation.getName());
			}
			else
			{
				// Example of inserting an entity:
				db.insert(new FavoriteStation((int)currentStation.getId(), 
						currentStation.getName(), currentStation.getZone()));
				
				setFavorite(true);
				
				Toaster.showToast(this, R.string.favorite_added, currentStation.getName());
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
		if (currentStation == null) return;
		
		final ListView lstStation = findListView("lstStations");
		
		progress = ProgressDialog.show(context, currentStation.getName(),
				getResources().getString(R.string.loading_departures));
		
		TrafikantenApi.getDeparturesAsync(currentStation.getId(), new DepartureHandler()
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
		if (currentStation == null) return;
		if (currentStation.getName() == null || currentStation.getName() == "") return;
		
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE, FavoriteStation.class);
			
			// Example of inserting an entity:
			ArrayList<FavoriteStation> stations = db.select();
			
			setFavorite(false);
			
			for (FavoriteStation fs : stations)
			{
				if (fs.getId() == (int)currentStation.getId())
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
	
	protected DBManager database(Mode mode, Class<?> entityClass) throws Exception {
    	return new DBManager(this, entityClass).open(mode);
    }

}
