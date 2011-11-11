package no.nith.android.trafikanten.controller.ui.adapter;

import java.util.ArrayList;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.DBManager.Mode;
import no.nith.android.trafikanten.model.Departure;
import no.nith.android.trafikanten.model.FavoriteStation;
import no.nith.android.trafikanten.util.Toaster;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class FavoriteListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<FavoriteStation> items;
	private LayoutInflater inflater;
	
	public FavoriteListAdapter(Context context, ArrayList<FavoriteStation> items)
	{
		this.context = context;
		this.items = items;
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
			convertView = inflater.inflate(R.layout.favorites_list_row, null);
			
			holder = new ViewHolder();
			
			holder.lblName = (TextView) convertView.findViewById(R.id.lblFavoriteStationName);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final FavoriteStation station = items.get(position);
		
		holder.lblName.setText(station.getName());
		
		return convertView;
	}
	
	private void notifyOfChange()
	{
		notifyDataSetChanged();
		
	}
	
	private static class ViewHolder
	{
		TextView lblName;
	}

}
