package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.fragment.edit.profile.OrdersPagesAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView cancel = findViewById(R.id.close);
        cancel.setOnClickListener(v -> { startActivity(new Intent(ProfileActivity.this, MapsActivity.class)); });

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new OrdersPagesAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tab);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
           switch (position){
               case 0: {
                   tab.setText("Имя");

                   break;
               }
               case 1: {
                   tab.setText("Пароль");

                   break;
               }
               case 2: {
                   tab.setText("Почта");

                   break;
               }

           }
        });
        tabLayoutMediator.attach();
    }

}