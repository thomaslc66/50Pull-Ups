package com.pullups.android.Alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.pullups.android.Alarm.db.PendingIntentsDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
//This Service Call When Mobile Restart for Re-Schedule All Alaram..
public class ResetAlarmService extends Service {
	private int mId;
	private int mHour;
	private int mMinutes;
	private int mSeconds;
	private int mYear;
	private int mMonth;
	private int mDay;
	private PendingIntentsDataSource mDatasource;
	private List<AlarmScheduleDetails> mAllAlarmList;
	private String nextOccerenceDate;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//Log.d("Reset Alarm service", "onDestroy Call");
		mDatasource.close();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//Log.d("Reset Alarm service", "onCreate Call");
		mDatasource = new PendingIntentsDataSource(this);
		mDatasource.open();

		//
		//Log.d("tag", "on Start Call");
		//Get All Alaram Data
		mAllAlarmList = mDatasource.getAllPendingIntents();
		if (mAllAlarmList.size() > 0) {
			
			//Re-Schedule All Alaram..............
			for (int i = 0; i < mAllAlarmList.size(); i++) {
				mId = (int) mAllAlarmList.get(i).getId();
				mDay = mAllAlarmList.get(i).getDay();
				mHour = mAllAlarmList.get(i).getHour();
				mMinutes = mAllAlarmList.get(i).getMinutes();
				mSeconds = mAllAlarmList.get(i).getSeconds();
				mYear = mAllAlarmList.get(i).getYear();
				mMonth = mAllAlarmList.get(i).getMonth();
				nextOccerenceDate = mAllAlarmList.get(i).getmNextOccerence();
				resetAlarmSchedule();
			}
			//Stop Service after Re-Schedule All Alaram..........
			stopSelf();
		} else {
			//System.out.println("No Alarm Schedule Found..............");
			stopSelf();
		}
	}

	// @Override
	// @Deprecated
	// public void onStart(Intent intent, int startId) {
	// super.onStart(intent, startId);
	//
	// }

	//Re-Schedule All ALaram ,using Database...........
	public void resetAlarmSchedule() {

		//Log.d("Reset Alarm service", "Reset Alaram Schedule Method Call");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			Date mNextIsExecute = sdf.parse(nextOccerenceDate);
			//System.out.println("Next Execute Date : " + mNextIsExecute);
			Calendar mNextCal = Calendar.getInstance();
			mNextCal.setTime(mNextIsExecute);
			mDay = mNextCal.get(Calendar.DATE);
			mMonth = mNextCal.get(Calendar.MONTH);
			mYear = mNextCal.get(Calendar.YEAR);
			mHour = mNextCal.get(Calendar.HOUR);
			mMinutes = mNextCal.get(Calendar.MINUTE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		scheduleAlaram(mId, mHour, mMinutes, mDay, mMonth+1, mYear);
	}

	//Set Alaram Method...........
	public void scheduleAlaram(int id, int hour, int minitus, int day,
			int month, int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minitus);
		c.set(Calendar.SECOND, 0);
		c.set(year, month, day);

		//Log.w("mMonth", ""+month +" Month++ = " + month++);

		Intent i = new Intent(getApplicationContext(), MyReceiver.class);
		Bundle b = new Bundle();
		b.putInt("hour", mHour);
		b.putInt("min", mMinutes);
		b.putInt("id", id);
		i.putExtras(b);

		AlarmManager am = (AlarmManager) getApplicationContext()
				.getSystemService(getApplicationContext().ALARM_SERVICE);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
				id, i, PendingIntent.FLAG_UPDATE_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, pi);
		addToDatabase(id, "1");
	}

	//Add Alaram Data into Database,it use when Phone Restart..........
	private void addToDatabase(int id, String isEnable) {

		boolean isDelete = mDatasource.deletePendingIntent(id);
		//Log.d("service", "isDelete : " + isDelete);

		String nextOccerenceDate = mDay + "/" + mMonth + "/" + mYear + " "
				+ mHour + ":" + mMinutes;
		AlarmScheduleDetails isInserRow = mDatasource.createPendingIntents(id,
				mHour, mMinutes, mSeconds, mYear, mMonth, mDay, isEnable,
				nextOccerenceDate);
		try {
			if (!isInserRow.getMessage().equals("")) {
				//Log.d("service", "Reminder set Successfully!");
			} else {
				//Log.d("service", "Reminder Not set Successfully!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("service", "Reminder Not set Successfully!");
		}

	}

	public String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date;
		date = new Date();
		String currentDate = dateFormat.format(date);
		return currentDate;
	}
}
