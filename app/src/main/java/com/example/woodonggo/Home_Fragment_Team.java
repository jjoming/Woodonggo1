package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home_Fragment_Team extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.home_fragment_team, container, false);

        ArrayList<String> testDataSet = new ArrayList<>(); // 테스트를 위한 더미데이터 생성
        for (int i = 0; i < 20; i++) {
            testDataSet.add("TEST DATA" + i);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        Home_Adapter adapter = new Home_Adapter(testDataSet);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
