package com.example.myapplication.fragment.marker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MarkerPagesAdapter extends FragmentStateAdapter {

    public MarkerPagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FragmentActiveMarker();
        }
        return new FragmentMyMarker();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
