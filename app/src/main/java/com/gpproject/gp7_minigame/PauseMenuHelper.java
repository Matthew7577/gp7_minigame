package com.gpproject.gp7_minigame;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PauseMenuHelper {

    public static void showPauseMenu(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_pause_menu, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        Button btnResume = view.findViewById(R.id.btnResume);
        Button btnRestart = view.findViewById(R.id.btnRestart);
        Button btnReturn = view.findViewById(R.id.btnReturn);

        MinigameLogic.isGamePaused = true;

        btnResume.setOnClickListener(v -> {
            MinigameLogic.isGamePaused = false;
            dialog.dismiss();
        });
        btnRestart.setOnClickListener(v -> {
            MinigameLogic.isGamePaused = false;
            dialog.dismiss();
            activity.finish();
            activity.startActivity(activity.getIntent());
        });
        btnReturn.setOnClickListener(v -> {
            MinigameLogic.isGamePaused = false;
            dialog.dismiss();
            activity.finish();
        });

        dialog.show();
    }

    public static void setupPauseButton(Activity activity) {
        Button btnPause = activity.findViewById(R.id.btnPause);
        if (btnPause != null) {
            btnPause.setOnClickListener(v -> showPauseMenu(activity));
        }
    }
}
