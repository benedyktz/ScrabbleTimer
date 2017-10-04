package com.benedyktgmail.ziolkowski.scrabbletimer;


import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Field {

    public TextView timerValue;

    public long startTime = 0L;

    public Handler customHandler = new Handler();

    public boolean running = false;
    public boolean hasTimeLeft = true;
    public static boolean timeUpFlag = false;
    public static boolean allTimesUp = false;
    public static int numberOfPlayersTimeUp = 0;
    public static int fieldColor = Color.parseColor("#333333");
    public static int fieldColorActive = Color.parseColor("#eeeeee");
    public static int fieldTextActive = Color.parseColor("#111111");
    public static int colorText = Color.parseColor("#006600");


    long timeInMiliseconds = 0L;
    long timeSwapBuff;
    long updatedTime = 0L;

    public Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            timeInMiliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff - timeInMiliseconds;

            if(hasTimeLeft && running){
                if(updatedTime > 0){
                    int secs = (int) (updatedTime / 1000);
                    int mins = secs / 60;
                    secs = secs % 60;
                    timerValue.setText("" + String.format("%02d", mins) + ":"
                            + String.format("%02d", secs));
                    customHandler.postDelayed(this, 1000);
                }
                else {
                    stop();
                    timerValue.setText(R.string.timerVal);
                    timerValue.setTextColor(fieldColor); //trzeba jakis czerwony kolor
                    timeUpFlag = true;
                    hasTimeLeft = false;
                    numberOfPlayersTimeUp++;
                    Timer.startButton.setVisibility(View.GONE);
                    Timer.continueButton.setVisibility(View.VISIBLE);
                }
                if(numberOfPlayersTimeUp == 4){
                    allTimesUp = true;
                }
            }
        }
    };

    public void start() {
        if(hasTimeLeft){
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            timerValue.setBackgroundColor(fieldColorActive);
            timerValue.setTextColor(fieldTextActive);
            running = true;
        }
    }


    public void stop() {
        timeSwapBuff -= timeInMiliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        timerValue.setBackgroundColor(fieldColor);
        timerValue.setTextColor(colorText);
        running = false;
    }

    public void setMinutes(int minutes){

        timeSwapBuff = minutes * 60 * 1000L;
    }
    public void setSeconds(int seconds) {

        timeSwapBuff = timeSwapBuff + seconds * 1000;

    }
}
