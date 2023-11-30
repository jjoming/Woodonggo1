package com.example.woodonggo.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Home_Fragment_Team extends Fragment {
    private Adapter_team_home adapter;
    private ArrayList<TeamModel> teamDataList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    String selectedTown;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.home_fragment_team, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager); // 리니어 레이아웃 매니저 설정

        adapter = new Adapter_team_home(getContext(), teamDataList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
        });

        loadData();

        return rootView;
    }

    private void refreshData() {
        loadData();
    }

    private void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // 데이터를 로드하기 전에 기존 데이터 클리어
        teamDataList.clear();
        db.collection("Writing")
                .whereEqualTo("team", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // 각 문서의 데이터를 추출
                            String content = document.getString("content");
                            Timestamp date = document.getTimestamp("date");
                            String sports = document.getString("sports");
                            boolean team = Boolean.TRUE.equals(document.getBoolean("team"));
                            String userId = document.getString("userId");
                            String writingId = document.getString("writingId");
                            // 수정된 부분: likesCount 값 가져오기
                            Long likesCountLong = document.getLong("likesCount");
                            int likesCount = likesCountLong != null ? Math.toIntExact(likesCountLong) : 0;
                            String region = document.getString("region");
                            findwriter(userId, new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String writer) {
                                    // TeamModel 객체 생성
                                    TeamModel teamModel = new TeamModel(content, date, team, userId, writingId, writer, sports, likesCount, region);
                                    teamDataList.add(teamModel);
                                    // Adapter에 데이터 설정
                                    adapter.teamDataList(teamDataList);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        // 데이터를 가져오는 데 실패한 경우
                        // 처리할 내용을 여기에 추가
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void findwriter(String userId, OnSuccessListener<String> successListener) {
        // userId가 null이면 처리하지 않고 리턴
        if (userId == null) {
            successListener.onSuccess("Unknown");
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(userDocument -> {
                    if (userDocument.exists()) {
                        String userName = userDocument.getString("name");
                        if (userName != null) {
                            successListener.onSuccess(userName);
                        } else {
                            // userName이 null인 경우, 기본값이나 처리할 내용을 설정
                            successListener.onSuccess("Unknown"); // 예: "Unknown"으로 설정
                        }
                    } else {
                        // 사용자 문서가 없는 경우, 기본값이나 처리할 내용을 설정
                        successListener.onSuccess("Unknown"); // 예: "Unknown"으로 설정
                    }
                })
                .addOnFailureListener(e -> {
                    // 오류가 발생한 경우, 기본값이나 처리할 내용을 설정
                    successListener.onSuccess("Unknown"); // 예: "Unknown"으로 설정
                });
    }
}