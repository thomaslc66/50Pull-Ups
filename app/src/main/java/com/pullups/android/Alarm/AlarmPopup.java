package com.pullups.android.Alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pullups.android.R;

public class AlarmPopup extends Activity {

	//This Class Call from Broadcast Reciver ,When Alaram Execute... 
	private int id;
	private TextView tvMsg;
	AudioPlayer playMp3;
	private Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminderdailog);

		WindowManager.LayoutParams windowManager = getWindow().getAttributes();
		windowManager.dimAmount = 0.75f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		//Id is Alaram Id ,Use id to find Day name of week.
		id = getIntent().getIntExtra("id", 0);
		tvMsg = (TextView) findViewById(R.id.tvMsgForReminder);
		tvMsg.setText(Const.getDay(id));

		btnOk = (Button) findViewById(R.id.btnOkForReminderDialog);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Stop Music and Remove Alaram Dialog ......
				playMp3.stop();
				AlarmPopup.this.finish();
			}
		});
		//Play Audio file ...........
		playMp3 = new AudioPlayer("beep.mp3", AlarmPopup.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("Reminder OnDestroy Call...");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode != KeyEvent.KEYCODE_BACK)
			return super.onKeyDown(keyCode, event);
		else {
			return false;
		}
	}
}
