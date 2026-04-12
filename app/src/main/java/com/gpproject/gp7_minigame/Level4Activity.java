package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level4Activity extends AppCompatActivity {
    
    private Button[] buttons;
    private int[] originalColors;
    private List<Integer> sequence;
    private int playerStep = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_4);
        PauseMenuHelper.setupPauseButton(this);

        buttons = new Button[]{
                findViewById(R.id.btnColor1),
                findViewById(R.id.btnColor2),
                findViewById(R.id.btnColor3),
                findViewById(R.id.btnColor4)
        };

        originalColors = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.YELLOW
        };

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].setOnClickListener(v -> handlePlayerClick(index));
        }

        startGame();
    }

    private void startGame() {
        playerStep = 0;
        sequence = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sequence.add(i);
        }
        Collections.shuffle(sequence);

        for (Button btn : buttons) {
            btn.setBackgroundColor(Color.GRAY);
            btn.setEnabled(false);
        }

        playSequence(0);
    }

    private void playSequence(int step) {
        if (step >= sequence.size()) {
            for (Button btn : buttons) {
                btn.setEnabled(true);
            }
            return;
        }

        int btnIndex = sequence.get(step);
        
        handler.postDelayed(() -> {
            if (MinigameLogic.isGamePaused) {
                playSequence(step);
                return;
            }
            buttons[btnIndex].setBackgroundColor(originalColors[btnIndex]);
            
            handler.postDelayed(() -> {
                if (MinigameLogic.isGamePaused) {
                    buttons[btnIndex].setBackgroundColor(Color.GRAY);
                    playSequence(step);
                    return;
                }
                buttons[btnIndex].setBackgroundColor(Color.GRAY);
                playSequence(step + 1);
            }, 500);

        }, 500);
    }

    private void handlePlayerClick(int index) {
        buttons[index].setBackgroundColor(originalColors[index]);

        if (sequence.get(playerStep) == index) {
            playerStep++;
            if (playerStep == sequence.size()) {
                completeLevel();
            }
        } else {
            Toast.makeText(this, "Wrong! Try again.", Toast.LENGTH_SHORT).show();
            for (Button btn : buttons) {
                btn.setEnabled(false);
            }
            handler.postDelayed(this::startGame, 1000);
        }
    }

    private void completeLevel() {
        for (Button btn : buttons) {
            btn.setEnabled(false);
        }
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");
        if (!currentUser.isEmpty()) {
            prefs.edit().putBoolean(currentUser + "_level_4_finished", true).apply();
            Toast.makeText(this, "Level 4 Finished!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
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
