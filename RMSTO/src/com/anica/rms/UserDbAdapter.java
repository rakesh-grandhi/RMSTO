package com.anica.rms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_USER = "user";
	public static final String KEY_PASSWORD = "password";

	private static final String TAG = "UserDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static final String DATABASE_NAME = "USERIDN";
	private static final String SQLITE_TABLE = "USERID";
	private static final int DATABASE_VERSION = 4;
	private final Context mCtx;
	private static final String DATABASE_CREATE = "CREATE TABLE if not exists "
			+ SQLITE_TABLE + " (" + KEY_ROWID
			+ " integer PRIMARY KEY autoincrement," + KEY_USER + ","
			+ KEY_PASSWORD + ","

			+ " UNIQUE (" + KEY_USER + "));";

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

	public UserDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public UserDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long createUser(String user, String password) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USER, user);
		initialValues.put(KEY_PASSWORD, password);

		return mDb.insert(SQLITE_TABLE, null, initialValues);
	}

	public void updateUser(long id, String user, String password) {
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(KEY_USER, user);
		updatedValues.put(KEY_PASSWORD, password);

		open(); // open the database
		mDb.update(SQLITE_TABLE, updatedValues, "_id=" + id, null);
		close(); // close the database

	}

	public boolean deleteAllUsers()

	{
		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, null, null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;
	}

	public Cursor fetchUserByName(String inputText) throws SQLException

	{

		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
					KEY_USER, KEY_PASSWORD }, null, null, null, null, null);
		} else {

			mCursor = mDb.query(true, SQLITE_TABLE, new String[] { KEY_ROWID,
					KEY_USER, KEY_PASSWORD }, KEY_USER + " like '%" + inputText
					+ "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchAllUsers() {

		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] { KEY_ROWID,
				KEY_USER, KEY_PASSWORD }, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public void insertFewUsers()

	{

		createUser("CMATHA", "initpass");

		createUser("RMATHA", "initpass");

	}

}
