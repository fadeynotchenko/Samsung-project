package com.example.myapplication.fragment.marker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.fragment.edit.profile.OrdersPagesAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyMarkerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_marker);

        ImageView cancel = findViewById(R.id.closeV);
        cancel.setOnClickListener(v -> {
            finish();
        });

        ViewPager2 viewPager2 = findViewById(R.id.viewPager22);
        viewPager2.setAdapter(new MarkerPagesAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tab2);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0: {
                    tab.setText("Активный маркер");

                    break;
                }
                case 1: {
                    tab.setText("Мой маркер");

                    break;
                }


            }
        });
        tabLayoutMediator.attach();
    }

}
