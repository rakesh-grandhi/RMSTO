package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OApplianceDisplay extends Activity {

	TextView TVApplName;
	TextView TVApplNickName;
	HouseObj houseobj;
	String SHouseID, ApplID;
	private HousesDbAdapter dbAppl;
	public static long rowID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oappliance_display);
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.
		DisplayAppliance();
	}

	private void DisplayAppliance() {

		String SrowID;
		long LrowID;

		Intent RoomDis_intent = getIntent();

		SrowID = RoomDis_intent.getStringExtra(RoomList.ROW_ID);

		LrowID = Long.parseLong(SrowID);
		rowID = LrowID;

		TVApplName = (TextView) findViewById(R.id.TV_AD_ApplName);
		TVApplNickName = (TextView) findViewById(R.id.TV_AD_ApplNickName);

		houseobj = new HouseObj();

		dbAppl = new HousesDbAdapter(this);
		dbAppl.open();

		Cursor Applcursor = dbAppl.fetchHousesByRowId(LrowID);

		/*
		 * Cursor Applcursor =
		 * dbAppl.fetchApplByApplName(LogIn.S_Username,OHouseDisplay.SHouseID,
		 * RoomList.SRoomID, Appliance_list.SApplID);
		 */
		houseobj.applianceName = Applcursor.getString(Applcursor
				.getColumnIndex("applianceName"));
		TVApplName.setText(houseobj.applianceName);

		houseobj.applianceNickName = Applcursor.getString(Applcursor
				.getColumnIndex("applianceNickName"));
		TVApplNickName.setText(houseobj.applianceNickName);
		
		ApplID = Applcursor.getString(Applcursor.getColumnIndex("applianceID"));
		

		dbAppl.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oappliance_display, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.editAppl:

			// create a new Intent to launch the AddEditHouse Activity
			Intent EditAppl = new Intent(this, OApplianceEdit.class);
			startActivity(EditAppl); // start the AddEditContact Activity

			return true;

		case R.id.deleteAppl:
			
			dbAppl.open();
			
			dbAppl.deleteAppl(ApplID);
			
			OApplianceDisplay.this.finish();
			dbAppl.close();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected
}
