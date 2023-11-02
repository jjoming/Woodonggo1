package com.example.woodonggo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
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

// Class 명에 _(언더스코어 금지!!!!!)
public class LoginMain extends AppCompatActivity {
    private static final String TAG = "LoginMain";
    Button login_btn;
    TextView signUpTextView;
    private String naver_client_id = "AlrQlFUIJfEvBysmrJ2_";
    private String naver_client_secret = "pGaTJRO0pk";
    private String naver_client_name = "woodonggo";
    private NidOAuthLoginButton naver_login;

    String responseBody;

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
                        String finalMobile = mobile;
                        runOnUiThread(() -> {
                            // 고객 DB에 id가 있으면 메인페이지로 넘어가고 로그인 처리
                            // 없으면 회원가입(Signup2로 전환)
                            if (true) {

                            }else {
                                // todo : 로그인 성공시 아이디와 전화번호 LoginSignup2로 전달하기
                                Intent intent = new Intent(LoginMain.this, LoginSignup2.class);
                                intent.putExtra("phone", finalMobile);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            }
                        });
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

}
