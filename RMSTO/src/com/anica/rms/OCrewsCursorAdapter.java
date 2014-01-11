package com.anica.rms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OCrewsCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public OCrewsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		// do the layout inflation here
		View v = inflater.inflate(R.layout.crew, parent, false);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// do everything else here

		TextView tvCrewName = (TextView) v.findViewById(R.id.crewName);

		String CrewName = c.getString(c.getColumnIndex("crewName"));

		tvCrewName.setText(CrewName);

	}

}
