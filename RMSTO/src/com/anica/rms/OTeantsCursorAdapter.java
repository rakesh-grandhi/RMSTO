package com.anica.rms;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OTeantsCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public OTeantsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		// do the layout inflation here
		View v = inflater.inflate(R.layout.teant, parent, false);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// do everything else here

		TextView tvRoomName = (TextView) v.findViewById(R.id.teantName);

		String TenantName = c.getString(c.getColumnIndex("teantName"));

		tvRoomName.setText(TenantName);

		/*
		 * TextView TVOrderNum = (TextView) v.findViewById(R.id.ordno); TextView
		 * TVRoute = (TextView) v.findViewById(R.id.route); TextView TVMatcode =
		 * (TextView) v.findViewById(R.id.mat_code_id); TextView TVStartDate =
		 * (TextView) v.findViewById(R.id.start_date); // TextView TVaddress =
		 * (TextView) v.findViewById(R.id.address);
		 * 
		 * ImageView IMHStatus = (ImageView) v.findViewById(R.id.Hstatus);
		 * 
		 * 
		 * 
		 * String SOrderNum = c.getString(c.getColumnIndex("ordernum"));
		 * TVOrderNum.setText(SOrderNum);
		 * 
		 * 
		 * 
		 * 
		 * String SRoute = c.getString(c.getColumnIndex("district"));
		 * TVRoute.setText(SRoute);
		 * 
		 * String SMatcode = c.getString(c.getColumnIndex("enddate"));
		 * TVMatcode.setText(SMatcode);
		 * 
		 * String SStartDate = c.getString(c.getColumnIndex("startdate"));
		 * TVStartDate.setText(SStartDate);
		 * 
		 * // String Saddress = c.getString(c.getColumnIndex("address")); //
		 * TVaddress.setText(Saddress);
		 * 
		 * 
		 * 
		 * // where the magic happens
		 * 
		 * String SIStatus = c.getString(c.getColumnIndex("hstatus"));
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
		 */
	}

}
