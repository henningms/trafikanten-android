package no.nith.android.trafikanten.controller.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.model.Station;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class StationArrayAdapter extends BaseAdapter implements Filterable
{
	private ArrayList<Station> items;
	private LayoutInflater inflater;
	private Filter filter;
	
	public StationArrayAdapter(Context context, ArrayList<Station> objects)
	{
		items = objects;
		inflater = LayoutInflater.from(context);
		
		filter = new StationFilter();
	}
	
	public int getCount()
	{
		return items.size();
	}
	
	public Object getItem(int position)
	{
		return items.get(position);
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.station_autocomplete_row, null);
			
			holder = new ViewHolder();
			
			holder.lblName = (TextView) convertView.findViewById(R.id.lblName);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Station station = items.get(position);
		
		holder.lblName.setText(station.getName());
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView lblName;
	}

	@Override
	public Filter getFilter()
	{
		if (filter == null)
			filter = new StationFilter();
		
		return filter;
	}
	
	private class StationFilter extends Filter 
	{
        @Override
        protected FilterResults performFiltering(CharSequence prefix) 
        {
        	prefix = prefix.toString().toLowerCase();
            FilterResults results = new FilterResults();
            
            if (prefix != null && prefix.toString().length() > 0)
            {
            	ArrayList<Station> tmpStations = new ArrayList<Station>();
            	ArrayList<Station> tmpList = new ArrayList<Station>();
            	
            	synchronized (this)
            	{
            		tmpList.addAll(items);
            	}
            	
            	for (int i = 0; i < tmpList.size(); i++)
            	{
            		if (tmpList.get(i).getName().toLowerCase().contains(prefix))
            			tmpStations.add(tmpList.get(i));
            	}
            	
            	results.values = tmpStations;
            	results.count = tmpStations.size();    	
            }
            else
            {
            	synchronized(this)
            	{
            		items.clear();
            		results.values = items;
            		results.count = items.size();
            	}
            }
            
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            ArrayList<Station> filteredStations = (ArrayList<Station>) results.values;
            
            if (filteredStations != null && filteredStations.size() > 0)
            	notifyDataSetChanged();
            else
            	notifyDataSetInvalidated();
        }
	}
	
}
