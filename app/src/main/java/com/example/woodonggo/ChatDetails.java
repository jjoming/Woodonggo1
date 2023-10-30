package com.example.woodonggo;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatDetails extends Activity {
    RecyclerView recyclerViewChat;
    ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_detail);

        // 커스텀 어댑터 생성
        adapter = new ChatMessageAdapter();

        // Xml에서 추가한 ListView 연결
        recyclerViewChat = (RecyclerView) findViewById(R.id.recyclerViewChat);

        // ListView에 어댑터 연결
        recyclerViewChat.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentTime = new Date();

        DataModelMessage myMessage = new DataModelMessage("This is my message", true, false, currentTime, currentTime);
        DataModelMessage otherMessage = new DataModelMessage("This is another person's message", false, false, currentTime, currentTime);
        DataModelMessage dateMessage = new DataModelMessage("2023-10-30 00:00:00", false, true, currentTime, currentTime);

        adapter.add(myMessage);
        adapter.add(otherMessage);
        adapter.add(dateMessage);
    }
}
