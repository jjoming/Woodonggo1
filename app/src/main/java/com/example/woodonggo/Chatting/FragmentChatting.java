package com.example.woodonggo.Chatting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class FragmentChatting extends Fragment {

    RecyclerView recyclerView;
    ChatRecyclerViewAdapter adapter;
    String myUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chatting, container, false);

        //툴바 초기화 및 설정
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Firebase Realtime Database 참조
        DatabaseReference chatRoomsRef = FirebaseDatabase.getInstance().getReference().child("chatrooms");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        myUserId = preferences.getString("userId", "");

        // 채팅 목록을 저장할 리스트
        ArrayList<DataModelChat> chatRooms = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.recyclerViewChat);
        adapter = new ChatRecyclerViewAdapter(getActivity(), chatRooms);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        // 현재 사용자 아이디를 기준으로 채팅 목록을 실시간으로 감지하는 리스너 등록
        Query myChatRoomsQuery = chatRoomsRef.orderByChild("users/" + myUserId).equalTo(true);

        myChatRoomsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                // 새로운 채팅방이 추가될 때의 처리
                DataModelChat chatRoom = snapshot.getValue(DataModelChat.class);
                if (chatRoom != null) {
                    chatRoom.roomId = snapshot.getKey(); // 채팅방의 키를 설정
                    chatRooms.add(chatRoom);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 채팅방이 변경될 때의 처리
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // 채팅방이 삭제될 때의 처리
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 채팅방이 이동될 때의 처리
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 오류 처리
            }
        });

        //데이터 모델리스트
//        ArrayList<DataModelChat> dataModels = new ArrayList<>();
//
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data0", "data0", "data0", "data0"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data1", "data1", "data1", "data1"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data2", "data2", "data2", "data1"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data3", "data3", "data3", "data1"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data4", "data4", "data4", "data1"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data5", "data5", "data5", "data1"));
//        dataModels.add(new DataModelChat(R.drawable.basketball_icon, "data6", "data6", "data6", "data1"));


        // 프래그먼트에서 옵션 메뉴 사용을 허용
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_others, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.notification) {
            //TODO: 인텐트 완성
            //Intent intent = new Intent(getContext().getApplicationContext(), );
            //startActivity(intent);
        }
        return false;
    }
}
