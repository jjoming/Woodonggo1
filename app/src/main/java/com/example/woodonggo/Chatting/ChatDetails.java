package com.example.woodonggo.Chatting;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatDetails extends AppCompatActivity {
    RecyclerView recyclerViewChat;
    ChatMessageAdapter adapter;
    Toolbar toolbar;
    TextView nickName, ing, title;
    ImageView sportsImg;
    EditText msgEdit;
    Button sendBtn;
    private DatabaseReference chatRef;

    private String chatRoomUid; //채팅방 하나 id
    private String myuid;       //나의 id
    private String destUid;     //상대방 uid

    //private RecyclerView recyclerView;
    //private Button button;
    //private EditText editText;

    private FirebaseDatabase firebaseDatabase;

    private User destUser;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_detail);

        nickName = findViewById(R.id.nickName);
        ing = findViewById(R.id.ing);
        //title = findViewById(R.id.title);
        sportsImg = findViewById(R.id.sportsImg);
        //msgEdit = findViewById(R.id.msgEdit);
        //sendBtn = findViewById(R.id.send_btn);

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

        // Firebase Realtime Database 참조 초기화
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference().child("chatrooms");

        //chatRef = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        myuid = preferences.getString("userId", "");
        //myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destUid = getIntent().getStringExtra("destUid");        //채팅 상대

        recyclerViewChat = (RecyclerView)findViewById(R.id.recyclerViewChat);
        sendBtn = (Button)findViewById(R.id.send_btn);
        msgEdit = (EditText)findViewById(R.id.msgEdit);

        if(msgEdit.getText().toString() == null) sendBtn.setEnabled(false);
        else sendBtn.setEnabled(true);

        checkChatRoom();

        // 채팅 메시지를 실시간으로 감지하는 리스너 등록
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                ChatModel.Comment comment = snapshot.getValue(ChatModel.Comment.class);
                if (comment != null) {
                    // Firebase에서 받은 메시지를 RecyclerView에 추가
                    DataModelMessage message = new DataModelMessage(comment.message, true, false, (Date) comment.timestamp);
                    adapter.add(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // 채팅 내용이 변경되었을 때의 처리
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // 채팅 내용이 삭제되었을 때의 처리
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // 채팅 내용이 이동되었을 때의 처리
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
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
            public void onClick(View v) {

                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myuid,true);
                chatModel.users.put(destUid,true);

                //push() 데이터가 쌓이기 위해 채팅방 key가 생성
                if(chatRoomUid == null){
                    Toast.makeText(ChatDetails.this, "채팅방 생성", Toast.LENGTH_SHORT).show();
                    sendBtn.setEnabled(false);
                    firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }

                    });
                } else {
                    //sendMsgToDataBase();
                }

            }
        });

        // 커스텀 어댑터 생성
        /*
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
        adapter.add(otherMessage);*/

    }   //onCreate

    //작성한 메시지를 데이터베이스에 보낸다.
    private void sendMsgToDataBase() {
        if (!msgEdit.getText().toString().equals("")) {
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myuid;
            comment.message = msgEdit.getText().toString();
            comment.timestamp = ServerValue.TIMESTAMP;

            // Firebase Realtime Database에 메시지 추가
            chatRef.push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    msgEdit.setText("");
                }
            });
        }
    }

    private void checkChatRoom()
    {
        //자신 key == true 일때 chatModel 가져온다.
        /* chatModel
        public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
        public Map<String, ChatModel.Comment> comments = new HashMap<>(); //채팅 메시지
        */
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/"+myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destUid)){           //상대방 id 포함돼 있을때 채팅방 key 가져옴
                        chatRoomUid = dataSnapshot.getKey();
                        sendBtn.setEnabled(true);

                        //동기화
                        recyclerViewChat.setLayoutManager(new LinearLayoutManager(ChatDetails.this));
                        recyclerViewChat.setAdapter(adapter);

                        //메시지 보내기
                        sendMsgToDataBase();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
