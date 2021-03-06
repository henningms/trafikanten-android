package no.nith.android.trafikanten.model;

import no.nith.android.trafikanten.db.annotation.Entity;
import no.nith.android.trafikanten.db.annotation.Int;
import no.nith.android.trafikanten.db.annotation.PrimaryKey;
import no.nith.android.trafikanten.db.annotation.VarChar;

@Entity(table="favorite_stations")
public class FavoriteStation {
	
	@PrimaryKey
	@Int
	private Integer id;
	
	@VarChar(notNull=true)
	private String name;
	
	@VarChar(notNull=true)
	private String zone;
	
	public FavoriteStation() {
		
	}
	
	public FavoriteStation(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public FavoriteStation(Integer id, String name, String zone) {
		this.id = id;
		this.name = name;
		this.zone = zone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getZone() {
		return zone;
	}
	
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%d)", getName(), getId());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FavoriteStation)) {
			return false;
		}
		if (o == this) {
			return true;
		}
		FavoriteStation fs = (FavoriteStation) o;
		
		return getId() == fs.getId();
	}
	
}
