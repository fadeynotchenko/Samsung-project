package com.example.myapplication.fragment.view;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.fragment.view.Fragment1;
import com.example.myapplication.fragment.view.Fragment3;

public class IntroActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ViewPager viewPager = findViewById(R.id.pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        animation = AnimationUtils.loadAnimation(this, R.anim.o_b_anim);
        viewPager.startAnimation(animation);

    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment1 tab1 = new Fragment1();
                    return tab1;
                case 1:
                    Fragment2 tab2 = new Fragment2();
                    return tab2;
                case 2:
                    Fragment3 tab3 = new Fragment3();
                    return tab3;

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}