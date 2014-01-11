package com.anica.rms;

import java.util.ArrayList;
import java.util.Calendar;

import com.anica.rms.OTenantsDisplay.DatePickerFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OTeantEdit extends Activity implements OnItemSelectedListener {

	TextView TVtenantID;
	EditText ETTenantName;
	EditText ETTenantNickName;
	static TextView TVStartDate;
	static TextView TVEndDate;

	Spinner SPTenantStatus;
	Spinner SPHouseID;
	ImageView IMHStatus;

	OTenantObj tenantobj;

	private long LrowID; // id of contact being edited, if any
	private String SrowID; // id of contact being edited, if any
	String STenantID;

	boolean dis_mode;
	String TenantStatus;
	String HouseID;

	// EditTexts for contact information

	private OTeantsDbAdapter dbTenants;

	public static String Datepick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oteant_edit);

		dis_mode = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oteant_edit, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayEditTenant();
	}

	private void DisplayEditTenant() {

		// Intent TenantEdit_intent = getIntent();
		// STenantID = TenantEdit_intent.getStringExtra(OTeantsList.TENANT_ID);
		// SrowID = Long.toString(OHouseDisplay.rowID);

		// String SrowID;
		long LrowID;

		// Intent RoomDis_intent = getIntent();

		// SrowID = RoomDis_intent.getStringExtra(OTeantsList.ROW_ID);

		// LrowID = Long.parseLong(SrowID);
		// rowID = LrowID;

		ETTenantName = (EditText) findViewById(R.id.ETTenantName);
		ETTenantName.setEnabled(dis_mode);

		ETTenantNickName = (EditText) findViewById(R.id.ETTenantNickName);
		ETTenantNickName.setEnabled(dis_mode);

		TVStartDate = (TextView) findViewById(R.id.ETStartDate);
		TVStartDate.setEnabled(dis_mode);

		TVEndDate = (TextView) findViewById(R.id.ETEndDate);
		TVEndDate.setEnabled(dis_mode);

		SPTenantStatus = (Spinner) findViewById(R.id.Status_spinner);
		// SPTenantStatus.setEnabled(dis_mode);

		SPHouseID = (Spinner) findViewById(R.id.HouseID_spinner);
		// SPHouseID.setEnabled(dis_mode);

		IMHStatus = (ImageView) findViewById(R.id.IMRentStatus);

		TVtenantID = (TextView) findViewById(R.id.TVTenantID);
		TVtenantID.setEnabled(dis_mode);

		tenantobj = new OTenantObj();

		dbTenants = new OTeantsDbAdapter(this);
		dbTenants.open();

		if (OTeantsList.STenantID.equals("0")) {
			// houseobj.houseID = "1";

			Cursor Tenantscursor = dbTenants
					.fetchTenantsByOwnerName(LogIn.S_Username);
			int rec_count = Tenantscursor.getCount();

			rec_count = rec_count + 1;

			tenantobj.tenantID = Integer.toString(rec_count);

			TVtenantID.setText(tenantobj.tenantID);

		} else {

			LrowID = OTenantsDisplay.rowID;
			Cursor Tenantcursor = dbTenants.fetchTenantByRowId(LrowID);

			// int rec_count = Housescursor.getCount();

			tenantobj.tenantID = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("tenantID"));
			TVtenantID.setText(tenantobj.tenantID);

			tenantobj.tenantName = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("teantName"));
			ETTenantName.setText(tenantobj.tenantName);

			tenantobj.tenantNickName = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("teantNickName"));
			ETTenantNickName.setText(tenantobj.tenantNickName);

			tenantobj.startdate = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("startdate"));
			TVStartDate.setText(tenantobj.startdate);

			tenantobj.enddate = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("enddate"));
			TVEndDate.setText(tenantobj.enddate);

			tenantobj.status = Tenantcursor.getString(Tenantcursor
					.getColumnIndex("status"));

			tenantobj.RecStatus = "A";

			SPTenantStatus.setSelection(2);
			SPHouseID.setSelection(1);
		}

		// ETStatus.setText(tenantobj.status);

		SPTenantStatus.setOnItemSelectedListener(OTeantEdit.this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.TenantStatus,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		SPTenantStatus.setAdapter(adapter);

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

		SPHouseID.setAdapter(SPadapterHI);

		SPHouseID.setOnItemSelectedListener(OTeantEdit.this);

		// where the magic happens
		/*
		 * String RentStatus = c.getString(c.getColumnIndex("hstatus"));
		 * 
		 * // Log.w("ORDERLIST", "Header Status"+SIStatus);
		 * 
		 * if (SIStatus != null && SIStatus.equals("C")) { SImageName =
		 * "small_green"; } else if (SIStatus != null && SIStatus.equals("P")) {
		 * SImageName = "small_yellow" ; }
		 * 
		 * 
		 * int image = context.getResources().getIdentifier(SImageName,
		 * "drawable", context.getPackageName());
		 * IMHStatus.setImageResource(image);
		 * 
		 * }
		 */

		String SImageName = "small_green";

		Context context;

		context = this.getBaseContext();

		int image = context.getResources().getIdentifier(SImageName,
				"drawable", context.getPackageName());
		IMHStatus.setImageResource(image);

		dbTenants.close();

	}

	public void SaveTenant(View view) {

		int house_count;

		LrowID = 0;
		tenantobj.tenantName = ETTenantName.getText().toString();
		tenantobj.tenantNickName = ETTenantNickName.getText().toString();
		tenantobj.houseOwner = LogIn.S_Username;

		tenantobj.startdate = TVStartDate.getText().toString();
		tenantobj.enddate = TVEndDate.getText().toString();
		// tenantobj.status = ETStatus.getText().toString();
		tenantobj.TenantStatus = TenantStatus;
		tenantobj.houseID = HouseID;

		dbTenants.open();

		if (OTeantsList.STenantID.equals("0")) {

			Cursor Tenantscursor = dbTenants
					.fetchTenantsByOwnerName(LogIn.S_Username);
			if (Tenantscursor != null) {
				int rec_count = Tenantscursor.getCount();

				rec_count = rec_count + 1;

				tenantobj.tenantID = Integer.toString(rec_count);

				dbTenants.insertTenant(tenantobj);
				OTeantEdit.this.finish();
			}

		} else {

			dbTenants.updateTenant(OTenantsDisplay.rowID, tenantobj);
			OTeantEdit.this.finish();

		}

		// }
	}

	public void Cancel(View view) {

		OTeantEdit.this.finish();

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) { // An item was selected. You can retrieve the selected
						// item using

		if (parent.getId() == SPHouseID.getId()) {
			HouseID = (String) parent.getItemAtPosition(pos);

		}

		if (parent.getId() == SPTenantStatus.getId()) {
			TenantStatus = (String) parent.getItemAtPosition(pos);
		}
		// Log.w("CON", condition);
	}

	public void onNothingSelected(AdapterView<?> parent) { // Another interface
															// callback

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		public String SDay;
		public String SMonth;
		public String date;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			month++;
			// Log.w("EQPINPUT", "MOnth after Increment"+month);

			if (day < 10)

				SDay = "0" + Integer.toString(day);
			else
				SDay = Integer.toString(day);

			if (month < 10)

				SMonth = "0" + Integer.toString(month);
			else
				SMonth = Integer.toString(month);

			date = Integer.toString(year) + "-" + SMonth + "-" + SDay;

			if (Datepick.equals("SD")) {

				TVStartDate.setText(date);

			}

			if (Datepick.equals("ED")) {

				TVEndDate.setText(date);

			}

		}
	}

	public void showDatePickerDialogSD(View view) {

		// Log.w("CPINPUT", "Date Picker Dialog called");

		Datepick = "SD";
		DialogFragment newFragment;

		newFragment = new DatePickerFragment();

		newFragment.show(getFragmentManager(), "Start Date");
	}

	public void showDatePickerDialogED(View view) {

		// Log.w("CPINPUT", "Date Picker Dialog called");

		Datepick = "ED";
		DialogFragment newFragment;

		newFragment = new DatePickerFragment();

		newFragment.show(getFragmentManager(), "End Date");
	}

}
