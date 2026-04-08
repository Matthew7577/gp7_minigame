package com.gpproject.gp7_minigame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button loginActionButton = findViewById(R.id.loginActionButton);
		Button createAccountButton = findViewById(R.id.createAccountButton);

		loginActionButton.setOnClickListener(v -> Toast.makeText(
				LoginPage.this,
				R.string.login_success_message,
				Toast.LENGTH_SHORT
		).show());

		createAccountButton.setOnClickListener(v -> Toast.makeText(
				LoginPage.this,
				R.string.create_account_click_message,
				Toast.LENGTH_SHORT
		).show());
	}
}
