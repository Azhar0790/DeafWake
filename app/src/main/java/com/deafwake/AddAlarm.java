package com.deafwake;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.deafwake.application.MyApp;
import com.deafwake.custom.CustomActivity;
import com.deafwake.model.AlarmWrapper;
import com.deafwake.service.AlarmService;

import java.util.ArrayList;
import java.util.Calendar;

public class AddAlarm extends CustomActivity {

    ArrayList<AlarmWrapper> alarmList = new ArrayList<>();
    int position = -1;
    private TextView txt_back, txt_time, txt_delete, update_alarm, txt_mon, txt_tue, txt_wed, txt_thu, txt_fri, txt_sat, txt_sun;
    private EditText et_desc;
    private AlarmWrapper alarm = new AlarmWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        setUpUi();
    }

    private void setUpUi() {

        alarmList = MyApp.getApplication().readAlarm();
        txt_back = (TextView) findViewById(R.id.txt_back);
        update_alarm = (TextView) findViewById(R.id.update_alarm);
        txt_delete = (TextView) findViewById(R.id.txt_delete);
        txt_time = (TextView) findViewById(R.id.txt_time);
        et_desc = (EditText) findViewById(R.id.et_desc);

        txt_mon = (TextView) findViewById(R.id.txt_mon);
        txt_tue = (TextView) findViewById(R.id.txt_tue);
        txt_wed = (TextView) findViewById(R.id.txt_wed);
        txt_thu = (TextView) findViewById(R.id.txt_thu);
        txt_fri = (TextView) findViewById(R.id.txt_fri);
        txt_sat = (TextView) findViewById(R.id.txt_sat);
        txt_sun = (TextView) findViewById(R.id.txt_sun);

        if (getIntent().getExtras() != null) {
            alarm = (AlarmWrapper) getIntent().getExtras().getSerializable("alarm");
            txt_time.setText(alarm.getTime());
            et_desc.setText(alarm.getDescription());
            position = getIntent().getExtras().getInt("pos");
        } else {
            txt_delete.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
            int selectedMinute = calendar.get(Calendar.MINUTE);
            String AM_PM;
            if (selectedHour < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }
            /*String currentLocalTime = (selectedHour + ":" + selectedMinute + " " + AM_PM);*/
            txt_time.setText(selectedHour + ":" + (selectedMinute<10?"0"+selectedMinute:selectedMinute) + " " + AM_PM);
        }


        setTouchNClick(txt_back);
        setTouchNClick(update_alarm);
        setTouchNClick(txt_delete);

        setTouchNClick(txt_mon);
        setTouchNClick(txt_tue);
        setTouchNClick(txt_wed);
        setTouchNClick(txt_thu);
        setTouchNClick(txt_fri);
        setTouchNClick(txt_sat);
        setTouchNClick(txt_sun);
        setTouchNClick(txt_time);

        txt_sun.setTag(alarm.getSunday().equalsIgnoreCase("0") ? "1" : "0");
        txt_mon.setTag(alarm.getMonday().equalsIgnoreCase("0") ? "1" : "0");
        txt_tue.setTag(alarm.getTuesday().equalsIgnoreCase("0") ? "1" : "0");
        txt_wed.setTag(alarm.getWednesday().equalsIgnoreCase("0") ? "1" : "0");
        txt_thu.setTag(alarm.getThursday().equalsIgnoreCase("0") ? "1" : "0");
        txt_fri.setTag(alarm.getFriday().equalsIgnoreCase("0") ? "1" : "0");
        txt_sat.setTag(alarm.getSaturday().equalsIgnoreCase("0") ? "1" : "0");
        txt_sun.performClick();
        txt_mon.performClick();
        txt_tue.performClick();
        txt_wed.performClick();
        txt_thu.performClick();
        txt_fri.performClick();
        txt_sat.performClick();


    }

    @Override
    public void onClick(View v) {
        if (v == txt_back) {
            finish();
            overridePendingTransition(R.anim.enter,R.anim.exit);
        } else if (v == update_alarm) {
            addAlarm();
        } else if (v == txt_time) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String AM_PM;
                    if (selectedHour < 12) {
                        AM_PM = "AM";
                    } else {
                        AM_PM = "PM";
                    }
                    txt_time.setText(selectedHour + ":" + (selectedMinute<10?"0"+selectedMinute:selectedMinute) + " " + AM_PM);
                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } else if (v == txt_delete) {
            alarmList.remove(position);
            MyApp.getApplication().writeAlarm(alarmList);
            MainActivity.mainActivity.refreshRecycler();
            if (isMyServiceRunning()) {
                stopService(new Intent(getApplicationContext(), AlarmService.class));
            }
            startService(new Intent(getApplicationContext(),AlarmService.class));
            finish();
            overridePendingTransition(R.anim.enter,R.anim.exit);
        } else {
            if (v.getTag().toString().equalsIgnoreCase("0")) {
                v.setTag("1");
            } else {
                v.setTag("0");
            }
            setData();
        }
    }

    private void setData() {
        if (txt_sun.getTag().toString().equalsIgnoreCase("0")) {
            txt_sun.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setSunday("0");
        } else {
            txt_sun.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setSunday("1");
        }

        if (txt_mon.getTag().toString().equalsIgnoreCase("0")) {
            txt_mon.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setMonday("0");
        } else {
            txt_mon.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setMonday("1");
        }
        if (txt_tue.getTag().toString().equalsIgnoreCase("0")) {
            txt_tue.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setTuesday("0");
        } else {

            alarm.setTuesday("1");
            txt_tue.setBackground(getResources().getDrawable(R.drawable.textvieworange));
        }

        if (txt_wed.getTag().toString().equalsIgnoreCase("0")) {
            txt_wed.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setWednesday("0");
        } else {
            txt_wed.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setWednesday("1");
        }

        if (txt_thu.getTag().toString().equalsIgnoreCase("0")) {
            txt_thu.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setThursday("0");
        } else {
            txt_thu.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setThursday("1");
        }

        if (txt_fri.getTag().toString().equalsIgnoreCase("0")) {
            txt_fri.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setFriday("0");
        } else {
            txt_fri.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setFriday("1");
        }

        if (txt_sat.getTag().toString().equalsIgnoreCase("0")) {
            txt_sat.setBackground(getResources().getDrawable(R.drawable.textviewgrey));
            alarm.setSaturday("0");
        } else {
            txt_sat.setBackground(getResources().getDrawable(R.drawable.textvieworange));
            alarm.setSaturday("1");
        }
    }

    private void addAlarm() {
        if(isDaySelected()) {
            alarm.setDescription(et_desc.getText().toString());
            alarm.setTime(txt_time.getText().toString());
            alarm.setMonday(txt_mon.getTag().toString());

            alarm.setMonday(txt_mon.getTag().toString());
            alarm.setTuesday(txt_tue.getTag().toString());
            alarm.setWednesday(txt_wed.getTag().toString());
            alarm.setThursday(txt_thu.getTag().toString());
            alarm.setFriday(txt_fri.getTag().toString());
            alarm.setSaturday(txt_sat.getTag().toString());
            alarm.setSunday(txt_sun.getTag().toString());
            alarm.setIsSelected("1");

            if (position == -1)
                alarmList.add(alarm);
            else
                alarmList.set(position, alarm);

            MyApp.getApplication().writeAlarm(alarmList);
            MainActivity.mainActivity.refreshRecycler();
            if (isMyServiceRunning()) {
                stopService(new Intent(getApplicationContext(), AlarmService.class));
            }
            startService(new Intent(getApplicationContext(), AlarmService.class));
            finish();
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select a day");
            builder.setMessage("Please select at least a day to add alarm.");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.show();

        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("AlarmService".equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private boolean isDaySelected(){
        if(txt_sun.getTag().equals("1")||txt_mon.getTag().equals("1")||txt_tue.getTag().equals("1")||txt_wed.getTag().equals("1")||txt_thu.getTag().equals("1")||txt_fri.getTag().equals("1")||txt_sat.getTag().equals("1")){
            return true;
        }
        return false;
    }
}
