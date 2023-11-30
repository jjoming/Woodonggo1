package com.example.woodonggo;

import static com.kakao.util.maps.helper.Utility.getKeyHash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.kakao.sdk.user.UserApiClient;


public class LoginMain extends AppCompatActivity {
    private static final String TAG = "LoginMain";
    Button login_btn;
    EditText id_edit, pw_edit;
    TextView signUpTextView, findId, findPw;
    CheckBox autologin_chk;
    private String naver_client_id = "AlrQlFUIJfEvBysmrJ2_";
    private String naver_client_secret = "pGaTJRO0pk";
    private String naver_client_name = "woodonggo";
    private NidOAuthLoginButton naver_login;

    ImageButton kakao_login;
    String responseBody, finalphone;
    private static final String PREF_AUTO_LOGIN = "auto_login";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    //Authentication 로그인 상태를 관리하는 인터페이스
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main); // 액티비티의 레이아웃 파일을 설정합니다.

        Context context = LoginMain.this;
        Log.d("getKeyHash", "" + getKeyHash(LoginMain.this));

        login_btn = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.signUp);
        naver_login = findViewById(R.id.naver_Login);
        naver_login.setImageResource(R.drawable.naverlogo);
        NaverIdLoginSDK.INSTANCE.initialize(context, naver_client_id, naver_client_secret, naver_client_name);
        kakao_login = findViewById(R.id.kakao_Login);
        findId = findViewById(R.id.findId);
        findPw = findViewById(R.id.findPw);
        id_edit = findViewById(R.id.loginId);
        pw_edit = findViewById(R.id.loginPw);
        autologin_chk = findViewById(R.id.check_login);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        //DB에서 데이터를 읽거나 쓰기 위해선 DatabaseReference가 필요!
        databaseReference = FirebaseDatabase.getInstance().getReference();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id, pw;
                id = id_edit.getText().toString();
                pw = pw_edit.getText().toString();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw)) {
                    Toast.makeText(LoginMain.this, "ID와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else login(id,pw);

                // todo : 아이디 패스워드 서버와 일치하는지 확인
            }
        });


        findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, LoginFindId.class);
                startActivity(intent);
            }
        });

        findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMain.this, LoginFindPw1.class);
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


        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginMain.this)){
                    login();
                }
                else{
                    accountLogin();
                }
            }
        });



        naver_login.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                String accessToken = NaverIdLoginSDK.INSTANCE.getAccessToken();
                String apiUrl = "https://openapi.naver.com/v1/nid/me";
                String token = accessToken;
                String header = "Bearer " + token;
                Map<String, String> requestHeaders = new HashMap<>();
                requestHeaders.put("Authorization", header);

                // 로그인 성공 시 처리
                new Thread(() -> {
                    responseBody = get(apiUrl, requestHeaders);
                    try {
                        // JSON 파싱
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String mobile = jsonResponse.getJSONObject("response").getString("mobile");
                        String id = jsonResponse.getJSONObject("response").getString("id");
                        mobile = mobile.replaceAll("-","");
                        finalphone = mobile;
                        checkIfUserExistsInFirestore(id);
                        // Firestore에 사용자 ID가 있는지 확인

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();

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

    private void login(String id, String pw) {
        DocumentReference userRef, autoLogin;
        userRef = db.collection("User").document(id);

        userRef.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               DocumentSnapshot document = task.getResult();
               if(document.exists()) {
                   String storedPw = document.getString("passwd");
                   if (pw.equals(storedPw)) {
                       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginMain.this);
                       preferences.edit().putBoolean(PREF_AUTO_LOGIN, autologin_chk.isChecked()).apply();
                       SharedPreferences.Editor editor = preferences.edit();
                       Log.d("HONG2",id);
                       editor.putString("userId", id);
                       editor.apply();
                       Intent intent = new Intent(LoginMain.this, MainActivity.class);
                       intent.putExtra("UserId",id);
                       startActivity(intent);
                       finish();
                   } else {
                       Toast.makeText(LoginMain.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                   }
               } else {
                   Toast.makeText(LoginMain.this,"해당 ID가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
               }
           }else {
               Toast.makeText(LoginMain.this, "사용자 정보 확인 중 오류 발생", Toast.LENGTH_SHORT).show();
           }
        });
    }

    // Firestore에서 사용자 확인 메서드
    private void checkIfUserExistsInFirestore(String userId) {
        checkIfUserExistsInFirestore(userId,null);
    }

    private void checkIfUserExistsInFirestore(String userId, String profile) {
        DocumentReference userRef = db.collection("User").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // 사용자가 Firestore의 users 컬렉션에 존재하는 경우
                    Intent intent = new Intent(LoginMain.this, MainActivity.class);
                    intent.putExtra("UserId",userId);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    Log.d("HONG",userId);
                    editor.putString("userId", userId);
                    editor.apply();
                    startActivity(intent);
                    finish();
                }else {
                    if (finalphone != null) {
                        gotosignup2(userId,finalphone);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.d("HONG",userId);
                    }
                    gotosignup2(userId);
            }
                // Firestore에서 사용자 확인 중 오류 발생
                // 오류 처리를 수행하거나 필요에 따라 처리
            }
        });
    }

    private void gotosignup2(String userId) {
        gotosignup2(userId , null);
    }
    private void gotosignup2(String userId, String finalphone) {
        Intent intent = new Intent(LoginMain.this,LoginSignup2.class);
        intent.putExtra("id",userId);
        intent.putExtra("phone", finalphone);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Log.d("HONG3",userId);
        editor.putString("userId", userId);
        editor.apply();
        startActivity(intent);
        finish();
    }


    // 네이버로그인 안될경우.
    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginMain.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "카카오 로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "카카오 로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginMain.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "카카오 로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "카카오 로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }
    public void getUserInfo(){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                Log.i(TAG, "로그인 완료");
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: " + user.getId() +
                        "\n이메일: " + user.getKakaoAccount().getEmail() +
                        "\n전화번호: " + user.getKakaoAccount().getPhoneNumber() +
                        "\n썸네일이미지: " + user.getKakaoAccount().getProfile().getThumbnailImageUrl() +
                        "\n이름: " + user.getKakaoAccount().getProfile().getNickname());
                //name = user.getKakaoAccount().getProfile().getNickname();
                // 로그인 성공 후 MainActivity로 전환하는 코드 추가
                String id = Long.toString(user.getId());
                String profile = user.getKakaoAccount().getProfile().getThumbnailImageUrl();
                id = id + "masterkey";
                checkIfUserExistsInFirestore(id,profile);
                 // 현재 액티비티를 종료하여 뒤로 가기 버튼으로 다시 돌아오지 않도록 합니다.
            }
            return null;
        });
        Log.i(TAG, "getUserInfo() 종료");
    }



}
