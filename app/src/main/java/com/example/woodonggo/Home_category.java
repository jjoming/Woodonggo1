package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home_category extends AppCompatActivity {

    GridView gridView1, gridView2;
    String[] categoryName1 = {"농구", "배드민턴", "야구", "축구", "족구"};
    String[] categoryName2 = {"골프", "당구", "배드민턴", "볼링", "탁구"};
    int[] categoryImg1 = {R.drawable.icon_basketball, R.drawable.icon_badminton,
            R.drawable.icon_baseball, R.drawable.icon_soccer, R.drawable.icon_football};
    int[] categoryImg2 = {R.drawable.icon_golf, R.drawable.icon_pool_snooker,
            R.drawable.icon_badminton, R.drawable.icon_bowling, R.drawable.icon_ping_pong};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView1 = findViewById(R.id.gridView1);
        gridView2 = findViewById(R.id.gridView2);

        Adapter_home_category1 adapter_home_category1 = new Adapter_home_category1(Home_category.this,
                categoryName1, categoryImg1);
        Adapter_home_category2 adapter_home_category2 = new Adapter_home_category2(Home_category.this,
                categoryName2, categoryImg2);

        gridView1.setAdapter(adapter_home_category1);
        gridView2.setAdapter(adapter_home_category2);

        //TODO: 그리드뷰 클릭 리스너 연결 및 이벤트 처리

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            Intent intent = new Intent(Home_category.this, Fragment_home.class);
            startActivity(intent);
        }
        return false;
    }
}
