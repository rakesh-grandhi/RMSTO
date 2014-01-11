package com.anica.rms;

import android.database.Cursor;
import android.database.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkRequestDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_HOUSEID = "houseID";
	public static final String KEY_HOUSENAME = "houseName";
	public static final String KEY_ROOMID = "roomID";
	public static final String KEY_ROOMNAME = "roomName";
	public static final String KEY_APPLIANCEID = "applianceID";
	public static final String KEY_APPLIANCENAME = "applianceName";
	public static final String KEY_MATCODE = "matCode";
	public static final String KEY_DATE = "date";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_REMARKS = "remarks";

	private static final String TAG = "WorkRequestDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "RMS2";
	private static final String SQLITE_TABLE = "WORKREQ";
	private static final int DATABASE_VERSION = 1;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_HOUSEID + ","
			+ KEY_HOUSENAME + "," + KEY_ROOMID + "," + KEY_ROOMNAME + ","
			+ KEY_APPLIANCEID + "," + KEY_APPLIANCENAME + "," + KEY_MATCODE
			+ "," + KEY_DATE + "," + KEY_USERNAME + "," + KEY_REMARKS + ","
			+ " UNIQUE (" + KEY_ROWID + " , " + KEY_USERNAME + "));";

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

	public WorkRequestDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public WorkRequestDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public boolean deleteAllRec()

	{
		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;
	}

	/*
	 * public Cursor fetchDisMatcodeByApplName(String inputText) throws
	 * SQLException
	 * 
	 * { // Cursor mCursor = null; Cursor mCursor = mDb.query(SQLITE_TABLE, new
	 * String[] {KEY_ROWID, KEY_APPLIANCENAME, KEY_APPLIANCETYPE, KEY_MAKE,
	 * KEY_MODELNUM, KEY_MATCODE, KEY_DES,KEY_WORKCENTER}, KEY_APPLIANCENAME +
	 * " like '%" + inputText + "%'", null, null, null,null,null);
	 * 
	 * if (mCursor != null) { mCursor.moveToFirst(); }
	 * 
	 * return mCursor; }
	 */
}
