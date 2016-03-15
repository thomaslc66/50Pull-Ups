package com.pullups.android.Alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.pullups.android.ActiviteAccueil;
import com.pullups.android.Alarm.db.PendingIntentsDataSource;
import com.pullups.android.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class MyReceiver extends BroadcastReceiver {

	private static final int UN = 1;
	private static final int ZERO = 0;
	
	private int mToday, mReminderDay;
	private PendingIntentsDataSource mDatasource;
	private int id;
    private Context mContext;


	@Override
	public void onReceive(Context context, Intent intent) {

		mDatasource = new PendingIntentsDataSource(context);
		mDatasource.open();
		id = intent.getIntExtra("id",ZERO);
        mContext = context;
		
		//Code for Find Today Day Name........ 
		SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
		//mToday = outFormat.format(new Date()).toLowerCase();
		//Log.d("tag mToday", "Day is : " + mToday);

		Calendar calendar = Calendar.getInstance();
		mToday = calendar.get(Calendar.DAY_OF_WEEK);
		//Log.d("tag mToday", "Day is : " + mToday);
		//Fine Alaram Day Name ,using Alaram Id..........

		mReminderDay = id;
		//Log.d("tag mReminderDay", "Reminder Day : " + mReminderDay);
		
		//update alaram nextOccurace Date,
		//it is use ,when Mobile Restart ,and Re-Schadule Alaram using NextOccurance Date.
		updateNextOccerenceOfEvent(id);
		
		//Check Alaram is Today Day Alaram or not
		//if Alaram is not Today Day ,than nothing ,popup dialog...only update next occurance date.
		//for Example..Today is Monday and Execute Alaram Day is TuesDay ,than not popup reminder dialog.
		//and if Execute Alaram Day is Monday ,than Popup Reminder Dialog.
		Calendar c = Calendar.getInstance();
		int mMinitus = intent.getIntExtra("min", ZERO);

		if (c.get(Calendar.MINUTE) == mMinitus) {

			//if (mToday.equals(mReminderDay))
			if(mToday == mReminderDay ){
				//Create Notification
                createNotification();

			} else {
				//Log.d("tag", "Today Reminder Not Execute : " + mReminderDay);
			}
		} else {
			//Log.d("tag", "Alaram Not Execute");
		}

	}

    private final void createNotification(){
        final NotificationManager mNotification = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);



        final Intent launchNotifiactionIntent = new Intent(mContext, ActiviteAccueil.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
				ZERO, launchNotifiactionIntent,
				PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(mContext)
                .setWhen(System.currentTimeMillis())
//                .setTicker(notificationTitle)
                .setSmallIcon(R.drawable.biceps_icon)
                .setContentTitle(mContext.getResources().getString(R.string.notification_title))
                .setContentText(mContext.getResources().getString(R.string.notification_content))
                .setAutoCancel(true)
				.setContentIntent(pendingIntent);

		//Check if Version is Before JELLY BEAN OR NOT
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mNotification.notify(UN, builder.build());
		} else {
			mNotification.notify(UN, builder.getNotification());
		}
    }//createNotification

	//This Method use for Update NextOccurance Date into Database.
	//nextoccurance date use when phone restart,and re-schedule alaram.
	public void updateNextOccerenceOfEvent(int alarmId) {

		String nextOccerenceDate = "";
		String dateString = mDatasource.getNextOccerenceOfEvent(alarmId);
		DateFormat formatter;
		Date date = null;
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
			nextOccerenceDate = c.get(Calendar.DATE) + "/"
					+ ((c.get(Calendar.MONTH)) + UN) + "/"
					+ c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + ":"
					+ c.get(Calendar.MINUTE);
		}
		//System.out.println("Next Occerence Date ......" + nextOccerenceDate);
		//Log.d("tag", "Next Occerence Date ......" + nextOccerenceDate);
		int effectedRow = mDatasource.updateNextOccerence(alarmId,
				nextOccerenceDate);
		//Log.d("tag", "NextOccurance is : " + effectedRow);
		mDatasource.close();
	}
}
