package com.anica.rms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OHousesCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public OHousesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		// do the layout inflation here
		View v = inflater.inflate(R.layout.house, parent, false);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// do everything else here

		TextView tvHouseName = (TextView) v.findViewById(R.id.TV_HL_HouseName);

		String SHouseName = c.getString(c.getColumnIndex("houseName"));

		tvHouseName.setText(SHouseName);

		TextView tvhouseID = (TextView) v.findViewById(R.id.TV_HL_HouseID);

		String SHouseID = c.getString(c.getColumnIndex("houseID"));

		tvhouseID.setText(SHouseID);

		TextView tvHouseAddress = (TextView) v
				.findViewById(R.id.TV_HL_HouseAddress);

		String RowID = c.getString(c.getColumnIndex("_id"));

		String RoomID = c.getString(c.getColumnIndex("roomID"));

		String ApplID = c.getString(c.getColumnIndex("applianceID"));

		String RecType = c.getString(c.getColumnIndex("rectype"));

		String SHouseAddress = "Sl #:" + RowID + " Rec Type:" + RecType
				+ "hID:" + SHouseID + " rID:" + RoomID + " aID:" + ApplID;

		String S_HouseNum = c.getString(c.getColumnIndex("houseNum"));
		String S_Street = c.getString(c.getColumnIndex("street"));
		String S_City = c.getString(c.getColumnIndex("city"));
		String S_State = c.getString(c.getColumnIndex("state"));
		String S_Zipcode = c.getString(c.getColumnIndex("zipCode"));

		String S_Address = S_HouseNum + " " + S_Street + " " + S_City + " "
				+ S_State + " " + S_Zipcode;
		tvHouseAddress.setText(S_Address);

	}

}
