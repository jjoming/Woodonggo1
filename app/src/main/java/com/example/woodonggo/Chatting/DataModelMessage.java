package com.example.woodonggo.Chatting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataModelMessage {

    private String content;
    private boolean isMyMessage; // 메시지의 소유자 여부
    private boolean isDateMessage; // 날짜 메시지 여부
    private Date time; // 시간을 저장하는 필드
    private Date date;

    public DataModelMessage(String content, boolean isMyMessage, boolean isDateMessage, Date time) {
        this.content = content;
        this.isMyMessage = isMyMessage;
        this.isDateMessage = isDateMessage;
        this.time = time;
        //this.date = date;
    }

    public String getContent() {
        return content;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public boolean isDateMessage() {
        return isDateMessage;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(time); // 시간을 원하는 형식으로 반환, 예시로 HH:mm (시:분) 형식 사용
    }

    public  String getDate() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        return sdf2.format(date);
    }

}
