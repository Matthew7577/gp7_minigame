package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Level1Activity extends AppCompatActivity {
    Button btnClickMe;
    Boolean clicked = false;
    ViewGroup root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_1);
        PauseMenuHelper.setupPauseButton(this);

        btnClickMe = findViewById(R.id.btnClickMe);
        root = findViewById(R.id.move_area);

        MinigameLogic.moveButton(root, btnClickMe, root, () -> clicked, 1000);

        if (btnClickMe != null) {
            btnClickMe.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                clicked = true;
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_1_finished", true).apply();
                    Toast.makeText(this, "Level 1 Finished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        Button btnUnfinishLevel = findViewById(R.id.btnUnfinishLevel);
//        if (btnUnfinishLevel != null) {
//            btnUnfinishLevel.setOnClickListener(v -> {
//                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//                String currentUser = prefs.getString("currentUser", "");
//                if (!currentUser.isEmpty()) {
//                    prefs.edit().putBoolean(currentUser + "_level_1_finished", false).apply();
//                    Toast.makeText(this, "Level 1 Unfinished!", Toast.LENGTH_SHORT).show();
//                    finish(); // Return to SelectLevelActivity
//                } else {
//                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
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
