package com.example.woodonggo;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginSignup extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText nameEdit, idEdit, passwordEdit, passwordCheckEdit, phoneEdit, certificationNumEdit;
    Button idCheckBtn, phoneCheckBtn, certiConfirm, joinConfirm;
    TextView cautionText, pwCautionText;

    String id, pw;
    boolean idFound = false;    //해당아이디가 있을 경유 true로 변환

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
                // todo : 아이디 중복확인
                id = idEdit.getText().toString();   //id 입력받은 값 가져와서 변수에 저장
                // 파이어베이스에 값 불러오기
                readUser(id);
            }
        });



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
                // todo : 인증번호 보내기
            }
        });

        joinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 로그인화면으로 넘어가기
            }
        });
    }

    private void readUser(String id) {
        /*db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MJC", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("MJC", "Error getting documents.", task.getException());
                        }
                    }
                });
         */

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
                                    break;
                                }
                            }


                        } else {
                            Log.w("MJC", "Error getting documents.", task.getException());
                        }

                        if (!idFound) {
                            cautionText.setText("사용할 수 있는 아이디입니다.");
                            cautionText.setVisibility(View.VISIBLE);
                            idCheckBtn.setEnabled(false);
                        }
                    }
                });
    }


    /*

     */
}
