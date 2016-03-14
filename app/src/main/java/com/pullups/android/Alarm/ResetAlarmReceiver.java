package com.pullups.android.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResetAlarmReceiver extends BroadcastReceiver {

	//This Broadcast call when Mobile Restart..........
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("reboot", "Reset Alaram Reciver Call");
		//Service Call for Reschdule Alaram using Alaram data save into Database.....
		context.startService(new Intent(context, ResetAlarmService.class));
	}
}
