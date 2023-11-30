package com.example.woodonggo.Chatting;
import java.util.Map;

public class ChatRoom {
    public String roomId; // 채팅방의 키
    public Map<String, Boolean> users; // 채팅방 유저 목록

    public ChatRoom() {
        // Default constructor required for calls to DataSnapshot.getValue(ChatRoom.class)
    }

    public ChatRoom(String roomId, Map<String, Boolean> users) {
        this.roomId = roomId;
        this.users = users;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }
}