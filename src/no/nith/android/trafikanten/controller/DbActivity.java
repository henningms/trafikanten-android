package no.nith.android.trafikanten.controller;

import no.nith.android.trafikanten.db.DBManager;
import no.nith.android.trafikanten.db.FavoriteStationsMapper;

public class DbActivity extends BaseActivity {

	@Override
	public void init() {
	}

	@Override
	public void controller() {
		DBManager db = new DBManager(this, FavoriteStationsMapper.class);
	}

	@Override
	public void view() {
		setLayout("main");
	}

}
