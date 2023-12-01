package com.example.woodonggo.Chatting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataModelMessage {

    private String message;
    private boolean isMyMessage; // 메시지의 소유자 여부
    private boolean isDateMessage; // 날짜 메시지 여부
    private Date time; // 시간을 저장하는 필드
    private Date date;

    public DataModelMessage() {
        // 기본 생성자
    }

    public DataModelMessage(String message, boolean isMyMessage, boolean isDateMessage, Date time) {
        this.message = message;
        this.isMyMessage = isMyMessage;
        this.isDateMessage = isDateMessage;
        this.time = time;
        //this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public boolean isDateMessage() {
        return isDateMessage;
    }

    public String getTime() {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(date);
        } else {
            return ""; // 또는 다른 기본값을 반환할 수 있음
        }
    }


    public  String getDate() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        return sdf2.format(date);
    }

}
