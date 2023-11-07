package com.example.woodonggo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFindId extends AppCompatActivity {

    EditText phone_edit , certification_edit;
    Button send_btn , certification_btn;

    TextView id_tv , main_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_findid);
        phone_edit = findViewById(R.id.phone_edit);
        certification_edit = findViewById(R.id.certification_edit);
        send_btn = findViewById(R.id.send_btn);
        certification_btn = findViewById(R.id.certification_btn);
        id_tv = findViewById(R.id.id_tv);
        main_tv = findViewById(R.id.main_tv);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: phone edit가 전화번호 잘 넣었는 지 확인하고 인증번호 송신)
            }
        });

        certification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 일치할 경우 id_Tv의 visible시켜서 아이디를 알려준다.
            }
        });

        main_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}