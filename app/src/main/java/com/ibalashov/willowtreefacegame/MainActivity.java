package com.ibalashov.willowtreefacegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PreferencesFile";


    protected static String playerName = "Player One";
    SharedPreferences settings;
    boolean gameIsOn = true;
    int currentScore = 0;
    @Bind(R.id.playerNameLabel)
    TextView mPlayerNameLabel;
    @Bind(R.id.topScoreValueLabel) TextView mHighScoreValueLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         settings = getSharedPreferences(PREFS_NAME, 0);
        getPlayerName();
        mPlayerNameLabel.setText(playerName);
        mHighScoreValueLabel.setText(settings.getString("highScore", "0"));

        while(gameIsOn) {
            playTheGame();
        }


    }

    private void playTheGame() {

    }

    private void getPlayerName() {
       // SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        playerName = settings.getString("playerName", "");
        if(playerName.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
