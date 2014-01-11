package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ORoomDisplay extends Activity {

	TextView TVRoomName;
	TextView TVRoomNickName;

	HouseObj houseobj;
	String SHouseID;
	public static long rowID;
	public static String SRoomName, SRoomNickName, SRoomID;
	private HousesDbAdapter dbRooms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oroom_display);
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.
		DisplayRoom();
	}

	private void DisplayRoom() {

		String SrowID;
		long LrowID;

		Intent RoomDis_intent = getIntent();

		SrowID = RoomDis_intent.getStringExtra(OHousesList.ROW_ID);

		LrowID = Long.parseLong(SrowID);
		rowID = LrowID;

		TVRoomName = (TextView) findViewById(R.id.TV_DR_Room_Name);
		TVRoomNickName = (TextView) findViewById(R.id.TV_DR_Room_Nick_Name);

		houseobj = new HouseObj();
		// SHouseID = OHouseDisplay.SHouseID;

		dbRooms = new HousesDbAdapter(this);
		dbRooms.open();

		Cursor Roomcursor = dbRooms.fetchHousesByRowId(LrowID);

		houseobj.roomName = Roomcursor.getString(Roomcursor
				.getColumnIndex("roomName"));
		TVRoomName.setText(houseobj.roomName);
		SRoomName = houseobj.roomName;

		houseobj.roomNickName = Roomcursor.getString(Roomcursor
				.getColumnIndex("roomNickName"));
		TVRoomNickName.setText(houseobj.roomNickName);
		SRoomNickName = houseobj.roomNickName;

		houseobj.roomID = Roomcursor.getString(Roomcursor
				.getColumnIndex("roomID"));
		SRoomID = houseobj.roomID;

		dbRooms.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oroom_display, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.editRoom:

			// create a new Intent to launch the AddEditHouse Activity
			Intent EditRoom = new Intent(this, ORoomEdit.class);
			startActivity(EditRoom); // start the AddEditContact Activity

			return true;

		case R.id.deleteRoom:

			dbRooms.open();

			dbRooms.deleteRoom(SRoomID);

			ORoomDisplay.this.finish();

			dbRooms.close();

			return true;

		case R.id.DisplayAppl:

			Intent DisplayAppl = new Intent(ORoomDisplay.this,
					Appliance_list.class);
			startActivity(DisplayAppl); // start the AddEditContact Activity

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
