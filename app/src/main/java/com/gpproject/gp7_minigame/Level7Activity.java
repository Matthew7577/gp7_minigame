package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class Level7Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_7);
        PauseMenuHelper.setupPauseButton(this);

        Button btnFinishLevel = findViewById(R.id.btnFinishLevel);
        if (btnFinishLevel != null) {
            btnFinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_7_finished", true).apply();
                    Toast.makeText(this, "Level 7 Finished!", Toast.LENGTH_SHORT).show();
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
                    prefs.edit().putBoolean(currentUser + "_level_7_finished", false).apply();
                    Toast.makeText(this, "Level 7 Unfinished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    
}
