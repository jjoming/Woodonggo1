package com.example.woodonggo.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.Adapter_notify_like;
import com.example.woodonggo.R;

import java.util.ArrayList;

public class Home_notification_like extends Fragment {

    private Adapter_notify_like adapter; // Adapter를 멤버 변수로 선언

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_notification_like, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        // 데이터 생성 또는 가져오기
        ArrayList<String> dataSet = new ArrayList<>();
        dataSet.add("치즈덕");
        dataSet.add("콩순이");
        dataSet.add("루비");

        adapter = new Adapter_notify_like(dataSet);
        recyclerView.setAdapter(adapter);

        // 프래그먼트가 시작될 때 'X' 버튼을 숨깁니다.
        hideImageView();

        return rootView;
    }

    // 프래그먼트에서 이미지뷰를 제어하는 메서드 추가
    public void showImageView() {
        if (adapter != null) {
            adapter.showImageView();
        }
    }

    public void hideImageView() {
        if (adapter != null) {
            adapter.hideImageView();
        }
    }
}