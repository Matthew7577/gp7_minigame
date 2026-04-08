package com.gpproject.gp7_minigame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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

        for (int i = 0; i < buttonIds.length; i++) {
            final int level = i + 1;
            Button btn = findViewById(buttonIds[i]);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    Toast.makeText(SelectLevelActivity.this, "Level " + level + " Selected", Toast.LENGTH_SHORT).show();
                    // TODO: Start relevant Level Activity
                });
            }
        }
    }
}
