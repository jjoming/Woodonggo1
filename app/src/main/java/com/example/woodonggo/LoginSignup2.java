package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSignup2 extends AppCompatActivity {
    Button nickConfirm;
    ImageView profile, picture;
    EditText nickname;
    String id, pw, phoneNum, nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_singup2);

        nickConfirm = findViewById(R.id.nickConfirm);
        profile = findViewById(R.id.profile);
        picture = findViewById(R.id.picture);
        nickname = findViewById(R.id.nickname);

        Intent inIntent = getIntent();
        id = inIntent.getStringExtra("id");
        //pw = inIntent.getStringExtra("password");
        phoneNum = inIntent.getStringExtra("phone");



        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 카메라 갤러리 접근권한
                // todo : 사진 가져와서 profile에 넣기
            }
        });

        nickConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 아이디, 비밀번호, 전화번호, 닉네임, 사진 파이어베이스에 올리기
            }
        });
    }
}
