package com.example.woodonggo.Raingking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.woodonggo.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class FragmentRanking extends Fragment {

    RecyclerView recyclerView;
    Ranking_RecyclerView_Adapter adapter;
    RadioGroup radioGroup;
    RadioButton btnGolf, btnBowling, btnPingpong;
    TextView myrank, nick_rank, name_rank1,name_rank2,name_rank3;
    ImageView img_myprofile,profile_rank1,profile_rank2,profile_rank3;
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
        setRanking("personalGolfScore");

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        //사용자 랭킹 인플레이팅
        myrank = rootView.findViewById(R.id.myrank);
        nick_rank = rootView.findViewById(R.id.nick_rank);
        img_myprofile = rootView.findViewById(R.id.img_myprofile);
        name_rank1 = rootView.findViewById(R.id.rank1_id);
        name_rank2 = rootView.findViewById(R.id.rank2_id);
        name_rank3 = rootView.findViewById(R.id.rank3_id);
        profile_rank1 = rootView.findViewById(R.id.img_profile1);
        profile_rank2 = rootView.findViewById(R.id.img_profile2);
        profile_rank3 = rootView.findViewById(R.id.img_profile3);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    setRanking("personalGolfScore");
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                    setRanking("personalBowlingScore");
                } else if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setTextColor(Color.DKGRAY);
                    setRanking("teamPingpongScore");
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


    private void setRanking(String keyword) {
        dataModels.clear();
        String scoreField = "";  // scoreField 변수 추가

        // 운동 종목에 따라 scoreField 설정
        if ("personalGolfScore".equals(keyword)) {
            scoreField = "personalGolfScore";
        } else if ("personalBowlingScore".equals(keyword)) {
            scoreField = "personalBowlingScore";
        } else if ("teamPingpongScore".equals(keyword)) {
            scoreField = "teamPingpongScore";
        }

        // scoreField가 빈 문자열이 아닌지 확인
        if (!scoreField.isEmpty()) {
            db.collection("User")
                    .orderBy(scoreField, Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Clear existing data before updating
                            name_rank1.setText("");
                            name_rank2.setText("");
                            name_rank3.setText("");
                            profile_rank1.setImageResource(R.drawable.noprofile);
                            profile_rank2.setImageResource(R.drawable.noprofile);
                            profile_rank3.setImageResource(R.drawable.noprofile);

                            int rankCounter = 1; // Initialize rank counter

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userId = document.getString("id");
                                String userName = document.getString("name");

                                // Update UI for 1, 2, 3위
                                if (rankCounter == 1) {
                                    name_rank1.setText(userName);
                                    // Update profile image for 1st rank
                                    fetchUserProfileImageForRank(userId, profile_rank1);
                                } else if (rankCounter == 2) {
                                    name_rank2.setText(userName);
                                    // Update profile image for 2nd rank
                                    fetchUserProfileImageForRank(userId, profile_rank2);
                                } else if (rankCounter == 3) {
                                    name_rank3.setText(userName);
                                    // Update profile image for 3rd rank
                                    fetchUserProfileImageForRank(userId, profile_rank3);
                                }
                                if(rankCounter>=4 && rankCounter<=10) {
                                    DataModelRank dataModel = new DataModelRank(rankCounter, userName, userId);
                                    dataModels.add(dataModel);
                                }

                                // Check if the current user's rank is found
                                if (userId.equals(upload_id)) {
                                    // Update myrank with the rankCounter
                                    myrank.setText(String.valueOf(rankCounter));
                                }

                                rankCounter++;



                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("error", "Error occurred");
                        }
                    });
        }
    }


    private void fetchUserName(String userId) {
        Log.d("UserId", "UserId: " + userId);
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userName = document.getString("name");
                    nick_rank.setText(userName);
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

    private void fetchUserProfileImageForRank(String userId, ImageView imageView) {
        StorageReference profileImageRef = storageReference.child("user_profiles/" + userId + ".jpg");

        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .into(imageView);
                })
                .addOnFailureListener(exception -> {
                    Glide.with(this)
                            .load(R.drawable.noprofile)
                            .into(imageView);
                });
    }

}
