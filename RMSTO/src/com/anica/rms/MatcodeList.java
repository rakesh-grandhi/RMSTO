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

public class MatcodeList extends Activity {
	private MatcodeDbAdapter dbHelper;
	String lv_applName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matcode_list);
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
		getMenuInflater().inflate(R.menu.matcode_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayMatcodes();
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

	private void displayMatcodes() {

		// Intent order_intent = getIntent();
		// lv_applName = order_intent.getStringExtra(Appliance_list.SApplName;
		dbHelper = new MatcodeDbAdapter(this);
		dbHelper.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		Cursor cursor = dbHelper
				.fetchDisMatcodeByApplName(Appliance_list.SApplName);

		int rec_count = cursor.getCount();

		Log.w("MatcodeList", "Number of order found:" + rec_count);

		ListView listView = (ListView) findViewById(R.id.MatcodelistView1);

		// Assign adapter to ListView

		MatcodeCursorAdapter matcodeListAdapter = new MatcodeCursorAdapter(
				getApplicationContext(), cursor, 0);

		// listView.setAdapter(dataAdapter);

		listView.setAdapter(matcodeListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				// Get the Order num from this row in the database.
				String matCodeId = cursor.getString(cursor
						.getColumnIndexOrThrow("matCode"));

				// create an Intent to launch the ViewMeter Activity
				Intent viewWorkRequest = new Intent(MatcodeList.this,
						WorkRequest.class);

				// pass the selected contact's row ID as an extra with the
				// Intent

				// viewEquipment.putExtra(SECTION, section );
				startActivity(viewWorkRequest); // start the ViewContact
												// Activity
			}
		});
	}
}
