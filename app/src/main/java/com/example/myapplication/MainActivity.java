package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.fragment.view.IntroActivity;

public class MainActivity extends AppCompatActivity {

    private static final int TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return;
                } else {
                    startActivity(new Intent(MainActivity.this, IntroActivity.class));
                    finish();
                }
            }
        }, TIME );

    }

}