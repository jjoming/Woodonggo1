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

import java.util.ArrayList;

public class Fragment_home extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //텍스트뷰
        TextView textView = rootView.findViewById(R.id.textView);

        //이미지뷰
        ImageView imgView = rootView.findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 바텀시트다이얼로그 나오게 하기
            }
        });

        ArrayList<String> testDataSet = new ArrayList<>(); // 테스트를 위한 더미데이터 생성
        for (int i = 0; i < 20; i++) {
            testDataSet.add("TEST DATA" + i);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        Home_Adapter adapter = new Home_Adapter(testDataSet);
        recyclerView.setAdapter(adapter);

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
        if(itemId == R.id.search) {
            Intent intent = new Intent(getContext().getApplicationContext(), Home_search.class);
            startActivity(intent);
        } else if (itemId == R.id.cartegory) {
            Intent intent = new Intent(getContext().getApplicationContext(), Home_category.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(); //TODO: intent값 넣기
            startActivity(intent);
        }
        return false;
    }
}