package com.example.woodonggo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MypageReview extends AppCompatActivity {

    CheckBox checkboxTime, checkboxResponse, checkboxManner;
    RadioGroup resultRadioGroup;
    Button postBtn;
    RadioButton winBtn, sameBtn, loseBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_review);

        /*TODO: 사용자가 이미지를 등록한 이후 iconPlus는 INVISIBLE, iconCancel는 VISIVLE시키고,
                이미지는 최대 5장까지만 등록할 수 있도록 코드 추가해주세요*/

        //선택된 라디오버튼에 따라 글자색 별도 처리
        resultRadioGroup = findViewById(R.id.resultRadioGroup);
        winBtn = findViewById(R.id.winBtn);
        sameBtn = findViewById(R.id.sameBtn);
        loseBtn = findViewById(R.id.loseBtn);
        postBtn = findViewById(R.id.textPost);


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeFragment();
            }
        });

        resultRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.winBtn) { //'승'을 골랐을 때
                    winBtn.setTextColor(Color.WHITE);
                    sameBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    loseBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
                else if(checkedId == R.id.sameBtn) { //'무'를 골랐을 때
                    sameBtn.setTextColor(Color.WHITE);
                    winBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    loseBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));

                }
                else { //'패'를 골랐을 때
                    loseBtn.setTextColor(Color.WHITE);
                    winBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    sameBtn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
            }
        });

        //체크박스 체크 여부에 따라 글자색 별도 처리
        checkboxTime = findViewById(R.id.checkboxTime);
        checkboxResponse = findViewById(R.id.checkboxResponse);
        checkboxManner = findViewById(R.id.checkboxManner);

        checkboxTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //체크O: 글자 색상 하얀색으로 설정
                    checkboxTime.setTextColor(Color.WHITE);
                }else {
                    //체크X: 글자 색상 다크그레이로 설정
                    checkboxTime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
            }
        });
        checkboxResponse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxResponse.setTextColor(Color.WHITE);
                }
                else {
                    checkboxResponse.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
            }
        });
        checkboxManner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxManner.setTextColor(Color.WHITE);
                }
                else {
                    checkboxManner.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                }
            }
        });
    }
    private void goToHomeFragment() {
        // 홈 프래그먼트로 이동하는 코드
        AppCompatActivity activity = this;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentHome())
                .addToBackStack(null)
                .commit();
    }
}
