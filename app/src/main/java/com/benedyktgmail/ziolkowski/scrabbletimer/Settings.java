package com.benedyktgmail.ziolkowski.scrabbletimer;

/**
 * Created by Benedykt on 2017-10-06.
 */
public class Settings {
    private boolean vibeOn = true;
    private boolean soundOn = false;
    private int minutes = 20;
    private int seconds = 0;
    private int addedSeconds = 0;
    private int numberOfPlayers = 4;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }


    public void setAddedSeconds(int addedSeconds) {
        this.addedSeconds = addedSeconds;
    }

    public void setVibeOn(boolean vibeOn) {
        this.vibeOn = vibeOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isVibeOn() {
        return vibeOn;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getAddedSeconds() {
        return addedSeconds;
    }

}
