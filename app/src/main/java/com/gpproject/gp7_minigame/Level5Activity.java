package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Level5Activity extends AppCompatActivity {

    private Button draggableBtn;
    private ImageView target;
    private RelativeLayout rootLayout;

    private int deltaX, deltaY;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_5);
        PauseMenuHelper.setupPauseButton(this);

        draggableBtn = findViewById(R.id.btnDrag);
        target = findViewById(R.id.targetArea);
        rootLayout = findViewById(R.id.rootLayout);

        setupDragListener();

        rootLayout.post(() -> {
            int maxX = rootLayout.getWidth() - target.getWidth();
            int maxY = rootLayout.getHeight() - target.getHeight();
            if (maxX > 0 && maxY > 0) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) target.getLayoutParams();
                params.leftMargin = random.nextInt(maxX);
                params.topMargin = random.nextInt(maxY);
                target.setLayoutParams(params);
            }
        });

        startCompletionCheck();

        Button btnReset = findViewById(R.id.btnReset);
        if (btnReset != null) {
            btnReset.setOnClickListener(v -> resetPositions());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDragListener() {
        draggableBtn.setOnTouchListener((v, event) -> {
            final int rawX = (int) event.getRawX();
            final int rawY = (int) event.getRawY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) draggableBtn.getLayoutParams();
                    deltaX = rawX - lParams.leftMargin;
                    deltaY = rawY - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (MinigameLogic.isGamePaused) return true;
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) draggableBtn.getLayoutParams();
                    params.leftMargin = rawX - deltaX;
                    params.topMargin = rawY - deltaY;
                    params.leftMargin = Math.max(0, Math.min(params.leftMargin, rootLayout.getWidth() - draggableBtn.getWidth()));
                    params.topMargin = Math.max(0, Math.min(params.topMargin, rootLayout.getHeight() - draggableBtn.getHeight()));
                    draggableBtn.setLayoutParams(params);
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    break;
            }
            return true;
        });
    }

    private void startCompletionCheck() {
        final Handler checkHandler = new Handler();
        final Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (isDraggedIntoTarget()) {
                    completeLevel();
                } else {
                    checkHandler.postDelayed(this, 200);
                }
            }
        };
        checkHandler.post(checkRunnable);
    }

    private boolean isDraggedIntoTarget() {
        if (draggableBtn == null || target == null) return false;
        int DEVIATION = 60;
        int btnLeft = draggableBtn.getLeft();
        int btnRight = draggableBtn.getRight();
        int btnTop = draggableBtn.getTop();
        int btnBottom = draggableBtn.getBottom();

        int targetLeft = target.getLeft();
        int targetRight = target.getRight();
        int targetTop = target.getTop();
        int targetBottom = target.getBottom();

        int btnCenterX = (btnLeft + btnRight) / 2;
        int btnCenterY = (btnTop + btnBottom) / 2;
        int targetCenterX = (targetLeft + targetRight) / 2;
        int targetCenterY = (targetTop + targetBottom) / 2;

        double distance = Math.hypot(btnCenterX - targetCenterX, btnCenterY - targetCenterY);
        return distance <= DEVIATION;
    }

    private void resetPositions() {
        RelativeLayout.LayoutParams btnParams = (RelativeLayout.LayoutParams) draggableBtn.getLayoutParams();
        btnParams.leftMargin = 50;
        btnParams.topMargin = 200;
        draggableBtn.setLayoutParams(btnParams);
    }

    private void completeLevel() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");
        if (!currentUser.isEmpty()) {
            prefs.edit().putBoolean(currentUser + "_level_5_finished", true).apply();
            Toast.makeText(this, "Level 5 Finished! Great job!", Toast.LENGTH_SHORT).show();
        }
        finish();
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
