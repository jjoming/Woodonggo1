package com.example.woodonggo;

public class DataModel {
    String title;
    String tel;
    String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public DataModel(String title, String address, String tel) {
        this.title = title;
        this.address = address;
        this.tel = tel;
    }
}
