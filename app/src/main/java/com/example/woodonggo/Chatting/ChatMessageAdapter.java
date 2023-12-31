package com.example.woodonggo.Chatting;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int MY_MESSAGE = 0;
    final int OTHER_MESSAGE = 1;
    final int DATE_MESSAGE = 2;

    private ArrayList<DataModelMessage> chatList;

    public ChatMessageAdapter() {
        chatList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view;
        switch (viewType) {
            case MY_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_right_text, parent, false);
                return new MyMessageViewHolder(view);
            case OTHER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_left_text, parent, false);
                return new OtherMessageViewHolder(view);
            case DATE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_date_item, parent, false);
                return new DateViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");

        int viewType = getItemViewType(position);
        DataModelMessage message = chatList.get(position);

        switch (viewType) {
            case MY_MESSAGE:
                ((MyMessageViewHolder) holder).bind(message);
                break;
            case OTHER_MESSAGE:
                ((OtherMessageViewHolder) holder).bind(message);
                break;
            case DATE_MESSAGE:
                ((DateViewHolder) holder).bind(message);
                break;
        }
    }

    // 외부에서 아이템 추가 요청 시 사용
    /*
    public void add(String msg, int type) {
        ListContents contents = new ListContents(msg, type);
        chatList.add(contents);
    }
*/

    public void add(DataModelMessage message) {
        chatList.add(message);
        notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
    }

    @Override
    public int getItemViewType(int position) {

        if (chatList.get(position).isMyMessage()) {
            return MY_MESSAGE;
        } else if (chatList.get(position).isDateMessage()) {
            return DATE_MESSAGE;
        } else {
            return OTHER_MESSAGE;
        }

        /*
        if (chatList.get(position).type == 0) {
            return OTHER_MESSAGE;
        } else if (chatList.get(position).type == 1) {
            return MY_MESSAGE;
        } else {
            return DATE_MESSAGE;
        }
        */
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        chatList.remove(_position);
    }

    /* @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    */

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void clear() {
        chatList.clear();
        notifyDataSetChanged();
    }
}

    class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.chatTextMy);
            timeText = itemView.findViewById(R.id.timeTextMy);
        }

        public void bind(DataModelMessage message) {
            // 메시지 및 시간 텍스트 설정
            messageText.setText(message.getMessage());
            timeText.setText(message.getTime());
            // 다른 UI 설정...
        }
    }

    class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public OtherMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.chatTextOther);
            timeText = itemView.findViewById(R.id.timeTextOther);
        }

        public void bind(DataModelMessage message) {
            // 메시지 및 시간 텍스트 설정
            messageText.setText(message.getMessage());
            timeText.setText(message.getTime());
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(DataModelMessage message) {
            date.setText(message.getDate());
        }
    }