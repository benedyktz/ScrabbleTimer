package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class Field {

    public TextView timerValue;

    public long startTime = 0L;

    public Handler customHandler = new Handler();

    public boolean running = false;

    public static boolean timeUp = false;

    long timeInMiliseconds = 0L;
    long timeSwapBuff;
    long updatedTime = 0L;

    public Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMiliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff - timeInMiliseconds;

            if(updatedTime>0){
                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                timerValue.setText("" + mins + ":"
                        + String.format("%02d", secs));
                customHandler.postDelayed(this, 0);
            }
            else {
                stop();
                timerValue.setText(R.string.timerVal);
                timerValue.setTextColor(Color.parseColor("#D50000"));
                timeUp = true;
            }

        }
    };

    public void start() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        timerValue.setBackgroundColor(Color.parseColor("#B2DFDB"));
        running = true;
    }

    public void stop() {
        timeSwapBuff -= timeInMiliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        timerValue.setBackgroundColor(Color.parseColor("#00695C"));
        running = false;
    }

//    public void setMinutes(int minutes){
//        timeSwapBuff = minutes * 60 * 1000L;
//    }
    public void setMinutes(int minutes){
        timeSwapBuff = 9 * 1000L;
    }
}
