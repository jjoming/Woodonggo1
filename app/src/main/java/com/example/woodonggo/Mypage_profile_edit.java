package com.example.woodonggo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Mypage_profile_edit extends AppCompatActivity {

    ImageView profile;
    EditText loginId;
    private FirebaseFirestore db;

    String userId;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_profile_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        profile = findViewById(R.id.imglogo);
        loginId = findViewById(R.id.loginId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("UserId", "");

        fetchUserInfo(userId);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserInfo(String userId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 사용자가 Firestore의 users 컬렉션에 존재하는 경우
                    String userName = document.getString("name");
                    // userName을 사용하여 EditText에 세팅
                    loginId.setText(userName);
                    fetchUserProfileImage(userId);
                }
            } else {
                // Firestore에서 사용자 확인 중 오류 발생
                // 오류 처리를 수행하거나 필요에 따라 처리
            }
        });
    }
    private void fetchUserProfileImage(String userId) {
        // StorageReference를 통해 프로필 이미지 파일에 대한 참조 획득
        StorageReference profileImageRef = storageReference.child("user_profiles/" + userId + ".jpg");

        // 이미지 다운로드 URL을 가져와서 Glide를 사용하여 이미지를 표시
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // 성공적으로 다운로드 URL을 얻은 경우
                    Glide.with(this)
                            .load(uri)
                            .into(profile);
                    Log.d("TAG",this.toString());
                })
                .addOnFailureListener(exception -> {
                    // 다운로드 URL을 얻는 중 오류가 발생한 경우 또는 이미지가 없는 경우
                    // 오류 처리를 수행하거나 기본 이미지를 표시하거나 필요에 따라 처리
                    // 예: Glide를 사용하여 기본 이미지를 표시
                    Glide.with(this)
                            .load(R.drawable.noprofile)
                            .into(profile);
                });
    }
}
