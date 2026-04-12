package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Level8Activity extends AppCompatActivity {
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_8);
        PauseMenuHelper.setupPauseButton(this);

        tvScore = findViewById(R.id.tvScore);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        List<ImageView> ballPool = new ArrayList<>();
        ballPool.add(findViewById(R.id.greenBall1));
        ballPool.add(findViewById(R.id.greenBall2));
        ballPool.add(findViewById(R.id.greenBall3));
        ballPool.add(findViewById(R.id.greenBall4));
        ballPool.add(findViewById(R.id.greenBall5));
        ballPool.add(findViewById(R.id.blackBall1));
        ballPool.add(findViewById(R.id.blackBall2));
        ballPool.add(findViewById(R.id.blackBall3));

        MinigameLogic.startCatchBallsGame(this, screenWidth, screenHeight, ballPool, new MinigameLogic.CatchBallsGameCallback() {
            @Override
            public void onScoreUpdated(int newScore) {
                tvScore.setText("Score: " + newScore + " / 10");
            }

            @Override
            public void onGameWon() {
                winGame();
            }
        });

        Button btnFinishLevel = findViewById(R.id.btnFinishLevel);
        if (btnFinishLevel != null) {
            btnFinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_8_finished", true).apply();
                    Toast.makeText(this, "Level 8 Finished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Button btnUnfinishLevel = findViewById(R.id.btnUnfinishLevel);
        if (btnUnfinishLevel != null) {
            btnUnfinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_8_finished", false).apply();
                    Toast.makeText(this, "Level 8 Unfinished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void winGame() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");
        if (!currentUser.isEmpty()) {
            prefs.edit().putBoolean(currentUser + "_level_8_finished", true).apply();
            Toast.makeText(this, "Level 8 Finished!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private boolean wasPaused = false;
    @Override
    protected void onPause() {
        super.onPause();
        MinigameLogic.isGamePaused = true;
        wasPaused = true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (wasPaused) {
            PauseMenuHelper.showPauseMenu(this);
            wasPaused = false;
        } else {
            MinigameLogic.isGamePaused = false;
        }
    }
}
