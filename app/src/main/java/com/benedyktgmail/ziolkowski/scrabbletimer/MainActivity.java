package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    int minutes;

    EditText minutesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutesEditText = (EditText) findViewById(R.id.minutes);

        // Find the View that shows the start button
        TextView start = (TextView) findViewById(R.id.start);

        // Set a click listener on that View
        if(start != null){
            start.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the start button is clicked on.
                @Override
                public void onClick(View view) {

                    minutes = Integer.parseInt("" + minutesEditText.getText());

                    // Create a new intent to open the {@link Timer}
                    Intent timerIntent = new Intent(MainActivity.this, Timer.class);
                    timerIntent.putExtra("MINUTES", minutes);
                    // Start the new activity
                    startActivity(timerIntent);
                }
            });
        }
    }

}