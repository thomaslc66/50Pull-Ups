package com.pullups.android.Alarm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pullups.android.Alarm.AlarmScheduleDetails;

import java.util.ArrayList;
import java.util.List;

public class PendingIntentsDataSource {

	private SQLiteDatabase database;
	private PendingIntentsSqlite dbHelper;

	private String[] allColumns = { PendingIntentsSqlite.COLUMN_ID,
			PendingIntentsSqlite.COLUMN_HOUR,
			PendingIntentsSqlite.COLUMN_MINUTES,
			PendingIntentsSqlite.COLUMN_SECONDS,
			PendingIntentsSqlite.COLUMN_YEAR,
			PendingIntentsSqlite.COLUMN_MONTH, PendingIntentsSqlite.COLUMN_DAY,
			PendingIntentsSqlite.COLUMN_RECEIVERNAME,
			PendingIntentsSqlite.COLUMN_MESSAGE,
			PendingIntentsSqlite.COLUMN_NEXT_OCCERENCE };

	public PendingIntentsDataSource(Context context) {
		dbHelper = new PendingIntentsSqlite(context, "pendingintents.db", null,
				2);
	}

	//Open Database.......
	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	//Cloase Database................
	public void close() {
		dbHelper.close();
	}

	//Insert Alaram Data.like as date,time..etc
	public AlarmScheduleDetails createPendingIntents(int id, int hour, int mins,
			int secs, int year, int month, int day,
			String message, String nextOccerence) {

		ContentValues values = new ContentValues();
		values.put(PendingIntentsSqlite.COLUMN_ID, id);
		values.put(PendingIntentsSqlite.COLUMN_HOUR, hour);
		values.put(PendingIntentsSqlite.COLUMN_MINUTES, mins);
		values.put(PendingIntentsSqlite.COLUMN_SECONDS, secs);
		values.put(PendingIntentsSqlite.COLUMN_YEAR, year);
		values.put(PendingIntentsSqlite.COLUMN_MONTH, month);
		values.put(PendingIntentsSqlite.COLUMN_DAY, day);
		values.put(PendingIntentsSqlite.COLUMN_MESSAGE, message);
		values.put(PendingIntentsSqlite.COLUMN_NEXT_OCCERENCE, nextOccerence);

		database.insert(PendingIntentsSqlite.TABLE_PENDINGINTENT, null, values);

		Cursor cursor = database.query(
				PendingIntentsSqlite.TABLE_PENDINGINTENT, allColumns,
				PendingIntentsSqlite.COLUMN_ID + " = " + id, null, null, null,
				null);

		cursor.moveToFirst();
		AlarmScheduleDetails newPendingIntent = cursorToPendingIntent(cursor);
		cursor.close();
		return newPendingIntent;

	}

	//Get All Alaram Data from Database.....
	public List<AlarmScheduleDetails> getAllPendingIntents() {
		List<AlarmScheduleDetails> pendingIntents = new ArrayList<AlarmScheduleDetails>();
		Cursor cursor = database.query(
				PendingIntentsSqlite.TABLE_PENDINGINTENT, allColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AlarmScheduleDetails pendingIntent = cursorToPendingIntent(cursor);
			pendingIntents.add(pendingIntent);
			cursor.moveToNext();
		}
		cursor.close();
		return pendingIntents;
	}

	//get Data from Cursor.........
	private AlarmScheduleDetails cursorToPendingIntent(Cursor cursor) {
		AlarmScheduleDetails pendingIntent = new AlarmScheduleDetails();
		pendingIntent.setId(cursor.getLong(0));
		pendingIntent.setHour(cursor.getInt(1));
		pendingIntent.setMinutes(cursor.getInt(2));
		pendingIntent.setSeconds(cursor.getInt(3));
		pendingIntent.setYear(cursor.getInt(4));
		pendingIntent.setMonth(cursor.getInt(5));
		pendingIntent.setDay(cursor.getInt(6));
		pendingIntent.setReceiverName(cursor.getString(7));
		pendingIntent.setMessage(cursor.getString(8));
		pendingIntent.setmNextOccerence(cursor.getString(9));
		return pendingIntent;
	}

	//Delete Alaram From Database base on Alaram Id
	//Alaram Data Delete base on Alaram Id.
	public boolean deletePendingIntent(int id) {

		return database.delete(PendingIntentsSqlite.TABLE_PENDINGINTENT,
				PendingIntentsSqlite.COLUMN_ID + "=" + id, null) > 0;

	}

	//Get Next Occurance data ...
	//it use when Phone Restart and re-schedule Alaram..
	public String getNextOccerenceOfEvent(int id) {

		String mNextOccerence = "";
		Cursor cursor = database.query(
				PendingIntentsSqlite.TABLE_PENDINGINTENT,
				new String[] { PendingIntentsSqlite.COLUMN_NEXT_OCCERENCE },
				PendingIntentsSqlite.COLUMN_ID + "=" + id, null, null, null,
				null);

		cursor.moveToFirst();
		System.out.println("Cursor size is : " + cursor.getCount());
		if (cursor != null) {
			AlarmScheduleDetails pendingIntent = cursorToPendingIntentForUpdate(cursor);
			mNextOccerence = pendingIntent.getmNextOccerence();
		}
		cursor.close();
		return mNextOccerence;
	}

	//Update NextOccurance date..
	//it Update Each time when Alaram Execute...
	public int updateNextOccerence(int id, String mNextOccerence) {

		ContentValues values = new ContentValues();
		values.put(PendingIntentsSqlite.COLUMN_NEXT_OCCERENCE, mNextOccerence);
		return database.update(PendingIntentsSqlite.TABLE_PENDINGINTENT,
				values, PendingIntentsSqlite.COLUMN_ID + "=" + id, null);
	}
	
	private AlarmScheduleDetails cursorToPendingIntentForUpdate(Cursor cursor) {
		AlarmScheduleDetails pendingIntent = new AlarmScheduleDetails();
		pendingIntent.setmNextOccerence(cursor.getString(0));
		return pendingIntent;
	}
}
