package com.example.woodonggo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabAdapter_notification extends FragmentStateAdapter {

    public TabAdapter_notification(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new Home_notification_like();
            //TODO: 편집 메뉴 보이게 하기
        }
        else {
            return new Home_notification_fight();
            //TODO: 편집 메뉴 안 보이게 하기
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}