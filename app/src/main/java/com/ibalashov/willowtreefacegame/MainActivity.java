package com.ibalashov.willowtreefacegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PreferencesFile";
    private static final String TAG = MainActivity.class.getSimpleName();
    protected static String playerName = "Player One";

    private Employee[] mEmployeeList;
    private int currentScoreValue;
    private int answer;
    private int highScoreValue;
    private Random rand = new Random();



    SharedPreferences settings;
    @Bind(R.id.playerNameLabel)
    TextView mPlayerNameLabel;
    @Bind(R.id.topScoreValueLabel)
    TextView mHighScoreValueLabel;
    @Bind(R.id.firstPhotoView)
    ImageView firstPhotoView;
    @Bind(R.id.secondPhotoView)
    ImageView secondPhotoView;
    @Bind(R.id.thirdPhotoView)
    ImageView thirdPhotoView;
    @Bind(R.id.nameLabel)
    TextView employeeName;
    @Bind(R.id.currentScoreValueLabel)
    TextView currentScoreText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        settings = getSharedPreferences(PREFS_NAME, 0);
        getPlayerName();
        mPlayerNameLabel.setText(playerName);
        highScoreValue = settings.getInt("highScore", 0);
        mHighScoreValueLabel.setText(highScoreValue + "");
        getEmployees();

    }

    private void playTheGame() {
        currentScoreText.setText(currentScoreValue + "");
        int i = 0;
        boolean next = false;
        Employee[] randomPersons = new Employee[3];
        while(i < 3) {
            Employee randomEmployee = mEmployeeList[rand.nextInt(mEmployeeList.length)];
            for (int count = 0; count < i; count++) {
                if (randomPersons[count].url.equals(randomEmployee.url)) {
                    next = true;
                    break;
                }
            }
            if(!next) {
                randomPersons[i] = randomEmployee;
                i++;
            }
        }
        answer = rand.nextInt(3);
        employeeName.setText(randomPersons[answer].name + "?");
        loadImages(randomPersons);


    }

    private void loadImages(Employee[] randomPersons) {
        Picasso.with(this).load(randomPersons[0].url).into(firstPhotoView);
        Picasso.with(this).load(randomPersons[1].url).into(secondPhotoView);
        Picasso.with(this).load(randomPersons[2].url).into(thirdPhotoView);

    }

    @OnClick(R.id.firstPhotoView)
    public void firstPhotoViewClick() {
        if (answer == 0) {
            nextRound();
        }
        else {
            gameOver();
        }
    }


    @OnClick(R.id.secondPhotoView)
    public void secondPhotoViewClick() {
        if (answer == 1) {
            nextRound();
        }
        else {
            gameOver();
        }
    }

    @OnClick(R.id.thirdPhotoView)
    public void thirdPhotoViewClick() {
        if (answer == 2) {
            nextRound();
        }
        else {
            gameOver();
        }
    }

    private void gameOver() {
        Toast.makeText(MainActivity.this, R.string.wrong_message, Toast.LENGTH_SHORT).show();
        if (currentScoreValue > highScoreValue) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("highScore", currentScoreValue);
            editor.apply();
            mHighScoreValueLabel.setText(currentScoreValue + "");
            highScoreValue = currentScoreValue;
        }
        currentScoreValue = 0;
        playTheGame();
    }


    private void nextRound() {
        Toast.makeText(MainActivity.this, R.string.right_message, Toast.LENGTH_SHORT).show();
        currentScoreValue++;
        playTheGame();
    }



    private void getPlayerName() {

        playerName = settings.getString("playerName", "");
        if(playerName.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private  void getEmployees() {
        if(isNetworkAvailable()) {
            String url = "http://api.namegame.willowtreemobile.com/";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Unable to get the data",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    mEmployeeList = gson.fromJson(jsonData, Employee[].class);
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           playTheGame();
                       }
                   });




                }
            });


        }
        else {
            Toast.makeText(MainActivity.this, "Network is unreachable", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        boolean isAvailable = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return  isAvailable;
    }

}
