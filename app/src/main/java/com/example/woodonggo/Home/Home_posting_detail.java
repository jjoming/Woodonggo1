package com.example.woodonggo.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home_posting_detail extends AppCompatActivity {

    Toolbar toolbar;
    Button btnChat;
    TextView textName, textContent, textTime, textLike;
    ImageView imgMore,imgProfile,imgCategory;
    String id, content, time, likeCount, writingId;
    StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnChat = findViewById(R.id.btnChat);
        textName = findViewById(R.id.textName); //사용자 아이디
        textContent = findViewById(R.id.textContent); //게시글 내용
        textTime = findViewById(R.id.textTime); //게시글 업로드 시간
        textLike = findViewById(R.id.text_like); //좋아요 숫자
        imgMore = findViewById(R.id.more); //더보기 아이콘
        imgProfile = findViewById(R.id.imgProfile);
        imgCategory = findViewById(R.id.imgCategory);
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        id = intent.getExtras().getString("userId");
        writingId = intent.getExtras().getString("writingId");
        Log.d("writingId",writingId);
        retrieveData(writingId);

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '더보기' 버튼을 클릭했을 때 showPopupMenu 메서드 호출
                showPopupMenu(view);
            }
        });
    }

    private void retrieveData(String writingId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Writing")
                .document(writingId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, retrieve data
                            String content = documentSnapshot.getString("content");
                            long timestampMillis = documentSnapshot.getTimestamp("date").toDate().getTime();
                            int likesCount = documentSnapshot.getLong("likesCount").intValue();
                            String region = documentSnapshot.getString("region");
                            String sports = documentSnapshot.getString("sports");
                            boolean team = documentSnapshot.getBoolean("team");
                            String title = documentSnapshot.getString("title");
                            String userId = documentSnapshot.getString("userId");

                            String formattedTime = convertTimestampToString(timestampMillis);
                            retrieveUserName(id);
                            retrieveProfilePicture(id);
                            categorySetImage(sports);
                            textContent.setText(content);
                            textTime.setText(formattedTime);
                            textLike.setText(String.valueOf(likesCount));
                        } else {
                            // Document does not exist
                            Log.d("FirestoreData", "Document does not exist");
                        }
                    }
                });
    }

    private void categorySetImage(String sports) {
        if(sports.equals("탁구")) {
            Glide.with(imgCategory.getContext())
                    .load(R.drawable.icon_ping_pong)
                    .into(imgCategory);
        }
        else if (sports.equals("골프")) {
            Glide.with(imgCategory.getContext())
                    .load(R.drawable.icon_golf)
                    .into(imgCategory);
        }
        else if (sports.equals("볼링")) {
            Glide.with(imgCategory.getContext())
                    .load(R.drawable.icon_bowling)
                    .into(imgCategory);
        }


    }


    private void retrieveProfilePicture(String id) {
        StorageReference profileRef = storageReference.child("user_profiles/" + id + ".jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the profile picture using Glide library
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(imgProfile);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Log.e("FirebaseStorage", "Error retrieving profile picture", e);
        });
    }

    private void retrieveUserName(String id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        textName.setText(userName);
                    } else {
                        // User document does not exist
                        Log.d("FirestoreData", "User document does not exist for userId: " + id);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("FirestoreData", "Error retrieving user data", e);
                });
    }


    private String convertTimestampToString(long timestampMillis) {
        // Convert timestamp to a readable format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm:ss");
        Date date = new Date(timestampMillis);
        return sdf.format(date);
    }


    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.home_more_menu, popupMenu.getMenu());

        //팝업 메뉴 아이템 클릭 이벤트
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.report_author) {
                    //TODO: 작성자 신고
                }
                else if(itemId == R.id.report_post) {
                    //TODO: 게시글 신고
                }
                else {
                    //TODO: 채팅 걸기
                }
                return false;
            }
        });
        popupMenu.show(); //팝업 메뉴 띄우기
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
