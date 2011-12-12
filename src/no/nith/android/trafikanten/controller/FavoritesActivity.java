package no.nith.android.trafikanten.controller;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.controller.ui.adapter.FavoriteListAdapter;
import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.DBManager.Mode;
import no.nith.android.trafikanten.model.FavoriteStation;
import no.nith.android.trafikanten.util.Toaster;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoritesActivity extends BaseActivity
{
	private ArrayList<FavoriteStation> favorites;
	private Context context;
	
	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void controller()
	{
		context = this;
		
		final ListView lstFavorites = findListView("lstFavorites");
		
		lstFavorites.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				
				Toaster.show(context, lstFavorites.getItemAtPosition(arg2).toString());
			}
		});
		
		lstFavorites.setOnLongClickListener(new OnLongClickListener()
		{
			
			@Override
			public boolean onLongClick(View v)
			{
				Toaster.show(context, "Toast");
				return true;
			}
		});
		
		refreshList();
		
		registerForContextMenu(lstFavorites);

	}

	@Override
	public void view()
	{
		setLayout("favorites");

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) 
	{
	  if (view.getId() == R.id.lstFavorites)
	  {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    
	    menu.setHeaderTitle(favorites.get(info.position).getName());
	    
	    String[] menuItems = getResources().getStringArray(R.array.favorites_context_menu);
	    
	    for (int i = 0; i < menuItems.length; i++) 
	    {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  
	  int menuItemIndex = item.getItemId();
	  
	  String[] menuItems = getResources().getStringArray(R.array.favorites_context_menu);
	  String menuItemName = menuItems[menuItemIndex];
	  FavoriteStation favoriteItem = favorites.get(info.position);
	  
	  try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE, FavoriteStation.class);
			
			// Example of inserting an entity:
			
			// Example of updating an entity:
			// db.update(new FavoriteStation(2, "Tøyen"));
			
			// Example of deleting an entity:
			// db.delete(new FavoriteStation(1, "Lysaker"));
			
			// Example of selecting entities:
			db.delete(favoriteItem);
			
			Toaster.show(this, R.string.favorite_removed, favoriteItem.getName());;
			
			// Close the database connection after use.
			db.close();
			
			refreshList();
			
		} catch (Exception e) {
			Toaster.show(this, R.string.error_db_remove_favorite, favoriteItem.getName());
			
			String output = e.getClass().getName() + " ";
			for (StackTraceElement se : e.getStackTrace()) {
				output += se.toString();
			}
			
			Log.e("DBMANAGER", output);
		}
	  return true;
	}
	
	private void refreshList()
	{
		ListView lstFavorites = findListView("lstFavorites");
		
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE, FavoriteStation.class);
			
			// Example of inserting an entity:
			
			// Example of updating an entity:
			// db.update(new FavoriteStation(2, "Tøyen"));
			
			// Example of deleting an entity:
			// db.delete(new FavoriteStation(1, "Lysaker"));
			
			// Example of selecting entities:
			favorites = db.select();
			
			lstFavorites.setAdapter(new FavoriteListAdapter(this, favorites));
			
			// Close the database connection after use.
			db.close();
			
		} catch (Exception e) {
			Toaster.show(this, R.string.error_db_list_favorites);
			
			String output = e.getClass().getName() + " ";
			for (StackTraceElement se : e.getStackTrace()) {
				output += se.toString();
			}
			Log.e("DBMANAGER", output);
		}
	}
	
	@Override
	protected void onResume()
	{
		refreshList();
		super.onResume();
	}

}
