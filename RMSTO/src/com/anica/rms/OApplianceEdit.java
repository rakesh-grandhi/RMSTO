package com.anica.rms;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class OApplianceEdit extends Activity {

	TextView TVApplID;
	Spinner ETApplName;
	EditText ETApplNickName;
	HouseObj houseobj;
	private long LrowID;
	String SApplID;

	private HousesDbAdapter dbAppl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oappliance_edit);
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.
		DisplayEditAppl();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oappliance_edit, menu);
		return true;
	}

	private void DisplayEditAppl() {

		// Intent HouseEdit_intent = getIntent();
		// SApplID = HouseEdit_intent.getStringExtra(Appliance_list.APPL_ID);

		SApplID = Appliance_list.SApplID;
		ETApplName = (Spinner) findViewById(R.id.ET_RE_ApplName);
		ETApplNickName = (EditText) findViewById(R.id.ET_RE_ApplNickName);
		TVApplID = (TextView) findViewById(R.id.TVappID);

		houseobj = new HouseObj();
		dbAppl = new HousesDbAdapter(this);

		dbAppl.open();

		if (SApplID.equals("0")) {
			// houseobj.houseID = "1";

			Cursor Applcursor = dbAppl.fetchDisApplByRoomNameAll(
					LogIn.S_Username, OHouseDisplay.SHouseID, RoomList.SRoomID);

			int rec_count = Applcursor.getCount();

			rec_count = rec_count + 1;

			houseobj.roomID = Integer.toString(rec_count);

			TVApplID.setText(houseobj.roomID);

		} else {

			// Cursor cursor = dbHelper.fetchDistnictOrders();

			// LrowID = Long.parseLong(SHouseID);
			LrowID = OApplianceDisplay.rowID;
			Cursor Applcursor = dbAppl.fetchHousesByRowId(LrowID);

			// int rec_count = Housescursor.getCount();

			houseobj.applianceName = Applcursor.getString(Applcursor
					.getColumnIndex("applianceName"));
			// ETApplName.setText(houseobj.roomName);

			String[] Appl_List = getResources().getStringArray(
					R.array.ApplianceList);
			/*
			 * for (String s : Appl_List) { int i =
			 * s.indexOf(houseobj.applianceName); if(i >=0 ) {
			 * ETApplName.setSelection(i + 1); break; } }
			 */
			List<String> ApplList = Arrays.asList(Appl_List);
			int index = ApplList.indexOf(houseobj.applianceName);
			ETApplName.setSelection(index + 1);

			houseobj.applianceNickName = Applcursor.getString(Applcursor
					.getColumnIndex("applianceNickName"));
			ETApplNickName.setText(houseobj.applianceNickName);

		}

		dbAppl.close();

	}

	public void saveAppl(View view) {

		dbAppl = new HousesDbAdapter(this);
		dbAppl.open();
		houseobj = new HouseObj();

		houseobj.houseID = OHouseDisplay.SHouseID;
		houseobj.roomID = ORoomDisplay.SRoomID;
		houseobj.roomName = ORoomDisplay.SRoomName;
		houseobj.roomNickName = ORoomDisplay.SRoomNickName;
		houseobj.houseOwner = LogIn.S_Username;
		houseobj.houseName = OHouseDisplay.SHouseName;
		houseobj.houseNickName = OHouseDisplay.SHouseNickName;
		houseobj.houseNum = OHouseDisplay.SHouseNum;
		houseobj.applianceID = Appliance_list.SApplID;
		houseobj.applianceName = ETApplName.getSelectedItem().toString();
		houseobj.applianceNickName = ETApplNickName.getText().toString();
		houseobj.street = OHouseDisplay.SStreet;
		houseobj.city = OHouseDisplay.SCity;
		houseobj.state = OHouseDisplay.SState;
		houseobj.zipCode = OHouseDisplay.SZipcode;

		if (SApplID.equals("0")) {
			Cursor Applcursor = dbAppl.fetchDisApplByRoomNameAll(
					LogIn.S_Username, OHouseDisplay.SHouseID, RoomList.SRoomID);

			if (Applcursor != null) {
				int Appl_count = Applcursor.getCount() + 1;

				// houseobj.houseID = getString(house_count);

				houseobj.applianceID = Integer.toString(Appl_count);
				houseobj.rectype = "A";
				dbAppl.insertAppliance(houseobj);
				dbAppl.close();
				OApplianceEdit.this.finish();
			}

		} else {
			// houseobj.houseID = SHouseID;

			dbAppl.updateAppliance(LogIn.S_Username, OHouseDisplay.SHouseID,
					ORoomDisplay.SRoomID, houseobj.applianceID, houseobj);
			dbAppl.close();
			OApplianceEdit.this.finish();
		}

	}

	public void cancelAppl(View view) {
		OApplianceEdit.this.finish();
	}

}
