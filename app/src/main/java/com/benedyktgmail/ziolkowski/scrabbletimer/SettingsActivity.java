package com.benedyktgmail.ziolkowski.scrabbletimer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends AppCompatActivity {


    EditText minutesEditText;
    EditText secondsEditText;
    EditText addedEditText;
    EditText pl1EditText;
    EditText pl2EditText;
    EditText pl3EditText;
    EditText pl4EditText;

    public static View soundButton;
    public static View vibeButton;
    public View twoPlayersButton;
    public View threePlayersButton;
    public View fourPlayersButton;

    static boolean fromSettingsFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        minutesEditText = (EditText) findViewById(R.id.minutesEditText);
        secondsEditText = (EditText) findViewById(R.id.secondsEditText);
        addedEditText = (EditText) findViewById(R.id.addSecondsEditText);
        pl1EditText = (EditText) findViewById(R.id.pl1EditText);
        pl2EditText = (EditText) findViewById(R.id.pl2EditText);
        pl3EditText = (EditText) findViewById(R.id.pl3EditText);
        pl4EditText = (EditText) findViewById(R.id.pl4EditText);

        twoPlayersButton = findViewById(R.id.twoPlayersButton);
        threePlayersButton = findViewById(R.id.threePlayersButton);
        fourPlayersButton = findViewById(R.id.fourPlayersButton);
        vibeButton = findViewById(R.id.vibeButton);
        soundButton = findViewById(R.id.soundButton);

        if(Timer.settings.isSoundOn())
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        if(Timer.settings.isVibeOn())
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        Button startButton = (Button) findViewById(R.id.start);
        if(startButton != null){
            startButton.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {
                    //check if user didn't set timer to 00:00
                    if(!(Integer.parseInt("0" + minutesEditText.getText()) == 0 && Integer.parseInt("0" + secondsEditText.getText()) == 0)){
                        Timer.settings.setMinutes(Integer.parseInt("0" + minutesEditText.getText()));
                        Timer.settings.setSeconds(Integer.parseInt("0" + secondsEditText.getText()));
                    }
                    Timer.settings.setAddedSeconds(Integer.parseInt("0" + addedEditText.getText()));
                    if(!pl1EditText.getText().toString().equals(""))
                        Timer.settings.setPlayer1(pl1EditText.getText().toString());
                    if(!pl2EditText.getText().toString().equals(""))
                        Timer.settings.setPlayer2(pl2EditText.getText().toString());
                    if(!pl3EditText.getText().toString().equals(""))
                        Timer.settings.setPlayer3(pl3EditText.getText().toString());
                    if(!pl4EditText.getText().toString().equals(""))
                        Timer.settings.setPlayer4(pl4EditText.getText().toString());
                    Timer.gameStarted = false;
                    Timer.gameStartedByField = false;
                    Timer.gamePaused = false;
                    fromSettingsFlag = true;
                    onBackPressed();
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

        pl1EditText.setHint(Timer.settings.getPlayer1());
        pl2EditText.setHint(Timer.settings.getPlayer2());
        pl3EditText.setHint(Timer.settings.getPlayer3());
        pl4EditText.setHint(Timer.settings.getPlayer4());
        minutesEditText.setHint(String.format("%02d", Timer.settings.getMinutes()));
        secondsEditText.setHint(String.format("%02d", Timer.settings.getSeconds()));
        addedEditText.setHint(String.format("%02d", Timer.settings.getAddedSeconds()));
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Timer.gameStarted = false;
        Timer.gameStartedByField = false;
        Timer.gamePaused = false;
        fromSettingsFlag = true;
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