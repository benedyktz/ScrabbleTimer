package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Timer extends AppCompatActivity {

    List<Player> players = new ArrayList<>();

    static Settings settings = new Settings();

    View soundButton;
    View vibeButton;
    View pauseButton;
    View resetButton;

    static boolean  gameStarted = false;
    static boolean gamePaused = false;
    static boolean gameStartedByField = false;

    Vibrator vibe;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a changePlayer of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSettings();
        createGame();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SettingsActivity.fromSettingsFlag){
            createGame();
            SettingsActivity.fromSettingsFlag = false;
        }
        hide();
    }

    @Override
    protected void onDestroy() {
        saveSettings(settings);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    int activePlayer = 0;

    private int getNextPlayerNumber(){
        if(activePlayer == Timer.settings.getNumberOfPlayers() - 1){
            return 0;
        }
        else{
            return activePlayer + 1;
        }
    }

    private void setNextPlayerNumberAsActivePlayer(){
        if(activePlayer == Timer.settings.getNumberOfPlayers() - 1){
            activePlayer = 0;
        }
        else{
            activePlayer++;
        }
    }

    //stops one player and starts next player
    public void changePlayer(View view) {
            //check if clicked field is active player's
            if (players.get(activePlayer).playerField.getId() == view.getId()) {
                //check if active player is not paused and next player has time left
                if (players.get(activePlayer).running && players.get(activePlayer).hasTimeLeft) {
                    players.get(activePlayer).stop();
                    players.get(getNextPlayerNumber()).start();
                    //this is only for the first move to not add seconds to player who have not move already
                    if(!gameStartedByField){
                        players.get(activePlayer).addSeconds(settings.getAddedSeconds());
                        gameStartedByField = false;
                    }
                    else{
                        gameStartedByField = false;
                    }
                    setNextPlayerNumberAsActivePlayer();
                    vibeAndSound();
                }
            }

            if (!players.get(activePlayer).hasTimeLeft) {
                skipPlayer();
            }
    }

    //skip player if the player has not time left
    public void skipPlayer() {
        if(!gamePaused && !Player.allPlayersHasNotTimeLeft && !players.get(activePlayer).hasTimeLeft) {

            boolean flag = true;

            while(flag){
                if (players.get(getNextPlayerNumber()).hasTimeLeft && !players.get(activePlayer).hasTimeLeft) {
                    players.get(getNextPlayerNumber()).start();
                    flag = false;
                    Player.timeUpFlag = false;
                }
                setNextPlayerNumberAsActivePlayer();
            }
        }
    }

    public void gamePause (View view) {
        if(!gamePaused) {
            if (gameStarted && !Player.allPlayersHasNotTimeLeft) {
                gamePaused = true;
                for (Player player : players) {
                    player.stop();
                }
                pauseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
                vibeAndSound();
            }
        }
        else{
            gamePaused = false;
            players.get(activePlayer).start();
            players.get(activePlayer).running = true;
            pauseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            vibeAndSound();
            if(players.get(activePlayer).hasTimeLeft)
                players.get(activePlayer).playerField.setBackgroundColor(Player.fieldColorActive);
            else
                players.get(activePlayer).playerField.setBackgroundColor(Player.fieldColor);
        }
    }

    boolean doubleBackToSettingsPressedOnce = false;
    public void settings(View view) {
            saveSettings(settings);
            if (doubleBackToSettingsPressedOnce || !gameStarted) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                for (Player player : players) {
                    player.stop();
                }
                gameStarted = false;
                return;
            }

            this.doubleBackToSettingsPressedOnce = true;
            Toast.makeText(this, "This will reset timer. If you are sure, click twice", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToSettingsPressedOnce=false;
                }
            }, 2000);
        }


    boolean doubleBackToResetPressedOnce = false;
    public void resetOnClick (View view) {

        if (doubleBackToResetPressedOnce) {
            reset();
            return;
        }

        this.doubleBackToResetPressedOnce = true;
        Toast.makeText(this, "This will restart timer. If you are sure, click twice", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToResetPressedOnce=false;
            }
        }, 2000);

    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            for (Player player : players) {
                player.stop();
            }
            gameStarted = false;
            saveSettings(settings);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void viberToggle (View view) {
        if(settings.isVibeOn()){
            settings.setVibeOn(false);
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            settings.setVibeOn(true);
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }
    }

    public void soundToggle (View view) {
        if(settings.isSoundOn()){
            settings.setSoundOn(false);
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            settings.setSoundOn(true);
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }
    }

    private void vibeAndSound() {
        if(settings.isSoundOn()){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.switch_sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
        if(settings.isVibeOn()){
            vibe.vibrate(100);
        }
    }

    //this method is called when a player's field is clicked
    public void fieldClick (View view) {
        //when game is stared just change the player
        if(gameStarted){
            changePlayer(view);
        }
        //when game is not started find ID of player who was clicked
        else{
            for(int j=0; j < settings.getNumberOfPlayers(); j++){
                if(players.get(j).playerField.getId() == view.getId()){
                    activePlayer=j;
                }
            }
            gameStarted = true;
            gameStartedByField = true;
            changePlayer(view);
        }
    }

    public void reset() {
        gameStarted = false;
        gameStartedByField = false;
        gamePaused = false;
        activePlayer = 0;
        createGame();
        hide();
    }

    void createGame(){

        players.clear();
        for (int i=0; i < settings.getNumberOfPlayers(); i++){
            players.add(new Player());
        }

        switch (settings.getNumberOfPlayers()) {
            case 2:
                setContentView(R.layout.activity_timer2);
                Player.vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                Player.soundPlayer = MediaPlayer.create(this, R.raw.time_up);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                players.get(0).displayedPlayerName = (TextView) findViewById(R.id.playerName1);
                players.get(1).displayedPlayerName = (TextView) findViewById(R.id.playerName2);
                players.get(0).displayedPlayerName.setText(Timer.settings.getPlayer1());
                players.get(1).displayedPlayerName.setText(Timer.settings.getPlayer2());
                players.get(0).playerField = (RelativeLayout) findViewById(R.id.field1);
                players.get(1).playerField = (RelativeLayout) findViewById(R.id.field2);
                break;
            case 3:
                setContentView(R.layout.activity_timer3);
                Player.vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                Player.soundPlayer = MediaPlayer.create(this, R.raw.time_up);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                players.get(2).timerValue = (TextView) findViewById(R.id.timerValue3);
                players.get(0).displayedPlayerName = (TextView) findViewById(R.id.playerName1);
                players.get(1).displayedPlayerName = (TextView) findViewById(R.id.playerName2);
                players.get(2).displayedPlayerName = (TextView) findViewById(R.id.playerName3);
                players.get(0).displayedPlayerName.setText(Timer.settings.getPlayer1());
                players.get(1).displayedPlayerName.setText(Timer.settings.getPlayer2());
                players.get(2).displayedPlayerName.setText(Timer.settings.getPlayer3());
                players.get(0).playerField = (RelativeLayout) findViewById(R.id.field1);
                players.get(1).playerField = (RelativeLayout) findViewById(R.id.field2);
                players.get(2).playerField = (RelativeLayout) findViewById(R.id.field3);
                break;
            case 4: default:
                setContentView(R.layout.activity_timer4);
                Player.vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                Player.soundPlayer = MediaPlayer.create(this, R.raw.time_up);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                players.get(2).timerValue = (TextView) findViewById(R.id.timerValue3);
                players.get(3).timerValue = (TextView) findViewById(R.id.timerValue4);
                players.get(0).displayedPlayerName = (TextView) findViewById(R.id.playerName1);
                players.get(1).displayedPlayerName = (TextView) findViewById(R.id.playerName2);
                players.get(2).displayedPlayerName = (TextView) findViewById(R.id.playerName3);
                players.get(3).displayedPlayerName = (TextView) findViewById(R.id.playerName4);
                players.get(0).displayedPlayerName.setText(Timer.settings.getPlayer1());
                players.get(1).displayedPlayerName.setText(Timer.settings.getPlayer2());
                players.get(2).displayedPlayerName.setText(Timer.settings.getPlayer3());
                players.get(3).displayedPlayerName.setText(Timer.settings.getPlayer4());
                players.get(0).playerField = (RelativeLayout) findViewById(R.id.field1);
                players.get(1).playerField = (RelativeLayout) findViewById(R.id.field2);
                players.get(2).playerField = (RelativeLayout) findViewById(R.id.field3);
                players.get(3).playerField = (RelativeLayout) findViewById(R.id.field4);
                break;
        }


        soundButton = findViewById(R.id.soundButtonTimer);
        vibeButton = findViewById(R.id.vibeButtonTimer);
        pauseButton = findViewById(R.id.pauseButtonTimer);
        resetButton = findViewById(R.id.resetButtonTimer);

        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        Player.fieldColor = getResources().getColor(R.color.fieldBackground);
        Player.fieldColorActive = getResources().getColor(R.color.fieldBackgroundActive);
        Player.fieldTextActive = getResources().getColor(R.color.fieldTextActive);
        Player.colorText = getResources().getColor(R.color.colorText);
        String timer = String.format(Locale.US, "%02d", settings.getMinutes()) + ":" + String.format(Locale.US, "%02d", settings.getSeconds());
        for (Player player: players) {
            player.timerValue.setText(timer);
            player.running = true;
            player.setMinutes(settings.getMinutes());
            player.setSeconds(settings.getSeconds());
        }
        Player.timeUpFlag = false;
        Player.allPlayersHasNotTimeLeft = false;
        gamePaused = false;
        gameStarted = false;
        gameStartedByField = false;
        Player.numberOfPlayersWithoutTimeLeft = 0;

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(settings.isVibeOn())
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        if(settings.isSoundOn())
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
    }

    public void info (View view) {
        // Create a new intent to open the {@link About}
        Intent aboutIntent = new Intent(Timer.this, About.class);

        // Start the new activity
        startActivity(aboutIntent);
    }

    String FILENAME = "settings.s";

    public void saveSettings(Settings settingsObject) {
        ObjectOutput out;
        try{
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(settingsObject);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettings() {
        ObjectInputStream in;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            in = new ObjectInputStream(fis);
            settings = (Settings) in.readObject();
            in.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
