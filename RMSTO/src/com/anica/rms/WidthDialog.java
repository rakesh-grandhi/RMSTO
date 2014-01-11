package com.anica.rms;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WidthDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_width_dialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.width_dialog, menu);
		return true;
	}

}
