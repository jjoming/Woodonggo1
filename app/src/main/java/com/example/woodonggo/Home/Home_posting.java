package com.example.woodonggo.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.woodonggo.R;

public class Home_posting extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgView_close;
    EditText edt_title, edt_content;
    RadioGroup radioGroupCategory, radioGroupTeam, radioGroupPersonal;
    RadioButton btnTeam, btnPersonal, btnGolf, btnBowling, btnPingpong;
    EditText edtTitle, edtContent;
    Button btnPost;

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

        //라디오그룹 팀 및 개인
        radioGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnTeam) {
                    radioGroupTeam.setVisibility(View.VISIBLE);
                    radioGroupPersonal.setVisibility(View.INVISIBLE);
                    btnTeam.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_left_radiobtn, 0);
                    btnPersonal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right_radiobtn, 0);
                } else if (checkedId == R.id.btnPersonal) {
                    radioGroupPersonal.setVisibility(View.VISIBLE);
                    radioGroupTeam.setVisibility(View.INVISIBLE);
                    btnPersonal.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_left_radiobtn, 0);
                    btnTeam.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.arrow_right_radiobtn, 0);
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
            }
        });
    }
}
