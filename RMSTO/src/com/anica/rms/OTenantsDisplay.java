package com.anica.rms;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OTenantsDisplay extends Activity implements OnItemSelectedListener {

	TextView TVtenantID;
	EditText ETTenantName;
	EditText ETTenantNickName;
	EditText ETStartDate;
	EditText ETEndDate;
	static TextView TVStartDate;
	static TextView TVEndDate;
	EditText ETHouseID;
	Spinner SPTenantStatus;
	ImageView IMHStatus;

	public static long rowID;
	public static String STenantID;

	boolean dis_mode;
	String TenantStatus;

	private OTeantsDbAdapter dbTenants;

	OTenantObj tenantobj;

	public static String Datepick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_otenants_display);

		dis_mode = OTeantsList.Gdis_mode;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.otenants_display, menu);
		return true;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) { // An item was selected. You can retrieve the selected
						// item using
		TenantStatus = (String) parent.getItemAtPosition(pos);
		// Log.w("CON", condition);
	}

	public void onNothingSelected(AdapterView<?> parent) { // Another interface
															// callback

	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayTenant();
	}

	private void DisplayTenant() {

		String SrowID;
		long LrowID;

		Intent TenantEdit_intent = getIntent();

		SrowID = TenantEdit_intent.getStringExtra(OTeantsList.ROW_ID);
		STenantID = TenantEdit_intent.getStringExtra(OTeantsList.TENANT_ID);

		LrowID = Long.parseLong(SrowID);
		rowID = LrowID;

		ETTenantName = (EditText) findViewById(R.id.ETTenantName);
		ETTenantName.setEnabled(dis_mode);

		ETTenantNickName = (EditText) findViewById(R.id.ETTenantNickName);
		ETTenantNickName.setEnabled(dis_mode);

		TVStartDate = (TextView) findViewById(R.id.ETStartDate);
		TVStartDate.setEnabled(dis_mode);

		TVEndDate = (TextView) findViewById(R.id.ETEndDate);
		TVEndDate.setEnabled(dis_mode);

		ETHouseID = (EditText) findViewById(R.id.ETHouseID);
		ETHouseID.setEnabled(dis_mode);

		SPTenantStatus = (Spinner) findViewById(R.id.Status_spinner);
		SPTenantStatus.setEnabled(dis_mode);

		IMHStatus = (ImageView) findViewById(R.id.IMRentStatus);

		TVtenantID = (TextView) findViewById(R.id.TVTenantID);
		TVtenantID.setEnabled(dis_mode);

		tenantobj = new OTenantObj();

		dbTenants = new OTeantsDbAdapter(this);
		dbTenants.open();

		Cursor Tenantcursor = dbTenants.fetchTenantByRowId(LrowID);

		tenantobj.tenantID = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("tenantID"));
		TVtenantID.setText(tenantobj.tenantID);

		tenantobj.tenantName = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("teantName"));
		ETTenantName.setText(tenantobj.tenantName);

		tenantobj.tenantNickName = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("teantNickName"));
		ETTenantNickName.setText(tenantobj.tenantNickName);

		tenantobj.houseID = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("houseID"));
		ETHouseID.setText(tenantobj.houseID);

		tenantobj.startdate = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("startdate"));
		TVStartDate.setText(tenantobj.startdate);

		tenantobj.enddate = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("enddate"));
		TVEndDate.setText(tenantobj.enddate);

		tenantobj.status = Tenantcursor.getString(Tenantcursor
				.getColumnIndex("status"));
		SPTenantStatus.setSelection(2);
		// ETStatus.setText(tenantobj.status);

		SPTenantStatus.setOnItemSelectedListener(OTenantsDisplay.this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.TenantStatus,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		SPTenantStatus.setAdapter(adapter);

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
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.EditTenant:

			OTeantsList.Gdis_mode = true;
			OTenantsDisplay.this.finish();
			startActivity(getIntent());

			// create a new Intent to launch the AddEditHouse Activity
			// Intent EditTenant = new Intent(OTenantsDisplay.this,
			// OTeantEdit.class);
			// startActivity(EditTenant); // start the AddEditContact Activity

			return true;

		case R.id.Delete:

			dbTenants.open();

			dbTenants.deleteTenant(tenantobj.tenantID);

			OTenantsDisplay.this.finish();

			dbTenants.close();
			return true;

		case R.id.CheckoutTenant:

			return true;

		case R.id.SendSMS:

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

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

	public void Edit(View view) {

		OTeantsList.Gdis_mode = true;

		Intent EditTenant = new Intent(OTenantsDisplay.this, OTeantEdit.class);

		// EditTenant.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// EditTenant.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(EditTenant);
		finish();

	}

	public void Delete(View view) {

		dbTenants.open();

		dbTenants.deleteTenant(tenantobj.tenantID);

		OTenantsDisplay.this.finish();

		dbTenants.close();

	}

	public void SendSMS(View view) {

	}

}
