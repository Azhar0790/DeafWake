package com.deafwake;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.deafwake.adapter.AlarmAdapter;
import com.deafwake.application.MyApp;
import com.deafwake.custom.CustomActivity;
import com.deafwake.model.AlarmWrapper;
import com.deafwake.service.AlarmService;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends CustomActivity implements Chronometer.OnChronometerTickListener {
    public static MainActivity mainActivity = null;
    RecyclerView recycler;
    AlarmAdapter adapter;
    Chronometer chrome;
    ArrayList<AlarmWrapper> data = new ArrayList<>();
    TextView txt_alarms,txt_am,txt_date,txt_day,time;
    TextClock txt_time;
    private ImageView img_setting, img_add_alarm;
    private boolean canAdd = true;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarTranslucent(true);
        mainActivity = this;
        startService(new Intent(getApplicationContext(), AlarmService.class));
        setUpUi();
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    private void setUpUi() {
        data = MyApp.getApplication().readAlarm();
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_add_alarm = (ImageView) findViewById(R.id.img_add_alarm);
        txt_alarms = (TextView) findViewById(R.id.txt_alarms);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_day = (TextView) findViewById(R.id.txt_day);
        time = (TextView) findViewById(R.id.time);
        txt_time = (TextClock) findViewById(R.id.txt_time);
        chrome = (Chronometer) findViewById(R.id.chrome);

        txt_am = (TextView) findViewById(R.id.txt_am);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        txt_alarms.setText(data.size() + " Alarms Active");
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        adapter = new AlarmAdapter(data, R.layout.row_alarmlist, this);
        recycler.setAdapter(adapter);
        if (data.size() == 5)
            canAdd = false;
        setTouchNClick(img_add_alarm);
        setTouchNClick(img_setting);
        chrome.start();
        chrome.setOnChronometerTickListener(this);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == img_add_alarm) {
            if (canAdd) {
                startActivity(new Intent(MainActivity.this, AddAlarm.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
            else
                Snackbar.make(img_add_alarm, "5 Alarms have been added already", Snackbar.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(MainActivity.this, Setting.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
        }
    }

    public void refreshRecycler() {
        data = MyApp.getApplication().readAlarm();
        canAdd = data.size() != 5;
        txt_alarms.setText(data.size() + " Alarms Active");
        adapter.setData(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA)) {
                        showAlert();
                    }
                }
            }
        }
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    }
                });
        alertDialog.show();
    }



    @Override
    public void onChronometerTick(Chronometer chronometer) {
        Log.e("time", chronometer.getText().toString());
        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy a");
        String time1 = localDateFormat.format(new Date());
        String[] separated = time1.split(" ");
        txt_am.setText(separated[1]);
        txt_date.setText(separated[0]);
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        txt_day.setText(weekDay);

        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE dd MM yyyy HH:mm", Locale.US);
        calendar = Calendar.getInstance();
        time.setText(timeFormat.format(calendar.getTime()));
    }
}
