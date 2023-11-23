package com.example.woodonggo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;



public class MypageInterest extends AppCompatActivity {


    RecyclerView recyclerView;
    RadioGroup matchGroup;
    RadioButton all_btn, personal_btn, team_btn;
    Mypage_interest_RecyclerView_Adapter adapter; // Adapter를 멤버 변수로 선언
    private List<Model_interest> dataList; // 데이터 목록을 멤버 변수로 선언

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_interest);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        matchGroup = findViewById(R.id.match_Group);
        all_btn = findViewById(R.id.all_btn);
        personal_btn = findViewById(R.id.personal_btn);
        team_btn = findViewById(R.id.team_btn);
        recyclerView = findViewById(R.id.recyclerView);
        dataList = getDataList(); // 데이터 목록 초기화
        adapter = new Mypage_interest_RecyclerView_Adapter(dataList); // 어댑터 초기화

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == android.R.id.home) {
                    onBackPressed();
                }
                return false;
            }
        });

        //클릭한 라디오 버튼에 해당하는 글자 색 변경 & 버튼에 해당하는 정보를 가진 리사이클러뷰로 업데이트
        matchGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateRecyclerViewContent(checkedId);
                if(checkedId == R.id.all_btn){
                    all_btn.setTextColor(Color.WHITE);
                    personal_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    team_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
                else if(checkedId == R.id.personal_btn){
                    personal_btn.setTextColor(Color.WHITE);
                    all_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    team_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
                else{
                    team_btn.setTextColor(Color.WHITE);
                    all_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    personal_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
            }
        });

        // RecyclerView 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener(new Mypage_interest_RecyclerView_Adapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {
                // 아이템 삭제 작업 수행
                dataList.remove(position); // 데이터 목록에서도 해당 아이템 제거
                adapter.notifyItemRemoved(position); // RecyclerView에 삭제된 것을 알림
            }
        });

    }

    private void updateRecyclerViewContent(int checkedId) {

        // 라디오 버튼에 따라 리사이클러뷰 내용 업데이트
        List<Model_interest> filteredDataList = new ArrayList<>();

        if (checkedId == R.id.all_btn) {
            filteredDataList.addAll(dataList);
        } else if (checkedId == R.id.personal_btn) {
            // 개인 관심사에 맞는 데이터만 필터링
            for (Model_interest item : dataList) {
                if (item.isPersonal()) {
                    filteredDataList.add(item);
                }
            }
        } else if (checkedId == R.id.team_btn) {
            // 팀 관심사에 맞는 데이터만 필터링
            for (Model_interest item : dataList) {
                if (item.isTeam()) {
                    filteredDataList.add(item);
                }
            }
        }

        // 어댑터에 필터링된 데이터 설정
        adapter.setData(filteredDataList);



    }

    private List<Model_interest> getDataList() {
        // 예제 데이터 생성
        List<Model_interest> dataList = new ArrayList<>();
        dataList.add(new Model_interest(R.drawable.camera, "도구리", "30분 전", "오늘 공원에서 배드민턴 하실 분", true, false));
        dataList.add(new Model_interest(R.drawable.camera, "도구리", "30분 전", "오늘 공원에서 배드민턴 하실 분", false, true));
        dataList.add(new Model_interest(R.drawable.camera, "도구리", "30분 전", "오늘 공원에서 배드민턴 하실 분", true, false));

        return dataList;
    }

}
