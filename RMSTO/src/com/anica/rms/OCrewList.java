package com.anica.rms;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OCrewList extends Activity {

	String lv_crewName;

	private OCrewsDbAdapter dbCrews;

	MatcodeDbAdapter dbMatcodes;
	public static final String CREW_NAME = "com.anica.rms.crewName";
	public static final String CREW_ID = "com.anica.rms.crewID";
	public static final String ROW_ID = "com.anica.rms.rowid";

	public static String SCrewID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocrew_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocrew_list, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayCrews();
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

	private void displayCrews() {

		dbCrews = new OCrewsDbAdapter(this);
		dbCrews.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		Cursor Crewcursor = dbCrews.fetchCrewsByOwnerName(LogIn.S_Username);

		int rec_count = Crewcursor.getCount();

		Log.w("DisplayHousesList", "Number of houses found:" + rec_count);

		ListView listView = (ListView) findViewById(R.id.CrewlistView1);

		// Assign adapter to ListView

		OCrewsCursorAdapter crewlistadapter = new OCrewsCursorAdapter(
				getApplicationContext(), Crewcursor, 0);

		// listView.setAdapter(dataAdapter);

		listView.setAdapter(crewlistadapter);

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
				SCrewID = cursor.getString(cursor
						.getColumnIndexOrThrow("crewID"));

				// create an Intent to launch the ViewMeter Activity
				Intent viewCrewDet = new Intent(OCrewList.this,
						OCrewDisplay.class);

				// pass the selected contact's row ID as an extra with the
				// Intent

				viewCrewDet.putExtra(CREW_ID, SCrewID);
				viewCrewDet.putExtra(ROW_ID, rowID);
				startActivity(viewCrewDet); // start the ViewContact Activity
			}
		});
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.addCrewItem:

			// create a new Intent to launch the AddEditHouse Activity

			Intent addNewCrew = new Intent(OCrewList.this, OCrewEdit.class);
			SCrewID = "0";

			addNewCrew.putExtra(CREW_ID, "0");
			startActivity(addNewCrew); // start the AddEditContact Activity

			return true;

		case R.id.InsertSampleMAT:

			dbMatcodes = new MatcodeDbAdapter(this);

			dbMatcodes.open();

			dbMatcodes.insertSampleData();

			dbMatcodes.close();

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
