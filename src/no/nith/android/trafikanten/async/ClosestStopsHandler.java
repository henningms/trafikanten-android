package no.nith.android.trafikanten.async;

import java.util.ArrayList;

import no.nith.android.trafikanten.model.Stop;

public interface ClosestStopsHandler 
{
	public void onParsed(ArrayList<Stop> closestStops);
}
