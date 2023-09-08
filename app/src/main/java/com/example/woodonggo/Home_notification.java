package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home_notification extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isEditMode = false; // "편집" 모드 여부를 나타내는 변수

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(isEditMode ? R.menu.toolbar_home_notification2 : R.menu.toolbar_home_notification, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), Fragment_home.class);
            startActivity(intent);
        } else if (itemId == R.id.editer) {
            // "편집" 메뉴 아이템 클릭 시 "편집" 모드로 전환
            isEditMode = true;
            invalidateOptionsMenu(); // onCreateOptionsMenu를 호출하여 메뉴를 업데이트
        } else if (itemId == R.id.close) {
            // "닫기" 메뉴 아이템 클릭 시 "편집" 모드 해제
            isEditMode = false;
            invalidateOptionsMenu(); // onCreateOptionsMenu를 호출하여 메뉴를 업데이트
        }
        return true; // true를 반환하여 이벤트 처리 완료를 나타냄
    }






}
