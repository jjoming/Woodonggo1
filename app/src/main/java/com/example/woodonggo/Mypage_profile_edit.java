package com.example.woodonggo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class Mypage_profile_edit extends AppCompatActivity {

    ImageView profile,picture;
    EditText loginId;
    private FirebaseFirestore db;

    String userId,profileurl;
    Button btn_set;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_profile_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        profile = findViewById(R.id.imglogo);
        loginId = findViewById(R.id.loginId);
        picture = findViewById(R.id.picture);
        btn_set = findViewById(R.id.btn_set);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("UserId", "");

        fetchUserInfo(userId);


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카메라 이미지 선택.
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                // 이미지 뷰 변경 함수.
                activityResult.launch(galleryIntent);
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileurl != null) {
                    if (loginId.getText().toString().equals("")) {
                        Toast.makeText(Mypage_profile_edit.this, "이름을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        updateProfileAndNickname(userId,loginId.getText().toString(),profileurl);
                    }
                } else {
                    if (loginId.getText().toString().equals("")) {
                        Toast.makeText(Mypage_profile_edit.this, "이름을 설정해주세요.", Toast.LENGTH_SHORT).show();
                    } else updateProfileAndNickname(userId, loginId.getText().toString());
                }
            }
        });

    }

    private void updateProfileAndNickname(String userId, String toString) {


        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("User").document(userId);

        userRef
                .update("name", toString)
                .addOnSuccessListener(aVoid -> {
                    onBackPressed();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,"수정이 실패되었습니다.",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
    }

    private void updateProfileAndNickname(String userId, String newNickname, String imageUrl) {
        // 이미지 업로드를 위한 고유한 파일명 생성
        String imageFileName = userId + ".jpg";

        // 업로드할 이미지 파일에 대한 참조 획득
        StorageReference imageRef = storageReference.child("user_profiles/" + imageFileName);

        // 이미지 업로드
        imageRef.putFile(Uri.parse(imageUrl))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 업로드 성공 시 프로필 업데이트
                        updateProfileWithImageUrl(userId, newNickname, imageFileName);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Mypage_profile_edit.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();

                });
    }

    private void updateProfileWithImageUrl(String userId, String newNickname, String imageFileName) {
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("User").document(userId);

        userRef
                .update("name", newNickname)
                .addOnSuccessListener(aVoid -> {
                    onBackPressed();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "수정이 실패되었습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
    }


    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null ) {
                        profileurl = String.valueOf(result.getData().getData());
                        profile.setImageURI(Uri.parse(profileurl));
                    }
                }
            }
    );
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
