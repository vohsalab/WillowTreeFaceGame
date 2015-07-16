package com.ibalashov.willowtreefacegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.enterNameView)
    TextView mEnterNameView;

    @Bind(R.id.enterNameButton)
    Button mEnterNameButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        


    }

    @OnClick(R.id.enterNameButton) public void submitName() {
        if (mEnterNameView.getText().toString().length() == 0) {
            Toast.makeText(this, "Please, enter your name", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("playerName", mEnterNameView.getText().toString());
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }


}
