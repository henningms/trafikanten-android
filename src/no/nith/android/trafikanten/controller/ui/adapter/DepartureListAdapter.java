package no.nith.android.trafikanten.controller.ui.adapter;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.model.Departure;
import android.content.Context;
import android.view.*;
import android.widget.*;

public class DepartureListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Departure> items;
	private LayoutInflater inflater;
	
	public DepartureListAdapter(Context context, ArrayList<Departure> objects)
	{
		this.context = context;
		items = objects;
		inflater = LayoutInflater.from(context);
		
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
			convertView = inflater.inflate(R.layout.departures_list_row, null);
			
			holder = new ViewHolder();
			
			holder.lblDepartureName = (TextView) convertView.findViewById(R.id.lblDepartureName);
			holder.lblDepartureTime = (TextView) convertView.findViewById(R.id.lblDepartureTime);
			holder.lblDeparturePlatformName = (TextView) convertView.findViewById(R.id.lblDeparturePlatformName);
			holder.lblDepartureLineRef = (TextView) convertView.findViewById(R.id.lblDepartureLineRef);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Departure departure = items.get(position);
		
		String platformName = context.getString(R.string.platformName);
		
		holder.lblDepartureName.setText(departure.getDestinationName());
		holder.lblDepartureTime.setText(departure.getHourMinute());
		holder.lblDeparturePlatformName.setText(String.format(platformName, departure.getDeparturePlatformName()));
		holder.lblDepartureLineRef.setText(departure.getLineRef());
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView lblDepartureTime;
		TextView lblDepartureName;
		TextView lblDeparturePlatformName;
		TextView lblDepartureLineRef;
	}
}
