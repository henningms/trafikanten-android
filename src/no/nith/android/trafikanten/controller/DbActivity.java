package no.nith.android.trafikanten.controller;

import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.model.FavoriteStation;
import android.util.Log;

public class DbActivity extends BaseActivity {

	@Override
	public void init() {
	}

	@Override
	public void controller() {
		DBManager db;
		try {
			db = database(FavoriteStation.class);
			db.open(DBManager.Mode.WRITE);
			db.close();
		} catch (Exception e) {
			Log.e("DBMANAGER", e.getMessage());
		}
	}

	@Override
	public void view() {
		setLayout("main");
	}

}
