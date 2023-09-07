package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class Fragment_home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // 툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 뷰페이저, 탭 레이아웃 초기화
        ViewPager2 viewPager = rootView.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        // 탭의 이름을 설정하기 위한 문자열 배열
        String[] tabTitles = {"팀", "개인"};

        // ViewPager2 어댑터 설정
        Home_TabAdapter home_tabAdapter = new Home_TabAdapter(requireActivity());
        viewPager.setAdapter(home_tabAdapter);

        // TabLayout에 ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabTitles[position])).attach();

        // TabLayout의 탭 선택 이벤트 처리
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 탭이 선택됐을 때
                int position = tab.getPosition();
                // 선택된 탭의 위치(position)를 이용하여 원하는 동작을 수행
                if (position == 0) {
                    // 첫 번째 탭 선택 시 처리
                } else if (position == 1) {
                    // 두 번째 탭 선택 시 처리
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 탭이 선택되지 않은 상태로 변경됐을 때
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 이미 선택된 탭이 다시 선택되었을 때
            }
        });

        // 텍스트뷰
        TextView textView = rootView.findViewById(R.id.textView);

        // 이미지뷰
        ImageView imgView = rootView.findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 바텀시트다이얼로그 나오게 하기
            }
        });

        // 프래그먼트에서 옵션 메뉴 사용을 허용
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // 프래그먼트 내에서 메뉴를 처리
        inflater.inflate(R.menu.toolbar_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Intent intent = new Intent(getContext(), Home_search.class);
            startActivity(intent);
        } else if (itemId == R.id.cartegory) {
            Intent intent = new Intent(getContext(), Home_category.class);
            startActivity(intent);
        } else {
            // TODO: 필요한 작업 수행
        }
        return false;
    }
}