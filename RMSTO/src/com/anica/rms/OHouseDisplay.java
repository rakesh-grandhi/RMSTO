package com.anica.rms;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OHouseDisplay extends Activity {

	TextView housenumID;
	TextView TVHouseName;
	TextView TVHouseNickName;
	TextView TVHouseAddress;
	TextView TVHouseID;
	TextView TVTenantName;
	HouseObj houseobj;
	Button BTNCheck;
	Spinner SPTenants;

	// id of contact being edited, if any

	public static long rowID;
	public static String SHouseID, SHouseName, SHouseNickName, SHouseNum,
			SStreet, SCity, SState, SZipcode, S_Address;

	// EditTexts for contact information

	private HousesDbAdapter dbHouses;
	private OTeantsDbAdapter dbTenants;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ohouse_display);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ohouse_display, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayHouse();
	}

	private void DisplayHouse() {

		String SrowID;
		long LrowID;

		
		TVHouseName = (TextView) findViewById(R.id.TV_HD_HouseName);
		TVHouseNickName = (TextView) findViewById(R.id.TV_HD_HouseNickName);
		TVHouseAddress = (TextView) findViewById(R.id.TV_HD_Address);
		BTNCheck = (Button) findViewById(R.id.BT_HD_CheckInOut);
		TVTenantName = (TextView) findViewById(R.id.TV_HD_TenantName);
		
		TVHouseAddress.setMovementMethod(LinkMovementMethod.getInstance());

		houseobj = new HouseObj();

		// Bundle extras = getIntent().getExtras(); // get Bundle of extras

		Intent HouseDis_intent = getIntent();
		SHouseID = HouseDis_intent.getStringExtra(OHousesList.HOUSE_ID);
		SrowID = HouseDis_intent.getStringExtra(OHousesList.ROW_ID);

		// SrowID = extras.getString("HOUSE_ID");

		dbHouses = new HousesDbAdapter(this);
		dbTenants = new OTeantsDbAdapter(this);
		dbHouses.open();

		// Cursor cursor = dbHelper.fetchDistnictOrders();

		LrowID = Long.parseLong(SrowID);
		rowID = LrowID;

		Cursor Housescursor = dbHouses.fetchHousesByRowId(LrowID);

		int rec_count = Housescursor.getCount();

		houseobj.houseID = Housescursor.getString(Housescursor
				.getColumnIndex("houseID"));
		SHouseID = houseobj.houseID;
		// TVHouseID.setText(houseobj.houseID);

		houseobj.houseName = Housescursor.getString(Housescursor
				.getColumnIndex("houseName"));
		TVHouseName.setText(houseobj.houseName);
		SHouseName = houseobj.houseName;

		houseobj.houseNickName = Housescursor.getString(Housescursor
				.getColumnIndex("houseNickName"));
		TVHouseNickName.setText(houseobj.houseNickName);
		SHouseNickName = houseobj.houseNickName;

		houseobj.houseNum = Housescursor.getString(Housescursor
				.getColumnIndex("houseNum"));
		SHouseNum = houseobj.houseNum;

		houseobj.street = Housescursor.getString(Housescursor
				.getColumnIndex("street"));
		// TVHouseStreet.setText(houseobj.houseNum + " " + houseobj.street);
		SStreet = houseobj.street;

		houseobj.city = Housescursor.getString(Housescursor
				.getColumnIndex("city"));
		// TVHouseCity.setText(houseobj.city);
		SCity = houseobj.city;

		houseobj.state = Housescursor.getString(Housescursor
				.getColumnIndex("state"));
		// TVHouseState.setText(houseobj.state);
		SState = houseobj.state;

		houseobj.zipCode = Housescursor.getString(Housescursor
				.getColumnIndex("zipCode"));
		// TVHouseZipcode.setText(houseobj.zipCode);
		SZipcode = houseobj.zipCode;
		
		houseobj.status = Housescursor.getString(Housescursor.getColumnIndex("status"));
		String S_Status = houseobj.status;
		
		if(S_Status.equals("O"))			//Occupied
		{
			BTNCheck.setText("Check Out");
		}
		else if(S_Status.equals("V"))		//Vacant
		{
			BTNCheck.setText("Check In");
		}
			
		S_Address = SHouseNum + " " + SStreet + ", " + SCity + ", " + SState
				+ " " + SZipcode;
		TVHouseAddress.setText(S_Address);
		dbHouses.close();
		
		dbTenants.open();
		
		Cursor Tenantcursor = dbTenants.fetchTenantsByHouseID(houseobj.houseName);
		
		String tenantName;
		
		if(Tenantcursor.getCount() > 0)
		{
			tenantName = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("teantName"));
			
			TVTenantName.setText(tenantName);
		}
		
		dbTenants.close();

		// Intent editHouseDet = new Intent(OHouseDisplay.this,
		// OHouseEdit.class);
		// startActivity(editHouseDet); // start the ViewContact Activity
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.editHouse:

			// create a new Intent to launch the AddEditHouse Activity
			Intent EditHouse = new Intent(OHouseDisplay.this, OHouseEdit.class);
			startActivity(EditHouse); // start the AddEditContact Activity

			return true;

		case R.id.deleteHouse:

			dbHouses.open();

			dbHouses.deleteHouse(SHouseID);

			OHouseDisplay.this.finish();

			dbHouses.close();
			return true;

		case R.id.DisplayRooms:

			Intent Displayrooms = new Intent(OHouseDisplay.this, RoomList.class);
			startActivity(Displayrooms); // start the AddEditContact Activity

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

	public void get_dir(View view) {

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("google.navigation:q=" + S_Address));
		startActivity(intent);
	}
	
	public void CheckIn_CheckOut(View view)
	{
		Intent checkinoutIntent = new Intent(this,OTeantsList.class);
		startActivity(checkinoutIntent);
	}

}
