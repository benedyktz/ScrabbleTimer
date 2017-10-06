package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Timer extends AppCompatActivity {

    List<Field> players = new ArrayList<>();

    static Settings settings = new Settings();

    public static Button startButton;
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
     * and a change of the status and navigation bar.
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
        Log.d("a", "oncreate");
        createGame();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SettingsActivity.fromSettingsFlag){
            createGame();
            SettingsActivity.fromSettingsFlag = false;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("a", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("a", "ondestroy");
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

    int i = 0;

    public void change(View view) {

        if (gameStarted) {
            if (i == settings.getNumberOfPlayers() - 1) {
                if (players.get(i).timerValue.getId() == view.getId()) {
                    if (players.get(i).running && players.get(i).hasTimeLeft) {
                        players.get(i).stop();
                        players.get(0).start();
                        if(!gameStartedByField){
                            players.get(i).addSeconds(settings.getAddedSeconds());
                            gameStartedByField = false;
                        }
                        i = 0;
                        vibeAndSound();
                    }
                    if (!players.get(0).hasTimeLeft) {
                        gameContinue();
                    }
                }
            }

            if (players.get(i).timerValue.getId() == view.getId()) {
                if (players.get(i).running && players.get(i).hasTimeLeft) {
                    players.get(i).stop();
                    players.get(i + 1).start();
                    if(!gameStartedByField){
                        players.get(i).addSeconds(settings.getAddedSeconds());
                    }
                    else{
                        gameStartedByField = false;
                    }
                    i++;
                    vibeAndSound();
                }
                if (!players.get(i).hasTimeLeft) {
                    gameContinue();
                }
            }
        }
    }

    public void gameStart(View view) {
        if(!gameStarted && !gamePaused) {
            gameStarted = true;
            players.get(0).start();
        }
        startButton.setVisibility(View.GONE);
    }

    public void gameContinue() {
        if(!gamePaused && !Field.allTimesUp && !players.get(i).hasTimeLeft) {
            boolean flag = true;
            while (flag && (i <= settings.getNumberOfPlayers() - 1)) {
                if (i < settings.getNumberOfPlayers() - 1 && players.get(i + 1).hasTimeLeft && !players.get(i).hasTimeLeft) {
                    players.get(i + 1).start();
                    flag = false;
                    Field.timeUpFlag = false;
                }
                if (i == settings.getNumberOfPlayers() - 1 && players.get(0).hasTimeLeft && !players.get(settings.getNumberOfPlayers() - 1).hasTimeLeft) {
                    players.get(0).start();
                    flag = false;
                    Field.timeUpFlag = false;
                }
                if (i < settings.getNumberOfPlayers() - 1) {
                    i++;
                } else {
                    i = 0;
                }
            }
        }
    }

    public void gamePause (View view) {
        if(!gamePaused) {
            if (gameStarted && !Field.allTimesUp) {
                gamePaused = true;
                for (Field player : players) {
                    player.stop();
                }
                players.get(i).timerValue.setBackgroundColor(Field.fieldColor);
                pauseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
                vibeAndSound();
            }
        }
        else{
            gamePaused = false;
            players.get(i).start();
            players.get(i).running = true;
            pauseButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            vibeAndSound();
            if(players.get(i).hasTimeLeft)
                players.get(i).timerValue.setBackgroundColor(Field.fieldColorActive);
            else
                players.get(i).timerValue.setBackgroundColor(Field.fieldColor);
        }
    }

    boolean doubleBackToSettingsPressedOnce = false;

    public void settings(View view) {

            if (doubleBackToSettingsPressedOnce) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                for (Field player : players) {
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


    boolean DoubleBackToResetPressedOnce = false;

    public void resetOnClick (View view) {

        if (DoubleBackToResetPressedOnce) {

            reset();
            return;
        }

        this.doubleBackToSettingsPressedOnce = true;
        Toast.makeText(this, "This will restart timer. If you are sure, click twice", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToSettingsPressedOnce=false;
            }
        }, 2000);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            for (Field player : players) {
                player.stop();
            }
            gameStarted = false;
            Log.d("a", "should finish");
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

    public void fieldClick (View view) {
        if(gameStarted){
            change(view);
        }
        else{
            for(int j=0; j < settings.getNumberOfPlayers(); j++){
                if(players.get(j).timerValue.getId() == view.getId()){
                    i=j;
                    Log.d("aaa", "" + i);
                }
            }
            gameStarted = true;
            gameStartedByField = true;
            startButton.setVisibility(View.GONE);
            change(view);
        }
    }

    public void reset() {

    }

    void createGame(){
        players.clear();
        for (int i=0; i < settings.getNumberOfPlayers(); i++){
            players.add(new Field());
        }

        switch (settings.getNumberOfPlayers()) {
            case 2:
                setContentView(R.layout.activity_timer2);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                break;
            case 3:
                setContentView(R.layout.activity_timer3);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                players.get(2).timerValue = (TextView) findViewById(R.id.timerValue3);
                break;
            case 4: default:
                setContentView(R.layout.activity_timer4);
                players.get(0).timerValue = (TextView) findViewById(R.id.timerValue);
                players.get(1).timerValue = (TextView) findViewById(R.id.timerValue2);
                players.get(2).timerValue = (TextView) findViewById(R.id.timerValue3);
                players.get(3).timerValue = (TextView) findViewById(R.id.timerValue4);
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

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        Field.fieldColor = getResources().getColor(R.color.fieldBackground);
        Field.fieldColorActive = getResources().getColor(R.color.fieldBackgroundActive);
        Field.fieldTextActive = getResources().getColor(R.color.fieldTextActive);
        Field.colorText = getResources().getColor(R.color.colorText);
        for (Field player: players) {
            Log.d("a", "" + settings.getNumberOfPlayers());
            player.timerValue.setText(String.format("%02d", settings.getMinutes()) + ":" + String.format("%02d", settings.getSeconds()));
            player.running = true;
            player.setMinutes(settings.getMinutes());
            player.setSeconds(settings.getSeconds());
        }
        Field.timeUpFlag = false;
        Field.allTimesUp = false;
        Field.numberOfPlayersTimeUp = 0;

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setVisibility(View.VISIBLE);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(settings.isVibeOn())
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        if(settings.isSoundOn())
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
    }


}
