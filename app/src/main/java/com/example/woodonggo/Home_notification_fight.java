package com.example.woodonggo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Home_notification_fight extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_notification_fight, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        // 데이터 생성 또는 가져오기
        ArrayList<String> dataSet = new ArrayList<>();
        dataSet.add("냥냥");
        dataSet.add("박휘벌애");
        dataSet.add("나비");

        Adapter_notify_fight adapter = new Adapter_notify_fight(dataSet);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
