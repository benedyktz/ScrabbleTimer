package com.benedyktgmail.ziolkowski.scrabbletimer;

import java.io.Serializable;

public class Settings implements Serializable{

    private boolean vibeOn = true;
    private boolean soundOn = false;
    private int minutes = 20;
    private int seconds = 0;
    private int addedSeconds = 0;
    private int numberOfPlayers = 4;
    private String player1 = "player1";
    private String player2 = "player2";
    private String player3 = "player3";
    private String player4 = "player4";

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

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
