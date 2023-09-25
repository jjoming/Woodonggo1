package com.example.woodonggo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class Splash extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.textView);

        // 첫 번째 텍스트 이동 애니메이션
        ObjectAnimator translateY1 = ObjectAnimator.ofFloat(textView, "translationY", 0, 540);
        translateY1.setDuration(1000);

        // 두 번째 텍스트 이동 애니메이션
        ObjectAnimator translateY2 = ObjectAnimator.ofFloat(textView, "translationY", 540, 960);
        translateY2.setDuration(2000); // 애니메이션 간격을 2000ms(2초)로 변경
        translateY2.setStartDelay(1000); // 첫 번째 애니메이션 이후 1초 후에 시작

        // 첫 번째 텍스트 색상 변경 애니메이션
        ObjectAnimator textColorAnimator1 = ObjectAnimator.ofArgb(textView, "textColor",
                getResources().getColor(R.color.dark_purple),
                getResources().getColor(R.color.purple));
        textColorAnimator1.setDuration(2000);

        // 두 번째 텍스트 색상 변경 애니메이션
        ObjectAnimator textColorAnimator2 = ObjectAnimator.ofArgb(textView, "textColor",
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.pink));
        textColorAnimator2.setDuration(2000); // 애니메이션 간격을 2000ms(2초)로 변경

        // 첫 번째 애니메이션 시작
        translateY1.start();
        textColorAnimator1.start();

        // 두 번째 애니메이션 시작
        translateY2.start();
        textColorAnimator2.start();

        // 일정 시간 후에 스플래시 화면 종료 및 메인 화면으로 이동
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, 2000); // 2초 후에 메인 화면으로 이동 (원하는 대로 조정 가능)
    }
}