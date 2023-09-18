package com.example.woodonggo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class Home_notification extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    Adapter_notify_like adapter_notify_like; // Adapter를 멤버 변수로 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_notification);

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 뷰페이저, 탭 레이아웃
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // ViewPager2 어댑터 설정
        TabAdapter_notification tabAdapter_notification = new TabAdapter_notification(this);
        viewPager.setAdapter(tabAdapter_notification);

        // 탭의 이름을 설정하기 위한 문자열 배열
        String[] tabTitles = {"좋아요 알림", "대결 신청 알림"};

        // TabLayout에 ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabTitles[position])).attach();

        // Adapter를 초기화
        adapter_notify_like = new Adapter_notify_like(new ArrayList<>());
    }

    private boolean isEditMode = false; // "편집" 모드 여부를 나타내는 변수

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(isEditMode ? R.menu.toolbar_home_notification2 : R.menu.toolbar_home_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.editer) {
            isEditMode = true;
            invalidateOptionsMenu();
            // "편집" 모드로 전환 시 프래그먼트의 showImageView 호출
            Home_notification_like fragment = (Home_notification_like) getSupportFragmentManager().findFragmentByTag("f0");
            if (fragment != null) {
                fragment.showImageView();
            }
        } else if (itemId == R.id.close) {
            isEditMode = false;
            invalidateOptionsMenu();
            // "닫기" 모드로 전환 시 프래그먼트의 hideImageView 호출
            Home_notification_like fragment = (Home_notification_like) getSupportFragmentManager().findFragmentByTag("f0");
            if (fragment != null) {
                fragment.hideImageView();
            }
        }
        return true;
    }
}