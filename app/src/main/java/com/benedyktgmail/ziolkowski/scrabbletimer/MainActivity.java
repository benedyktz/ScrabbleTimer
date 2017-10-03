package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    int minutes;
    int seconds;

    EditText minutesEditText;
    EditText secondsEditText;

    Button soundButton;
    Button vibeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutesEditText = (EditText) findViewById(R.id.minutes);
        secondsEditText = (EditText) findViewById(R.id.seconds);

        soundButton = (Button) findViewById(R.id.soundButtonTimer);
        vibeButton = (Button) findViewById(R.id.vibeButtonTimer);
        soundOn = false;
        vibeOn = false;

        // Find the View that shows the start button
        TextView start = (TextView) findViewById(R.id.start);

        // Set a click listener on that View
        if(start != null){
            start.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {

                    minutes = Integer.parseInt("0" + minutesEditText.getText());
                    seconds = Integer.parseInt("0" + secondsEditText.getText());

                    // Create a new intent to open the {@link Timer}
                    Intent timerIntent = new Intent(MainActivity.this, Timer.class);
                    timerIntent.putExtra("MINUTES", minutes);
                    timerIntent.putExtra("SECONDS", seconds);
                    // Start the new activity
                    if((minutes>0 && minutes < 240 && seconds>=0 && seconds <60) || minutes==0 && seconds>0)
                        startActivity(timerIntent);
                }
            });
        }
    }

    public static boolean vibeOn = false;

    public void viberToggle (View view) {
        if(vibeOn){
            vibeOn = false;
            vibeButton.setTextColor(Color.parseColor("#000000"));
        }
        else{
            vibeOn = true;
            vibeButton.setTextColor(Color.parseColor("#00ff00"));
        }

    }

    public static boolean soundOn = false;
    public void soundToggle (View view) {
        if(soundOn){
            soundOn = false;
            soundButton.setTextColor(Color.parseColor("#000000"));
        }
        else{
            soundOn = true;
            soundButton.setTextColor(Color.parseColor("#00ff00"));
        }
    }
}