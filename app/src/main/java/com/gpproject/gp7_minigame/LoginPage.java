package com.gpproject.gp7_minigame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
			Log.d("LoginPage", "Login button clicked!");
			String username = usernameInput.getText().toString().trim();
			String password = passwordInput.getText().toString().trim();

			if (username.isEmpty() || password.isEmpty()) {
				Log.d("LoginPage", "Login failed: empty username or password");
				Toast.makeText(LoginPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
				return;
			}

			if (prefs.contains(username)) {
				Log.d("LoginPage", "Username found in Preferences: " + username);
				String storedPassword = prefs.getString(username, "");
				if (password.equals(storedPassword)) {
					Log.d("LoginPage", "Password matches! Logging in...");
					prefs.edit().putString("currentUser", username).apply();
					Log.d("LoginPage", "Account: " + username + " is login");
					Intent intent = new Intent(LoginPage.this, SelectLevelActivity.class);
					startActivity(intent);
					finish();
				} else {
					Log.d("LoginPage", "Login failed: invalid password for user: '" + username + "'");
					Toast.makeText(LoginPage.this, "Invalid password or account already exists", Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("LoginPage", "Login failed: account not found for user: '" + username + "'");
				Toast.makeText(LoginPage.this, "Account not found, please register", Toast.LENGTH_SHORT).show();
			}
		});

		createAccountButton.setOnClickListener(v -> {
			Log.d("LoginPage", "Create account button clicked!");
			String username = usernameInput.getText().toString().trim();
			String password = passwordInput.getText().toString().trim();
			Log.d("LoginPage", "Attempted to register username: " + username);

			if (username.isEmpty() || password.isEmpty()) {
				Log.d("LoginPage", "Registration failed: empty username or password");
				Toast.makeText(LoginPage.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
				return;
			}

			if (prefs.contains(username)) {
				Log.d("LoginPage", "Registration failed: account already exists for user: '" + username + "'");
				Toast.makeText(LoginPage.this, "Account already exists", Toast.LENGTH_SHORT).show();
			} else {
				Log.d("LoginPage", "Registering new account: " + username);
				prefs.edit().putString(username, password)
						.putString("currentUser", username).apply();
				Log.d("LoginPage", "Account: " + username + " is now register");
				Intent intent = new Intent(LoginPage.this, SelectLevelActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
