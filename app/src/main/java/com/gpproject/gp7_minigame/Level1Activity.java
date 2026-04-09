package com.gpproject.gp7_minigame;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import java.util.Random;
public class Level1Activity extends AppCompatActivity {
    Button btnClickMe;
    Boolean clicked = false;
    private int deltaX, deltaY;
    ViewGroup root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_1);
        PauseMenuHelper.setupPauseButton(this);

        btnClickMe = findViewById(R.id.btnClickMe);
        root = findViewById(R.id.move_area);
        moveButton();
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
        Button btnUnfinishLevel = findViewById(R.id.btnUnfinishLevel);
        if (btnUnfinishLevel != null) {
            btnUnfinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_1_finished", false).apply();
                    Toast.makeText(this, "Level 1 Unfinished!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to SelectLevelActivity
                } else {
                    Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void moveButton() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ViewGroup main = findViewById(R.id.move_area);
                Random rand = new Random();
                final int X = rand.nextInt(main.getWidth()-btnClickMe.getWidth()-2);
                final int Y = rand.nextInt(main.getHeight()-btnClickMe.getHeight()-2);
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) btnClickMe.getLayoutParams();
                deltaX = X - lParams.leftMargin;
                deltaY = Y - lParams.topMargin;
                Log.d("Level1Activity", "Moving button to: (" + X + ", " + Y + ") Delta: (" + deltaX + ", " + deltaY + ")");
                lParams.leftMargin += deltaX;
                lParams.topMargin += deltaY;
                btnClickMe.setLayoutParams(lParams);
                if (clicked){
                    return;
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable); // Runs on UI thread
        // Delayed execution (2 seconds)
        root.invalidate();
    }

    
}
