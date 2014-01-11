package com.anica.rms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity {
	private String TAG = "logIn";
	public static String S_Username;
	private String S_PassWord;
	private String S_PassWord_d = "NOPW";
	private String S_RowIndex_value;

	public final static String EXTRA_USERID = "com.anica.riserinsp.USERID";
	public static String DeviceID;

	public String cblogin = "Y";

	UUID deviceUuid;
	UserDbAdapter dbHelper;
	SharedPreferences prefs;
	EditText ET_UserName;
	EditText ET_PassWord;
	String logins = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		// Load the preference defaults
		PreferenceManager.setDefaultValues(this, R.xml.preference, false);

		DeviceUuid MyDeviceUuid = new DeviceUuid(this.getApplicationContext());
		deviceUuid = MyDeviceUuid.getDeviceUuid();

		DeviceID = deviceUuid.toString();

		// Log.w(TAG, DeviceID);

		ET_UserName = (EditText) findViewById(R.id.EditText01);
		ET_PassWord = (EditText) findViewById(R.id.EditText02);

		prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
		logins = prefs.getString("LOGINS", "NOTFOUND");
		if (logins.equals("STORED")) {

			Log.w("CPD10APP", "User ID and PAssword retrived from memory");
			S_Username = prefs.getString("USER_ID", "NOTFOUND");
			Log.w("CPD10APP", "User ID from memory is:" + S_Username);

			S_PassWord = prefs.getString("PASSWORD", "NOTFOUND");
			ET_UserName.setText(S_Username);
			ET_PassWord.setText(S_PassWord);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}

	public void ButtonLogin(View view) {

		Log.w("CP10APP", "display camera method called");

		S_Username = ET_UserName.getText().toString();
		S_PassWord = ET_PassWord.getText().toString();

		Log.w("CPD10APP", "password entered by user is::" + S_PassWord);

		dbHelper = new UserDbAdapter(this);

		dbHelper.open();

		// Clean all data
		// dbHelper.deleteAllUsers();
		// Add some data
		// dbHelper.insertFewUsers();

		Cursor CruUser = dbHelper.fetchUserByName(S_Username);

		int rec_count = CruUser.getCount();

		Log.w("CPD10APP", "# of rwos retruned" + rec_count);

		if (rec_count > 0)

		{
			int pwIndex = CruUser.getColumnIndex("password");
			int rowIndex = CruUser.getColumnIndex("_id");

			Boolean flagPWNull = CruUser.isNull(pwIndex);

			if (flagPWNull == false) {

				S_PassWord_d = CruUser.getString(pwIndex);
				S_RowIndex_value = CruUser.getString(rowIndex);

				Log.w("CPD10APP", "found the user passowrd in DB:"
						+ S_PassWord_d);

				Log.w("CPD10APP", "password entered by user is::" + S_PassWord);

				if (S_PassWord.equals(S_PassWord_d)) {

					if (cblogin.equals("X")) {

						// prefs =
						// getSharedPreferences("login",Context.MODE_PRIVATE);

						Editor editor = prefs.edit();
						editor.putString("LOGINS", "STORED");
						editor.putString("USER_ID", S_Username);
						editor.putString("PASSWORD", S_PassWord);
						editor.commit();
						Log.w("CPD10APP", "User ID and PAssword set in memory");
					} else {
						Editor editor = prefs.edit();
						editor.clear();
						// editor.putString("LOGINS", "");
						// editor.putString("USER_ID", "");
						// editor.putString("PASSWORD", "");

					}

					CruUser.close(); // close the result cursor
					dbHelper.close(); // close database connection

					Intent intent = new Intent(this, OInitialScreen.class);
					startActivity(intent);

				} else {
					Log.w("CP10APP", "password did not matched");
					LogInTask task = new LogInTask();
					task.execute(S_Username, S_PassWord);

				} // pass word check in database

			} else {
				Log.w("CP10APP", "password is blank");
				LogInTask task = new LogInTask();
				task.execute(S_Username, S_PassWord);

			} // Password is blank

		} else {
			Log.w("CP10APP", "no records found in database");
			LogInTask task = new LogInTask();
			task.execute(S_Username, S_PassWord);

		} // records found

	}

	// Class to Validate login.

	private class LogInTask extends AsyncTask<String, String, String> {

		String result;

		@Override
		protected String doInBackground(String... params) {
			Log.w("LOGIN", "doInBackground");

			try {
				Log.w("LOGIN", "calling login method with:" + params[0] + ":"
						+ params[1]);

				result = loginB(params[0], params[1]);

				Log.w("LOGIN", "Login http call" + result);

			}

			catch (Exception e) {
				// TODO Auto-generated catch block

				Log.w("LOGIN", "password match through network failed");
			}

			return result;

		}

		@Override
		protected void onPostExecute(String result)

		{
			Log.w("CPD10APP", "onPostExecute");

			long userTableRowId = 0;

			LoginObj loginobj = new LoginObj();

			InputStream data;
			String SResult = "";
			String SRole;

			data = new ByteArrayInputStream(result.getBytes());

			try {
				// SResult =
				// XmlPullSQuestionParser.parseQuestion(data,"STATUS");

				loginobj = XMLPullLoginParser.parseOrder(data);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SResult = loginobj.getStatus();
			SRole = loginobj.getRole();

			if (SResult.equals("1")) {

				Log.w("CPD10APP", "Inside DB update");

				if (S_PassWord_d.equals("NOPW")) {

					dbHelper.createUser(S_Username, S_PassWord);
					Log.w("CP10APP",
							"password match through network and created new user"
									+ S_Username + S_PassWord);

				} else {
					userTableRowId = Long.valueOf(S_RowIndex_value);
					dbHelper.updateUser(userTableRowId, S_Username, S_PassWord);
					Log.w("CP10APP",
							"password match through network and updated  user"
									+ userTableRowId + S_Username + S_PassWord);
				}

				dbHelper.close(); // close database connection

				Intent intent = new Intent(LogIn.this, OInitialScreen.class);
				startActivity(intent);
			} else {

				Toast.makeText(getApplicationContext(),
						"User ID and Password Incorrect", Toast.LENGTH_SHORT)
						.show();
			}

		} // on PostExecute

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
		}

	} // end of Background class

	public String loginB(String UID, String PWD) throws Exception

	{

		HttpClient httpclient = new DefaultHttpClient();

		try {

			HttpPost httpPost = new HttpPost(
					"http://anavia.azurewebsites.net/auth/Logon_v2.asp");

			// Add your data

			List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);

			nvp.add(new BasicNameValuePair("USER", UID));

			nvp.add(new BasicNameValuePair("PWD", PWD));

			nvp.add(new BasicNameValuePair("DVID", DeviceID));

			httpPost.setEntity(new UrlEncodedFormEntity(nvp));

			// Create a response handler

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responseBody = httpclient.execute(httpPost, responseHandler);

			Log.w("LOGIN", "Login Call Value" + responseBody);

			return responseBody;

		} finally {

			// When HttpClient instance is no longer needed,

			// shut down the connection manager to ensure

			// immediate deallocation of all system resources

			httpclient.getConnectionManager().shutdown();

		}

	} // end of Login Method

	public void Registeruser(View view) {

		Intent register = new Intent(LogIn.this, Register.class);
		startActivity(register);

	}

	public void ForgotPassword(View view) {

		Intent security = new Intent(LogIn.this, SecurityQ.class);

		EditText ET_UserName = (EditText) findViewById(R.id.EditText01);

		S_Username = ET_UserName.getText().toString();

		security.putExtra(EXTRA_USERID, S_Username);
		startActivity(security);

	}

	public void onCheckboxClicked(View view) { // Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();
		// Check which checkbox was clicked
		switch (view.getId()) {
		case R.id.CBLogin:
			if (checked) {
				cblogin = "X";
				// Log.w ("CP10APP", "Check box checked"+cblogin);
			}

			else {
				cblogin = null;
				// Log.w ("CP10APP", "Check box unchecked"+cblogin);

			}
			break;

		}
	}// end login check box

	public boolean onOptionsItemSelected(MenuItem item) {
		// switch based on the MenuItem id
		switch (item.getItemId()) {
		case R.id.menu_change_password:

			Intent Changepass = new Intent(LogIn.this, PasswordChange.class);
			startActivity(Changepass);

			return true; // consume the menu event

		} // end switch

		return super.onOptionsItemSelected(item); // call super's method
	} // end method onOptionsItemSelected
}// end of class

