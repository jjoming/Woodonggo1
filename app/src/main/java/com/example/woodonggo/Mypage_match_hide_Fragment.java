package com.example.woodonggo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class Mypage_match_hide_Fragment extends Fragment {
    private RecyclerView recyclerView; // RecyclerView 선언

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 프래그먼트 레이아웃을 인플레이트합니다.
        View view = inflater.inflate(R.layout.fragment_mypage_match_3, container, false);

        // RecyclerView와 연결
        recyclerView = view.findViewById(R.id.rcv_1);

        // 추가적인 RecyclerView 설정 및 데이터 연결 등을 수행할 수 있습니다.

        return view;
    }
}
