package com.example.woodonggo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginFindPw1 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_findpw);

        EditText name_edit , id_edit , phone_edit , certification_edit;
        Button send_btn , certification_btn;
        TextView gotologin;

        name_edit = findViewById(R.id.name_edit);
        id_edit = findViewById(R.id.id_edit);
        phone_edit = findViewById(R.id.phone_edit);
        certification_edit = findViewById(R.id.certification_edit);
        send_btn = findViewById(R.id.send_btn);
        certification_btn = findViewById(R.id.certification_btn);
        gotologin = findViewById(R.id.tv_1);


        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}