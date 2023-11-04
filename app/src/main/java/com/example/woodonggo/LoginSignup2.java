package com.example.woodonggo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginSignup2 extends AppCompatActivity {
    Button nickConfirm;
    ImageView profile, picture;
    EditText nickname;
    String id, pw, phoneNum, nick, profileurl;
    Bitmap bitmap;
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
        profileurl = inIntent.getStringExtra("profile");
        //pw = inIntent.getStringExtra("password");
        phoneNum = inIntent.getStringExtra("phone");

        if(profileurl != null) {
            // 카카오는 이미지링크로 받아오므로 스레드로 이미지 url 작업
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(profileurl);

                        // Web에서 이미지를 가져온 뒤
                        // ImageView에 지정할 Bitmap을 만든다
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // 서버로 부터 응답 수신
                        conn.connect();

                        InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                        bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                    } catch (MalformedURLException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            mThread.start(); // Thread 실행

            try {
                // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                mThread.join();

                // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
                profile.setImageBitmap(bitmap);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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