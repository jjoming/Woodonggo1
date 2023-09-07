package com.example.woodonggo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Home_TabAdapter extends FragmentStateAdapter {
    public Home_TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new Home_Fragment_Team();
        }
        else {
            return new Home_Fragment_Personal();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
