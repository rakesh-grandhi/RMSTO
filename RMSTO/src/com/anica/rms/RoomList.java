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

public class RoomList extends Activity {

	String lv_roomName;
	private HousesDbAdapter dbHelper;
	public static final String ROOM_NAME = "com.anica.rms.roomName";
	public static final String ROOM_ID = "com.anica.rms.houseID";
	public static final String ROW_ID = "com.anica.rms.rowid";
	public static String SRoomID, SRowID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_list, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		displayRooms();
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

	private void displayRooms() {

		dbHelper = new HousesDbAdapter(this);
		dbHelper.open();

		// SHouseID = Long.toString(OHouseDisplay.SHouseID);

		Cursor cursor = dbHelper.fetchDisRoomsByHouseID(LogIn.S_Username,
				OHouseDisplay.SHouseID);

		int rec_count = cursor.getCount();

		ListView listView = (ListView) findViewById(R.id.RoomslistView1);

		// Assign adapter to ListView

		RoomsCursorAdapter roomlistadapter = new RoomsCursorAdapter(
				getApplicationContext(), cursor, 0);

		// listView.setAdapter(dataAdapter);

		listView.setAdapter(roomlistadapter);

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
				SRoomID = cursor.getString(cursor
						.getColumnIndexOrThrow("roomID"));
				SRowID = rowID;
				// SRoomName = roomID;

				// create an Intent to launch the ViewMeter Activity
				// Intent viewAppliances = new Intent(RoomList.this,
				// Appliance_list.class);
				Intent displayRoom = new Intent(RoomList.this,
						ORoomDisplay.class);
				// pass the selected contact's row ID as an extra with the
				// Intent

				// viewAppliances.putExtra(ROOM_NAME, roomName);
				displayRoom.putExtra(ROOM_ID, SRoomID);
				displayRoom.putExtra(ROW_ID, rowID);
				startActivity(displayRoom); // start the ViewContact Activity
			}
		});
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.addNewRoom:
			Intent AddEditRoom = new Intent(this, ORoomEdit.class);
			AddEditRoom.putExtra(ROOM_ID, "0");
			SRowID = "0";
			startActivity(AddEditRoom);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
