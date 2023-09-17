package com.example.woodonggo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment_chatting extends Fragment {

    RecyclerView recyclerView;
    Chat_RecyclerView_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chatting, container, false);

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //데이터 모델리스트
        ArrayList<DataModel_chat> dataModels = new ArrayList<>();

        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data0", "data0", "data0", "data0"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data1", "data1", "data1", "data1"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data2", "data2", "data2", "data1"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data3", "data3", "data3", "data1"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data4", "data4", "data4", "data1"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data5", "data5", "data5", "data1"));
        dataModels.add(new DataModel_chat(R.drawable.basketball_icon, "data6", "data6", "data6", "data1"));

        recyclerView = rootView.findViewById(R.id.recyclerViewChat);
        adapter = new Chat_RecyclerView_Adapter(getActivity(), dataModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        // 프래그먼트에서 옵션 메뉴 사용을 허용
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_others, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.notification) {
            //TODO: 인텐트 완성
            //Intent intent = new Intent(getContext().getApplicationContext(), );
            //startActivity(intent);
        }
        return false;
    }
}
