package com.anica.rms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class OInitialScreen extends Activity {

	private HousesDbAdapter dbHelper;
	private ApplDbAdapter dbMatcodeHelper;

	String SUpdatedXml;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oinitial_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oinitial_screen, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.Synch:

			dbHelper = new HousesDbAdapter(this);

			dbHelper.open();

			dbHelper.insertSampleData();

			dbHelper.close();

			dbMatcodeHelper = new ApplDbAdapter(this);
			dbMatcodeHelper.open();
			dbMatcodeHelper.insertSampleData();
			dbMatcodeHelper.close();

			return true;

		case R.id.SynchUp:

			Log.w("INITIALSCR", " SYnch Up called");

			SynchWRTask synchwrtask = new SynchWRTask();
			synchwrtask.execute();

			return true;

		case R.id.UpLoadPic:

			return true;

		case R.id.DeleteData:

			dbHelper = new HousesDbAdapter(this);

			dbHelper.open();

			boolean BResult = dbHelper.deleteAllRec();

			dbHelper.close();

			return true;

		case R.id.Register:

			// Register();
			return true;

		case R.id.UnRegister:

			// UnRegister();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

	public void serReq(View view) {
		Intent intent = new Intent(this, RoomList.class);
		startActivity(intent);
	}

	public void DisHouses(View view) {

		Intent HouseList = new Intent(this, OHousesList.class);
		startActivity(HouseList);

	}

	public void DisTeants(View view) {

		Intent TeantsList = new Intent(this, OTeantsList.class);
		startActivity(TeantsList);

	}

	private class SynchWRTask extends AsyncTask<String, String, String> {

		// CPOrders cpordersL;

		// List<CPOrders> cporderslistL = null;

		private String SHStatus = "N";
		Context context;
		private String Smatcode;

		private SynchDataToServer synchdatatoserver;

		static final String TAG_REMARKS = "COMMENTS";

		/*
		 * public GetOrderListTask(CPOrders cpordersO) { cpordersL = cpordersO;
		 * }
		 */
		@Override
		protected String doInBackground(String... params) {

			String GetorderResult = "0";

			synchdatatoserver = new SynchDataToServer();

			String SUpdatedXml;

			context = getApplicationContext();

			Log.i("ORDERLIST", "doInBackground Synch up");

			// String GetorderResult = "0";
			dbHelper.close();
			dbHelper.open();

			// Cursor CUOrdEqpByUsername =
			// dbHelper.fetchDisOrdersByUserName(Login.S_Username);

			// Cursor CUOrdEqpByUsername =
			// dbHelper.fetchDisOrdersByUserName(LogIn.S_Username);

			try {

				SUpdatedXml = "XML data from database";
				String SResult = SynchDataToServer.UpdateOrder("SRoleID",
						"SProcessID", "SOrderNum", "SOperNum",
						LogIn.S_Username, SHStatus, SUpdatedXml);

				Log.w("ORDERLIST", "Order Delete Check" + "SOrderNum"
						+ SHStatus + SResult);

				if (SResult.equals("<STATUS>1</STATUS>")
						&& SHStatus.equals("C")) {
					// boolean BResult = dbHelper.deleteOrder(SOrderNum);

					// Log.w("ORDERLIST", "Order Delete Result"+BResult);
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return GetorderResult;

		}

		@Override
		protected void onPostExecute(String result)

		{
			Log.w("ORDERLIST", "onPostExecute Save");

			// displayListView();

		} // on PostExecute

		@Override
		protected void onPreExecute() {
			// Log.i(TAG, "onPreExecute");
		}

	}// end of class SvaeOrderListTask

	public void ServPers(View view) {

		Intent CrewList = new Intent(this, OCrewList.class);
		startActivity(CrewList);

	}

}
