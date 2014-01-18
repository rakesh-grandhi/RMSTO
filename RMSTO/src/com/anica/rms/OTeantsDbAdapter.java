package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OTeantsDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TENANTID = "tenantID";
	public static final String KEY_TENANTNAME = "teantName";
	public static final String KEY_TENANTNICKNAME = "teantNickName";
	public static final String KEY_HOUSEID = "houseID";
	public static final String KEY_HOUSEOWNER = "houseOwner";
	public static final String KEY_ADDRESSID = "addressID";
	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	public static final String KEY_STATUS = "status";
	public static final String KEY_RECSTATUS = "recstatus";

	/*
	 * public static final String KEY_ZIPCODE = "zipCode"; public static final
	 * String KEY_ROOMID = "roomID"; public static final String KEY_ROOMNAME =
	 * "roomName"; public static final String KEY_ROOMNICKNAME = "roomNickName";
	 * public static final String KEY_LEVEL = "level"; public static final
	 * String KEY_TYPE = "type"; public static final String KEY_APPLIANCEID =
	 * "applianceID"; public static final String KEY_APPLIANCENAME =
	 * "applianceName"; public static final String KEY_APPLIANCENICKNAME =
	 * "applianceNickName";
	 */
	private static final String TAG = "TenantDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "TENANT";
	private static final String SQLITE_TABLE = "TENANTLIST";
	private static final int DATABASE_VERSION = 7;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_TENANTID + ","
			+ KEY_TENANTNAME + "," + KEY_TENANTNICKNAME + "," + KEY_HOUSEID
			+ "," + KEY_HOUSEOWNER + "," + KEY_ADDRESSID + "," + KEY_STARTDATE
			+ "," + KEY_ENDDATE + "," + KEY_RECSTATUS + "," + KEY_STATUS + ","

			+ " UNIQUE (" + KEY_ROWID + " , " + KEY_TENANTID + "));";

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

	public OTeantsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public OTeantsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long insertTenant(OTenantObj tenant) {

		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_TENANTID, tenant.tenantID);
		initialValues.put(KEY_HOUSEID, tenant.houseID);
		initialValues.put(KEY_TENANTNAME, tenant.tenantName);
		initialValues.put(KEY_TENANTNICKNAME, tenant.tenantNickName);
		initialValues.put(KEY_HOUSEOWNER, tenant.houseOwner);
		initialValues.put(KEY_ADDRESSID, tenant.addressID);
		initialValues.put(KEY_STARTDATE, tenant.startdate);
		initialValues.put(KEY_ENDDATE, tenant.enddate);
		initialValues.put(KEY_STATUS, tenant.status);
		initialValues.put(KEY_RECSTATUS, "A");

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

	public Cursor fetchTenantByTenantID(String inputText) throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb
				.query(SQLITE_TABLE, new String[] { KEY_ROWID, KEY_TENANTID,
						KEY_HOUSEID, KEY_TENANTNAME, KEY_TENANTNICKNAME,
						KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE,
						KEY_ENDDATE, KEY_STATUS, KEY_RECSTATUS }, null, null,
						null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchTenantByRowId(long rowID) throws SQLException

	{
		// Cursor mCursor = null;

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_TENANTID, KEY_HOUSEID, KEY_TENANTNAME, KEY_TENANTNICKNAME,
				KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE, KEY_ENDDATE,
				KEY_STATUS, KEY_RECSTATUS }, KEY_ROWID + " = " + rowID, null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void updateTenant(long id, OTenantObj tenant) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_TENANTID, tenant.tenantID);
		initialValues.put(KEY_HOUSEID, tenant.houseID);
		initialValues.put(KEY_TENANTNAME, tenant.tenantName);
		initialValues.put(KEY_TENANTNICKNAME, tenant.tenantNickName);
		initialValues.put(KEY_HOUSEOWNER, tenant.houseOwner);
		initialValues.put(KEY_ADDRESSID, tenant.addressID);
		initialValues.put(KEY_STARTDATE, tenant.startdate);
		initialValues.put(KEY_ENDDATE, tenant.enddate);
		initialValues.put(KEY_STATUS, tenant.status);
		initialValues.put(KEY_RECSTATUS, tenant.RecStatus);

		open(); // open the database
		mDb.update(SQLITE_TABLE, initialValues, "_id=" + id, null);
		close(); // close the database

	}

	public Cursor fetchTenantsByOwnerName(String inputText) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_TENANTID, KEY_HOUSEID, KEY_TENANTNAME, KEY_TENANTNICKNAME,
				KEY_HOUSEOWNER, KEY_ADDRESSID, KEY_STARTDATE, KEY_ENDDATE,
				KEY_STATUS }, KEY_HOUSEOWNER + " like '%" + inputText + "%'"
				+ " AND " + KEY_RECSTATUS + " like '%" + "A" + "%'", null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}
	

	public Cursor fetchTenantsByHouseID(String inputText) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_TENANTID, KEY_HOUSEID, KEY_TENANTNAME,
				KEY_TENANTNICKNAME, KEY_HOUSEOWNER, KEY_ADDRESSID,
				KEY_STARTDATE, KEY_ENDDATE, KEY_STATUS }, KEY_HOUSEID
				+ " like '%" + inputText + "%'" + " AND " + KEY_RECSTATUS
				+ " like '%" + "A" + "%'", null, KEY_HOUSEID, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean deleteTenant(String tenantID)

	{
		int doneDelete = 0;

		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_RECSTATUS, "D");

		open(); // open the database
		String[] args = new String[] { tenantID };

		int Iresult = mDb
				.update(SQLITE_TABLE, updateValues, "tenantID=?", args);
		close(); // close the database

		return doneDelete > 0;

	}

}
