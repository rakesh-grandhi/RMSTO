package com.anica.rms;

import android.database.Cursor;
import android.database.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MatcodeDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_MAKE = "make";
	public static final String KEY_MODELNUM = "modelNum";
	public static final String KEY_MATCODE = "matCode";
	public static final String KEY_DES = "des";
	public static final String KEY_WORKCENTER = "workCenter";
	public static final String KEY_APPLIANCETYPE = "applianceType";
	public static final String KEY_APPLIANCENAME = "applianceName";
	public static final String KEY_AREA = "area";

	private static final String TAG = "MatcodeDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "RMS1";
	private static final String SQLITE_TABLE = "MATCODE";
	private static final int DATABASE_VERSION = 4;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_LEVEL + ","
			+ KEY_AREA + "," + KEY_APPLIANCENAME + "," + KEY_APPLIANCETYPE
			+ "," + KEY_MAKE + "," + KEY_MODELNUM + "," + KEY_MATCODE + ","
			+ KEY_DES + "," + KEY_WORKCENTER + "," + " UNIQUE (" + KEY_ROWID
			+ " , " + KEY_MATCODE + " , " + KEY_APPLIANCENAME + "));";

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

	public MatcodeDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public MatcodeDbAdapter open() throws SQLException {
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

	public Cursor fetchDisMatcodeByApplName(String inputText)
			throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_AREA, KEY_APPLIANCENAME, KEY_APPLIANCETYPE, KEY_MAKE,
				KEY_MODELNUM, KEY_MATCODE, KEY_DES, KEY_WORKCENTER },
				KEY_APPLIANCENAME + " like '%" + inputText + "%'", null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchDesAllApplName(String inputText) throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_AREA, KEY_APPLIANCENAME, KEY_APPLIANCETYPE,
				KEY_MAKE, KEY_MODELNUM, KEY_MATCODE, KEY_DES, KEY_WORKCENTER },
				null, null, KEY_APPLIANCENAME, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void insertSampleData()

	{
		MatCodeObj matcode = new MatCodeObj();

		matcode.applianceName = "Refrigerator";
		matcode.applianceType = "";
		matcode.des = "Light is not Working";
		matcode.matCode = "100";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "HVAC";

		insertMatcode(matcode);

		matcode.applianceName = "Refrigerator";
		matcode.applianceType = "";
		matcode.des = "Cooling is not Working";
		matcode.matCode = "101";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "HVAC";

		insertMatcode(matcode);

		matcode.applianceName = "Microwave";
		matcode.applianceType = "";
		matcode.des = " Not Working";
		matcode.matCode = "201";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "ELECTRICAL";

		insertMatcode(matcode);

		matcode.applianceName = "Microwave";
		matcode.applianceType = "";
		matcode.des = "Light is not working";
		matcode.matCode = "202";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "ELECTRICAL";

		insertMatcode(matcode);

		matcode.applianceName = "Dish Washer";
		matcode.applianceType = "";
		matcode.des = " Not Working";
		matcode.matCode = "301";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "ELECTRICAL";

		insertMatcode(matcode);

		matcode.applianceName = "Dish Washer";
		matcode.applianceType = "";
		matcode.des = "Drying is not working";
		matcode.matCode = "302";
		matcode.modelNum = "ML320";
		matcode.make = "Samsung";
		matcode.workCenter = "";
		matcode.area = "ELECTRICAL";

		insertMatcode(matcode);

	}

	public void insertMatcode(MatCodeObj matcode) {
		ContentValues insertValues = new ContentValues();

		insertValues.put(KEY_AREA, matcode.area);
		insertValues.put(KEY_APPLIANCENAME, matcode.applianceName);
		insertValues.put(KEY_APPLIANCETYPE, matcode.applianceType);
		insertValues.put(KEY_MAKE, matcode.make);
		insertValues.put(KEY_MODELNUM, matcode.modelNum);
		insertValues.put(KEY_MATCODE, matcode.matCode);
		insertValues.put(KEY_DES, matcode.des);
		insertValues.put(KEY_WORKCENTER, matcode.workCenter);

		// open();
		long result = mDb.insert(SQLITE_TABLE, null, insertValues);
		// close();
	}
}
