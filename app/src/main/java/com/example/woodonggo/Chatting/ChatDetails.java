package com.example.woodonggo.Chatting;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatDetails extends AppCompatActivity {
    RecyclerView recyclerViewChat;
    ChatMessageAdapter adapter;
    Toolbar toolbar;
    TextView nickName, ing, placeChat;
    ImageView chatImg;
    EditText msgEdit;
    Button sendBtn;
    String userName; //ㅅㅏㅇ대닉네임
    private DatabaseReference chatRef;
    private String chatRoomUid; //채팅방 하나 id
    String postingId;
    private String myuid;       //나의 id
    private String destUid;     //상대방 uid
    private FirebaseDatabase firebaseDatabase;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_detail);

        nickName = findViewById(R.id.nickName);
        ing = findViewById(R.id.ing);
        chatImg = findViewById(R.id.sportsImg);
        placeChat = findViewById(R.id.placeChat);

        adapter = new ChatMessageAdapter();

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
        destUid = getIntent().getStringExtra("destUid");        //채팅 상대
        postingId = getIntent().getStringExtra("postingId");  //게시글 자체 아이디

        loadUserNickname(destUid);
        retrieveProfilePicture(destUid);
        loadWritingTitle(postingId);

        recyclerViewChat = (RecyclerView)findViewById(R.id.recyclerViewChat);
        sendBtn = (Button)findViewById(R.id.send_btn);
        msgEdit = (EditText)findViewById(R.id.msgEdit);

        if(msgEdit.getText().toString() == null) sendBtn.setEnabled(false);
        else sendBtn.setEnabled(true);

        checkChatRoom(); // 채팅방이 있는지 확인
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        //DataModelMessage message = new DataModelMessage(sdf2.format(new Date()), false, true, new Date());
        //adapter.add(new DataModelMessage(sdf2.format(new Date()), false, true, new Date()));

        // 채팅 메시지를 실시간으로 감지하는 리스너 등록
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                ChatModel.Comment comment = snapshot.getValue(ChatModel.Comment.class);
                if (comment != null) {
                    // Firebase에서 받은 메시지를 RecyclerView에 추가
                    DataModelMessage message = new DataModelMessage(comment.message, false, false, (Date) comment.timestamp);
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
                    sendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = msgEdit.getText().toString().trim();

                if (!TextUtils.isEmpty(message)) {
                    DataModelChat chatModel = new DataModelChat();
                    chatModel.users.put(myuid, true);
                    chatModel.users.put(destUid, true);

                    // push() 데이터가 쌓이기 위해 채팅방 key가 생성
                    if (chatRoomUid == null) {
                        sendBtn.setEnabled(false);
                        firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        checkChatRoom();
                                        sendMsgToDataBase(message);
                                        msgEdit.setText(""); // 메시지 전송 후에는 EditText를 초기화
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        sendBtn.setEnabled(true);
                                        Toast.makeText(ChatDetails.this, "채팅방 생성 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        sendMsgToDataBase(message);
                    }
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

    private void sendMsgToDataBase(String message) {
        if (!TextUtils.isEmpty(message)) {
            DataModelChat.Comment comment = new DataModelChat.Comment();
            comment.uid = myuid;
            comment.message = message;
            comment.timestamp = ServerValue.TIMESTAMP;
            comment.sentByMe = true; // 내가 보낸 메시지

            // Firebase Realtime Database에 메시지 추가
            chatRef.child(chatRoomUid).child("comments").push().setValue(comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // 메시지 전송 성공 시 어댑터에 메시지 추가
                            DataModelMessage dataModelMessage = new DataModelMessage(message, true, false, new Date());
                            adapter.add(dataModelMessage);
                            msgEdit.setText("");

                            recyclerViewChat.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatDetails.this, "메시지 전송 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /*
    private void checkChatRoom()
    {
        //자신 key == true 일때 chatModel 가져온다.
//        /* chatModel
//        public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
//        public Map<String, ChatModel.Comment> comments = new HashMap<>(); //채팅 메시지
//
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
    */

    private void checkChatRoom() {
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/" + myuid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean chatRoomExists = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destUid) && chatModel.users.containsKey(postingId)) {
                        // 채팅방이 이미 존재할 때
                        chatRoomUid = dataSnapshot.getKey();
                        chatRoomExists = true;

                        loadChatRoomData(chatRoomUid);

                        break;
                    }
                }

                if (!chatRoomExists) {
                    // 채팅방이 존재하지 않을 때
                    createChatRoom(postingId, destUid);
                }

                // 어댑터 설정
                recyclerViewChat.setLayoutManager(new LinearLayoutManager(ChatDetails.this));
                recyclerViewChat.setAdapter(adapter);

                // 메시지 전송
                // sendMsgToDataBase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }
        });
    }


    private void createChatRoom(String postingId, String destUid) {
        DatabaseReference chatroomsRef = firebaseDatabase.getReference("chatrooms");

        // 새로운 채팅방을 위한 키 생성
        String chatRoomKey = chatroomsRef.push().getKey();

        // 채팅방 유저 목록 설정
        Map<String, Boolean> users = new HashMap<>();
        users.put(myuid, true);
        users.put(destUid, true);
        users.put(postingId, true);
//
//        Map<String, DataModelChat> lastMessage = new HashMap<>();
//        lastMessage.put

        // 채팅방 정보 설정
        DataModelChat chatRoom = new DataModelChat();
        chatRoom.roomId = chatRoomKey;
        chatRoom.users = users;

        chatRoom.setRoomId(chatRoomKey);
        chatRoom.setAdd("홍은동");
        chatRoom.setName(userName);
        chatRoom.setUserId(destUid);

        Log.d("destId", destUid);


        // Firebase Realtime Database에 데이터 추가
        chatroomsRef.child(chatRoomKey).setValue(chatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // 채팅방이 성공적으로 생성되었을 때 수행할 동작
                chatRoomUid = chatRoomKey;
                sendBtn.setEnabled(true);

                addToMyChatList(chatRoom);

                // 동기화
                recyclerViewChat.setLayoutManager(new LinearLayoutManager(ChatDetails.this));
                recyclerViewChat.setAdapter(adapter);

                // 메시지 보내기
                //sendMsgToDataBase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("cmk", "채팅방 개설 실패");
            }
        });
    }

    private void addToMyChatList(DataModelChat chatRoom) {
        DatabaseReference userChatListRef = firebaseDatabase.getReference("user_chat_list").child(myuid);
        userChatListRef.child(chatRoom.getRoomId()).setValue(true);
    }

    private void retrieveProfilePicture(String id) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        // StorageReference 초기화
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference profileRef = storageRef.child("user_profiles/" + id + ".jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the profile picture using Glide library
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(chatImg);
        }).addOnFailureListener(e -> {
            // Handle any errors
            Log.e("FirebaseStorage", "Error retrieving profile picture", e);
        });
    }

    private void loadChatRoomData(String chatRoomUid) {
        firebaseDatabase.getReference().child("chatrooms").child(chatRoomUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear(); // 기존 데이터 클리어

                for (DataSnapshot dataSnapshot : snapshot.child("comments").getChildren()) {
                    String uid = dataSnapshot.child("uid").getValue(String.class);
                    String messageContent = dataSnapshot.child("message").getValue(String.class);
                    Long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);

                    boolean isMyMessage = myuid.equals(uid);
                    boolean isDateMessage = false;

                    Date time = new Date(timestamp);
                    DataModelMessage message = new DataModelMessage(messageContent, isMyMessage, isDateMessage, time);
                    adapter.add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }
        });
    }

    private void loadUserNickname(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userName = documentSnapshot.getString("name");
                        nickName.setText(userName);
                    } else {
                        // User document does not exist
                        //Log.d("FirestoreData", "User document does not exist for userId: " + id);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("FirestoreData", "Error retrieving user data", e);
                });
    }

    private void loadWritingTitle(String postingId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Writing")
                .document(postingId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String postId = documentSnapshot.getString("title");
                        placeChat.setText(postId);
                    } else {
                        // User document does not exist
                        //Log.d("FirestoreData", "User document does not exist for userId: " + id);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.e("FirestoreData", "Error retrieving user data", e);
                });
    }
}
