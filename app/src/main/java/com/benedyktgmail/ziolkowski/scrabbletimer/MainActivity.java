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
    Field player3 = new Field();
    Field player4 = new Field();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1.timerValue = (TextView) findViewById(R.id.timerValue);
        player2.timerValue = (TextView) findViewById(R.id.timerValue2);
        player3.timerValue = (TextView) findViewById(R.id.timerValue3);
        player4.timerValue = (TextView) findViewById(R.id.timerValue4);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        for (Field player: players) {
            player.running = true;
        }
    }

    int i = 0;

    public void change(View view) {

        if (i == 3) {
            if(players.get(i).timerValue.getId() == view.getId()) {
                if(players.get(i).running == true){
                    players.get(i).stop();
                    players.get(0).start();
                }
                i=0;
            }
        }

        if(players.get(i).timerValue.getId() == view.getId()) {
            if(players.get(i).running == true){
                players.get(i).stop();
                players.get(i+1).start();
                i++;
            }
        }


    }

}