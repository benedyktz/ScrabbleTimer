package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

public class Field {

    public TextView timerValue;

    public long startTime = 0L;

    public Handler customHandler = new Handler();

    public boolean running = false;
    public boolean hasTimeLeft = true;
    public static boolean timeUpFlag = false;
    public static int numberOfPlayersTimeUp = 0;


    long timeInMiliseconds = 0L;
    long timeSwapBuff;
    long updatedTime = 0L;

    public Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMiliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff - timeInMiliseconds;

            if(hasTimeLeft){
                if(updatedTime > 0){
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
                    timeUpFlag = true;
                    hasTimeLeft = false;
                    numberOfPlayersTimeUp++;
                    Timer.startButton.setVisibility(View.GONE);
                    Timer.continueButton.setVisibility(View.VISIBLE);
                }
                if(numberOfPlayersTimeUp == 4){
                    Timer.gameStarted = false;
                }
            }

        }
    };

    public void start() {
        if(hasTimeLeft){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            timerValue.setBackgroundColor(Color.parseColor("#B2DFDB"));
            running = true;
        }
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
        timeSwapBuff = 3 * 1000L;
    }
}
