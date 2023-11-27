package com.example.woodonggo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView nickName, ing;
    ImageView sportsImg;
    EditText msgEdit;
    Button sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_detail);

        nickName = findViewById(R.id.nickName);
        ing = findViewById(R.id.ing);
        sportsImg = findViewById(R.id.sportsImg);
        msgEdit = findViewById(R.id.msgEdit);
        sendBtn = findViewById(R.id.send_btn);

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

        //msgEdit에 입력할 경우 전송 버튼 색 바꾸기
        msgEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String msg = msgEdit.getText().toString();
                if (msg != null) {
                    sendBtn.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 메시지 전송
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

        adapter.add(dateMessage);
        adapter.add(myMessage);
        adapter.add(otherMessage);

    }
}
