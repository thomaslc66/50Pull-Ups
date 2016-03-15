package com.pullups.android.Alarm;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pullups.android.ActiviteAccueil;
import com.pullups.android.Alarm.db.PendingIntentsDataSource;
import com.pullups.android.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends FragmentActivity implements OnClickListener {

    // Week Checkbox Declaration
    private CheckBox cbLundiForDashboard, cbMardiForDashboard,
            cbMercrediForDashboard, cbJeudiForDashboard,
            cbVendrediForDashboard, cbSamediForDashboard,
            cbDimancheForDashboard;

    // Name of days Declaration
    private TextView tvLundiForDashboard, tvMardiForDashboard,
            tvMercrediForDashboard, tvJeudiForDashboard,
            tvVendrediForDashboard, tvSamediForDashboard,
            tvDimancheForDashboard;

    private Button btnValidate;

    private FragmentManager fragmentManager;

    private int currentDay;

    private int mSeconds;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinutes;
    private TimePickerDialog mTimePickerDialog;
    private PendingIntentsDataSource mDatasource;
    private ArrayList<AlarmScheduleDetails> alListOfReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_sccreen);
        init();
        bindView();
        addListner();

        btnValidate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retourAccueil = new Intent(getApplicationContext(), ActiviteAccueil.class);
                retourAccueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(retourAccueil);
                finish();
            }
        });
    }

    // initialise Calender object ,Database object and TimePicker Object.
    private void init() {
        Calendar cc = Calendar.getInstance();
        int mCurrentHour = cc.get(Calendar.HOUR_OF_DAY);
        int mCurrentMinute = cc.get(Calendar.MINUTE);
        alListOfReminder = new ArrayList<AlarmScheduleDetails>();
        mDatasource = new PendingIntentsDataSource(this);
        mDatasource.open();
        alListOfReminder = (ArrayList<AlarmScheduleDetails>) mDatasource
                .getAllPendingIntents();
    }

    // find id of week Checkbox and days Textview.
    private void bindView() {

        btnValidate = (Button) findViewById(R.id.button_validate_alarm);

        // Find Id of Checkbox
        cbLundiForDashboard = (CheckBox) findViewById(R.id.cbLundiForDashboard);
        cbMardiForDashboard = (CheckBox) findViewById(R.id.cbMardiForDashboard);
        cbMercrediForDashboard = (CheckBox) findViewById(R.id.cbMercrediForDashboard);
        cbJeudiForDashboard = (CheckBox) findViewById(R.id.cbJeudiForDashboard);
        cbVendrediForDashboard = (CheckBox) findViewById(R.id.cbVendrediForDashboard);
        cbSamediForDashboard = (CheckBox) findViewById(R.id.cbSamediForDashboard);
        cbDimancheForDashboard = (CheckBox) findViewById(R.id.cbDimancheForDashboard);

        // Set Tag to Checkbox,tag is Alaram Id of each days
        cbLundiForDashboard.setTag(Const.MONDAY_REMINDER_ID);
        cbMardiForDashboard.setTag(Const.TUESDAY_REMINDER_ID);
        cbMercrediForDashboard.setTag(Const.WENSDAY_REMINDER_ID);
        cbJeudiForDashboard.setTag(Const.THURSDAY_REMINDER_ID);
        cbVendrediForDashboard.setTag(Const.FRIDAY_REMINDER_ID);
        cbSamediForDashboard.setTag(Const.SATERDAY_REMINDER_ID);
        cbDimancheForDashboard.setTag(Const.SUNDAY_REMINDER_ID);

        // find id of week days.
        tvDimancheForDashboard = (TextView) findViewById(R.id.tvDimancheForDashboard);
        tvJeudiForDashboard = (TextView) findViewById(R.id.tvTimeJeudiForDashboard);
        tvLundiForDashboard = (TextView) findViewById(R.id.tvTimeLundiForDashboard);
        tvMardiForDashboard = (TextView) findViewById(R.id.tvTimeMardiForDashboard);
        tvMercrediForDashboard = (TextView) findViewById(R.id.tvTimeMercrediForDashboard);
        tvSamediForDashboard = (TextView) findViewById(R.id.tvTimeSamediForDashboard);
        tvVendrediForDashboard = (TextView) findViewById(R.id.tvTimeVendrediForDashboard);

        // this loop for get All Currently Active days Alaram from Database and
        // set into checkbox

        for (int i = 0; i < alListOfReminder.size(); i++) {
            setInitialLayout((int) alListOfReminder.get(i).getId(),
                    alListOfReminder.get(i).getHour(), alListOfReminder.get(i)
                            .getMinutes());
        }
    }

    // Apply OnclickListner on Checkbox.
    private void addListner() {
        cbDimancheForDashboard.setOnClickListener(this);
        cbLundiForDashboard.setOnClickListener(this);
        cbMardiForDashboard.setOnClickListener(this);
        cbMercrediForDashboard.setOnClickListener(this);
        cbJeudiForDashboard.setOnClickListener(this);
        cbVendrediForDashboard.setOnClickListener(this);
        cbSamediForDashboard.setOnClickListener(this);
    }

    // this call when Application open and Checked Checkbox of day that is
    // Currently Active Alaram
    // also set Alaram Execute Time into Textview.
    public void setInitialLayout(int reminderId, int hour, int min) {
        switch (reminderId) {
            case 2:
                tvLundiForDashboard.setText(hour + ":" + min);
                cbLundiForDashboard.setChecked(true);
                break;
            case 3:
                tvMardiForDashboard.setText(hour + ":" + min);
                cbMardiForDashboard.setChecked(true);
                break;
            case 4:
                tvMercrediForDashboard.setText(hour + ":" + min);
                cbMercrediForDashboard.setChecked(true);
                break;
            case 5:
                tvJeudiForDashboard.setText(hour + ":" + min);
                cbJeudiForDashboard.setChecked(true);
                break;
            case 6:
                tvVendrediForDashboard.setText(hour + ":" + min);
                cbVendrediForDashboard.setChecked(true);
                break;
            case 7:
                tvSamediForDashboard.setText(hour + ":" + min);
                cbSamediForDashboard.setChecked(true);
                break;
            case 1:
                tvDimancheForDashboard.setText(hour + ":" + min);
                cbDimancheForDashboard.setChecked(true);
                break;
        }
    }

    // TimePicker,Execute when User Checked Checkbox and Set Alaram of Day.

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int currentDay) {
            mHour = hourOfDay;
            mMinutes = minute;

            switch (currentDay) {
                case 2:
                    tvLundiForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(2);// 12345 is Alaram Id ,Please see into
                    // Const.java class.
                    break;
                case 3:
                    tvMardiForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(3);
                    break;
                case 4:
                    tvMercrediForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(4);
                    break;
                case 5:
                    tvJeudiForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(5);
                    break;
                case 6:
                    tvVendrediForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(6);
                    break;
                case 7:
                    tvSamediForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(7);
                    break;
                case 1:
                    tvDimancheForDashboard.setText(formatHHMMValue(mHour) + ":" + formatHHMMValue(minute));
                    scheduleAlaram(1);
                    break;
            }
        }
    };

    // this Method is Cancel Currently Active Alaram of days and Delete Data
    // from Database
    public void CancelAnAlarm(int id) {
        AlarmManager am = (AlarmManager) getApplicationContext()
                .getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), MyReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pi);

        switch (id) {
            case 2:
                tvLundiForDashboard.setText("10:00");
                break;
            case 3:
                tvMardiForDashboard.setText("10:00");
                break;
            case 4:
                tvMercrediForDashboard.setText("10:00");
                break;
            case 5:
                tvJeudiForDashboard.setText("10:00");
                break;
            case 6:
                tvVendrediForDashboard.setText("10:00");
                break;
            case 7:
                tvSamediForDashboard.setText("10:00");
                break;
            case 1:
                tvDimancheForDashboard.setText("10:00");
                break;
        }
    }

    // this method use for Active Alaram ,when user checked Checkbox this method
    // call,and set alaram of day with user selected time.
    public void scheduleAlaram(int id) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, mHour);
        c.set(Calendar.MINUTE, mMinutes);
        c.set(Calendar.SECOND, 00);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        //this intent is Boardcast Reciver,call when Alaram Execute.
        Intent i = new Intent(getApplicationContext(), MyReceiver.class);
        Bundle b = new Bundle();
        b.putInt("hour", mHour);
        b.putInt("min", mMinutes);
        b.putInt("id", id);//id is Alaram id ,same as Define Const.java,it is use for detect which day alaram execute.
        i.putExtras(b);

        //Schedule Alaram Code...
        AlarmManager am = (AlarmManager) getApplicationContext()
                .getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
        //Add Alaram Data into Database.
        addToDatabase(id, "1");
    }

    //Save Alaram Data like day,month ,year,time into database.
    private void addToDatabase(int id, String isEnable) {

        boolean isDelete = mDatasource.deletePendingIntent(id);
        //Log.d("tag", "isDelete : " + isDelete);

        //month +1
        mMonth++;

        String nextOccerenceDate = mDay + "/" + mMonth + "/" + mYear + " "
                + mHour + ":" + mMinutes;
        AlarmScheduleDetails isInserRow = mDatasource.createPendingIntents(id,
                mHour, mMinutes, mSeconds, mYear, mMonth, mDay, isEnable,
                nextOccerenceDate);
        try {
            if (!isInserRow.getMessage().equals("")) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.reminderSet), Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.reminderNotSet), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    getString(R.string.reminderNotSet), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //Checkbox Onclick Method
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbLundiForDashboard:
                if (cbLundiForDashboard.isChecked()) {
                    currentDay = Const.MONDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.MONDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.MONDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }
                break;
            case R.id.cbMardiForDashboard:
                if (cbMardiForDashboard.isChecked()) {
                    currentDay = Const.TUESDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.TUESDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.TUESDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }
                break;
            case R.id.cbMercrediForDashboard:
                if (cbMercrediForDashboard.isChecked()) {
                    currentDay = Const.WENSDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.WENSDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.WENSDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }

                break;
            case R.id.cbJeudiForDashboard:
                if (cbJeudiForDashboard.isChecked()) {
                    currentDay = Const.THURSDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.THURSDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.THURSDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }

                break;
            case R.id.cbVendrediForDashboard:
                if (cbVendrediForDashboard.isChecked()) {
                    currentDay = Const.FRIDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.FRIDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.FRIDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }

                break;
            case R.id.cbSamediForDashboard:
                if (cbSamediForDashboard.isChecked()) {
                    currentDay = Const.SATERDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.SATERDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.SATERDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }

                break;
            case R.id.cbDimancheForDashboard:
                if (cbDimancheForDashboard.isChecked()) {
                    currentDay = Const.SUNDAY_REMINDER_ID;
                } else {
                    //When User Unchecked Checkbox,at that time Cancel Active Alaram of day and Delete Data from Database.
                    CancelAnAlarm(Const.SUNDAY_REMINDER_ID);
                    boolean isDeleted = mDatasource
                            .deletePendingIntent(Const.SUNDAY_REMINDER_ID);
                    //Log.d("tag", "Reminder Deleted : " + isDeleted);
                }
                break;
        }

        mTimePickerDialog = TimePickerDialog.newInstance(mTimeSetListener,
                10, 10, true, (int) v.getTag());

        CheckBox cb = (CheckBox) v;
        //Open TimePicker Dialog when User Checked Checkbox of day.
        if (cb.isChecked()) {
            mTimePickerDialog.show(getFragmentManager(), "");
        }
    }

    /* EN - Method needed to display hour with zeros before singel numbers
* Exemple: 1:4 will be displayed 01:04
*
* params (value) -> number we need to convert
* */
    private String formatHHMMValue(int value) {
        String retour;
        /* FR - si value est plus petit que 10 cela veut dire qu'il manque un zéro avant l'unité et il faut le rajouter */
        /* EN - if value is minus the 10 it means we need to add zero in front of value*/
        if (value < 10) {
            retour = "0" + value;
        } else {
            retour = value + "";
        }
        //return the String with a 0 before
        return retour;
    }//formatHHMMValue

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //on est va vers l'accueil alors on tue l'activité
        Intent accueil = new Intent(getApplicationContext(), ActiviteAccueil.class);
        accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(accueil);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish Activity. Because We don't need it anymore
        finish();
    }
}
