package com.example.woodonggo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MypageReview extends AppCompatActivity {

    CheckBox checkboxTime, checkboxResponse, checkboxManner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_review);

        /*TODO: 사용자가 이미지를 등록한 이후 iconPlus는 INVISIBLE, iconCancel는 VISIVLE시키고,
                이미지는 최대 5장까지만 등록할 수 있도록 코드 추가해주세요*/

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
}
