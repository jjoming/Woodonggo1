package com.example.woodonggo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Mypage_match_Adapter extends FragmentStateAdapter  {
    private static final int NUM_TABS = 3;

    public Mypage_match_Adapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Mypage_matching_Fragment();
            case 1:
                return new Mypage_matched_Fragment();
            case 2:
                return new Mypage_match_hide_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
