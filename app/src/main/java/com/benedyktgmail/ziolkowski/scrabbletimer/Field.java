package com.benedyktgmail.ziolkowski.scrabbletimer;


import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Field {

    public TextView timerValue;
    public TextView playerName;
    public RelativeLayout field;
    public long startTime = 0L;

    public Handler customHandler = new Handler();

    public boolean running = false;
    public boolean hasTimeLeft = true;
    public static boolean timeUpFlag = false;
    public static boolean allTimesUp = false;
    public static int numberOfPlayersTimeUp = 0;
    public static int timeUpColor = Color.parseColor("#ff0000");
    public static int fieldColor = Color.parseColor("#333333");
    public static int fieldColorActive = Color.parseColor("#eeeeee");
    public static int fieldTextActive = Color.parseColor("#111111");
    public static int colorText = Color.parseColor("#006600");



    long timeInMiliseconds = 0L;
    long timeSwapBuff;
    long updatedTime = 0L;

    int secs, mins;

    public Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMiliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff - timeInMiliseconds;

            if(hasTimeLeft && running){
                if(updatedTime > 0){
                    secs = (int) (updatedTime / 1000);
                    mins = secs / 60;
                    secs = secs % 60;
                    timerValue.setText("" + String.format("%02d", mins) + ":"
                            + String.format("%02d", secs));
                    customHandler.postDelayed(this, 10);
                }
                else {
                    stop();
                    timerValue.setText(R.string.timerVal);
                    timerValue.setTextColor(timeUpColor);
                    timeUpFlag = true;
                    hasTimeLeft = false;
                    numberOfPlayersTimeUp++;
                    Log.d("timeLeftFieldThread", "goes");
                }
                if(numberOfPlayersTimeUp == SettingsActivity.numberOfPlayers - 1){
                    allTimesUp = true;
                }
            }
        }
    };

    public void start() {
        if(hasTimeLeft){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            field.setBackgroundColor(fieldColorActive);
            timerValue.setBackgroundColor(fieldColorActive);
            timerValue.setTextColor(fieldTextActive);
            playerName.setTextColor(fieldTextActive);
            running = true;
        }
    }


    public void stop() {
        timeSwapBuff -= timeInMiliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        field.setBackgroundColor(fieldColor);
        timerValue.setBackgroundColor(fieldColor);
        timerValue.setTextColor(colorText);
        playerName.setTextColor(colorText);
        running = false;
    }

    public void setMinutes(int minutes){

        timeSwapBuff = minutes * 60 * 1000L;
    }
    public void setSeconds(int seconds) {

        timeSwapBuff = timeSwapBuff + seconds * 1000;
    }

    public void addSeconds(int seconds) {
        if(Timer.gameStarted){
            timeSwapBuff+=seconds*1000;
            updatedTime = timeSwapBuff;
            secs = (int) (updatedTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            timerValue.setText("" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
        }
    }
}
