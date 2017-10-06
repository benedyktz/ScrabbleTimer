package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class SettingsActivity extends AppCompatActivity {


    EditText minutesEditText;
    EditText secondsEditText;
    EditText addedEditText;

    public static View soundButton;
    public static View vibeButton;
    public View twoPlayersButton;
    public View threePlayersButton;
    public View fourPlayersButton;
    public static int numberOfPlayers = 2;
    static boolean fromSettingsFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        minutesEditText = (EditText) findViewById(R.id.minutes);
        secondsEditText = (EditText) findViewById(R.id.seconds);
        addedEditText = (EditText) findViewById(R.id.addSecondsEditText);

        twoPlayersButton = findViewById(R.id.twoPlayersButton);
        threePlayersButton = findViewById(R.id.threePlayersButton);
        fourPlayersButton = findViewById(R.id.fourPlayersButton);


        soundButton = findViewById(R.id.soundButton);
        if(Timer.settings.isSoundOn())
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        vibeButton = findViewById(R.id.vibeButton);
        if(Timer.settings.isVibeOn())
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        // Find the View that shows the start button
        TextView start = (TextView) findViewById(R.id.start);

        // Set a click listener on that View
        if(start != null){
            start.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {

                    Timer.settings.setMinutes(Integer.parseInt("0" + minutesEditText.getText()));
                    Timer.settings.setSeconds(Integer.parseInt("0" + secondsEditText.getText()));
                    Timer.settings.setAddedSeconds(Integer.parseInt("0" + addedEditText.getText()));
                    Timer.gameStarted = false;
                    Timer.gameStartedByField = false;
                    Timer.gamePaused = false;
                    fromSettingsFlag = true;
                    onBackPressed();
                }
            });
        }
        // Find the View that shows the about button
        TextView about = (TextView) findViewById(R.id.about);

        // Set a click listener on that View
        if(about != null){
            about.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {

                // Create a new intent to open the {@link About}
                Intent aboutIntent = new Intent(SettingsActivity.this, About.class);

                // Start the new activity
                    startActivity(aboutIntent);
                }
            });
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(Timer.settings.getNumberOfPlayers() == 2)
            twoPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        if(Timer.settings.getNumberOfPlayers() == 3)
            threePlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        if(Timer.settings.getNumberOfPlayers() == 4)
            fourPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
    }

    public void viberToggle (View view) {
        if(Timer.settings.isVibeOn()){
            Timer.settings.setVibeOn(false);
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            Timer.settings.setVibeOn(true);
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }

    }

    public void soundToggle (View view) {
        if(Timer.settings.isSoundOn()){
            Timer.settings.setSoundOn(false);
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            Timer.settings.setSoundOn(true);
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }
    }
    public void twoPlayers (View view){
        if(Timer.settings.getNumberOfPlayers() != 2){
            Timer.settings.setNumberOfPlayers(2);
            twoPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
            threePlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            fourPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
    }
    public void threePlayers (View view){
        if(Timer.settings.getNumberOfPlayers() != 3){
            Timer.settings.setNumberOfPlayers(3);
            twoPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            threePlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
            fourPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
    }
    public void fourPlayers (View view){
        if(Timer.settings.getNumberOfPlayers() != 4){
            Timer.settings.setNumberOfPlayers(4);
            twoPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            threePlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
            fourPlayersButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }
    }

}