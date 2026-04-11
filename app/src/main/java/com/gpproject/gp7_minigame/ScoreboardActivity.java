package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreboardActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewScoreboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        ScoreboardAdapter adapter = new ScoreboardAdapter(scores);
        recyclerView.setAdapter(adapter);
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

    private static class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {
        private final List<ScoreEntry> scores;

        ScoreboardAdapter(List<ScoreEntry> scores) {
            this.scores = scores;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tv.setTextSize(32f);
            tv.setTextColor(0xFF000000); // Color.BLACK
            tv.setPadding(32, 48, 32, 48); // Adding some padding to match previous look
            return new ViewHolder(tv);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ScoreEntry entry = scores.get(position);
            int rank = position + 1;
            holder.tvScore.setText(rank + ". " + entry.username + " - " + entry.score + " levels");
        }

        @Override
        public int getItemCount() {
            return scores.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvScore;

            ViewHolder(View view) {
                super(view);
                tvScore = (TextView) view;
            }
        }
    }
}
