package com.example.woodonggo;

import android.annotation.SuppressLint;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.woodonggo.Home.Home_category;
import com.example.woodonggo.Home.Home_notification;
import com.example.woodonggo.Home.Home_search;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentHome extends Fragment {

    FloatingActionButton floatingBtn;
    ImageView imgView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // 툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //플로팅 버튼
        floatingBtn = rootView.findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), com.example.woodonggo.Home.Home_posting.class);
                startActivity(intent);
            }
        });

        // 뷰페이저, 탭 레이아웃 초기화
        ViewPager2 viewPager = rootView.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        // 탭의 이름을 설정하기 위한 문자열 배열
        String[] tabTitles = {"팀", "개인"};

        // ViewPager2 어댑터 설정
        TabAdapter_main home_tabAdapter = new TabAdapter_main(requireActivity());
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

        // 팝업 메뉴
        ImageView imgView = rootView.findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        // 프래그먼트에서 옵션 메뉴 사용을 허용
        setHasOptionsMenu(true);

        return rootView;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.home_location_menu, popupMenu.getMenu());

        //팝업 메뉴 클릭 이벤트
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.neighborhood) {
                    //TODO: 우리 동네
                }
                else if(itemId == R.id.list_team) {
                    //TODO: 팀 목록
                }
                else {
                    //TODO:우리 동네 설정
                }
                return false;
            }
        });
        popupMenu.show();
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
            Intent intent = new Intent(getContext(), Home_notification.class);
            startActivity(intent);
        }
        return false;
    }
}