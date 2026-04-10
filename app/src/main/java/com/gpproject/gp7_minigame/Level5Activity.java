package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class Level5Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context mContext = this;
        setContentView(R.layout.activity_level_5);
        PauseMenuHelper.setupPauseButton(this);
        MinigameLogic minigameLogic = new MinigameLogic();
        minigameLogic.movetheButton(findViewById(R.id.area), findViewById(R.id.btnClickMe), findViewById(R.id.area), () -> false, findViewById(R.id.buttonZone));
        Button btnFinishLevel = findViewById(R.id.btnFinishLevel);
        if (btnFinishLevel != null) {
            btnFinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_5_finished", true).apply();
                    Toast.makeText(this, "Level 5 Finished!", Toast.LENGTH_SHORT).show();
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
                    prefs.edit().putBoolean(currentUser + "_level_2_finished", false).apply();
                    Toast.makeText(this, "Level 5 Unfinished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (minigameLogic.isClicked()){
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    String currentUser = prefs.getString("currentUser", "");
                    if (!currentUser.isEmpty()) {
                        prefs.edit().putBoolean(currentUser + "_level_5_finished", true).apply();
                        Toast.makeText(mContext, "Level 5 Finished!", Toast.LENGTH_SHORT).show();
                        finish(); // Return to SelectLevelActivity
                    } else {
                        Toast.makeText(mContext, "Not logged in!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    handler.post(this);
                }
                }
            };
        handler.post(runnable);
    }

}
