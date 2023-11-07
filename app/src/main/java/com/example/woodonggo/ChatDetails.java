package com.example.woodonggo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatDetails extends AppCompatActivity {
    RecyclerView recyclerViewChat;
    ChatMessageAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_detail);

        //툴바 초기화 및 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == android.R.id.home) {
                    onBackPressed();
                }
                return false;
            }
        });

        // 커스텀 어댑터 생성
        adapter = new ChatMessageAdapter();


        // Xml에서 추가한 ListView 연결
        recyclerViewChat = (RecyclerView) findViewById(R.id.recyclerViewChat);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);

        // ListView에 어댑터 연결
        recyclerViewChat.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentTime = new Date();

        DataModelMessage myMessage = new DataModelMessage("This is my message", true, false, currentTime, currentTime);
        DataModelMessage otherMessage = new DataModelMessage("This is another person's message", false, false, currentTime, currentTime);
        DataModelMessage dateMessage = new DataModelMessage("2023-11-07", false, true, currentTime, currentTime);

        adapter.add(myMessage);
        adapter.add(otherMessage);
        adapter.add(dateMessage);
    }
}
