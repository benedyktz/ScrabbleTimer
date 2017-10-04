package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    int minutes;
    int seconds;

    EditText minutesEditText;
    EditText secondsEditText;

    public static View soundButton;
    public static View vibeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutesEditText = (EditText) findViewById(R.id.minutes);
        secondsEditText = (EditText) findViewById(R.id.seconds);

        soundButton = findViewById(R.id.soundButton);
        if(soundOn)
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

        vibeButton = findViewById(R.id.vibeButton);
        if(vibeOn)
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));

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
        // Find the View that shows the about button
        TextView about = (TextView) findViewById(R.id.about);

        // Set a click listener on that View
        if(about != null){
            about.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {

                // Create a new intent to open the {@link About}
                Intent aboutIntent = new Intent(MainActivity.this, About.class);

                // Start the new activity
                    startActivity(aboutIntent);
                }
            });
        }
    }

    public static boolean vibeOn = false;

    public void viberToggle (View view) {
        if(vibeOn){
            vibeOn = false;
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            vibeOn = true;
            vibeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }

    }

    public static boolean soundOn = false;
    public void soundToggle (View view) {
        if(soundOn){
            soundOn = false;
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle));
        }
        else{
            soundOn = true;
            soundButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius_rectangle_clicked));
        }
    }
}