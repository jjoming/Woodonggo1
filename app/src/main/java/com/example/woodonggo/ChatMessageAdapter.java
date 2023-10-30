package com.example.woodonggo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {
    final int MY_MESSAGE = 0;
    final int OTHER_MESSAGE = 1;
    final int DATE_MESSAGE = 2;
    public class ListContents{
        String msg;
        int type;
        ListContents(String msg,int type)
        {
            this.msg = msg;
            this.type = type;
        }
    }

    private ArrayList<ListContents> chatList;
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

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");

        MyViewHolder myViewHolder = (MyViewHolder)holder;

        /* myViewHolder.name_chat.setText(dataModels.get(position).getName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat_Details.class);
                context.startActivity(intent);
            }
        }); */
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg,int _type) {
        chatList.add(new ListContents(_msg,_type));
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).type == 0) {
            return OTHER_MESSAGE;
        } else if (chatList.get(position).type == 1) {
            return MY_MESSAGE;
        } else {
            return DATE_MESSAGE;
        }
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        chatList.remove(_position);
    }

    @Override
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

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView        text    = null;
        CustomHolder    holder  = null;
        LinearLayout layout  = null;
        View            viewRight = null;
        View            viewLeft = null;



        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chatting_detail, parent, false);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView = text;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder = (CustomHolder) convertView.getTag();
            text = holder.m_TextView;
            layout = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }

        // Text 등록
        text.setText(chatList.get(position).msg);

        if(chatList.get(position).type == 0 ) {

        } else if(chatList.get(position).type == 1){

        } else if(chatList.get(position).type == 2){

        }



        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : " + chatList.get(pos), Toast.LENGTH_SHORT).show();
            }
        });



        // 리스트 아이템을 길게 터치 했을때 이벤트 발생
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : " + chatList.get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }

    private class CustomHolder {
        TextView m_TextView;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_chat;
        TextView chat_chat;
        TextView add_chat;
        TextView time_chat;
        int img_source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_chat =  itemView.findViewById(R.id.name_chat);
            chat_chat = itemView.findViewById(R.id.chat_chat);
            add_chat = itemView.findViewById(R.id.add_chat);
            time_chat = itemView.findViewById(R.id.time_chat);
        }
    }
}
