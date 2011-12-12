package no.nith.android.trafikanten.controller.ui.adapter;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.model.FavoriteStation;
import no.nith.android.trafikanten.model.Stop;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CloseByListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Stop> items;
	private LayoutInflater inflater;
	
	
	public CloseByListAdapter(Context context, ArrayList<Stop> stops)
	{
		this.context = context;
		items = stops;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.closeby_list_row, null);
			
			holder = new ViewHolder();
			
			holder.lblName = (TextView) convertView.findViewById(R.id.lblCloseByListName);
			holder.lblWalkingDistance = (TextView) convertView.findViewById(R.id.lblCloseByListMeters);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Stop stop = items.get(position);
		
		holder.lblName.setText(stop.getName());
		holder.lblWalkingDistance.setText(
				String.format(context.getString(R.string.walkingDistanceShort), stop.getWalkingDistance()));
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView lblWalkingDistance;
		TextView lblName;
	}

}
