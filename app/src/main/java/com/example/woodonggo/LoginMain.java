package com.example.woodonggo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;

// Class 명에 _(언더스코어 금지!!!!!)
public class LoginMain extends AppCompatActivity {
    private static final String TAG = "LoginMain";
    Button login_btn;
    TextView signUpTextView;
    private String naver_client_id = "AlrQlFUIJfEvBysmrJ2_";
    private String naver_client_secret = "pGaTJRO0pk";
    private String naver_client_name = "woodonggo";
    private NidOAuthLoginButton naver_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main); // 액티비티의 레이아웃 파일을 설정합니다.

        Context context = LoginMain.this;

        login_btn = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.signUp);
        naver_login = findViewById(R.id.naver_Login);
        naver_login.setImageResource(R.drawable.naverlogo);
        NaverIdLoginSDK.INSTANCE.initialize(context, naver_client_id, naver_client_secret, naver_client_name);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, MainActivity.class);
                startActivity(intent);

                // todo : 아이디 패스워드 서버와 일치하는지 확인
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, LoginSignup.class);
                startActivity(intent);
            }
        });

        naver_login.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                String accessToken = NaverIdLoginSDK.INSTANCE.getAccessToken();

                // 로그인 성공 시 처리
                // todo : 로그인 성공시 아이디와 전화번호 LoginSignup2로 전달하기
                Intent intent = new Intent(LoginMain.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int httpStatus, String message) {
                // 로그인 실패 시 처리
            }

            @Override
            public void onError(int errorCode, String message) {
                // 로그인 에러 시 처리
            }
        });

    }
}
