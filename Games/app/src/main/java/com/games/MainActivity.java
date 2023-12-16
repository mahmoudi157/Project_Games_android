package com.games;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private ArrayList<HashMap<String, Object>> VersionCode = new ArrayList<>();

    private TimerTask Timer;
    private Intent Screen = new Intent();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

    }

    private void initializeLogic() {
        Timer = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Screen.setClass(getApplicationContext(), AuthActivity.class);
                        startActivity(Screen);
                        finish();
                    }
                });
            }
        };
        _timer.schedule(Timer, (int)(3500));
    }

}