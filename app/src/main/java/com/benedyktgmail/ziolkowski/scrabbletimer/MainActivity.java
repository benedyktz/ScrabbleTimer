package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }

    public void startStop(View view){
        player1.startStop(view);
    }
    public void startStop2(View view){
        player2.startStop(view);
    }


}