package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Class 명에 _(언더스코어 금지!!!!!)
public class LoginMain extends AppCompatActivity {

    Button login_btn;
    TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main); // 액티비티의 레이아웃 파일을 설정합니다.

        login_btn = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.signUp);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, LoginSignup.class);
                startActivity(intent);
            }
        });
    }
}
