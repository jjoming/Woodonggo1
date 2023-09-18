package com.example.woodonggo;

public class DataModel_chat {
    int img_source;
    String name;
    String chat;
    String add;
    String time;

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

    public DataModel_chat(int img_source, String name, String chat, String add, String time) {
        this.img_source = img_source;
        this.name = name;
        this.chat = chat;
        this.add = add;
        this.time = time;
    }
}
