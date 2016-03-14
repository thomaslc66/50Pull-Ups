package com.pullups.android.Alarm;

import com.pullups.android.R;

public class Const {

	//Define Static Alaram Id...... 
	public static int MONDAY_REMINDER_ID = 2;
	public static int TUESDAY_REMINDER_ID = 3;
	public static int WENSDAY_REMINDER_ID = 4;
	public static int THURSDAY_REMINDER_ID = 5;
	public static int FRIDAY_REMINDER_ID = 6;
	public static int SATERDAY_REMINDER_ID = 7;
	public static int SUNDAY_REMINDER_ID = 1;

	//get Day name of Week Using Alaram Id...........
	public static int getDay(int id) {
		switch (id) {
		case 2:
			return R.string.monday;
		case 3:
			return R.string.thuesday;
		case 4:
			return R.string.wednesday;
		case 5:
			return R.string.thursday;
		case 6:
			return R.string.friday;
		case 7:
			return R.string.saturday;
		case 1:
			return R.string.sunday;
		}
		return 0;
	}
}
