package com.gpproject.gp7_minigame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

	private static final String PREFS_NAME = "UserPrefs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		EditText usernameInput = findViewById(R.id.usernameInput);
		EditText passwordInput = findViewById(R.id.passwordInput);
		Button loginActionButton = findViewById(R.id.loginActionButton);
		Button createAccountButton = findViewById(R.id.createAccountButton);

		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		loginActionButton.setOnClickListener(v -> {
			String username = usernameInput.getText().toString().trim();
			String password = passwordInput.getText().toString().trim();

			if (username.isEmpty() || password.isEmpty()) {
				Toast.makeText(LoginPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
				return;
			}

			if (prefs.contains(username)) {
				String storedPassword = prefs.getString(username, "");
				if (password.equals(storedPassword)) {
					prefs.edit().putString("currentUser", username).apply();
					Toast.makeText(LoginPage.this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginPage.this, SelectLevelActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginPage.this, "Invalid password or account is being registered", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(LoginPage.this, "Account not found, please create one", Toast.LENGTH_SHORT).show();
			}
		});

		createAccountButton.setOnClickListener(v -> {
			String username = usernameInput.getText().toString().trim();
			String password = passwordInput.getText().toString().trim();

			if (username.isEmpty() || password.isEmpty()) {
				Toast.makeText(LoginPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
				return;
			}

			if (prefs.contains(username)) {
				Toast.makeText(LoginPage.this, "Account already exists", Toast.LENGTH_SHORT).show();
			} else {
				prefs.edit().putString(username, password)
						.putString("currentUser", username).apply();
				Toast.makeText(LoginPage.this, R.string.create_account_click_message, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginPage.this, SelectLevelActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
