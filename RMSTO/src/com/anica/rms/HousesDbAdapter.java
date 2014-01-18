package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HousesDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_HOUSEID = "houseID";
	public static final String KEY_HOUSENAME = "houseName";
	public static final String KEY_HOUSENUM = "houseNum";
	public static final String KEY_HOUSENICKNAME = "houseNickName";
	public static final String KEY_HOUSEOWNER = "houseOwner";
	public static final String KEY_STREET = "street";
	public static final String KEY_CITY = "city";
	public static final String KEY_STATE = "state";
	public static final String KEY_ZIPCODE = "zipCode";
	public static final String KEY_ROOMID = "roomID";
	public static final String KEY_ROOMNAME = "roomName";
	public static final String KEY_ROOMNICKNAME = "roomNickName";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_TYPE = "type";
	public static final String KEY_APPLIANCEID = "applianceID";
	public static final String KEY_APPLIANCENAME = "applianceName";
	public static final String KEY_APPLIANCENICKNAME = "applianceNickName";
	public static final String KEY_STATUS = "status";
	public static final String KEY_RECSTATUS = "recStatus";
	public static final String KEY_RECTYPE = "rectype";

	private static final String TAG = "RiserDbAdapter";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "RMS";
	private static final String SQLITE_TABLE = "HOUSELIST";
	private static final int DATABASE_VERSION = 11;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_HOUSEID + ","
			+ KEY_HOUSENAME + "," + KEY_HOUSENICKNAME + "," + KEY_HOUSEOWNER
			+ "," + KEY_HOUSENUM + "," + KEY_STREET + "," + KEY_CITY + ","
			+ KEY_STATE + "," + KEY_ZIPCODE + "," + KEY_ROOMNAME + ","
			+ KEY_ROOMID + "," + KEY_ROOMNICKNAME + "," + KEY_LEVEL + ","
			+ KEY_TYPE + "," + KEY_APPLIANCENAME + "," + KEY_APPLIANCEID + ","
			+ KEY_APPLIANCENICKNAME + "," + KEY_RECSTATUS + "," + KEY_STATUS + "," + KEY_RECTYPE
			+ "," + " UNIQUE (" + KEY_ROWID + " , " + KEY_ROOMNAME + " , "
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

	public HousesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public HousesDbAdapter open() throws SQLException {
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

	public boolean deleteHouse(String houseID)

	{
		int doneDelete = 0;

		/*
		 * String[] args = new String[]{houseID};
		 * 
		 * 
		 * doneDelete = mDb.delete(SQLITE_TABLE, "houseID=?", args); Log.w(TAG,
		 * Integer.toString(doneDelete)); return doneDelete > 0;
		 */
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_STATUS, "D");

		open(); // open the database
		String[] args = new String[] { houseID };

		int Iresult = mDb.update(SQLITE_TABLE, updateValues, "houseID=?", args);
		close(); // close the database

		return doneDelete > 0;

	}

	public void insertSampleData()

	{
		HouseObj house = new HouseObj();

		house.houseID = "1";
		house.houseName = "Bay House";
		house.houseNickName = "Bay House";
		house.houseNum = "10142";
		house.street = "Black Mountain Rd";
		house.city = "San Diego";
		house.state = "CA";
		house.zipCode = "92126";
		house.roomID = "1";
		house.roomName = "Bedroom";
		house.roomNickName = "Master Bedroom";
		house.type = "Bedroom";
		house.level = "1";
		house.applianceName = "Fan";
		house.applianceNickName = "Fan";
		house.applianceID = "1";

		// createHouse(house);

		house.houseID = "1";
		house.houseName = "Bay House";
		house.houseNickName = "Bay House";
		house.houseNum = "10142";
		house.street = "Black Mountain Rd";
		house.city = "San Diego";
		house.state = "CA";
		house.zipCode = "92126";
		house.roomID = "2";
		house.roomName = "Kitchen";
		house.roomNickName = "Kitchen";
		house.type = "Kitchen";
		house.level = "1";
		house.applianceName = "Refrigirator";
		house.applianceNickName = "Refrigirator";
		house.applianceID = "2";

		// createHouse(house);

	}

	public Cursor fetchDisRoomsByHouseID(String OwnerName, String houseID)
			throws SQLException {
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM,
				KEY_HOUSENICKNAME, KEY_HOUSEOWNER, KEY_ROOMNAME,
				KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET, KEY_CITY, KEY_STATE,
				KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE, KEY_APPLIANCENAME,
				KEY_APPLIANCENICKNAME, KEY_APPLIANCEID, KEY_STATUS, KEY_RECSTATUS },
				KEY_HOUSEID + " like '%" + houseID + "%'" + " AND "
						+ KEY_RECSTATUS + " like '%" + "A" + "%'" + " AND "
						+ KEY_HOUSEOWNER + " like '%" + OwnerName + "%'"
						+ " AND " + KEY_RECTYPE + " like '%" + "R" + "%'",
				null, KEY_ROOMNAME, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchDisRoomsByHouseIDAll(String OwnerName, String houseID)
			throws SQLException {
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM,
				KEY_HOUSENICKNAME, KEY_HOUSEOWNER, KEY_ROOMNAME,
				KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET, KEY_CITY, KEY_STATE,
				KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE, KEY_APPLIANCENAME,
				KEY_APPLIANCENICKNAME, KEY_APPLIANCEID, KEY_STATUS, KEY_RECSTATUS },
				KEY_HOUSEID + " like '%" + houseID + "%'"

				+ " AND " + KEY_HOUSEOWNER + " like '%" + OwnerName + "%'"
						+ " AND " + KEY_RECTYPE + " like '%" + "R" + "%'",
				null, KEY_ROOMNAME, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/*
	 * public Cursor fetchDisRoomsByHouseID(String houseID) throws SQLException
	 * { // Cursor mCursor = null; Cursor mCursor = mDb.query(true,
	 * SQLITE_TABLE, new String[] { KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME,
	 * KEY_HOUSENUM, KEY_HOUSENICKNAME, KEY_ROOMNAME, KEY_ROOMNICKNAME,
	 * KEY_ROOMID, KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL,
	 * KEY_TYPE, KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME, KEY_APPLIANCEID },
	 * KEY_HOUSEID + " like '%" + houseID + "%'"+ " AND " + KEY_ROOMNAME + "!="
	 * + "null", null, null, null, null, null);
	 * 
	 * if (mCursor != null) { mCursor.moveToFirst(); }
	 * 
	 * return mCursor; }
	 */

	public Cursor fetchDisApplByRoomName(String Owner, String houseID,
			String roomID) throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM,
				KEY_HOUSENICKNAME, KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID,
				KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL,
				KEY_TYPE, KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME,
				KEY_APPLIANCEID }, KEY_HOUSEID + " like '%" + houseID + "%'"
				+ " AND " + KEY_RECSTATUS + " like '%" + "A" + "%'" + " AND "
				+ KEY_RECTYPE + " like '%" + "A" + "%'" + " AND "
				+ KEY_HOUSEOWNER + " like '%" + Owner + "%'" + " AND "
				+ KEY_ROOMID + " like '%" + roomID + "%'", null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchDisApplByRoomNameAll(String Owner, String houseID,
			String roomID) throws SQLException

	{
		// Cursor mCursor = null;
		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM,
				KEY_HOUSENICKNAME, KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID,
				KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL,
				KEY_TYPE, KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME,
				KEY_APPLIANCEID }, KEY_HOUSEID + " like '%" + houseID + "%'"
				+ " AND " + KEY_RECTYPE + " like '%" + "A" + "%'" + " AND "
				+ KEY_HOUSEOWNER + " like '%" + Owner + "%'" + " AND "
				+ KEY_ROOMID + " like '%" + roomID + "%'", null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchHousesByRowId(long rowID) throws SQLException

	{
		// Cursor mCursor = null;

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM, KEY_HOUSENICKNAME,
				KEY_STATUS, KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID,
				KEY_STREET, KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL,
				KEY_TYPE, KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME,
				KEY_APPLIANCEID }, KEY_ROWID + " = " + rowID, null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchRoomByRoomName(String OwnerName, String houseID,
			String SroomName) throws SQLException

	{
		// Cursor mCursor = null;

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM, KEY_HOUSENICKNAME,
				KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET,
				KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE,
				KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME, KEY_APPLIANCEID },
				KEY_HOUSEID + " like '%" + houseID + "%'" + " AND "
						+ KEY_HOUSEOWNER + " like '%" + OwnerName + "%'"
						+ " AND " + KEY_ROOMNAME + " like '%" + SroomName
						+ "%'", null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchApplByApplName(String Owner, String houseID,
			String roomName, String applName) throws SQLException {
		// Cursor mCursor = null;

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM, KEY_HOUSENICKNAME,
				KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET,
				KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE,
				KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME, KEY_APPLIANCEID },
				KEY_HOUSEID + " like '%" + houseID + "%'" + " AND "
						+ KEY_HOUSEOWNER + " like '%" + Owner + "%'" + " AND "
						+ KEY_ROOMNAME + " like '%" + roomName + "%'" + " AND "
						+ KEY_APPLIANCENAME + " like '%" + applName + "%'",
				null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void updateHouse(long id, HouseObj houseObj) {
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_HOUSEID, houseObj.houseID);
		updateValues.put(KEY_HOUSENAME, houseObj.houseName);
		updateValues.put(KEY_HOUSENICKNAME, houseObj.houseNickName);
		updateValues.put(KEY_HOUSENUM, houseObj.houseNum);
		updateValues.put(KEY_HOUSEOWNER, houseObj.houseOwner);
		updateValues.put(KEY_STREET, houseObj.street);
		updateValues.put(KEY_CITY, houseObj.city);
		updateValues.put(KEY_STATE, houseObj.state);
		updateValues.put(KEY_ZIPCODE, houseObj.zipCode);
		updateValues.put(KEY_ROOMID, houseObj.roomID);
		updateValues.put(KEY_ROOMNAME, houseObj.roomName);
		updateValues.put(KEY_ROOMNICKNAME, houseObj.roomNickName);
		updateValues.put(KEY_APPLIANCENAME, houseObj.applianceName);
		updateValues.put(KEY_APPLIANCENICKNAME, houseObj.applianceNickName);
		updateValues.put(KEY_APPLIANCEID, houseObj.applianceID);
		updateValues.put(KEY_TYPE, houseObj.type);
		updateValues.put(KEY_LEVEL, houseObj.level);

		open(); // open the database
		mDb.update(SQLITE_TABLE, updateValues, "_id=" + id, null);
		close(); // close the database

	}

	public void insertHouse(HouseObj houseObj) {
		ContentValues insertValues = new ContentValues();

		insertValues.put(KEY_HOUSEID, houseObj.houseID);
		insertValues.put(KEY_HOUSENAME, houseObj.houseName);
		insertValues.put(KEY_HOUSENICKNAME, houseObj.houseNickName);
		insertValues.put(KEY_HOUSENUM, houseObj.houseNum);
		insertValues.put(KEY_STREET, houseObj.street);
		insertValues.put(KEY_CITY, houseObj.city);
		insertValues.put(KEY_STATE, houseObj.state);
		insertValues.put(KEY_ZIPCODE, houseObj.zipCode);
		insertValues.put(KEY_HOUSEOWNER, houseObj.houseOwner);
		insertValues.put(KEY_RECTYPE, houseObj.rectype);
		insertValues.put(KEY_STATUS, "V");
		insertValues.put(KEY_RECSTATUS, "A");

		/*
		 * insertValues.put(KEY_APPLIANCEID,houseObj.applianceID);
		 * insertValues.put(KEY_APPLIANCENAME,houseObj.applianceName);
		 * insertValues.put(KEY_APPLIANCENICKNAME,houseObj.applianceNickName);
		 * insertValues.put(KEY_ROOMID,houseObj.roomID);
		 * insertValues.put(KEY_ROOMNAME,houseObj.roomName);
		 * insertValues.put(KEY_ROOMNICKNAME,houseObj.roomNickName);
		 * insertValues.put(KEY_LEVEL,houseObj.level);
		 * insertValues.put(KEY_TYPE,houseObj.type);
		 */

		// open();
		long result = mDb.insert(SQLITE_TABLE, null, insertValues);
		// close();
	}

	public void insertAppliance(HouseObj houseObj) {
		ContentValues insertValues = new ContentValues();

		insertValues.put(KEY_HOUSEID, houseObj.houseID);
		insertValues.put(KEY_HOUSENAME, houseObj.houseName);
		insertValues.put(KEY_HOUSENICKNAME, houseObj.houseNickName);
		insertValues.put(KEY_HOUSENUM, houseObj.houseNum);
		insertValues.put(KEY_STREET, houseObj.street);
		insertValues.put(KEY_CITY, houseObj.city);
		insertValues.put(KEY_STATE, houseObj.state);
		insertValues.put(KEY_ZIPCODE, houseObj.zipCode);
		insertValues.put(KEY_HOUSEOWNER, houseObj.houseOwner);
		insertValues.put(KEY_RECTYPE, houseObj.rectype);
		insertValues.put(KEY_STATUS, "A");
		insertValues.put(KEY_RECSTATUS, "A");

		insertValues.put(KEY_APPLIANCEID, houseObj.applianceID);
		insertValues.put(KEY_APPLIANCENAME, houseObj.applianceName);
		insertValues.put(KEY_APPLIANCENICKNAME, houseObj.applianceNickName);
		insertValues.put(KEY_ROOMID, houseObj.roomID);
		insertValues.put(KEY_ROOMNAME, houseObj.roomName);
		insertValues.put(KEY_ROOMNICKNAME, houseObj.roomNickName);
		insertValues.put(KEY_LEVEL, houseObj.level);
		insertValues.put(KEY_TYPE, houseObj.type);

		// open();
		long result = mDb.insert(SQLITE_TABLE, null, insertValues);
		// close();
	}

	public void insertRoom(HouseObj houseObj) {
		ContentValues insertValues = new ContentValues();

		insertValues.put(KEY_HOUSEID, houseObj.houseID);
		insertValues.put(KEY_HOUSENAME, houseObj.houseName);
		insertValues.put(KEY_HOUSENICKNAME, houseObj.houseNickName);
		insertValues.put(KEY_HOUSENUM, houseObj.houseNum);
		insertValues.put(KEY_STREET, houseObj.street);
		insertValues.put(KEY_CITY, houseObj.city);
		insertValues.put(KEY_STATE, houseObj.state);
		insertValues.put(KEY_ZIPCODE, houseObj.zipCode);
		insertValues.put(KEY_HOUSEOWNER, houseObj.houseOwner);
		insertValues.put(KEY_RECTYPE, houseObj.rectype);
		insertValues.put(KEY_STATUS, "A");
		insertValues.put(KEY_RECSTATUS, "A");

		insertValues.put(KEY_ROOMID, houseObj.roomID);
		insertValues.put(KEY_ROOMNAME, houseObj.roomName);
		insertValues.put(KEY_ROOMNICKNAME, houseObj.roomNickName);

		// open();
		long result = mDb.insert(SQLITE_TABLE, null, insertValues);
		// close();
	}

	public Cursor fetchDisHousesByUserName(String owner) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE,
				new String[] { KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME,
						KEY_HOUSENUM, KEY_HOUSENICKNAME, KEY_HOUSEOWNER,
						KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET,
						KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE,
						KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME,
						KEY_APPLIANCEID, KEY_STATUS,KEY_RECSTATUS, KEY_RECTYPE },
				KEY_HOUSEOWNER + " like '%" + owner + "%'" + " AND "
						+ KEY_RECSTATUS + " like '%" + "A" + "%'" + " AND "
						+ KEY_RECTYPE + " like '%" + "H" + "%'", null,
				KEY_HOUSEID, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchDisHousesByUserNameall(String owner) throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE,
				new String[] { KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME,
						KEY_HOUSENUM, KEY_HOUSENICKNAME, KEY_HOUSEOWNER,
						KEY_ROOMNAME, KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET,
						KEY_CITY, KEY_STATE, KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE,
						KEY_APPLIANCENAME, KEY_APPLIANCENICKNAME,
						KEY_APPLIANCEID, KEY_STATUS, KEY_RECSTATUS, KEY_RECTYPE },
				KEY_HOUSEOWNER + " like '%" + owner + "%'" + " AND "
						+ KEY_RECTYPE + " like '%" + "H" + "%'", null,
				KEY_HOUSEID, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public void updateHouseHouseID(String OwnerName, String houseID,
			HouseObj houseObj) {
		ContentValues updateValues = new ContentValues();

		// updateValues.put(KEY_HOUSEID, houseObj.houseID);
		updateValues.put(KEY_HOUSENAME, houseObj.houseName);
		updateValues.put(KEY_HOUSENICKNAME, houseObj.houseNickName);
		updateValues.put(KEY_HOUSENUM, houseObj.houseNum);
		updateValues.put(KEY_STREET, houseObj.street);
		updateValues.put(KEY_CITY, houseObj.city);
		updateValues.put(KEY_STATE, houseObj.state);
		updateValues.put(KEY_ZIPCODE, houseObj.zipCode);
		/*
		 * updateValues.put(KEY_ROOMID, houseObj.roomID);
		 * updateValues.put(KEY_ROOMNAME, houseObj.roomName);
		 * updateValues.put(KEY_ROOMNICKNAME, houseObj.roomNickName);
		 * updateValues.put(KEY_APPLIANCENAME, houseObj.applianceName);
		 * updateValues.put(KEY_APPLIANCENICKNAME, houseObj.applianceNickName);
		 * updateValues.put(KEY_APPLIANCEID, houseObj.applianceID);
		 * updateValues.put(KEY_TYPE, houseObj.type);
		 * updateValues.put(KEY_LEVEL, houseObj.level);
		 */
		open(); // open the database
		String[] args = new String[] { OwnerName, houseID };

		int Iresult = mDb.update(SQLITE_TABLE, updateValues,
				"houseOwner=? AND houseID=?", args);
		close(); // close the database
	}

	public void updateRoomRoomID(String OwnerName, String houseID,
			String roomID, HouseObj houseObj) {
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_ROOMNAME, houseObj.roomName);
		updateValues.put(KEY_ROOMNICKNAME, houseObj.roomNickName);

		open(); // open the database
		String[] args = new String[] { OwnerName, houseID, roomID };

		int Iresult = mDb.update(SQLITE_TABLE, updateValues,
				"houseOwner=? AND houseID=? AND roomID=?", args);
		close(); // close the database
	}
	
	public void updateHouseStatus(String houseID, HouseObj houseObj) {
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_STATUS, houseObj.status);	//Occupied

		open(); // open the database
		String[] args = new String[] {houseID};

		int Iresult = mDb.update(SQLITE_TABLE, updateValues, "houseName=?", args);
		close(); // close the database
	}

	public void updateAppliance(String OwnerName, String houseID,
			String roomID, String applID, HouseObj houseObj) {
		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_APPLIANCENAME, houseObj.applianceName);
		updateValues.put(KEY_APPLIANCENICKNAME, houseObj.applianceNickName);

		updateValues.put(KEY_TYPE, houseObj.type);
		updateValues.put(KEY_LEVEL, houseObj.level);

		open(); // open the database
		String[] args = new String[] { OwnerName, houseID, roomID, applID };
		mDb.update(SQLITE_TABLE, updateValues,
				"houseOwner=? AND houseID=? AND roomID=? AND applianceID=?",
				args);
		close(); // close the database
	}
	
	

	public Cursor fetchAll() throws SQLException

	{

		// Cursor mCursor = null;

		Cursor mCursor = mDb.query(true, SQLITE_TABLE, new String[] {
				KEY_ROWID, KEY_HOUSEID, KEY_HOUSENAME, KEY_HOUSENUM,
				KEY_HOUSENICKNAME, KEY_HOUSEOWNER, KEY_ROOMNAME,
				KEY_ROOMNICKNAME, KEY_ROOMID, KEY_STREET, KEY_CITY, KEY_STATE,
				KEY_ZIPCODE, KEY_LEVEL, KEY_TYPE, KEY_APPLIANCENAME,
				KEY_APPLIANCENICKNAME, KEY_APPLIANCEID }, null, null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean deleteRoom(String roomID)

	{
		int doneDelete = 0;

		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_RECSTATUS, "D");

		open(); // open the database
		String[] args = new String[] { roomID };

		doneDelete = mDb.update(SQLITE_TABLE, updateValues, "roomID=?", args);
		close(); // close the database

		return doneDelete > 0;

	}

	public boolean deleteAppl(String applID)

	{
		int doneDelete = 0;

		ContentValues updateValues = new ContentValues();

		updateValues.put(KEY_RECSTATUS, "D");

		open(); // open the database
		String[] args = new String[] { applID };

		doneDelete = mDb.update(SQLITE_TABLE, updateValues, "applianceID=?",
				args);
		close(); // close the database

		return doneDelete > 0;

	}

}
