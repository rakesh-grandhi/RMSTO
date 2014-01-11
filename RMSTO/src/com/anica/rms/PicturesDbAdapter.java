package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PicturesDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DOCNUM = "docnum";
	public static final String KEY_INUM = "itemnum";
	public static final String SEQ_NUM = "seqnum";

	public static final String KEY_CREATEDATE = "createdate";
	public static final String KEY_HSTATUS = "hstatus";
	public static final String KEY_OBJNUM = "objnum";
	public static final String KEY_REMARKS = "remarks";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_USER = "user";
	public static final String KEY_FILEPATH = "filepath";

	private static final String TAG = "PictDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "UTILITY";
	private static final String SQLITE_TABLE = "pictable";
	private static final int DATABASE_VERSION = 1;
	private final Context mCtx;

	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_DOCNUM + ","
			+ KEY_INUM + "," + SEQ_NUM + "," + KEY_CREATEDATE + ","
			+ KEY_HSTATUS + "," + KEY_OBJNUM + "," + KEY_REMARKS + ","
			+ KEY_LOCATION + "," + KEY_USER + "," + KEY_FILEPATH + ","
			+ " UNIQUE (" + KEY_DOCNUM + " , " + KEY_INUM + " , " + SEQ_NUM
			+ "));";

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

	public PicturesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public PicturesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long addPicture(String ordernum, String itemnum, String SeqNum,
			String ObjNum, String CreatDate, String RecStatus, String Remarks,
			String Location, String User, String Filepath)

	{

		Log.w(TAG, ordernum + itemnum + SeqNum + SeqNum + CreatDate + RecStatus
				+ ObjNum + Remarks + Location + User + Filepath);

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DOCNUM, ordernum);
		initialValues.put(KEY_INUM, itemnum);
		initialValues.put(SEQ_NUM, SeqNum);
		initialValues.put(KEY_CREATEDATE, CreatDate);
		initialValues.put(KEY_HSTATUS, RecStatus);
		initialValues.put(KEY_OBJNUM, ObjNum);
		initialValues.put(KEY_REMARKS, Remarks);
		initialValues.put(KEY_LOCATION, Location);
		initialValues.put(KEY_USER, User);
		initialValues.put(KEY_FILEPATH, Filepath);

		return mDb.insert(SQLITE_TABLE, null, initialValues);
	}

	public boolean deleteAllPictures()

	{
		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;
	}

	public Cursor fetchPictByOrdNum(String OrderNum, String opernum)
			throws SQLException

	{

		Cursor mCursor = null;

		mCursor = mDb.query(true, SQLITE_TABLE,
				new String[] { KEY_ROWID, KEY_DOCNUM, KEY_INUM, SEQ_NUM,
						KEY_CREATEDATE, KEY_HSTATUS, KEY_OBJNUM, KEY_REMARKS,
						KEY_LOCATION, KEY_USER, KEY_FILEPATH }, KEY_DOCNUM
						+ " like '%" + OrderNum + "%'" + " AND " + KEY_INUM
						+ " like '%" + opernum + "%'", null, null, null, null,
				null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchAllPict() {

		Cursor mCursor = mDb.query(SQLITE_TABLE,
				new String[] { KEY_ROWID, KEY_DOCNUM, KEY_INUM, SEQ_NUM,
						KEY_CREATEDATE, KEY_HSTATUS, KEY_OBJNUM, KEY_REMARKS,
						KEY_LOCATION, KEY_USER, KEY_FILEPATH }, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean deleteOrdOperSeqPicture(String ordnum, String opernum,
			String seqnum)

	{
		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, KEY_DOCNUM + " like '%" + ordnum
				+ "%'" + " AND " + KEY_INUM + " like '%" + opernum + "%'"
				+ " AND " + SEQ_NUM + " like '%" + seqnum + "%'", null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;
	}

}