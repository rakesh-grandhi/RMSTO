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

public class OTeantsList extends Activity {

	String lv_teantName;
	private OTeantsDbAdapter dbTeants;
	public static final String TENANT_ID = "com.anica.rms.tenantID";
	public static final String ROW_ID = "com.anica.rms.rowID";
	public static String STenantID;
	public static boolean Gdis_mode = Boolean.FALSE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oteants_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oteants_list, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayTeants();
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

	private void displayTeants() {

		dbTeants = new OTeantsDbAdapter(this);
		dbTeants.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		Cursor Teantcursor = dbTeants.fetchTenantsByOwnerName(LogIn.S_Username);

		int rec_count = Teantcursor.getCount();

		Log.w("DisplayTeantsList", "Number of teants found:" + rec_count);

		ListView listView = (ListView) findViewById(R.id.TeantslistView1);

		// Assign adapter to ListView

		OTeantsCursorAdapter teantlistadapter = new OTeantsCursorAdapter(
				getApplicationContext(), Teantcursor, 0);

		// listView.setAdapter(dataAdapter);

		listView.setAdapter(teantlistadapter);

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

				STenantID = cursor.getString(cursor
						.getColumnIndexOrThrow("tenantID"));

				// create an Intent to launch the ViewMeter Activity
				Intent viewTenant = new Intent(OTeantsList.this,
						OTenantsDisplay.class);

				viewTenant.putExtra(ROW_ID, rowID);

				viewTenant.putExtra(TENANT_ID, STenantID);
				startActivity(viewTenant); // start the ViewContact Activity
			}
		});
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.addTenantItem:

			// create a new Intent to launch the AddEditHouse Activity

			Intent addNewTenant = new Intent(this, OTeantEdit.class);

			STenantID = "0";

			addNewTenant.putExtra(TENANT_ID, "0");
			// addNewTenant.putExtra(ROW_ID, "0");
			startActivity(addNewTenant); // start the AddEditContact Activity

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
