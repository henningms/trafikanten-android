package no.nith.android.trafikanten.async;

import java.util.ArrayList;

import no.nith.android.trafikanten.model.Departure;

public interface DepartureHandler extends BaseHandler
{
	public void onParsed(ArrayList<Departure> departures);
}
