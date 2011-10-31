package no.nith.android.trafikanten.async;

import java.util.ArrayList;

import no.nith.android.trafikanten.model.Station;

public interface StationHandler
{
	public void onParsed(ArrayList<Station> arrayList);
}
