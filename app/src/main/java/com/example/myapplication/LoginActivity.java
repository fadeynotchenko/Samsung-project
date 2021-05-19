package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView email;
    private TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button log = findViewById(R.id.log);
        TextView signIn = findViewById(R.id.signIn);
        email = findViewById(R.id.emailSignIn);
        pass = findViewById(R.id.passSignIn);
        TextView text = findViewById(R.id.textView);
        TextView textView4 = findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();

        AnimView animView = new AnimView(email, pass, text, textView4, signIn, log);
        animView.animLogin();

        getCurrentUser();

        log.setOnClickListener(v -> LoginUser());

        text.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

    }

    private void LoginUser() {
        String emailAuth = email.getText().toString().trim();
        String passAuth = pass.getText().toString().trim();

        if (passAuth.isEmpty()) {
            pass.setError(getString(R.string.nameEmpty));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAuth).matches()) {
            email.setError(getString(R.string.emailEmpty));
            return;
        }

        mAuth.signInWithEmailAndPassword(emailAuth, passAuth).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getCurrentUser();
            } else {
                Toast.makeText(this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentUser() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
        }
    }


}