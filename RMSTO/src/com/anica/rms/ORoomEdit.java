package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ORoomEdit extends Activity {

	TextView TVRoomID;
	EditText ETRoomName;
	EditText ETRoomNickName;
	HouseObj houseobj;

	private HousesDbAdapter dbRooms;
	private long LrowID;
	String SRoomID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oroom_edit);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayEditRoom();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oroom_edit, menu);
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

	private void DisplayEditRoom() {
		// Intent HouseEdit_intent = getIntent();
		// SRoomID = HouseEdit_intent.getStringExtra(RoomList.ROOM_ID);
		SRoomID = RoomList.SRowID;

		ETRoomName = (EditText) findViewById(R.id.ET_RE_RoomName);
		ETRoomNickName = (EditText) findViewById(R.id.ET_RE_RoomNickName);
		TVRoomID = (TextView) findViewById(R.id.TVroomID);

		houseobj = new HouseObj();
		dbRooms = new HousesDbAdapter(this);
		dbRooms.open();

		if (SRoomID.equals("0")) {
			// houseobj.houseID = "1";

			Cursor Roomcursor = dbRooms.fetchDisRoomsByHouseIDAll(
					LogIn.S_Username, OHouseDisplay.SHouseID);

			int rec_count = Roomcursor.getCount();

			rec_count = rec_count + 1;

			houseobj.roomID = Integer.toString(rec_count);

			TVRoomID.setText(houseobj.roomID);

		} else {

			// Cursor cursor = dbHelper.fetchDistnictOrders();

			// LrowID = Long.parseLong(SHouseID);
			LrowID = ORoomDisplay.rowID;
			Cursor Roomcursor = dbRooms.fetchHousesByRowId(LrowID);

			// int rec_count = Housescursor.getCount();

			houseobj.roomName = Roomcursor.getString(Roomcursor
					.getColumnIndex("roomName"));
			ETRoomName.setText(houseobj.roomName);

			houseobj.roomNickName = Roomcursor.getString(Roomcursor
					.getColumnIndex("roomNickName"));
			ETRoomNickName.setText(houseobj.roomNickName);

		}

		dbRooms.close();
	}

	public void saveRoom(View view) {

		houseobj = new HouseObj();

		int room_count;

		houseobj.roomName = ETRoomName.getText().toString();
		houseobj.roomNickName = ETRoomNickName.getText().toString();

		houseobj.houseID = OHouseDisplay.SHouseID;
		houseobj.houseName = OHouseDisplay.SHouseName;
		houseobj.houseNickName = OHouseDisplay.SHouseNickName;
		houseobj.houseOwner = LogIn.S_Username;
		houseobj.houseNum = OHouseDisplay.SHouseNum;
		houseobj.street = OHouseDisplay.SStreet;
		houseobj.city = OHouseDisplay.SCity;
		houseobj.state = OHouseDisplay.SState;
		houseobj.zipCode = OHouseDisplay.SZipcode;

		dbRooms = new HousesDbAdapter(this);
		dbRooms.open();

		if (SRoomID.equals("0")) {
			Cursor RoomList = dbRooms.fetchDisRoomsByHouseIDAll(
					LogIn.S_Username, OHouseDisplay.SHouseID);

			if (RoomList != null) {
				room_count = RoomList.getCount() + 1;

				// houseobj.houseID = getString(house_count);

				houseobj.roomID = Integer.toString(room_count);
				houseobj.rectype = "R";
				dbRooms.insertRoom(houseobj);
				dbRooms.close();
				ORoomEdit.this.finish();
			}

		} else {
			// houseobj.houseID = SHouseID;

			dbRooms.updateRoomRoomID(LogIn.S_Username, OHouseDisplay.SHouseID,
					SRoomID, houseobj);
			dbRooms.close();
			ORoomEdit.this.finish();
		}
	}

	public void cancelRoom(View view) {
		ORoomEdit.this.finish();
	}
}
