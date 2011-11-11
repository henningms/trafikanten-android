package no.nith.android.trafikanten.controller.ui.dialog;

import java.util.ArrayList;
import java.util.Date;

import no.nith.android.trafikanten.R;
import no.nith.android.trafikanten.api.TrafikantenApi;
import no.nith.android.trafikanten.async.DepartureHandler;
import no.nith.android.trafikanten.controller.DeparturesFullActivity;
import no.nith.android.trafikanten.model.Departure;
import no.nith.android.trafikanten.model.Stop;
import no.nith.android.trafikanten.util.TimeSpan;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StopDialog
{
	public static void showDialog(final Context context, final Stop stop)
	{
		String message = String.format(context.getString(R.string.walkingDistance), stop.getWalkingDistance());
		
		final Dialog dialog = new Dialog(context);
		
		dialog.setTitle(stop.getName());
		dialog.setContentView(R.layout.dialog_stop);
		
		final TextView lblWalkingDistance = (TextView)dialog.findViewById(R.id.lblStopDialogWalkingDistance);
		final TextView lblDepartures = (TextView)dialog.findViewById(R.id.lblStopDialogDepartures);
		final ProgressBar progressDepartures = (ProgressBar)dialog.findViewById(R.id.progressStopDialogDepartures);
		final TextView lblNextDepartures = (TextView)dialog.findViewById(R.id.lblStopDialogNextDepartures);
		
		final Button btnShowDepartures = (Button)dialog.findViewById(R.id.btnStopDialogShowDepartures);
		final Button btnClose = (Button)dialog.findViewById(R.id.btnStopDialogClose);
		
		btnClose.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				dialog.cancel();
				
			}
		});
		
		btnShowDepartures.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, DeparturesFullActivity.class);
				
				intent.putExtra("name", stop.getName());
				intent.putExtra("zone", stop.getZone());
				intent.putExtra("id", stop.getId());
				
				context.startActivity(intent);
				
			}
		});
		
		lblNextDepartures.setText(context.getString(R.string.loading_departures));
		
		lblWalkingDistance.setText(message);
		
		dialog.show();
		
		TrafikantenApi.getDeparturesAsync(stop.getId(), new DepartureHandler()
		{
			
			@Override
			public void onError(String message)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onParsed(ArrayList<Departure> departures)
			{
				progressDepartures.setVisibility(View.GONE);
				
				lblNextDepartures.setText(context.getString(R.string.nextDeparture));
				
				String message = "";
				
				int maxDepartures = 2;
				
				if (departures.size() > 0)
				{			
					for (int i = 0; i < 2; i++)
					{
						if (departures.get(i) == null) break;
						
						String departure = "";
						
						String name = departures.get(i).getLineRef() + " " + departures.get(i).getDestinationName();
						
						long diff = departures.get(i).getExpectedDepartureTime().getTime() - new Date().getTime();
						long seconds = diff / 1000;
						long minutes = seconds / 60;
						long hours = minutes / 60;
						
						if (hours > 0)
							departure = String.format(context.getString(R.string.departureInHours),
									name, hours);
						else if (minutes > 0)
							departure = String.format(context.getString(R.string.departureInMinutes),
									name, minutes);
						else if (seconds > 0)
							departure = String.format(context.getString(R.string.departureInSeconds),
									name, seconds);
						else
							departure = context.getString(R.string.departureNow);
						
						message += departure + "\n";		
					}
				}
				else
				{
					message = context.getString(R.string.no_departures);
				}
				
				lblDepartures.setText(message);
				
			}
		});
		
	}
}
