package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OCrewsDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_RECTYPE = "rectype";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_CREWID = "crewID";
	public static final String KEY_CREWNAME = "crewName";
	public static final String KEY_CREWNICKNAME = "crewNickName";
	public static final String KEY_COMPANYNAME = "crewCompanyName";
	public static final String KEY_AREA = "area";
	public static final String KEY_HOUSEOWNER = "houseOwner";
	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	public static final String KEY_STATUS = "status";
	public static final String KEY_PHONENUM = "phonenum";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_ADDRESSID = "addressID";
	public static final String KEY_HOUSENUM = "houseNum";
	public static final String KEY_STREET = "street";
	public static final String KEY_CITY = "city";
	public static final String KEY_STATE = "state";
	public static final String KEY_ZIPCODE = "zipCode";
	public static final String KEY_MATCODE = "matCode";
	public static final String KEY_APPLIANCENAME = "applianceName";
	public static final String KEY_WORKCENTER = "workCenter";

	private static final String TAG = "CrewDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "CREW";
	private static final String SQLITE_TABLE = "CREWLIST";
	private static final int DATABASE_VERSION = 5;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_CREWID + ","
			+ KEY_RECTYPE + "," + KEY_LEVEL + "," + KEY_CREWNAME + ","
			+ KEY_CREWNICKNAME + "," + KEY_COMPANYNAME + "," + KEY_AREA + "," + KEY_HOUSEOWNER + ","
			+ KEY_ADDRESSID + "," + KEY_STARTDATE + "," + KEY_ENDDATE + ","
			+ KEY_STATUS + "," + KEY_PHONENUM + "," + KEY_EMAIL + ","
			+ KEY_HOUSENUM + "," + KEY_STREET + "," + KEY_CITY + ","
			+ KEY_STATE + "," + KEY_ZIPCODE + "," + KEY_MATCODE + ","
			+ KEY_APPLIANCENAME + "," + KEY_WORKCENTER + ","

			+ " UNIQUE (" + KEY_ROWID + " , " + KEY_CREWID + "));";

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

	public OCrewsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public OCrewsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long insertCrew(OCrewObj crew) {

		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_CREWID, crew.crewID);
		initialValues.put(KEY_RECTYPE, crew.rectype);
		initialValues.put(KEY_LEVEL, crew.level);
		initialValues.put(KEY_AREA, crew.area);
		initialValues.put(KEY_CREWNAME, crew.crewName);
		initialValues.put(KEY_CREWNICKNAME, crew.crewNickName);
		initialValues.put(KEY_COMPANYNAME,crew.crewCompanyName);
		initialValues.put(KEY_HOUSEOWNER, crew.houseOwner);
		initialValues.put(KEY_ADDRESSID, crew.addressID);
		initialValues.put(KEY_STARTDATE, crew.startdate);
		initialValues.put(KEY_ENDDATE, crew.enddate);
		initialValues.put(KEY_STATUS, crew.status);
		initialValues.put(KEY_PHONENUM, crew.phonenum);
		initialValues.put(KEY_EMAIL, crew.email);
		initialValues.put(KEY_HOUSENUM, crew.houseNum);
		initialValues.put(KEY_STREET, crew.street);
		initialValues.put(KEY_CITY, crew.city);
		initialValues.put(KEY_STATE, crew.state);
		initialValues.put(KEY_ZIPCODE, crew.zipCode);
		initialValues.put(KEY_MATCODE, crew.matcode);
		initialValues.put(KEY_WORKCENTER, crew.workcenter);
		initialValues.put(KEY_APPLIANCENAME, crew.applianceName);

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
		HouseObj house = new HouseObj();
		/*
		 * house.houseID = "1"; house.houseName = "Bay House";
		 * house.houseNickName = "Bay House"; house.houseNum = "10142";
		 * house.street = "Black Mountain Rd"; house.city = "San Diego";
		 * house.state = "CA"; house.zipCode = "92126"; house.roomID = "1";
		 * house.roomName = "Bedroom"; house.roomNickName = "Master Bedroom";
		 * house.type = "Bedroom"; house.level = "1"; house.applianceName =
		 * "Fan"; house.applianceNickName = "Fan"; house.applianceID = "1";
		 * 
		 * createHouse(house);
		 * 
		 * house.houseID = "1"; house.houseName = "Bay House";
		 * house.houseNickName = "Bay House"; house.houseNum = "10142";
		 * house.street = "Black Mountain Rd"; house.city = "San Diego";
		 * house.state = "CA"; house.zipCode = "92126"; house.roomID = "2";
		 * house.roomName = "Kitchen"; house.roomNickName = "Kitchen";
		 * house.type = "Kitchen"; house.level = "1"; house.applianceName =
		 * "Refrigirator"; house.applianceNickName = "Refrigirator";
		 * house.applianceID = "2";
		 * 
		 * createHouse(house);
		 */
	}

	public Cursor fetchCrewByCrewID(String inputText) throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_CREWID, KEY_RECTYPE, KEY_LEVEL, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_EMAIL, KEY_PHONENUM, KEY_MATCODE,
				KEY_APPLIANCENAME, KEY_WORKCENTER }, null, null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchCrewByRowId(long rowID) throws SQLException

	{
		// Cursor mCursor = null;

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_CREWID, KEY_RECTYPE, KEY_LEVEL, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_PHONENUM, KEY_EMAIL, KEY_HOUSENUM,
				KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_MATCODE,
				KEY_APPLIANCENAME, KEY_WORKCENTER }, KEY_ROWID + " = " + rowID,
				null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void updateCrew(long id, OCrewObj crew) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_AREA, crew.area);
		initialValues.put(KEY_CREWNAME, crew.crewName);
		initialValues.put(KEY_CREWNICKNAME, crew.crewNickName);
		initialValues.put(KEY_COMPANYNAME, crew.crewCompanyName);
		initialValues.put(KEY_HOUSEOWNER, crew.houseOwner);
		initialValues.put(KEY_ADDRESSID, crew.addressID);
		initialValues.put(KEY_STARTDATE, crew.startdate);
		initialValues.put(KEY_ENDDATE, crew.enddate);
		initialValues.put(KEY_STATUS, crew.status);
		initialValues.put(KEY_PHONENUM, crew.phonenum);
		initialValues.put(KEY_EMAIL, crew.email);
		initialValues.put(KEY_HOUSENUM, crew.houseNum);
		initialValues.put(KEY_STREET, crew.street);
		initialValues.put(KEY_CITY, crew.city);
		initialValues.put(KEY_STATE, crew.state);
		initialValues.put(KEY_ZIPCODE, crew.zipCode);
		initialValues.put(KEY_MATCODE, crew.matcode);
		initialValues.put(KEY_WORKCENTER, crew.workcenter);
		initialValues.put(KEY_APPLIANCENAME, crew.applianceName);

		open(); // open the database
		mDb.update(SQLITE_TABLE, initialValues, "_id=" + id, null);
		close(); // close the database

	}

	public Cursor fetchCrewsByOwnerName(String inputText) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_CREWID, KEY_RECTYPE, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_MATCODE, KEY_WORKCENTER },
				KEY_HOUSEOWNER + " like '%" + inputText + "%'", null,
				KEY_CREWID, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean deleteCrew(String crewID)

	{
		int doneDelete = 0;

		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_STATUS, "D");

		open(); // open the database
		String[] args = new String[] { crewID };

		int Iresult = mDb.update(SQLITE_TABLE, updateValues, "crewID=?", args);
		close(); // close the database

		return doneDelete > 0;

	}

	public boolean activateCrew(String crewID)

	{
		int doneActivate = 0;
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_STATUS, "A");

		open(); // open the database

		String[] args = new String[] { crewID };

		doneActivate = mDb.update(SQLITE_TABLE, updateValues, "crewID=?", args);

		close(); // close the database

		return doneActivate > 0;

	}

	public Cursor fetchAll() throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_CREWID, KEY_CREWNAME, KEY_HOUSENUM,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_PHONENUM, KEY_AREA,
				KEY_EMAIL, KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE,
				KEY_MATCODE, KEY_WORKCENTER }, null, null, null, null, null,
				null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void updateCrewMatcode(int level, String crewID, String applName,
			String matcode) {
		ContentValues initialValues = new ContentValues();

		if (level == 2) {
			String[] args = new String[] { crewID };
			initialValues.put(KEY_MATCODE, matcode);
			open(); // open the database
			mDb.update(SQLITE_TABLE, initialValues, "crewID=?", args);
			close(); // close the database
		}

		if (level == 1) {
			initialValues.put(KEY_APPLIANCENAME, applName);
			String[] args = new String[] { crewID };

			open(); // open the database
			mDb.update(SQLITE_TABLE, initialValues, "crewID=?", args);
			close(); // close the database
		}

	}

	public Cursor fetchCrewsByMatcode(String inputText) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_CREWID, KEY_RECTYPE, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_MATCODE, KEY_WORKCENTER },
				KEY_MATCODE + " like '%" + inputText + "%'", null, KEY_CREWID,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchCrewsByAppliance(String inputText) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_CREWID, KEY_RECTYPE, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME,KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_MATCODE, KEY_APPLIANCENAME,
				KEY_WORKCENTER }, KEY_APPLIANCENAME + " like '%" + inputText
				+ "%'", null, KEY_CREWID, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean deleteCrewMAT(String crewID, String rectype)

	{
		int doneDelete = 0;

		// ContentValues updateValues = new ContentValues();

		// updateValues.put(KEY_STATUS, "D");

		open(); // open the database
		// String[] args = new String[]{crewID,"S"};

		doneDelete = mDb.delete(SQLITE_TABLE, KEY_CREWID + " like '%" + crewID
				+ "%'" + " AND " + KEY_RECTYPE + " like '%" + rectype + "%'",
				null);

		// int Iresult = mDb.update(SQLITE_TABLE, updateValues,
		// "crewID=? AND rectype=?" , args);
		close(); // close the database

		return doneDelete > 0;

	}

	public Cursor CrewExistsByMatcodeCrewName(String CrewID, String Matcode)
			throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_CREWID, KEY_RECTYPE, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME, KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_MATCODE, KEY_WORKCENTER },
				KEY_MATCODE + " like '%" + Matcode + "%'" + " AND "
						+ KEY_CREWID + " like '%" + CrewID + "%'", null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor CrewExistsByApplCrewName(String CrewID, String Appl)
			throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_CREWID, KEY_RECTYPE, KEY_AREA, KEY_CREWNAME,
				KEY_CREWNICKNAME, KEY_COMPANYNAME, KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
				KEY_ENDDATE, KEY_STATUS, KEY_MATCODE, KEY_APPLIANCENAME,
				KEY_WORKCENTER }, KEY_APPLIANCENAME + " like '%" + Appl + "%'"
				+ " AND " + KEY_CREWID + " like '%" + CrewID + "%'", null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

}
