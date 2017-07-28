package com.deafwake.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.deafwake.AlarmActivated;
import com.deafwake.application.MyApp;
import com.deafwake.model.AlarmWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wel on 17-04-2017.
 */

public class AlarmService extends Service {
    ArrayList<AlarmWrapper> alarmList = new ArrayList<>();
    private int mInterval = 1000 * 10;
    private Handler mHandler;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (alarmList.size() > 0) {
                    getAlarm();
                    for (int i = 0; i < alarmList.size(); i++)
                        if (checkTimings(alarmList.get(i).getTime())) {
                            Intent intent = new Intent(getApplicationContext(), AlarmActivated.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("alarm", alarmList.get(i));
                            intent.putExtras(bundle);
                            alarmList.remove(i);
                            startActivity(intent);
                        }
                }
            } finally {
                mInterval = 1000 * 10;
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass())
                .setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        myAlarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
        alarmList = MyApp.getApplication().readAlarm();
        mHandler = new Handler();

        startRepeatingTask();
    }

    private void getAlarm() {
        int hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int second=Calendar.getInstance().get(Calendar.SECOND);
        if(hour==0 && second<10 ){
            alarmList = MyApp.getApplication().readAlarm();
        }

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Log.e("before size", alarmList.size() + "");
        for (int i = 0; i < alarmList.size(); i++) {
            if (alarmList.get(i).getIsSelected().equals("0")) {
                alarmList.remove(i);
                i--;
            }
        }
        if (day == 1) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getSunday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        if (day == 2) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getMonday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        if (day == 3) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getTuesday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        if (day == 4) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getWednesday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                    Log.e("Removed", "Removed");
                }
            }
        }
        if (day == 5) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getThursday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        if (day == 6) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getFriday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        if (day == 7) {
            for (int i = 0; i < alarmList.size(); i++) {
                if (alarmList.get(i).getSaturday().equalsIgnoreCase("0")) {
                    alarmList.remove(i);
                }
            }
        }
        Log.e("after size", alarmList.size() + "");

    }

    private boolean checkTimings(String time) {

        String pattern = "KK:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = calendar.get(Calendar.MINUTE);
        String AM_PM;
        if (selectedHour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        String currentLocalTime = (selectedHour + ":" + selectedMinute + " " + AM_PM);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(currentLocalTime);
            return date1.equals(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


}
