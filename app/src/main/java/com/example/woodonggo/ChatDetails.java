package com.example.woodonggo;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

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

        adapter.add("이건 뭐지",1);
        adapter.add("쿨쿨",1);
        adapter.add("쿨쿨쿨쿨",0);
        adapter.add("재미있게",1);
        adapter.add("놀자라구나힐힐 감사합니다. 동해물과 백두산이 마르고 닳도록 놀자 놀자 우리 놀자",1);
        adapter.add("재미있게",1);
        adapter.add("재미있게",0);
        adapter.add("2015/11/20",2);
        adapter.add("재미있게",1);
        adapter.add("재미있게",1);

    }
}
