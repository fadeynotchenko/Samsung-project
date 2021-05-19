package com.example.myapplication.fragment.edit.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.edit.profile.FragmentEditEmail;
import com.example.myapplication.fragment.edit.profile.FragmentEditName;
import com.example.myapplication.fragment.edit.profile.FragmentEditPass;

public class OrdersPagesAdapter extends FragmentStateAdapter {

    public OrdersPagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new FragmentEditName();
            case 1:
                return new FragmentEditPass();
            default:
                return new FragmentEditEmail();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
