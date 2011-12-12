package no.nith.android.trafikanten.controller;

import no.nith.android.trafikanten.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TrafikantenTabActivity extends TabActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_trafikanten);
		
		TabHost host = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		String tab_departures = getResources().getString(R.string.tab_departures);
		String tab_closeby = getResources().getString(R.string.tab_closeby);
		String tab_favorites = getResources().getString(R.string.tab_favorites);
		
		intent = new Intent().setClass(this, DeparturesActivity.class);
		
		spec = host.newTabSpec("departures").setIndicator(tab_departures,
				getResources().getDrawable(R.drawable.ic_tab_departures)).setContent(intent);
		
		host.addTab(spec);
		intent = new Intent().setClass(this, CloseByActivity.class);
		
		spec = host.newTabSpec("closeby").setIndicator(tab_closeby, 
				getResources().getDrawable(R.drawable.ic_tab_closeby)).setContent(intent);
		host.addTab(spec);
		
		intent = new Intent().setClass(this, FavoritesActivity.class);
		spec = host.newTabSpec("favorites").setIndicator(tab_favorites, 
				getResources().getDrawable(R.drawable.ic_tab_favorites)).setContent(intent);
		
		host.addTab(spec);
		
		host.setCurrentTab(0);
		
	}
	
	public void switchTab(int tab)
	{
        getTabHost().setCurrentTab(tab);
	}
}
