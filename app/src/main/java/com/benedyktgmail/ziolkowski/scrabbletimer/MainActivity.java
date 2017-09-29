package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    private TextView timerValue;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    private boolean player1 = false;

    long timeInMiliseconds = 0L;
    long timeSwapBuff = 20*60*1000L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerValue = (TextView) findViewById(R.id.timerValue);
        
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMiliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff - timeInMiliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

    public void startStop(View view) {

        if(!player1) {
            player1 = !player1;
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }

        else  {
            player1 = !player1;
            timeSwapBuff -= timeInMiliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }
    }

}