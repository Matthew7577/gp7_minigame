package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.Random;

public class MinigameLogic implements View.OnTouchListener {
private int deltaX;
    private int deltaY;
public static boolean isGamePaused = false;
private boolean clicked = false;
    public interface GameState {
        boolean isFinished();
    }

    public static void moveButton(ViewGroup main, Button btnClickMe, ViewGroup root, GameState gameState, int delayMillis) {
        final Handler handler = new Handler();
        Log.d("MinigameLogic", "Start moveButton.");
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isGamePaused) {
                    if (gameState == null || !gameState.isFinished()) {
                        handler.postDelayed(this, delayMillis);
                    }
                    return;
                }
                if (main != null && btnClickMe != null && main.getWidth() > 0 && main.getHeight() > 0) {
                    Random rand = new Random();
                    int xBound = main.getWidth() - btnClickMe.getWidth() - 2;
                    int yBound = main.getHeight() - btnClickMe.getHeight() - 2;

                    if (xBound > 0 && yBound > 0) {
                        final int X = rand.nextInt(xBound);
                        final int Y = rand.nextInt(yBound);
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) btnClickMe.getLayoutParams();
                        int deltaX = X - lParams.leftMargin;
                        int deltaY = Y - lParams.topMargin;
                        Log.d("MinigameLogic", "Moving button to: (" + X + ", " + Y + ") Delta: (" + deltaX + ", " + deltaY + ")");
                        lParams.leftMargin += deltaX;
                        lParams.topMargin += deltaY;
                        btnClickMe.setLayoutParams(lParams);
                    }
                }

                if (gameState != null && gameState.isFinished()) {
                    Log.d("MinigameLogic", "Stop moveButton.");

                } else {
                    handler.postDelayed(this, delayMillis);
                }
            }
        };
        handler.post(runnable); // Runs on UI thread
        if (root != null) {
            root.invalidate();
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void moveTheButton(Button btn, ViewGroup root, ImageView btnZone, int deviation) {
        Log.d("MinigameLogic", "bye");
        btn.setOnTouchListener((v, event) -> {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
                deltaX = X - lParams.leftMargin;
                deltaY = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
                layoutParams.leftMargin = X - deltaX;
                layoutParams.topMargin = Y - deltaY;
                btn.setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("MinigameLogic", "Button released at: (" + btn.getLeft() + ", " + btn.getY() + ")");
                if (btn.getLeft() >= btnZone.getX()- deviation && btn.getLeft() <= btnZone.getX() + deviation
                        && btn.getY() <= btnZone.getY() + deviation && btn.getY() >= btnZone.getY() - deviation) {
                    Log.d("MinigameLogic", "Button in zone, stopping movement.");
                    clicked = true;

                }
            }
            root.invalidate();
            return false;
        });
    }
    public boolean isClicked(){
        return clicked;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public interface CatchBallsGameCallback {
        void onScoreUpdated(int newScore);
        void onGameWon();
    }

    public static void startCatchBallsGame(Context context, int screenWidth, int screenHeight, List<ImageView> ballPool, CatchBallsGameCallback callback) {
        for (ImageView ball : ballPool) {
            setupDrawables(ball);
        }

        final Handler handler = new Handler();
        final Random random = new Random();
        final int[] score = {0};
        final boolean[] isGameOver = {false};

        Runnable spawnRunnable = new Runnable() {
            @Override
            public void run() {
                if (isGameOver[0]) return;

                if (isGamePaused) {
                    handler.postDelayed(this, 100); // Check frequently while paused
                    return;
                }

                boolean isBlack = random.nextFloat() < 0.3f; // 30% chance of black ball

                ImageView availableBall = null;
                for (ImageView ball : ballPool) {
                    if (ball.getVisibility() == View.GONE) {
                        boolean ballIsBlack = "blackBall".equals(ball.getTag());
                        if (ballIsBlack == isBlack) {
                            availableBall = ball;
                            break;
                        }
                    }
                }

                if (availableBall != null) {
                    spawnCatchBall(context, screenWidth, screenHeight, score, isGameOver, callback, random, availableBall, isBlack);
                }

                handler.postDelayed(this, 800 + random.nextInt(400));
            }
        };

        handler.postDelayed(spawnRunnable, 1000);
    }

    private static void setupDrawables(ImageView ball) {
        boolean isBlack = "blackBall".equals(ball.getTag());
        if (isBlack) {
            android.graphics.drawable.Drawable customDrawable = new android.graphics.drawable.Drawable() {
                private final android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

                @Override
                public void draw(@androidx.annotation.NonNull android.graphics.Canvas canvas) {
                    int w = getBounds().width();
                    int h = getBounds().height();

                    paint.setStyle(android.graphics.Paint.Style.FILL);
                    paint.setColor(Color.BLACK);
                    canvas.drawOval(new android.graphics.RectF(0, 0, w, h), paint);

                    paint.setStyle(android.graphics.Paint.Style.STROKE);
                    paint.setColor(Color.RED);
                    paint.setStrokeWidth(10);
                    canvas.drawOval(new android.graphics.RectF(5, 5, w - 5, h - 5), paint);

                    float pad = w * 0.25f;
                    canvas.drawLine(pad, pad, w - pad, h - pad, paint);
                    canvas.drawLine(w - pad, pad, pad, h - pad, paint);
                }

                @Override
                public void setAlpha(int alpha) { paint.setAlpha(alpha); }

                @Override
                public void setColorFilter(@androidx.annotation.Nullable android.graphics.ColorFilter colorFilter) { paint.setColorFilter(colorFilter); }

                @Override
                public int getOpacity() { return android.graphics.PixelFormat.TRANSLUCENT; }
            };
            ball.setImageDrawable(customDrawable);
        } else {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.GREEN);
            ball.setImageDrawable(shape);
        }
    }

    private static void spawnCatchBall(Context context, int screenWidth, int screenHeight, int[] score, boolean[] isGameOver, CatchBallsGameCallback callback, Random random, ImageView ball, boolean isBlack) {
        // Assume all balls have fixed size from xml, e.g., 75dp
        // Note: constraint layout handles position with translation
        int size = ball.getLayoutParams().width;
        int startX = random.nextInt(Math.max(1, screenWidth - size));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ball.getLayoutParams();
        params.leftMargin = startX;
        params.topMargin = -size;
        ball.setLayoutParams(params);

        ball.setVisibility(View.VISIBLE);
        ball.setTranslationY(0f);

        attachCatchBallClickListener(context, ball, isBlack, score, isGameOver, callback);

        int duration = 2500 + random.nextInt(1500);
        animateCatchBall(ball, screenHeight, size, duration);
    }

    private static void attachCatchBallClickListener(Context context, ImageView ball, boolean isBlack, int[] score, boolean[] isGameOver, CatchBallsGameCallback callback) {
        ball.setOnClickListener(v -> {
            if (isGameOver[0]) return;

            ball.animate().cancel();
            ball.setVisibility(View.GONE);

            if (isBlack) {
                score[0] = 0;
                callback.onScoreUpdated(score[0]);
                Toast.makeText(context, "Score reset!", Toast.LENGTH_SHORT).show();
            } else {
                score[0]++;
                callback.onScoreUpdated(score[0]);
                if (score[0] >= 10) {
                    isGameOver[0] = true;
                    callback.onGameWon();
                }
            }
        });
    }

    private static void animateCatchBall(ImageView ball, int screenHeight, int size, int duration) {
        ball.animate().cancel();
        
        final Handler handler = new Handler();
        final long interval = 16;
        final float targetY = screenHeight + size;
        final float step = targetY / (duration / (float) interval);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ball.getVisibility() != View.VISIBLE) return;

                if (isGamePaused) {
                    handler.postDelayed(this, interval);
                    return;
                }

                float currentY = ball.getTranslationY();
                float newY = currentY + step;

                if (newY >= targetY) {
                    ball.setTranslationY(targetY);
                    ball.setVisibility(View.GONE);
                } else {
                    ball.setTranslationY(newY);
                    handler.postDelayed(this, interval);
                }
            }
        });
    }
}
