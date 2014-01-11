package com.anica.rms;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class OCrewDisplay extends Activity {

	TextView TVCrewID;
	EditText ETCrewName;
	TextView TVAddress;
	EditText ETPhoneNum;
	EditText ETEmail;
	EditText ETStatus;
	EditText ETArea;
	EditText ETCrewNickName;
	EditText ETAddress;
	static TextView TVStartDate;
	static TextView TVEndDate;

	Spinner SPTenantStatus;
	// ImageView IMHStatus ;

	public static long rowID;
	public static String SCrewID;

	boolean dis_mode;
	String CrewStatus, S_PhoneNum, S_Email;

	private OCrewsDbAdapter dbCrews;

	OCrewObj crewobj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocrew_display);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ocrew_display, menu);
		return true;
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.

		// Log.w("ORDERLIST", "On Start Called");

		DisplayCrew();
	}

	private void DisplayCrew() {

		String SrowID;
		long LrowID;

		Intent TenantEdit_intent = getIntent();

		SrowID = TenantEdit_intent.getStringExtra(OCrewList.ROW_ID);
		SCrewID = TenantEdit_intent.getStringExtra(OCrewList.CREW_ID);

		LrowID = Long.parseLong(SrowID);
		rowID = LrowID;

		ETCrewName = (EditText) findViewById(R.id.TV_CD_CrewName);
		ETCrewNickName = (EditText) findViewById(R.id.TV_CD_CrewNickName);
		ETAddress = (EditText) findViewById(R.id.TV_CD_Address);
		ETPhoneNum = (EditText) findViewById(R.id.TV_CD_Phone);
		ETEmail = (EditText) findViewById(R.id.TV_CD_Email);

		ETStatus = (EditText) findViewById(R.id.TV_CD_Status);

		crewobj = new OCrewObj();

		dbCrews = new OCrewsDbAdapter(this);
		dbCrews.open();

		Cursor Crewcursor = dbCrews.fetchCrewByRowId(LrowID);

		TVCrewID = (TextView) findViewById(R.id.TV_CD_CrewID);

		crewobj.crewID = Crewcursor.getString(Crewcursor
				.getColumnIndex("crewID"));
		TVCrewID.setText(crewobj.crewID);

		OCrewList.SCrewID = crewobj.crewID;

		crewobj.crewName = Crewcursor.getString(Crewcursor
				.getColumnIndex("crewName"));
		ETCrewName.setText(crewobj.crewName);

		crewobj.crewNickName = Crewcursor.getString(Crewcursor
				.getColumnIndex("crewNickName"));
		ETCrewNickName.setText(crewobj.crewNickName);

		crewobj.phonenum = Crewcursor.getString(Crewcursor
				.getColumnIndex("phonenum"));
		ETPhoneNum.setText(crewobj.phonenum);

		S_PhoneNum = crewobj.phonenum;

		String S_Address = Crewcursor.getString(Crewcursor
				.getColumnIndex("houseNum"))
				+ " "
				+ Crewcursor.getString(Crewcursor.getColumnIndex("street"))
				+ ", "
				+ Crewcursor.getString(Crewcursor.getColumnIndex("city"))
				+ ", "
				+ Crewcursor.getString(Crewcursor.getColumnIndex("state"))
				+ " - "
				+ Crewcursor.getString(Crewcursor.getColumnIndex("zipCode"));

		ETAddress.setText(S_Address);

		int emailIndex = Crewcursor.getColumnIndex("email");

		crewobj.email = Crewcursor.getString(emailIndex);

		ETEmail.setText(crewobj.email);
		S_Email = crewobj.email;

		crewobj.area = Crewcursor.getString(Crewcursor.getColumnIndex("area"));
		// ETArea.setText(crewobj.area);

		crewobj.status = Crewcursor.getString(Crewcursor
				.getColumnIndex("status"));

		ETStatus.setText(crewobj.status);

	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.editCrew:

			Intent EditCrew = new Intent(OCrewDisplay.this, OCrewEdit.class);
			startActivity(EditCrew);
			return true;

		case R.id.deleteCrew:

			dbCrews.open();

			dbCrews.deleteCrew(crewobj.crewID);

			OCrewDisplay.this.finish();

			dbCrews.close();
			return true;

		case R.id.CheckoutTenant:

			return true;

		case R.id.SendSMS:

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

	public void AssignMAT(View view) {

		Intent AssignCrewMAT = new Intent(OCrewDisplay.this, OCrewMAT.class);
		startActivity(AssignCrewMAT); // start the AddEditContact Activity

	}

	public void call_number(View view) {

		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ S_PhoneNum));
		startActivity(phoneIntent);
	}

	public void send_sms(View view) {

		Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
				+ S_PhoneNum));
		startActivity(smsIntent);
	}

	public void send_email(View view) {

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				"mailto", S_Email, null));
		startActivity(emailIntent);
	}

}
