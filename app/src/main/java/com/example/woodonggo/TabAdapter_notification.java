package com.example.woodonggo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.woodonggo.Home.Home_notification;
import com.example.woodonggo.Home.Home_notification_fight;
import com.example.woodonggo.Home.Home_notification_like;

public class TabAdapter_notification extends FragmentStateAdapter {

    private Home_notification home_notification; // 프래그먼트를 멤버 변수로 선언

    public TabAdapter_notification(@NonNull FragmentActivity fragmentActivity, Home_notification home_notification) {
        super(fragmentActivity);
        this.home_notification = home_notification; // home_notification 객체 초기화
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new Home_notification_like();
        } else {
            return new Home_notification_fight();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}