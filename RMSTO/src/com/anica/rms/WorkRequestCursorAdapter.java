package com.anica.rms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkRequestCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public WorkRequestCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		// do the layout inflation here
		View v = inflater.inflate(R.layout.matcodes, parent, false);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// do everything else here

		/*
		 * TextView tvDesc = (TextView) v.findViewById(R.id.desc); TextView
		 * tvMatcode = (TextView) v.findViewById(R.id.mat_code_id);
		 * 
		 * String dbDesc = c.getString(c.getColumnIndex("des")); String
		 * dbMatcode = c.getString(c.getColumnIndex("matCode"));
		 * 
		 * tvDesc.setText(dbDesc); tvMatcode.setText(dbMatcode);
		 */

	}

}
