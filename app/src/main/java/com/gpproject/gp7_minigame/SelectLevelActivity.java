package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectLevelActivity extends AppCompatActivity {

    private final int[] buttonIds = {
        R.id.btnLevel1, R.id.btnLevel2, R.id.btnLevel3, R.id.btnLevel4, R.id.btnLevel5,
        R.id.btnLevel6, R.id.btnLevel7, R.id.btnLevel8, R.id.btnLevel9, R.id.btnLevel10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        Class<?>[] levelClasses = {
            Level1Activity.class, Level2Activity.class, Level3Activity.class, Level4Activity.class, Level5Activity.class,
            Level6Activity.class, Level7Activity.class, Level8Activity.class, Level9Activity.class, Level10Activity.class
        };

        for (int i = 0; i < buttonIds.length; i++) {
            final int level = i + 1;
            final int index = i;
            Button btn = findViewById(buttonIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    Log.d("SelectLevel", "Level " + level + " button clicked");
                    Toast.makeText(SelectLevelActivity.this, "Level " + level + " Selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectLevelActivity.this, levelClasses[index]);
                    startActivity(intent);
                });
            }
        }

        Button btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                Intent intent = new Intent(SelectLevelActivity.this, MainActivity.class);
                // Clear the back stack so the user cannot go back to the previous activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        Button btnScoreboard = findViewById(R.id.btnScoreboard);
        if (btnScoreboard != null) {
            btnScoreboard.setOnClickListener(v -> {
                Intent intent = new Intent(SelectLevelActivity.this, ScoreboardActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLevelProgress();
    }

    @SuppressLint("SetTextI18n")
    private void updateLevelProgress() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");

        int completedCount = 0;

        for (int i = 0; i < buttonIds.length; i++) {
            int level = i + 1;
            boolean isFinished = prefs.getBoolean(currentUser + "_level_" + level + "_finished", false);

            Button btn = findViewById(buttonIds[i]);
            if (btn != null) {
                if (isFinished) {
                    completedCount++;
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // Green
                } else {
                    // Default button color or reset it if user changes
                    // Using a parsed color for standard light gray button background
                    btn.setBackgroundTintList(null); // Removes custom tint, falling back to theme default
                }
            }
        }

        TextView textCompletedLevels = findViewById(R.id.textCompletedLevels);
        if (textCompletedLevels != null) {
            textCompletedLevels.setText("Completed: " + completedCount + "/10");
        }
    }
}
