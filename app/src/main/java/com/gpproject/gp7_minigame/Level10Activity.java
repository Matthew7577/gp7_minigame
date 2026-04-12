package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Level10Activity extends AppCompatActivity {

    private TextView tvTimer, tvInstruction, tvProgress, tvScore;
    private Button targetButton;
    private List<Button> sequenceButtons;
    private int currentTask = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private final Handler handler = new Handler();
    private final Random random = new Random();

    private final int TOTAL_TASKS = 4;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_10);
        PauseMenuHelper.setupPauseButton(this);

        tvTimer = findViewById(R.id.tvTimer);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvProgress = findViewById(R.id.tvProgress);
        tvScore = findViewById(R.id.tvScore);
        targetButton = findViewById(R.id.targetButton);

        Button btnSeq1 = findViewById(R.id.btnSeq1);
        Button btnSeq2 = findViewById(R.id.btnSeq2);
        Button btnSeq3 = findViewById(R.id.btnSeq3);
        Button btnSeq4 = findViewById(R.id.btnSeq4);
        sequenceButtons = Arrays.asList(btnSeq1, btnSeq2, btnSeq3, btnSeq4);

        btnSeq1.setText("1");
        btnSeq2.setText("2");
        btnSeq3.setText("3");
        btnSeq4.setText("4");

        startGame();

        Button btnFinishLevel = findViewById(R.id.btnFinishLevel);
        if (btnFinishLevel != null) {
            btnFinishLevel.setOnClickListener(v -> completeLevel());
        }

        Button btnUnfinishLevel = findViewById(R.id.btnUnfinishLevel);
        if (btnUnfinishLevel != null) {
            btnUnfinishLevel.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String currentUser = prefs.getString("currentUser", "");
                if (!currentUser.isEmpty()) {
                    prefs.edit().putBoolean(currentUser + "_level_10_finished", false).apply();
                    Toast.makeText(this, "Level 10 Unfinished!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void startGame() {
        currentTask = 0;
        score = 0;
        updateScoreDisplay();
        updateProgressDisplay();
        startTimer();
        nextTask();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        int TIME_LIMIT = 60;
        countDownTimer = new CountDownTimer(TIME_LIMIT * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvTimer != null) {
                    tvTimer.setText("Time: " + millisUntilFinished / 1000 + "s");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                if (tvTimer != null) {
                    tvTimer.setText("Time's Up!");
                }
                Toast.makeText(Level10Activity.this, "Time's up! Challenge failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();
    }

    private void nextTask() {
        if (currentTask >= TOTAL_TASKS) {
            completeLevel();
            return;
        }

        currentTask++;
        updateProgressDisplay();

        switch (currentTask) {
            case 1:
                setupClickTask();
                break;
            case 2:
                setupSequenceTask();
                break;
            case 3:
                setupMemoryTask();
                break;
            case 4:
                setupFinalReactionTask();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupClickTask() {
        if (tvInstruction != null) {
            tvInstruction.setText("Task 1: Click button 8 times");
        }
        if (targetButton != null) {
            targetButton.setVisibility(View.VISIBLE);
            targetButton.setText("Click Me");

            targetButton.setOnClickListener(new View.OnClickListener() {
                int clickCount = 0;

                @Override
                public void onClick(View v) {
                    clickCount++;
                    score += 15;
                    updateScoreDisplay();
                    Toast.makeText(Level10Activity.this, clickCount + "/8", Toast.LENGTH_SHORT).show();

                    if (clickCount >= 8) {
                        targetButton.setOnClickListener(null);
                        targetButton.setVisibility(View.GONE);
                        score += 60;
                        updateScoreDisplay();
                        nextTask();
                    }
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupSequenceTask() {
        if (tvInstruction != null) {
            tvInstruction.setText("Task 2: Click in order 1 -> 2 -> 3 -> 4");
        }

        for (Button btn : sequenceButtons) {
            btn.setVisibility(View.VISIBLE);
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        }

        final int[] step = {0};
        String[] order = {"1", "2", "3", "4"};

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;
            if (btn.getText().toString().equals(order[step[0]])) {
                btn.setEnabled(false);
                btn.setAlpha(0.4f);
                step[0]++;
                score += 25;
                updateScoreDisplay();

                if (step[0] >= 4) {
                    for (Button b : sequenceButtons) {
                        b.setOnClickListener(null);
                        b.setVisibility(View.GONE);
                    }
                    score += 100;
                    updateScoreDisplay();
                    nextTask();
                }
            } else {
                Toast.makeText(Level10Activity.this, "Wrong order! Start over", Toast.LENGTH_SHORT).show();
                step[0] = 0;
                for (Button b : sequenceButtons) {
                    b.setEnabled(true);
                    b.setAlpha(1.0f);
                }
            }
        };

        for (Button btn : sequenceButtons) {
            btn.setOnClickListener(listener);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupMemoryTask() {
        if (tvInstruction != null) {
            tvInstruction.setText("Task 3: Remember the order and click");
        }

        for (Button btn : sequenceButtons) {
            btn.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
            btn.setAlpha(0.6f);
        }

        // Generate random order
        List<Integer> order = Arrays.asList(1, 2, 3, 4);
        java.util.Collections.shuffle(order);

        final int[] sequenceIndex = {0};

        // Show the sequence
        for (int i = 0; i < order.size(); i++) {
            final int buttonIndex = order.get(i) - 1;
            handler.postDelayed(() -> {
                Button btn = sequenceButtons.get(buttonIndex);
                btn.setAlpha(0.3f);
                handler.postDelayed(() -> btn.setAlpha(0.6f), 300);
            }, i * 700L);
        }

        // Enable clicking
        handler.postDelayed(() -> {
            if (tvInstruction != null) {
                tvInstruction.setText("Task 3: Repeat the order");
            }
            for (Button btn : sequenceButtons) {
                btn.setEnabled(true);
                btn.setAlpha(1.0f);
            }

            View.OnClickListener listener = v -> {
                Button btn = (Button) v;
                int pressedNum = Integer.parseInt(btn.getText().toString());

                if (pressedNum == order.get(sequenceIndex[0])) {
                    btn.setEnabled(false);
                    btn.setAlpha(0.4f);
                    sequenceIndex[0]++;
                    score += 30;
                    updateScoreDisplay();

                    if (sequenceIndex[0] >= order.size()) {
                        for (Button b : sequenceButtons) {
                            b.setOnClickListener(null);
                            b.setVisibility(View.GONE);
                        }
                        score += 120;
                        updateScoreDisplay();
                        nextTask();
                    }
                } else {
                    Toast.makeText(Level10Activity.this, "Wrong order!", Toast.LENGTH_SHORT).show();
                    sequenceIndex[0] = 0;
                    for (Button b : sequenceButtons) {
                        b.setEnabled(true);
                        b.setAlpha(1.0f);
                    }
                }
            };

            for (Button btn : sequenceButtons) {
                btn.setOnClickListener(listener);
            }
        }, order.size() * 700L + 500);
    }

    @SuppressLint("SetTextI18n")
    private void setupFinalReactionTask() {
        if (tvInstruction != null) {
            tvInstruction.setText("Final Task: Click moving button 5 times");
        }
        if (targetButton != null) {
            targetButton.setVisibility(View.VISIBLE);
            targetButton.setText("Click Fast");

            // Make button move randomly
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (targetButton.getVisibility() == View.VISIBLE && currentTask == 4) {
                        ViewGroup parent = (ViewGroup) targetButton.getParent();
                        if (parent != null) {
                            int maxX = parent.getWidth() - targetButton.getWidth();
                            int maxY = parent.getHeight() - targetButton.getHeight();
                            if (maxX > 0 && maxY > 0) {
                                targetButton.setX(random.nextInt(maxX));
                                targetButton.setY(random.nextInt(maxY));
                            }
                        }
                        handler.postDelayed(this, 500);
                    }
                }
            });

            targetButton.setOnClickListener(new View.OnClickListener() {
                int hitCount = 0;

                @Override
                public void onClick(View v) {
                    hitCount++;
                    score += 40;
                    updateScoreDisplay();
                    Toast.makeText(Level10Activity.this, "Hit! " + hitCount + "/5", Toast.LENGTH_SHORT).show();

                    if (hitCount >= 5) {
                        handler.removeCallbacksAndMessages(null);
                        targetButton.setOnClickListener(null);
                        targetButton.setVisibility(View.GONE);
                        score += 150;
                        updateScoreDisplay();
                        nextTask();
                    }
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateScoreDisplay() {
        if (tvScore != null) {
            tvScore.setText("Score: " + score);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateProgressDisplay() {
        if (tvProgress != null) {
            tvProgress.setText("Task: " + currentTask + "/" + TOTAL_TASKS);
        }
    }

    private void completeLevel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUser = prefs.getString("currentUser", "");
        if (!currentUser.isEmpty()) {
            prefs.edit().putBoolean(currentUser + "_level_10_finished", true).apply();

            int totalScore = prefs.getInt(currentUser + "_totalScore", 0);
            int levelScore = 500 + score;
            prefs.edit().putInt(currentUser + "_totalScore", totalScore + levelScore).apply();

            Toast.makeText(this, "🎉 Congratulations! You completed Level 10! +" + levelScore + " points 🎉", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }

    private boolean wasPaused = false;

    @Override
    protected void onPause() {
        super.onPause();
        MinigameLogic.isGamePaused = true;
        wasPaused = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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