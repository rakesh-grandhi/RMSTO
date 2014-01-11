package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OHouseEdit extends Activity {

	TextView TVhousenumID;
	EditText ETHouseName;
	EditText ETHouseNickName;
	EditText ETHouseNum;
	EditText ETHouseStreet;
	EditText ETHouseCity;
	EditText ETHouseState;
	EditText ETHouseZipcode;

	HouseObj houseobj;

	private long LrowID; // id of contact being edited, if any
	private String SrowID; // id of contact being edited, if any
	String SHouseID;

	// EditTexts for contact information

	private HousesDbAdapter dbHouses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ohouse_edit);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ohouse_edit, menu);

		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		DisplayEditHouse();
	}

	private void DisplayEditHouse() {

		SHouseID = OHousesList.SHouseID;

		ETHouseName = (EditText) findViewById(R.id.ETHouseName);
		ETHouseNickName = (EditText) findViewById(R.id.ETHouseNickName);
		ETHouseNum = (EditText) findViewById(R.id.ETHouseNum);
		ETHouseStreet = (EditText) findViewById(R.id.ETStreet);
		ETHouseCity = (EditText) findViewById(R.id.ETCity);
		ETHouseState = (EditText) findViewById(R.id.ETState);
		ETHouseZipcode = (EditText) findViewById(R.id.ETZipcode);
		TVhousenumID = (TextView) findViewById(R.id.TVHouseID);

		houseobj = new HouseObj();

		dbHouses = new HousesDbAdapter(this);
		dbHouses.open();

		if (SHouseID.equals("0")) {

			Cursor Housescursor = dbHouses
					.fetchDisHousesByUserNameall(LogIn.S_Username);
			int rec_count = Housescursor.getCount();

			rec_count = rec_count + 1;

			houseobj.houseID = Integer.toString(rec_count);

			TVhousenumID.setText(houseobj.houseID);

		} else {

			LrowID = OHouseDisplay.rowID;
			Cursor Housescursor = dbHouses.fetchHousesByRowId(LrowID);

			houseobj.houseID = Housescursor.getString(Housescursor
					.getColumnIndex("houseID"));

			TVhousenumID.setText(houseobj.houseID);

			houseobj.houseName = Housescursor.getString(Housescursor
					.getColumnIndex("houseName"));
			ETHouseName.setText(houseobj.houseName);

			houseobj.houseNickName = Housescursor.getString(Housescursor
					.getColumnIndex("houseNickName"));
			ETHouseNickName.setText(houseobj.houseNickName);

			houseobj.houseNum = Housescursor.getString(Housescursor
					.getColumnIndex("houseNum"));
			ETHouseNum.setText(houseobj.houseNum);

			houseobj.street = Housescursor.getString(Housescursor
					.getColumnIndex("street"));
			ETHouseStreet.setText(houseobj.street);

			houseobj.city = Housescursor.getString(Housescursor
					.getColumnIndex("city"));
			ETHouseCity.setText(houseobj.city);

			houseobj.state = Housescursor.getString(Housescursor
					.getColumnIndex("state"));
			ETHouseState.setText(houseobj.state);

			houseobj.zipCode = Housescursor.getString(Housescursor
					.getColumnIndex("zipCode"));
			ETHouseZipcode.setText(houseobj.zipCode);

		}

		dbHouses.close();
	}

	public void SaveHouse(View view) {

		int house_count;

		LrowID = 0;
		houseobj.houseName = ETHouseName.getText().toString();
		houseobj.houseNickName = ETHouseNickName.getText().toString();
		houseobj.houseOwner = LogIn.S_Username;
		houseobj.houseNum = ETHouseNum.getText().toString();
		houseobj.street = ETHouseStreet.getText().toString();
		houseobj.city = ETHouseCity.getText().toString();
		houseobj.state = ETHouseState.getText().toString();
		houseobj.zipCode = ETHouseZipcode.getText().toString();
		/*
		 * houseobj.roomID = ""; houseobj.roomName = ""; houseobj.roomNickName =
		 * ""; houseobj.applianceID = ""; houseobj.applianceName = "";
		 * houseobj.applianceNickName = ""; houseobj.level = ""; houseobj.type =
		 * "";
		 */
		dbHouses.open();

		if (SHouseID.equals("0")) {
			Cursor HouseList = dbHouses
					.fetchDisHousesByUserNameall(LogIn.S_Username);

			if (HouseList != null) {
				house_count = HouseList.getCount() + 1;
				houseobj.houseID = Integer.toString(house_count);
				houseobj.rectype = "H";
				dbHouses.insertHouse(houseobj);
				dbHouses.close();
				OHouseEdit.this.finish();
			}

		} else {
			// houseobj.houseID = SHouseID;

			dbHouses.updateHouseHouseID(LogIn.S_Username, SHouseID, houseobj);
			dbHouses.close();
			OHouseEdit.this.finish();
		}

	}

	public void Cancel(View view) {

		OHouseEdit.this.finish();

	}
}
