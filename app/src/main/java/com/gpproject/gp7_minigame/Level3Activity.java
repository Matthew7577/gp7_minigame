package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class Level3Activity extends AppCompatActivity {

    private List<Button> sequenceButtons;
    private int nextExpected = 1;
    private final int totalSteps = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_3);
        PauseMenuHelper.setupPauseButton(this);

        Button btnSeq1 = findViewById(R.id.btnSeq1);
        Button btnSeq2 = findViewById(R.id.btnSeq2);
        Button btnSeq3 = findViewById(R.id.btnSeq3);
        Button btnSeq4 = findViewById(R.id.btnSeq4);

        sequenceButtons = Arrays.asList(btnSeq1, btnSeq2, btnSeq3, btnSeq4);

        btnSeq1.setText("1");
        btnSeq2.setText("2");
        btnSeq3.setText("3");
        btnSeq4.setText("4");

        View.OnClickListener sequenceListener = v -> {
            Button btn = (Button) v;
            String txt = btn.getText().toString();
            int num;
            try {
                num = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                return;
            }

            if (num == nextExpected) {
                btn.setEnabled(false);
                btn.setAlpha(0.4f);
                nextExpected++;

                if (nextExpected > totalSteps) {
                    completeLevel();
                }
            } else {
                Toast.makeText(this, "Incorrect order! Start over.", Toast.LENGTH_SHORT).show();
                resetSequence();
            }
        };

        for (Button btn : sequenceButtons) {
            btn.setOnClickListener(sequenceListener);
        }

        /*Button btnFinishLevel = findViewById(R.id.btnFinishLevel);
        if (btnFinishLevel != null) {
            btnFinishLevel.setOnClickListener(v -> completeLevel());
        }

        Button btnUnfinishLevel = findViewById(R.id.btnUnfinishLevel);
        if (btnUnfinishLevel != null) {
            btnUnfinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_3_finished", false).apply();
                    Toast.makeText(this, "Level 3 incomplete", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }*/
    }

    private void resetSequence() {
        nextExpected = 1;
        for (Button btn : sequenceButtons) {
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        }
    }

    private void completeLevel() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");
        if (!currentUser.isEmpty()) {
            prefs.edit().putBoolean(currentUser + "_level_3_finished", true).apply();
            Toast.makeText(this, "Level 3 finished", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}