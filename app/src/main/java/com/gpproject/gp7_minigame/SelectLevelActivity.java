package com.gpproject.gp7_minigame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class SelectLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        int[] buttonIds = {
            R.id.btnLevel1, R.id.btnLevel2, R.id.btnLevel3, R.id.btnLevel4, R.id.btnLevel5,
            R.id.btnLevel6, R.id.btnLevel7, R.id.btnLevel8, R.id.btnLevel9, R.id.btnLevel10
        };

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
                Toast.makeText(SelectLevelActivity.this, "Scoreboard Selected", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
