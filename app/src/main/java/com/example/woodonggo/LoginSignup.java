package com.example.woodonggo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class LoginSignup extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String storedVerificationId = "";
    //private PhoneAuthProvider.ForceResendingToken resendToken;

    EditText nameEdit, idEdit, passwordEdit, passwordCheckEdit, phoneEdit, certificationNumEdit;
    Button idCheckBtn, phoneCheckBtn, certiConfirm, joinConfirm;
    TextView cautionText, pwCautionText;

    String id, pw, phone;
    boolean idFound = false;    //해당아이디가 있을 경유 true로 변환
    boolean idConfirm = false;
    boolean pwConfirm = false;
    boolean authConfirm = false;

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

        auth.setLanguageCode("fr");

        /*int permissionCheck = ContextCompat.checkSelfPermission(LoginSignup.this, Manifest.permission.SEND_SMS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // 문자보내기 권한 거부
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, Manifest.permission.SEND_SMS)) {
                Toast.makeText(getApplicationContext(), "SMS 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            // 문자 보내기 권한 허용
            ActivityCompat.requestPermissions(LoginSignup.this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSION);
        }
        */

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

        passwordCheckEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pw = passwordEdit.getText().toString();     //비밀번호 입력창에 입력된 텍스트 가져오기
                return false;
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
                if (pw.equals(passwordCheckEdit.getText().toString())) {    // 비밀번호 입력창에 입력된 값과 비밀번호 확인창에 입력된 값이 같다면
                    pwCautionText.setText("비밀번호 일치");
                    pwCautionText.setVisibility(View.VISIBLE);
                    pwCautionText.setTextColor(Color.BLUE);
                    pwConfirm = true;
                }
                else {
                    pwCautionText.setText("비밀번호가 일치하지 않습니다.");
                    pwCautionText.setVisibility(View.VISIBLE);
                    pwConfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //phoneAuth(phoneEdit.getText().toString(), "메시지 전송 테스트");

                callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        Toast.makeText(LoginSignup.this, "인증코드가 전송되었습니다. 60초 이내에 입력해주세요 :)", Toast.LENGTH_SHORT).show();
                        Log.d("Auth", "onVerificationCompleted:" + credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.w("Auth", "onVerificationFailed", e);
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        }
                        Toast.makeText(LoginSignup.this, "인증실패", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        Log.d("Auth", "onCodeSent:" + verificationId);
                        storedVerificationId = verificationId;
                    }
                };

                phone = phoneEdit.getText().toString();
                if (phone.isEmpty()) {
                    Toast.makeText(LoginSignup.this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!phone.matches("[0-9]+")) {
                    Toast.makeText(LoginSignup.this, "숫자로만 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // todo : 인증번호 보내기
                    String ph = "+82";
                    //ph += phone;
                    ph += phone.substring(1);

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber(ph)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(LoginSignup.this)                 // (optional) Activity for callback binding
                                    // If no activity is passed, reCAPTCHA verification can not be used.
                                    .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }

            }
        });

        certiConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 서버 인증번호와 입력된 인증번호가 같은지 확인
                String code = certificationNumEdit.getText().toString();

                if (code.isEmpty()) {
                    Toast.makeText(LoginSignup.this, "인증 코드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (storedVerificationId != null) {
                    // Firebase에 저장된 인증 코드와 사용자가 입력한 코드를 비교하여 인증을 시도합니다.
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(storedVerificationId, code);

                    // signInWithPhoneAuthCredential 메소드로 전달하여 사용자를 인증합니다.
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(LoginSignup.this, "인증번호를 먼저 요청하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        joinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idConfirm && pwConfirm && authConfirm) {  //아이디, 비밀번호확인 변수 검사, 인증확인 변수 검사
                    // 아이디와 비밀번호 넘겨주기, 프로필 설정으로 넘어가기
                    Intent intent = new Intent(LoginSignup.this, LoginSignup2.class);
                    intent.putExtra("id", id);
                    intent.putExtra("password", pw);
                    intent.putExtra("phone", phone);
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

    private void phoneAuth(String phoneNumber, String message) {
//        PendingIntent pi = PendingIntent.getActivity(LoginSignup.this, 0, new Intent(LoginSignup.this, LoginSignup.this.getClass()), PendingIntent.FLAG_IMMUTABLE);
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, pi, null);
//
//        Toast.makeText(LoginSignup.this, "메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 사용자가 성공적으로 인증됨
                            // AuthResult result = task.getResult();
                            Log.d("Auth", "signInWithCredential:success");
                            //FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(LoginSignup.this, "인증되었습니다. 가입완료 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                            // 이후의 작업을 수행
                            authConfirm = true;
                        } else {
                            // 인증이 실패한 경우
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // 올바르지 않은 코드가 입력된 경우
                                Toast.makeText(LoginSignup.this, "올바르지 않은 코드입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /*
    private void initViewModelCallback() {
        viewModel.getRequestPhoneAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean it) {
                UserInfo.tel = viewModel.getEtPhoneNum().getValue().toString();
                if (it) {
                    startPhoneNumberVerification("+82" + viewModel.getEtPhoneNum().getValue().toString().substring(1));
                } else {
                    Toast.makeText(LoginSignup.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getRequestResendPhoneAuth().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean it) {
                if (it) {
                    resendVerificationCode(
                            "+82" + viewModel.getEtPhoneNum().getValue().toString().substring(1),
                            resendToken
                    );
                } else {
                    Toast.makeText(LoginSignup.this, "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                }
            }
        });

        viewModel.getAuthComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean it) {
                try {
                    PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(
                            storedVerificationId,
                            viewModel.getEtAuthNum().getValue()
                    );
                    verifyPhoneNumberWithCode(phoneCredential);
                } catch (Exception e) {
                    Log.d("Auth", e.toString());
                }
            }
        });
    }

     */
}
