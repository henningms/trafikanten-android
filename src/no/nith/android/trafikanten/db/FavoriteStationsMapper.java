package no.nith.android.trafikanten.db;

public class FavoriteStationsMapper extends DBMapper {
	
	public static final String DBNAME = "favorite_stations_db";
	
	public static final String TABLE = "favorite_stations";
	
	public static final String PRIMARY_KEY = "station_id";
	
	public static final String[] columns = {"name"};
	
}
