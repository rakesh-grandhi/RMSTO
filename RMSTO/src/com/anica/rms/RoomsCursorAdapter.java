package com.anica.rms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RoomsCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public RoomsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		// do the layout inflation here
		View v = inflater.inflate(R.layout.rooms, parent, false);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// do everything else here

		TextView tvRoomName = (TextView) v.findViewById(R.id.TV_RL_roomName);

		String dbRoomName = c.getString(c.getColumnIndex("roomName"));

		tvRoomName.setText(dbRoomName);

	}

}
