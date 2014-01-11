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

public class OHousesList extends Activity {

	String lv_houseName;

	private HousesDbAdapter dbHouses;
	public static final String HOUSE_NAME = "com.anica.rms.houseName";
	public static final String HOUSE_ID = "com.anica.rms.houseID";
	public static final String ROW_ID = "com.anica.rms.rowid";

	public static String SHouseID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ohouses_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ohouses_list, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayHouses();
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

	private void displayHouses() {

		dbHouses = new HousesDbAdapter(this);
		dbHouses.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		Cursor Housescursor = dbHouses
				.fetchDisHousesByUserName(LogIn.S_Username);

		int rec_count = Housescursor.getCount();

		Log.w("DisplayHousesList", "Number of houses found:" + rec_count);

		ListView listView = (ListView) findViewById(R.id.HouseslistView1);

		// Assign adapter to ListView

		OHousesCursorAdapter houselistadapter = new OHousesCursorAdapter(
				getApplicationContext(), Housescursor, 0);

		// listView.setAdapter(dataAdapter);

		listView.setAdapter(houselistadapter);

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
				SHouseID = cursor.getString(cursor
						.getColumnIndexOrThrow("houseID"));

				// create an Intent to launch the ViewMeter Activity
				Intent viewHouseDet = new Intent(OHousesList.this,
						OHouseDisplay.class);

				// pass the selected contact's row ID as an extra with the
				// Intent

				viewHouseDet.putExtra(HOUSE_ID, SHouseID);
				viewHouseDet.putExtra(ROW_ID, rowID);
				startActivity(viewHouseDet); // start the ViewContact Activity
			}
		});
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.addHouseItem:

			// create a new Intent to launch the AddEditHouse Activity

			Intent addNewHouse = new Intent(OHousesList.this, OHouseEdit.class);
			SHouseID = "0";
			addNewHouse.putExtra(HOUSE_ID, "0");
			startActivity(addNewHouse); // start the AddEditContact Activity

			return true;

		case R.id.displayAll:

			dbHouses.open();

			// Cursor cursor = dbHelper.fetchDistnictOrders();

			Cursor Housescursor = dbHouses.fetchAll();

			int rec_count = Housescursor.getCount();

			Log.w("DisplayHousesList", "Number of houses found:" + rec_count);

			ListView listView = (ListView) findViewById(R.id.HouseslistView1);

			// Assign adapter to ListView

			OHousesCursorAdapter houselistadapter = new OHousesCursorAdapter(
					getApplicationContext(), Housescursor, 0);

			// listView.setAdapter(dataAdapter);

			listView.setAdapter(houselistadapter);

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
