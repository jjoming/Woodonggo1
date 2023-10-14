package com.example.woodonggo.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.Adapter_home;
import com.example.woodonggo.R;

import java.util.ArrayList;

public class Home_Fragment_Personal extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.home_fragment_personal, container, false);

        ArrayList<String> testDataSet = new ArrayList<>(); // 테스트를 위한 더미데이터 생성
        for (int i = 0; i < 20; i++) {
            testDataSet.add("TEST DATA" + i);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        Adapter_home adapter = new Adapter_home(testDataSet);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
