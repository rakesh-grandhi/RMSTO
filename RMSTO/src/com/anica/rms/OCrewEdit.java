package com.anica.rms;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OCrewEdit extends Activity implements OnItemSelectedListener {

	TextView TVCrewID;
	EditText ETCrewName;
	EditText ETCrewNickName;
	static TextView TVStartDate;
	static TextView TVEndDate;
	EditText ETEmail;
	EditText ETStatus;
	EditText ETArea;
	EditText ETPhoneNum;
	EditText ETHouseNum;
	EditText ETStreet;
	EditText ETCity;
	EditText ETState;
	EditText ETZipcode;

	Spinner SPCrewStatus;
	Spinner SPHouseID;
	ImageView IMHStatus;

	public static OCrewObj Crewobj;

	private long LrowID; // id of contact being edited, if any
	private String SrowID; // id of contact being edited, if any
	String SCrewID;

	boolean dis_mode;
	String CrewStatus;
	String CrewID;
	public static String STCrewID;

	// EditTexts for contact information

	private OCrewsDbAdapter dbCrews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocrew_edit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocrew_edit, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayEditCrew();
	}

	private void DisplayEditCrew() {

		long LrowID;

		ETCrewName = (EditText) findViewById(R.id.ET_CE_CrewName);

		ETCrewNickName = (EditText) findViewById(R.id.ET_CE_CrewNickName);

		SPCrewStatus = (Spinner) findViewById(R.id.Status_spinner);

		ETPhoneNum = (EditText) findViewById(R.id.ET_CE_Phone);
		ETEmail = (EditText) findViewById(R.id.ET_CE_Email);
		ETHouseNum = (EditText) findViewById(R.id.ET_CE_HouseNum);
		ETStreet = (EditText) findViewById(R.id.ET_CE_Street);
		ETCity = (EditText) findViewById(R.id.ET_CE_City);
		ETState = (EditText) findViewById(R.id.ET_CE_State);
		ETZipcode = (EditText) findViewById(R.id.ET_CE_Zipcode);

		TVCrewID = (TextView) findViewById(R.id.TV_CE_CrewID);

		Crewobj = new OCrewObj();

		dbCrews = new OCrewsDbAdapter(this);
		dbCrews.open();

		if (OCrewList.SCrewID.equals("0")) {

			Cursor Crewscursor = dbCrews
					.fetchCrewsByOwnerName(LogIn.S_Username);
			int rec_count = Crewscursor.getCount();

			rec_count = rec_count + 1;

			Crewobj.crewID = Integer.toString(rec_count);
			STCrewID = Crewobj.crewID;

			TVCrewID.setText(Crewobj.crewID);

		} else {

			STCrewID = OCrewList.SCrewID;
			LrowID = OCrewDisplay.rowID;
			Cursor Crewcursor = dbCrews.fetchCrewByRowId(LrowID);

			Crewobj.crewID = Crewcursor.getString(Crewcursor
					.getColumnIndex("crewID"));
			TVCrewID.setText(Crewobj.crewID);

			Crewobj.crewName = Crewcursor.getString(Crewcursor
					.getColumnIndex("crewName"));
			ETCrewName.setText(Crewobj.crewName);

			Crewobj.crewNickName = Crewcursor.getString(Crewcursor
					.getColumnIndex("crewNickName"));
			ETCrewNickName.setText(Crewobj.crewNickName);

			Crewobj.startdate = Crewcursor.getString(Crewcursor
					.getColumnIndex("startdate"));
			// TVStartDate.setText(Crewobj.startdate);

			Crewobj.enddate = Crewcursor.getString(Crewcursor
					.getColumnIndex("enddate"));
			// TVEndDate.setText(Crewobj.enddate);

			Crewobj.status = Crewcursor.getString(Crewcursor
					.getColumnIndex("status"));

			Crewobj.phonenum = Crewcursor.getString(Crewcursor
					.getColumnIndex("phonenum"));
			ETPhoneNum.setText(Crewobj.phonenum);

			Crewobj.email = Crewcursor.getString(Crewcursor
					.getColumnIndex("email"));
			ETEmail.setText(Crewobj.email);

			Crewobj.houseNum = Crewcursor.getString(Crewcursor
					.getColumnIndex("houseNum"));
			ETHouseNum.setText(Crewobj.houseNum);

			Crewobj.street = Crewcursor.getString(Crewcursor
					.getColumnIndex("street"));
			ETStreet.setText(Crewobj.street);

			Crewobj.city = Crewcursor.getString(Crewcursor
					.getColumnIndex("city"));
			ETCity.setText(Crewobj.city);

			Crewobj.state = Crewcursor.getString(Crewcursor
					.getColumnIndex("state"));
			ETState.setText(Crewobj.state);

			Crewobj.zipCode = Crewcursor.getString(Crewcursor
					.getColumnIndex("zipCode"));
			ETZipcode.setText(Crewobj.zipCode);

			SPCrewStatus.setSelection(2);
			// SPHouseID.setSelection(1);

			// ETStatus.setText(Crewobj.status);

			SPCrewStatus.setOnItemSelectedListener(OCrewEdit.this);
			// Create an ArrayAdapter using the string array and a default
			// spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.CrewStatus,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			SPCrewStatus.setAdapter(adapter);

			// to populate spinner with values from database.

			ArrayList<String> list;

			list = new ArrayList<String>();
			HousesDbAdapter dbHouses;

			dbHouses = new HousesDbAdapter(this);
			dbHouses.open();

			// Cursor cursor = dbHelper.fetchDistnictOrders();

			Cursor Housescursor = dbHouses
					.fetchDisHousesByUserName(LogIn.S_Username);

			int rec_count = Housescursor.getCount();

			Log.w("DisplayHousesList", "Number of houses found:" + rec_count);

			Housescursor.moveToFirst();
			// String lista = "";
			if (Housescursor.getCount() > 0) {
				while (true) {

					String HouseName = Housescursor.getString(Housescursor
							.getColumnIndex("houseName"));
					String HouseID = Housescursor.getString(Housescursor
							.getColumnIndexOrThrow("houseID"));
					String HouseIDName = HouseID + ":" + HouseName;
					list.add(HouseIDName);
					if (!Housescursor.moveToNext())
						break;
				}

			}

			ArrayAdapter<String> SPadapterHI = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);

			SPadapterHI
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// SPHouseID.setAdapter(SPadapterHI);

			// SPHouseID.setOnItemSelectedListener(OCrewEdit.this);

		}

		dbCrews.close();

	}

	public void SaveCrew(View view) {

		int crew_count;

		LrowID = 0;
		Crewobj.crewName = ETCrewName.getText().toString();
		Crewobj.crewNickName = ETCrewNickName.getText().toString();
		// Crewobj.area = ETArea.getText().toString();
		Crewobj.phonenum = ETPhoneNum.getText().toString();
		Crewobj.email = ETEmail.getText().toString();
		Crewobj.houseNum = ETHouseNum.getText().toString();
		Crewobj.street = ETStreet.getText().toString();
		Crewobj.city = ETCity.getText().toString();
		Crewobj.state = ETState.getText().toString();
		Crewobj.zipCode = ETZipcode.getText().toString();

		Crewobj.houseOwner = LogIn.S_Username;

		// Crewobj.status = ETStatus.getText().toString();
		Crewobj.crewStatus = CrewStatus;

		dbCrews.open();

		if (OCrewList.SCrewID.equals("0")) {

			Cursor Crewscursor = dbCrews
					.fetchCrewsByOwnerName(LogIn.S_Username);
			if (Crewscursor != null) {
				int rec_count = Crewscursor.getCount();

				rec_count = rec_count + 1;

				Crewobj.crewID = Integer.toString(rec_count);

				Crewobj.rectype = "M";

				dbCrews.insertCrew(Crewobj);
				OCrewEdit.this.finish();
			}

		} else {

			dbCrews.updateCrew(OCrewDisplay.rowID, Crewobj);
			OCrewEdit.this.finish();

		}

		// }
	}

	public void Cancel(View view) {

		OCrewEdit.this.finish();

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) { // An item was selected. You can retrieve the selected
						// item using

		if (parent.getId() == SPCrewStatus.getId()) {
			CrewStatus = (String) parent.getItemAtPosition(pos);
		}
		// Log.w("CON", condition);
	}

	public void onNothingSelected(AdapterView<?> parent) { // Another interface
															// callback

	}

}
