package no.nith.android.trafikanten.model;

import no.nith.android.trafikanten.db.annotation.Entity;
import no.nith.android.trafikanten.db.annotation.Int;
import no.nith.android.trafikanten.db.annotation.PrimaryKey;
import no.nith.android.trafikanten.db.annotation.VarChar;

@Entity(db="favorite_stations_db",table="favorite_stations")
public class FavoriteStation {
	
	@PrimaryKey
	@Int
	private int id;
	
	@VarChar(notNull=true)
	private String name;
	
}
