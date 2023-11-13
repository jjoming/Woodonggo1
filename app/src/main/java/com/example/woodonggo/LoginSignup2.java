package com.example.woodonggo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginSignup2 extends AppCompatActivity {
    Button nickConfirm;
    ImageView profile, picture;
    EditText nickname;
    String id, pw, phoneNum, nick, profileurl;
    Bitmap bitmap;
    private FirebaseFirestore db;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup2);

        nickConfirm = findViewById(R.id.nickConfirm);
        profile = findViewById(R.id.profile);
        picture = findViewById(R.id.picture);
        nickname = findViewById(R.id.nickname);


        // 이전 페이지에서 정보를 받아옴.
        Intent inIntent = getIntent();
        id = inIntent.getStringExtra("id");
        profileurl = inIntent.getStringExtra("profile");
        pw = inIntent.getStringExtra("password");
        phoneNum = inIntent.getStringExtra("phone");

        if (id == null) {
            // id가 null이면 에러 메시지 표시 또는 처리 방법을 선택하세요.
            Toast.makeText(this, "에러: 사용자 ID가 없습니다.", Toast.LENGTH_SHORT).show();
            return; // 초기화되지 않은 id로 인해 더 이상 진행하지 않도록 종료.
        }
        // 카카오는 프로필 이미지를 가져오고 나머지는 안가져옴.
        if (profileurl == null) {
            Glide.with(this)
                    .load(R.drawable.noprofile)
                    .into(profile);
        } else {
                Glide.with(this)
                        .load(profileurl)
                        .into(profile);
        }

        //db설정.
        db = FirebaseFirestore.getInstance();


        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 카메라 갤러리 접근권한
                // todo : 사진 가져와서 profile에 넣기
                // 카메라 이미지 선택.
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                // 이미지 뷰 변경 함수.
                activityResult.launch(galleryIntent);
            }
        });

        nickConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 아이디, 비밀번호, 전화번호, 닉네임, 사진 파이어베이스에 올리기
                String Id = id;
                String Name = nickname.getText().toString();
                String Passwd = pw;
                String PhoneNum = phoneNum;
                String ProfileUrl = profileurl;
                saveToFireStore(Id, Name, Passwd, PhoneNum, ProfileUrl);
            }
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


    private void saveToFireStore(String Id, String Name, String Passwd, String PhoneNum) {
        // profileuri를 전달하지 않은 경우 기본 이미지 사용
        saveToFireStore(Id, Name, Passwd, PhoneNum,null);
    }

    private void saveToFireStore(String Id , String Name , String Passwd, String PhoneNum, String Profileurl) {
        if(!Id.isEmpty() && !Name.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",Id);
            map.put("name",Name);
            map.put("passwd",Passwd);
            map.put("phoneNum",PhoneNum);
            Log.d("TAG", String.valueOf(profileurl));

            StorageReference profileRef = storageReference.child("user_profiles/" + Id + ".jpg");
            if(profileurl != null) {
                profileRef.putFile(Uri.parse(profileurl))
                        .addOnSuccessListener(taskSnapshot -> {
                            profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                profileurl = uri.toString();
                            });
                        });
            } else {
                StorageReference defaultProfileRef = storageReference.child("user_profiles/" + Id + ".jpg");

                defaultProfileRef.putFile(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.noprofile))
                        .addOnSuccessListener(taskSnapshot -> {
                            defaultProfileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                profileurl = uri.toString();
                            });
                        });
            }


            db.collection("User").document(Id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginSignup2.this, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginSignup2.this, LoginSignup3.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginSignup2.this, "설정이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });


        }else
            Toast.makeText(this, "사용자 이름이 공백입니다. 입력해주세요.", Toast.LENGTH_SHORT).show();
    }

}
