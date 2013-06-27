package com.netsavvies.cyberoam.backend;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "DetailsManager";

	// Contacts table name
	private static final String TABLE_DETAILS = "UserDetails";

	// Contacts Table Columns names
	private static final String KEY_PRIORITY = "priority";
	private static final String KEY_ID = "id";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_CHECKED = "checked";

	private static DatabaseHandler mInstance = null;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
				+ KEY_PRIORITY + " INTEGER PRIMARY KEY," + KEY_ID + " TEXT,"
				+ KEY_PASSWORD + " TEXT," + KEY_CHECKED + " INTEGER" + ")";
		db.execSQL(CREATE_DETAILS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new user
	public void addUser(UserDetails userDetails) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		// userDetails.setPriority(this.getContactsCount() + 1);
		values.put(KEY_PRIORITY, userDetails.getPriority());
		values.put(KEY_ID, userDetails.getId()); // user id
		values.put(KEY_PASSWORD, userDetails.getPassword()); // Contact password
		values.put(KEY_CHECKED, userDetails.getChecked());

		db.replace(TABLE_DETAILS, null, values);
		// Inserting Row

		db.close(); // Closing database connection
	}

	// Getting single contact
	public UserDetails getUser(int priority) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DETAILS, new String[] { KEY_PRIORITY,
				KEY_ID, KEY_PASSWORD, KEY_CHECKED }, KEY_PRIORITY + "=?",
				new String[] { String.valueOf(priority) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		UserDetails userDetails = new UserDetails(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getString(2),
				Integer.parseInt(cursor.getString(3)));
		cursor.close();
		db.close();

		return userDetails;
	}

	// Getting All Contacts
	public ArrayList<UserDetails> getAllUsers() {
		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
		// Selec All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DETAILS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				UserDetails userDetails = new UserDetails();

				userDetails.setPriority(Integer.parseInt(cursor.getString(0)));
				userDetails.setId(cursor.getString(1));
				userDetails.setPassword(cursor.getString(2));
				userDetails.setChecked(Integer.parseInt(cursor.getString(3))); // Adding
				// contact to list
				userList.add(userDetails);
			} while (cursor.moveToNext());
		}

		int i, j;
		int n = userList.size();
		UserDetails temp = new UserDetails(1, "airforce1", "reset123", 1);
		for (i = 0; i < n; i++) {
			for (j = 1; j < (n - i); j++) {
				if (userList.get(j - 1).getPriority() > userList.get(j)
						.getPriority()) {
					temp.copy(userList.get(j - 1));
					userList.get(j - 1).copy(userList.get(j));
					userList.get(j).copy(temp);
				}
			}
		}
		cursor.close();
		db.close();
		return userList;

	}

	// Getting All Contacts
	public void deleteAllUsers() {
		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
		// Selec All Query
		String selectQuery = "SELECT  * FROM " + TABLE_DETAILS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				db.delete(TABLE_DETAILS, KEY_PRIORITY + " = ?",
						new String[] { cursor.getString(0) });

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();
		

	}

	// Updating single contact
	public int updateContact(UserDetails userDetails, int i) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CHECKED, userDetails.getChecked());
		values.put(KEY_PRIORITY, userDetails.getPriority());
		values.put(KEY_ID, userDetails.getId());
		values.put(KEY_PASSWORD, userDetails.getPassword());

		// updating row
		int r = db.update(TABLE_DETAILS, values, KEY_PRIORITY + " = ?",
				new String[] { String.valueOf(i) });
		db.close();
		return r;
	}

	// Deleting single contact
	public void deleteContact(int priority) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DETAILS, KEY_PRIORITY + " = ?",
				new String[] { String.valueOf(priority) });
		db.close();
	}

	// Getting contacts Count
	public long getContactsCount() {

		SQLiteDatabase db = this.getWritableDatabase();

		long count = DatabaseUtils.queryNumEntries(db, TABLE_DETAILS);
		db.close();
		return count;
	}

}
