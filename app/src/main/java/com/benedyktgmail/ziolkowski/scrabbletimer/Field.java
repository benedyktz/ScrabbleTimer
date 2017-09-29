package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
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
    long timeSwapBuff = 20*60*1000L;
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

    public void startStop(View view) {

        if(!running) {
            running = !running;
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            view.setBackgroundColor(Color.parseColor("#B2DFDB"));
        }

        else  {
            running = !running;
            timeSwapBuff -= timeInMiliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            view.setBackgroundColor(Color.parseColor("#00695C"));
        }
    }
}
