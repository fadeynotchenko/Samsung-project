package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    private String id, zoom;
    private SharedPreferences sharedPreferences;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageView close = findViewById(R.id.closeSetting);
        EditText getZoom = findViewById(R.id.getZoom);

        close.setOnClickListener(v1 -> { finish(); });

        RadioGroup radioGroup = findViewById(R.id.radioGroup2);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        sharedPreferences = getSharedPreferences(emailUser, MODE_PRIVATE);

        Button button = findViewById(R.id.sv);
        button.setOnClickListener(v -> {

            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            zoom = getZoom.getText().toString().trim();
            editor.putString("zoom", zoom);
            editor.putString("map", radioButton.getText().toString());
            editor.apply();
            startActivity(new Intent(SettingActivity.this, MapsActivity.class));
        });

    }
}