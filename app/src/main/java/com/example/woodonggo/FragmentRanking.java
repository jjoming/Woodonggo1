package com.example.woodonggo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentRanking extends Fragment {

    RecyclerView recyclerView;
    Ranking_RecyclerView_Adapter adapter;
    RadioGroup radioGroup;
    RadioButton btnGolf, btnBowling, btnPingpong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ranking, container, false);

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //라디오
        radioGroup = rootView.findViewById(R.id.radioGroup);
        btnGolf = rootView.findViewById(R.id.btnGolf);
        btnBowling = rootView.findViewById(R.id.btnBowling);
        btnPingpong = rootView.findViewById(R.id.btnPingpong);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                } else if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setTextColor(Color.DKGRAY);
                }
            }
        });

        ArrayList<DataModelRank> dataModels = new ArrayList<>();

        dataModels.add(new DataModelRank("4", R.drawable.basketball_icon, "data0"));
        dataModels.add(new DataModelRank("5", R.drawable.basketball_icon, "data1"));
        dataModels.add(new DataModelRank("6", R.drawable.basketball_icon, "data2"));
        dataModels.add(new DataModelRank("7", R.drawable.basketball_icon, "data3"));
        dataModels.add(new DataModelRank("8", R.drawable.basketball_icon, "data4"));
        dataModels.add(new DataModelRank("9", R.drawable.basketball_icon, "data5"));
        dataModels.add(new DataModelRank("10", R.drawable.basketball_icon, "data6"));

        recyclerView = rootView.findViewById(R.id.recyclerViewRank);
        adapter = new Ranking_RecyclerView_Adapter(getActivity(), dataModels);
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
        if (itemId == R.id.notification){
            //Intent intent = new Intent(getContext().getApplicationContext(), );
            //startActivity(intent);
        }
        return false;
    }
}
