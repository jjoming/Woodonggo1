package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class LoginSignup extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nameEdit, idEdit, passwordEdit, passwordCheckEdit, phoneEdit, certificationNumEdit;
    Button idCheckBtn, phoneCheckBtn, certiConfirm, joinConfirm;
    TextView cautionText, pwCautionText;

    String id, pw, phone;
    boolean idFound = false;    //해당아이디가 있을 경유 true로 변환
    boolean idConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        nameEdit  = findViewById(R.id.name_edit);   //이름 입력
        idEdit = findViewById(R.id.id_edit);        //id 입력
        passwordEdit = findViewById(R.id.password_edit);
        passwordCheckEdit = findViewById(R.id.password_chk_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        certificationNumEdit = findViewById(R.id.phone_chk_edit);
        idCheckBtn = findViewById(R.id.id_chk_btn);
        phoneCheckBtn = findViewById(R.id.phone_chk_btn);
        certiConfirm = findViewById(R.id.certification_btn);
        joinConfirm = findViewById(R.id.joinConfirm);
        cautionText = findViewById(R.id.caution_text);
        pwCautionText = findViewById(R.id.pw_caution_text);


        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아이디 중복확인
                idFound = false;
                cautionText.setVisibility(View.INVISIBLE);  // 설정 초기화
                id = idEdit.getText().toString();   //id 입력받은 값 가져와서 변수에 저장
                // 파이어베이스에 값 불러오기
                readUser(id);
            }
        });

        //passwordEdit.

        passwordCheckEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 비밀번호 재입력 텍스트 입력 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 비밀번호 재입력 텍스트 입력중에 표시

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneAuth();
                // todo : 인증번호 보내기

            }
        });

        joinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idConfirm) {  //todo : 비밀번호확인 변수 검사, 인증확인 변수 검사
                    // 아이디와 비밀번호 넘겨주기, 프로필 설정으로 넘어가기
                    Intent intent = new Intent(LoginSignup.this, LoginSignup2.class);
                    intent.putExtra("id", id);
                    //intent.putExtra("password", pw);
                    //intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
    }

    private void readUser(String id) {
        db.collection("User")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MJC", document.getId() + " => " + document.getData());
                                // 여기서 document.getData()로 가져온 값과 원하는 값 비교
                                // 예를 들어, document.getString("fieldToCompare")를 통해 특정 필드 값을 가져와서 비교
                                String valueToCompare = document.getString("id");
                                if (valueToCompare.equals(id)) {
                                    // 비교하고자 하는 값과 일치할 때 처리
                                    cautionText.setText("사용할 수 없는 아이디입니다. 다시 입력해주세요");
                                    cautionText.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "사용할 수 없는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    idFound = true;
                                    idConfirm = false;
                                    break;
                                }
                            }


                        } else {
                            Log.w("MJC", "Error getting documents.", task.getException());
                        }

                        if (!idFound) {
                            cautionText.setText("사용할 수 있는 아이디입니다.");
                            cautionText.setVisibility(View.VISIBLE);
                            idFound = false;
                            idConfirm = true;
                        }
                    }
                });
    }

    private void phoneAuth() {
        /*
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        */
    }
}
