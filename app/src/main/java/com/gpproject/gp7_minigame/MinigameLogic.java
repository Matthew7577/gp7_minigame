package com.gpproject.gp7_minigame;

import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

public class MinigameLogic {
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
}
