package com.example.woodonggo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Mypage_profile extends AppCompatActivity {

    Button edit_btn;
    ImageView profile;
    TextView username;
    String userId;

    private StorageReference storageReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // storageReference 초기화
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        edit_btn = findViewById(R.id.edit_btn);
        profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.textName);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("UserId", "");
        db = FirebaseFirestore.getInstance();
        fetchUserName(userId);
        storageReference = storage.getReference();
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mypage_profile.this, Mypage_profile_edit.class);
                startActivity(intent);
            }
        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserName(String userId) {
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 사용자가 Firestore의 users 컬렉션에 존재하는 경우
                    String userName = document.getString("name");
                    // userName을 사용할 수 있습니다.
                    username.setText(userName);
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
