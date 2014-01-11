package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class Appliance_list extends Activity {

	String lv_roomName;

	private HousesDbAdapter houseDbHelper;

	public static final String APPL_ID = "com.anica.rms.applID";
	public static String SApplName, SApplID;
	public static final String ROW_ID = "com.anica.rms.rowid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appliance_list);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appliance_list, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayAppliances();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").

		// Log.w("ORDERLIST", "On Resume Called");

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be
		// "paused").

		// Log.w("ORDERLIST", "On Pause Called");
	}

	@Override
	protected void onStop() {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")

		// Log.w("ORDERLIST", "On stop Called");
		// dbHelper.close();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.w("ORDERLIST", "On Destroy Called");
	}

	private void displayAppliances() {

		// Intent order_intent = getIntent();
		// lv_roomName = order_intent.getStringExtra(RoomList.ROOM_NAME);

		houseDbHelper = new HousesDbAdapter(this);
		houseDbHelper.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		Cursor cursor = houseDbHelper.fetchDisApplByRoomName(LogIn.S_Username,
				OHouseDisplay.SHouseID, ORoomDisplay.SRoomID);

		int rec_count = cursor.getCount();

		ListView listView = (ListView) findViewById(R.id.ApplianceslistView1);

		// Assign adapter to ListView

		AppliancesCursorAdapter applListadapter = new AppliancesCursorAdapter(
				getApplicationContext(), cursor, 0);
		// listView.setAdapter(dataAdapter);

		listView.setAdapter(applListadapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				// Get the Order num from this row in the database.
				String rowID = cursor.getString(cursor
						.getColumnIndexOrThrow("_id"));
				// SApplName = applName;
				SApplID = cursor.getString(cursor
						.getColumnIndexOrThrow("applianceID"));
				SApplName = cursor.getString(cursor
						.getColumnIndexOrThrow("applianceName"));

				// create an Intent to launch the ViewMeter Activity
				Intent viewAppl = new Intent(Appliance_list.this,
						OApplianceDisplay.class);

				// pass the selected contact's row ID as an extra with the
				// Intent
				viewAppl.putExtra(APPL_ID, SApplID);
				viewAppl.putExtra(ROW_ID, rowID);
				startActivity(viewAppl); // start the ViewContact Activity
			}
		});
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.addAppl:

			Intent addNewAppl = new Intent(Appliance_list.this,
					OApplianceEdit.class);
			addNewAppl.putExtra(APPL_ID, "0");
			SApplID = "0";
			startActivity(addNewAppl); // start the AddEditContact Activity

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
