package no.nith.android.trafikanten.controller;

import java.util.ArrayList;

import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.DBManager.Mode;
import no.nith.android.trafikanten.model.FavoriteStation;
import android.util.Log;

public class DbActivity extends BaseActivity {

	@Override
	public void init() {
	}

	@Override
	public void controller() {
		try {
			// Open a database connection.
			DBManager db = database(Mode.WRITE);
			
			// Set the entity that the database should map its results to.
			db.setEntity(FavoriteStation.class);
			
			// Example of inserting an entity:
			// db.insert(new FavoriteStation(5, "Jernbanetorget"));
			
			// Example of updating an entity:
			// db.update(new FavoriteStation(2, "Tøyen"));
			
			// Example of deleting an entity:
			// db.delete(new FavoriteStation(1, "Lysaker"));
			
			// Example of selecting entities:
			ArrayList<FavoriteStation> stations = db.select();
			
			for (FavoriteStation station : stations) {
				Log.i("DBMANAGER", station.toString());
			}
			
			// Close the database connection after use.
			db.close();
			
		} catch (Exception e) {
			String output = e.getClass().getName() + " ";
			for (StackTraceElement se : e.getStackTrace()) {
				output += se.toString();
			}
			Log.e("DBMANAGER", output);
		}
	}

	@Override
	public void view() {
		setLayout("main");
	}

}
