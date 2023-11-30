package com.example.woodonggo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import com.example.woodonggo.DataModelRank;


public class FragmentRanking extends Fragment {

    RecyclerView recyclerView;
    Ranking_RecyclerView_Adapter adapter;
    RadioGroup radioGroup;
    RadioButton btnGolf, btnBowling, btnPingpong;
    TextView myrank, nick_rank;
    ImageView img_myprofile;
    ArrayList<DataModelRank> dataModels;
    String upload_id;
    private StorageReference storageReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // storageReference 초기화
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String userName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_ranking, container, false);

        //라디오
        radioGroup = rootView.findViewById(R.id.radioGroup);
        btnGolf = rootView.findViewById(R.id.btnGolf);
        btnBowling = rootView.findViewById(R.id.btnBowling);
        btnPingpong = rootView.findViewById(R.id.btnPingpong);
        Log.d("FragmentRanking", "btnGolf: " + (btnGolf != null));
        Log.d("FragmentRanking", "btnBowling: " + (btnBowling != null));
        Log.d("FragmentRanking", "btnPingpong: " + (btnPingpong != null));


        db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        upload_id = preferences.getString("userId", "");
        db = FirebaseFirestore.getInstance();
        fetchUserName(upload_id);
        storageReference = storage.getReference();

        recyclerView = rootView.findViewById(R.id.recyclerViewRank);
        dataModels = new ArrayList<>();
        adapter = new Ranking_RecyclerView_Adapter(getActivity(), dataModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        btnGolf.setChecked(true);
        btnGolf.setTextColor(Color.WHITE);
        setRanking("Golf");

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //사용자 랭킹 인플레이팅
        myrank = rootView.findViewById(R.id.myrank);
        nick_rank = rootView.findViewById(R.id.nick_rank);
        img_myprofile = rootView.findViewById(R.id.img_myprofile);

        nick_rank.setText(userName);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    setRanking("Golf");
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    setRanking("Bowling");
                } else if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setTextColor(Color.DKGRAY);
                    setRanking("PingPong");
                }
            }
        });

        // 프래그먼트에서 옵션 메뉴 사용을 허용
        setHasOptionsMenu(true);

        return rootView;
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


    // 랭킹 업데이트
    private void setRanking(String keyword) {
        db.collection("Ranking")
                .whereEqualTo("sports", keyword)  // "sport"는 Firestore 문서 필드에 따라 조정
                .orderBy("rank", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //dataModels = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String rank = document.getString("rank");
                            String data = document.getString("userId");
                            int imageResource = R.drawable.basketball_icon;

                            Log.d("cmk", data);

                            dataModels.add(new DataModelRank(rank, imageResource, data));
                        }

                        // RecyclerView에 데이터 설정
                        adapter.setDataModels(dataModels);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("error", "에러발생");
                    }
                });
    }

    private void fetchUserName(String userId) {
        Log.d("UserId", "UserId: " + userId);
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userName = document.getString("name");
                    fetchUserProfileImage(userId);
                }
            } else {
                // Firestore에서 사용자 확인 중 오류 발생
                // 오류 처리를 수행하거나 필요에 따라 처리
            }
        });
    }

    private void fetchUserProfileImage(String userId) {
        StorageReference profileImageRef = storageReference.child("user_profiles/" + userId + ".jpg");

        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .into(img_myprofile);
                })
                .addOnFailureListener(exception -> {
                    Glide.with(this)
                            .load(R.drawable.noprofile)
                            .into(img_myprofile);
                });
    }
}
