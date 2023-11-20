package com.example.woodonggo.Home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Home_posting extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgView_close;
    RadioGroup radioGroupCategory, radioGroupTeam, radioGroupPersonal;
    RadioButton btnTeam, btnPersonal, btnGolf, btnBowling, btnPingpong;
    EditText edtTitle, edtContent;
    Button btnPost;

    String upload_content,upload_date,upload_title,upload_sports,upload_id;
    Boolean upload_team;
    long nowtime;

    private FirebaseFirestore db;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting);

        imgView_close = findViewById(R.id.imgView_close);
        toolbar = findViewById(R.id.toolbar);
        radioGroupCategory = findViewById(R.id.radioGroupCategory);
        radioGroupTeam = findViewById(R.id.radioGroupTeam);
        radioGroupPersonal = findViewById(R.id.radioGroupPersonal);
        btnTeam = findViewById(R.id.btnTeam);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnGolf = findViewById(R.id.btnGolf);
        btnBowling = findViewById(R.id.btnBowling);
        btnPingpong = findViewById(R.id.btnPingpong);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnPost = findViewById(R.id.btnPost);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        upload_id = preferences.getString("UserId", "");
        if(edtTitle.getText().toString().length() > 0 && edtContent.getText().toString().length() > 0) {
            btnPost.setBackgroundColor(getResources().getColor(R.color.navy));
        }
        // 에디트 텍스트 입력 감지
        edtTitle.addTextChangedListener(textWatcher);
        edtContent.addTextChangedListener(textWatcher);

        //db설정.
        db = FirebaseFirestore.getInstance();

        //라디오그룹 팀 및 개인
        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnTeam) {
                    radioGroupTeam.setVisibility(View.VISIBLE);
                    radioGroupPersonal.setVisibility(View.INVISIBLE);
                    btnTeam.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_left_radiobtn, 0);
                    btnPersonal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_radiobtn, 0);

                    btnPingpong.setChecked(true);
                    btnPingpong.setTextColor(Color.WHITE);

                } else if (checkedId == R.id.btnPersonal) {
                    radioGroupPersonal.setVisibility(View.VISIBLE);
                    radioGroupTeam.setVisibility(View.INVISIBLE);
                    btnPersonal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_left_radiobtn, 0);
                    btnTeam.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_right_radiobtn, 0);

                    btnGolf.setChecked(true);
                    btnGolf.setTextColor(Color.WHITE);
                }
            }
        });

        //라디오그룹 팀 스포츠 //TODO:다른 그룹의 라디오 버튼이 눌렸을 때 기존의 선택된 라디오 버튼이 해제되게 해야 함!!
        radioGroupTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                }
                // 다른 라디오 그룹의 라디오 버튼 선택 해제
                if (group.getCheckedRadioButtonId() != R.id.btnPingpong) {
                    btnPingpong.setChecked(false);
                    btnPingpong.setTextColor(Color.DKGRAY);
                }

            }
        });

        //라디오그룹 개인 스포츠
        radioGroupPersonal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                }
                // 다른 라디오 그룹의 라디오 버튼 선택 해제
                if (group.getCheckedRadioButtonId() != R.id.btnGolf && group.getCheckedRadioButtonId() != R.id.btnBowling) {
                    btnGolf.setChecked(false);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setChecked(false);
                    btnBowling.setTextColor(Color.DKGRAY);
                }

            }
        });


        //이미지 뷰 'X'
        imgView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //게시글 올리기 버튼
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 에디트 텍스트 및 라디오 버튼이 선택되었다면 버튼색상(+텍스트) 변경 시키기



                upload_title = edtTitle.getText().toString();
                upload_content = edtContent.getText().toString();
                nowtime = System.currentTimeMillis();
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                upload_date = String.valueOf(timeFormat.format(new Date(nowtime)));
                Log.d("TAG",upload_date);

                // 체크된 버튼에 따라 팀 여부, 종목이 정해짐
                upload_team = btnTeam.isChecked();
                if (upload_team) {
                    if (btnPingpong.isChecked()) {
                        upload_sports = "탁구";
                    }
                } else {
                    if (btnGolf.isChecked()) {
                        upload_sports = "골프";
                    } else if (btnBowling.isChecked()) {
                        upload_sports = "볼링";
                    }
                }

                Log.d("TAG",upload_sports);
                Log.d("TAG", String.valueOf(upload_team));

                if(Objects.equals(upload_title, "") || Objects.equals(upload_content, "")) {
                    Toast.makeText(Home_posting.this,"제목, 내용, 팀 또는 개인 선택, 종목 선택은 필수 입력 사항입니다.",Toast.LENGTH_SHORT).show();
                } else {
                    uploadToFirestore();
                }

            }
        });
    }

    private void uploadToFirestore() {

        // Firebase Firestore에 데이터 업로드

        // Firestore 컬렉션 및 문서 경로 설정
        String collectionPath = "Writing";  // Writing 컬렉션에 저장
        String documentId = UUID.randomUUID().toString();  // 랜덤한 문서 ID 생성
        String documentPath = collectionPath + "/" + documentId;

        // 업로드할 데이터 준비
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", upload_title);
        postData.put("content", upload_content);
        postData.put("date", upload_date);
        postData.put("team", upload_team);
        postData.put("sports", upload_sports);
        postData.put("userId", upload_id);
        postData.put("writingId",documentId);

        // Firestore에 데이터 업로드
        FirebaseFirestore.getInstance().document(documentPath)
                .set(postData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 성공적으로 업로드된 경우
                        Toast.makeText(Home_posting.this, "게시글이 성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();  // 액티비티 종료 또는 필요에 따라 다른 동작 수행
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 업로드 실패 시
                        Toast.makeText(Home_posting.this, "게시글 업로드 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkButtonState();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    // 게시글 올리기 버튼 상태 확인 및 설정
    private void checkButtonState() {
        if (edtTitle.getText().toString().length() > 0 && edtContent.getText().toString().length() > 0) {
            btnPost.setBackgroundColor(getResources().getColor(R.color.navy));
        }
    }
}
