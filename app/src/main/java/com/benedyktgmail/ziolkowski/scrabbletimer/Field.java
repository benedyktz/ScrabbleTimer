package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Benedykt on 2017-09-29.
 */
public class Field {

    public TextView timerValue;

    public long startTime = 0L;

    public Handler customHandler = new Handler();

    public boolean running = false;

    long timeInMiliseconds = 0L;
    long timeSwapBuff;
    long updatedTime = 0L;

    public Runnable updateTimerThread = new Runnable() {
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

    public void start() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        timerValue.setBackgroundColor(Color.parseColor("#B2DFDB"));
        running = true;
        Log.d("aaa", "" + timeSwapBuff);
    }

    public void stop() {
        timeSwapBuff -= timeInMiliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        timerValue.setBackgroundColor(Color.parseColor("#00695C"));
        running = false;
    }

    public void setMinutes(int minutes){
        timeSwapBuff = minutes * 60 * 1000L;
    }
}
