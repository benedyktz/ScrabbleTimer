package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    Field player1 = new Field();
    Field player2 = new Field();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1.timerValue = (TextView) findViewById(R.id.timerValue);
        player2.timerValue = (TextView) findViewById(R.id.timerValue2);
        player1.running = true; // when player1 click his field, time start in player's 2 field

    }

    public void change(View view) {

        if(player1.timerValue.getId() == view.getId()) {
            if(player1.running == true){
                player1.stop();
                player2.start();
            }
        }

        if(player2.timerValue.getId() == view.getId()) {
            if(player2.running == true){
                player2.stop();
                player1.start();
            }
        }

    }
    
}