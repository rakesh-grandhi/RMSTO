package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplDbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MAKE = "make";
	public static final String KEY_MODELNUM = "modelNum";
	public static final String KEY_MATCODE = "matCode";
	public static final String KEY_DES = "des";
	public static final String KEY_WORKCENTER = "workCenter";
	public static final String KEY_APPLIANCETYPE = "applianceType";
	public static final String KEY_APPLIANCENAME = "applianceName";

	private static final String TAG = "ApplDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "RMS1";
	private static final String SQLITE_TABLE = "MATCODE";
	private static final int DATABASE_VERSION = 4;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_APPLIANCENAME + ","
			+ KEY_APPLIANCETYPE + "," + KEY_MAKE + "," + KEY_MODELNUM + ","
			+ KEY_MATCODE + "," + KEY_DES + "," + KEY_WORKCENTER + ","
			+ " UNIQUE (" + KEY_ROWID + " , " + KEY_MATCODE + " , "
			+ KEY_APPLIANCENAME + "));";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)

		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);

			onCreate(db);
		}
	}

	public ApplDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public ApplDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long createAppliance(ApplianceObj appliance) {

		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_APPLIANCENAME, appliance.applianceName);
		initialValues.put(KEY_APPLIANCETYPE, appliance.applianceType);
		initialValues.put(KEY_MAKE, appliance.make);
		initialValues.put(KEY_MODELNUM, appliance.modelNum);
		initialValues.put(KEY_MATCODE, appliance.matCode);
		initialValues.put(KEY_DES, appliance.des);
		initialValues.put(KEY_WORKCENTER, appliance.workCenter);

		return mDb.insert(SQLITE_TABLE, null, initialValues);

	}

	public boolean deleteAllRec()

	{
		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;
	}

	public void insertSampleData()

	{

		ApplianceObj appliance = new ApplianceObj();

		appliance.applianceName = "Refrigirator";
		appliance.applianceType = "Refrigirator";
		appliance.make = "Samsung";
		appliance.modelNum = "123";
		appliance.matCode = "R1";
		appliance.des = "Not powering on";
		appliance.workCenter = "";

		createAppliance(appliance);

		appliance.applianceName = "Refrigirator";
		appliance.applianceType = "Refrigirator";
		appliance.make = "Samsung";
		appliance.modelNum = "123";
		appliance.matCode = "R2";
		appliance.des = "Water leaking";
		appliance.workCenter = "";

		createAppliance(appliance);

	}
}
