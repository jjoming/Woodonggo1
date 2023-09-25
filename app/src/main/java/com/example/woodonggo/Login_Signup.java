package com.example.woodonggo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login_Signup extends AppCompatActivity {
    private FirebaseAuth auth;
    ImageView imglogo, camera;
    EditText name_edit, id_edit;
    private EditText signupID;
    private EditText signupPassword;
    private Button signup_okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.login_signup);


        // 계정 생성 버튼
        signup_okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createAccount(signupID.getText().toString(), signupPassword.getText().toString());
            }
        });
    }

}
