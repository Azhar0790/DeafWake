package com.deafwake;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import com.deafwake.application.MyApp;
import com.deafwake.custom.CustomActivity;

import java.util.ArrayList;

public class Setting extends CustomActivity {
    Switch switch_flash, switch_vib, switch_screen, switch_sound;
    TextView txt_back,txt_save,txt_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setUpUI();
    }

    private void setUpUI() {
        switch_flash= (Switch) findViewById(R.id.switch_flash);
        switch_vib= (Switch) findViewById(R.id.switch_vib);
        switch_screen= (Switch) findViewById(R.id.switch_screen);
        switch_sound= (Switch) findViewById(R.id.switch_sound);
        txt_back= (TextView) findViewById(R.id.txt_back);
        txt_save= (TextView) findViewById(R.id.txt_save);
        txt_preview= (TextView) findViewById(R.id.txt_preview);

        switch_flash.setChecked(MyApp.getStatus("flash"));
        switch_vib.setChecked(MyApp.getStatus("vib"));
        switch_screen.setChecked(MyApp.getStatus("screen"));
        switch_sound.setChecked(MyApp.getStatus("sound"));

        txt_back.setOnClickListener(this);
        txt_save.setOnClickListener(this);
        txt_preview.setOnClickListener(this);

        setTouchNClick(txt_back);
        setTouchNClick(txt_save);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
       if(view==txt_back){
           finish();
           overridePendingTransition(R.anim.enter,R.anim.exit);
       }else if(view==txt_save){
           MyApp.setStatus("flash",switch_flash.isChecked());
           MyApp.setStatus("vib",switch_vib.isChecked());
           MyApp.setStatus("screen",switch_screen.isChecked());
           MyApp.setStatus("sound",switch_sound.isChecked());
           finish();
           overridePendingTransition(R.anim.enter,R.anim.exit);
       }else if(view==txt_preview){
           boolean[] setting=new boolean[4];
           setting[0]=(switch_flash.isChecked());
           setting[1]=(switch_vib.isChecked());
           setting[2]=(switch_screen.isChecked());
           setting[3]=(switch_sound.isChecked());
           startActivity(new Intent(Setting.this, AlarmActivated.class).putExtra("setting",setting).putExtra("preview","preview"));
           overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
       }

    }

}
