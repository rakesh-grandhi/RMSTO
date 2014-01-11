package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PasswordChange extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_change, menu);
		return true;
	}

}
