package com.example.woodonggo.Chatting;

import java.util.HashMap;
import java.util.Map;

public class DataModelChat {
    int img_source;
    String name;
    String chat;
    String add;
    String time;

    public String roomId; // 채팅방의 키
    public Map<String, Boolean> users = new HashMap<>(); // 채팅방 유저 목록
    public Map<String, DataModelChat> lastMessage = new HashMap<>(); // 채팅방의 마지막 메시지

    public int getImg() {
        return img_source;
    }

    public void setImg(int img_source) {
        this.img_source = img_source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataModelChat(int img_source, String name, String chat, String add, String time) {
        this.img_source = img_source;
        this.name = name;
        this.chat = chat;
        this.add = add;
        this.time = time;
    }
}