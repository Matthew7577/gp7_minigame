package com.gpproject.gp7_minigame;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

import javax.net.ssl.HandshakeCompletedEvent;

public class MinigameLogic implements View.OnTouchListener {
private int deltaX;
    private int deltaY;
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
    public void movetheButton(ViewGroup main, Button btn, ViewGroup root, GameState gameState, ImageView btnZone) {
        Log.d("MinigameLogic", "bye");
        btn.setOnTouchListener(new View.OnTouchListener (){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                    if (btn.getLeft() >= btnZone.getX()-20 && btn.getLeft() <= btnZone.getX()+20
                            && btn.getY() <= btnZone.getY()+20 && btn.getY() >= btnZone.getY()-20) {
                        Log.d("MinigameLogic", "Button in zone, stopping movement.");
                        clicked = true;

                    }
                }
                root.invalidate();
                return false;
            }
        });
    }
    public boolean isClicked(){
        return clicked;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
