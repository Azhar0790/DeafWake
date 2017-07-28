package com.deafwake;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deafwake.application.MyApp;
import com.deafwake.custom.SlideButton;
import com.deafwake.model.AlarmWrapper;

import java.util.ArrayList;


public class AlarmActivated extends AppCompatActivity {
    static int counter = 0;
    ArrayList<Integer> colors = new ArrayList<>();
    RelativeLayout activity_alarm_activated;
    SlideButton slide_btn;
    Vibrator vibrator;
    boolean isFlash, isScreen, isVib, isSound;
    AlarmWrapper alarm = new AlarmWrapper();
    TextView txt_time, txt_desc, txt_am, txt_stop;
    boolean isLightOn = false;
    Camera camera;
    boolean brightnessHigh = false;
    MediaPlayer mp;
    private int mInterval = 400;
    private Handler mHandler;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (isLightOn) {
                    turnLightOff();

                } else {
                    turnLightOn();

                }
                if (brightnessHigh) {
                    screenBrightness(20);
                    brightnessHigh = false;
                } else {
                    screenBrightness(255);
                    brightnessHigh = true;
                }
                changeColor();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_activated);
        setStatusBarTranslucent(true);
        setUpUi();

    }

    private void setUpUi() {

        colors.add(R.color.blue);
        colors.add(R.color.yellow);
        colors.add(R.color.orange);
        activity_alarm_activated = (RelativeLayout) findViewById(R.id.activity_alarm_activated);
        txt_am = (TextView) findViewById(R.id.txt_am);
        txt_stop = (TextView) findViewById(R.id.txt_stop);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_desc = (TextView) findViewById(R.id.txt_desc);
        slide_btn = (SlideButton) findViewById(R.id.slide_btn);
        String string = getIntent().getStringExtra("preview");
        if (string == null) {
            alarm = (AlarmWrapper) getIntent().getExtras().getSerializable("alarm");
            String[] separated = alarm.getTime().split(" ");
            txt_time.setText(separated[0]);
            txt_am.setText(separated[1]);
            txt_desc.setText(alarm.getDescription());
            isFlash = MyApp.getStatus("flash");
            isScreen = MyApp.getStatus("screen");
            isSound = MyApp.getStatus("sound");
            isVib = MyApp.getStatus("vib");
        } else {
            boolean[] setting = getIntent().getBooleanArrayExtra("setting");
            isFlash = setting[0];
            isVib = setting[1];
            isScreen = setting[2];
            isSound = setting[3];
            txt_time.setVisibility(View.GONE);
            txt_am.setVisibility(View.GONE);
            txt_desc.setVisibility(View.GONE);
        }
        playRingtone();
        mHandler = new Handler();
        startRepeatingTask();

        slide_btn.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void handleSlide() {
                finish();
            }
        });
        slide_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txt_stop.setVisibility(View.GONE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (slide_btn.getProgress() < 95) {
                        txt_stop.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void turnLightOn() {
        if (isFlash) {
            isLightOn = true;


            if (hasFlash()) {
                camera = Camera.open();
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }
        }
    }

    private void turnLightOff() {

        isLightOn = false;

        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private boolean hasFlash() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVibrate();
        if (mp.isPlaying())
            mp.stop();
        mp.release();
        stopRepeatingTask();
        if (isLightOn) {
            turnLightOff();
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void screenBrightness(double newBrightnessValue) {
        if (isScreen) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            float newBrightness = (float) newBrightnessValue;
            lp.screenBrightness = newBrightness / (float) 255;
            getWindow().setAttributes(lp);
        }
    }

    private void changeColor() {
        if (isScreen) {
            if (counter == 3) {
                counter = 0;
                activity_alarm_activated.setBackgroundColor(getResources().getColor(colors.get(counter)));
                counter++;
                return;
            }
            activity_alarm_activated.setBackgroundColor(getResources().getColor(colors.get(counter)));
            counter++;
        }
    }

    private void playRingtone() {

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        mp = MediaPlayer.create(AlarmActivated.this, R.raw.ringtone);
        if (isSound) {
            mp.start();
            mp.setLooping(true);
        }
        startVibrate();
    }

    public void startVibrate() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (isVib) {
            long pattern[] = {0, 100, 200, 300, 400};
            vibrator.vibrate(pattern, 0);
        }
    }

    public void stopVibrate() {
        vibrator.cancel();
    }
}
