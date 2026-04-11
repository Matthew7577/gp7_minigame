package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreboardActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        LinearLayout container = findViewById(R.id.scoreboardContainer);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        List<ScoreEntry> scores = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue() instanceof String) {
                if (!key.equals("currentUser")) {
                    int completedLevels = getCompletedLevels(prefs, key);
                    scores.add(new ScoreEntry(key, completedLevels));
                }
            }
        }

        scores.sort((a, b) -> Integer.compare(b.score, a.score));

        int rank = 1;
        for (ScoreEntry entry : scores) {
            TextView tv = new TextView(this);
            tv.setText(rank + ". " + entry.username + " - " + entry.score + " levels");
            tv.setTextSize(32f);
            tv.setPadding(0, 48, 0, 48);
            tv.setTextColor(Color.BLACK);
            container.addView(tv);
            rank++;
        }
    }

    private int getCompletedLevels(SharedPreferences prefs, String username) {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            if (prefs.getBoolean(username + "_level_" + i + "_finished", false)) {
                count++;
            }
        }
        return count;
    }

    private static class ScoreEntry {
        String username;
        int score;

        ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}
