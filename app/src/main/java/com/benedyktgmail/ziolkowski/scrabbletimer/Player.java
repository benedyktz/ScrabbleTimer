package com.benedyktgmail.ziolkowski.scrabbletimer;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class Player {

    public TextView timerValue;
    public TextView displayedPlayerName;
    public RelativeLayout playerField;

    public static Vibrator vibe;
    public static MediaPlayer soundPlayer;

    public Handler customHandler = new Handler();

    public boolean running = false;
    public boolean hasTimeLeft = true;
    public static boolean timeUpFlag = false;
    public static boolean allPlayersHasNotTimeLeft = false;
    public static int numberOfPlayersWithoutTimeLeft = 0;
    public static int timeUpColor = Color.parseColor("#ff0000");
    public static int fieldColor = Color.parseColor("#333333");
    public static int fieldColorActive = Color.parseColor("#eeeeee");
    public static int fieldTextActive = Color.parseColor("#111111");
    public static int colorText = Color.parseColor("#006600");

    public long startMoveTime = 0L;
    long milisecondsThisMove = 0L;
    long timeLeftAtBeginingOfTheMove;
    long actualTimeDisplay = 0L;

    int secondsToDisplay, minutesToDisplay;

    public Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {

            milisecondsThisMove = SystemClock.uptimeMillis() - startMoveTime;

            actualTimeDisplay = timeLeftAtBeginingOfTheMove - milisecondsThisMove;

            if(hasTimeLeft && running){
                if(actualTimeDisplay > 0){
                    secondsToDisplay = (int) (actualTimeDisplay / 1000);
                    minutesToDisplay = secondsToDisplay / 60;
                    secondsToDisplay = secondsToDisplay % 60;
                    String timer = String.format(Locale.US, "%02d", minutesToDisplay) + ":"
                            + String.format(Locale.US, "%02d", secondsToDisplay);
                    timerValue.setText(timer);
                    customHandler.postDelayed(this, 10);
                }
                else {
                    stop();
                    timerValue.setText(R.string.timerVal);
                    timerValue.setTextColor(timeUpColor);
                    timeUpFlag = true;
                    hasTimeLeft = false;
                    numberOfPlayersWithoutTimeLeft++;
                    if(Timer.settings.isVibeOn())
                        vibe.vibrate(400);
                    if(Timer.settings.isSoundOn()) {
                        soundPlayer.start();
                    }

                }
                if(numberOfPlayersWithoutTimeLeft == Timer.settings.getNumberOfPlayers()){
                    allPlayersHasNotTimeLeft = true;
                }
            }
        }
    };

    public void start() {
        if(hasTimeLeft){
            startMoveTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            playerField.setBackgroundColor(fieldColorActive);
            timerValue.setBackgroundColor(fieldColorActive);
            timerValue.setTextColor(fieldTextActive);
            displayedPlayerName.setTextColor(fieldTextActive);
            running = true;
        }
    }

    public void stop() {
        timeLeftAtBeginingOfTheMove -= milisecondsThisMove;
        customHandler.removeCallbacks(updateTimerThread);
        if(!Timer.gamePaused){
            playerField.setBackgroundColor(fieldColor);
            timerValue.setBackgroundColor(fieldColor);
            timerValue.setTextColor(colorText);
            displayedPlayerName.setTextColor(colorText);
        }
        running = false;
    }

    public void setMinutes(int minutes){
        timeLeftAtBeginingOfTheMove = minutes * 60 * 1000L;
    }

    public void setSeconds(int seconds) {
        timeLeftAtBeginingOfTheMove = timeLeftAtBeginingOfTheMove + seconds * 1000;
    }

    public void addSeconds(int seconds) {
        if(Timer.gameStarted){
            timeLeftAtBeginingOfTheMove +=seconds*1000;
            actualTimeDisplay = timeLeftAtBeginingOfTheMove;
            secondsToDisplay = (int) (actualTimeDisplay / 1000);
            minutesToDisplay = secondsToDisplay / 60;
            secondsToDisplay = secondsToDisplay % 60;
            String timer = String.format(Locale.US, "%02d", minutesToDisplay) + ":"
                    + String.format(Locale.US, "%02d", secondsToDisplay);
            timerValue.setText(timer);
        }
    }
}
