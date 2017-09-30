package com.benedyktgmail.ziolkowski.scrabbletimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Field> players = new ArrayList<Field>();
    Field player1 = new Field();
    Field player2 = new Field();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1.timerValue = (TextView) findViewById(R.id.timerValue);
        player2.timerValue = (TextView) findViewById(R.id.timerValue2);
        players.add(player1);
        players.add(player2);
        for (Field player: players) {
            player.running = true;
        }
    }



    public void change(View view) {

        if(players.get(0).timerValue.getId() == view.getId()) {
            if(players.get(0).running == true){
                players.get(0).stop();
                players.get(1).start();
            }
        }

        if(players.get(1).timerValue.getId() == view.getId()) {
            if(players.get(1).running == true){
                players.get(1).stop();
                players.get(0).start();
            }
        }

    }

}