package com.anica.rms;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OCrewMAT extends ListActivity {

	private MatcodeDbAdapter dbMatcodes;
	private OCrewsDbAdapter dbCrews;

	public ArrayList<String> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		Window window = getWindow();

		window.requestFeature(9);

		dbMatcodes = new MatcodeDbAdapter(this);
		dbCrews = new OCrewsDbAdapter(this);

		dbMatcodes.open();

		list = new ArrayList<String>();

		Cursor ApplNamecursor = dbMatcodes
				.fetchDesAllApplName(LogIn.S_Username);

		ApplNamecursor.moveToFirst();

		if (ApplNamecursor.getCount() > 0) {
			while (true) {

				String ApplName = ApplNamecursor.getString(ApplNamecursor
						.getColumnIndex("applianceName"));

				list.add("APPL:" + ApplName);

				Cursor Matcodecursor = dbMatcodes
						.fetchDisMatcodeByApplName(ApplName);

				Matcodecursor.moveToFirst();
				// String lista = "";
				if (Matcodecursor.getCount() > 0) {
					while (true) {

						String Matcode = Matcodecursor.getString(Matcodecursor
								.getColumnIndex("matCode"));
						String MatcodeDes = Matcodecursor
								.getString(Matcodecursor
										.getColumnIndexOrThrow("des"));

						String CodeDes = "MATC:" + Matcode + "-" + MatcodeDes;
						list.add(CodeDes);
						if (!Matcodecursor.moveToNext())
							break;
					}// end of mat code for given appliance

				} // Mat code is preent

				if (!ApplNamecursor.moveToNext())
					break;
			} // end of Appl list
		}// Appl is present

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, list));

		final ListView listView = getListView();

		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	private static final String[] GENRES = new String[] { "Action",
			"Adventure", "Animation", "Children", "Comedy", "Documentary",
			"Drama", "Foreign", "History", "Independent", "Romance", "Sci-Fi",
			"Television", "Thriller" };

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Make the newly clicked item the currently selected one.
		// getListView().setItemChecked(position, true);

		boolean itemStatus = getListView().isItemChecked(position);

		getListView().setItemChecked(position, itemStatus);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crewmat, menu);
		return true;
	}

	// handle choice from options menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) // switch based on selected MenuItem's ID
		{
		case R.id.Save:

			dbCrews.open();

			dbCrews.deleteCrewMAT(OCrewEdit.STCrewID, "S");

			long[] LlistofCheked = getListView().getCheckedItemIds();

			for (int i = 0; i < LlistofCheked.length; i++) {

				String Lablel = list.get(i);

				if (Lablel.startsWith("APPL:")) {
					String ApplianceName = Lablel.substring(5);

					OCrewEdit.Crewobj.applianceName = ApplianceName;
					OCrewEdit.Crewobj.rectype = "S";
					OCrewEdit.Crewobj.level = "1";
					dbCrews.insertCrew(OCrewEdit.Crewobj);

				} else {
					String Matcode = Lablel.substring(5, 8);

					OCrewEdit.Crewobj.matcode = Matcode;
					OCrewEdit.Crewobj.rectype = "S";
					OCrewEdit.Crewobj.level = "1";
					dbCrews.insertCrew(OCrewEdit.Crewobj);
				}

			}

			dbCrews.close();

			OCrewMAT.this.finish();

			return true;

		case R.id.Cancel:

			OCrewMAT.this.finish();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		} // end switch
	} // end method onOptionsItemSelected

}
