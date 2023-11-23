package com.example.woodonggo;

public class DataModel {
    String title;
    String tel;
    String address;
    String placeUrl;

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

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public DataModel(String title, String address, String tel, String url) {
        this.title = title;
        this.address = address;
        this.tel = tel;
        this.placeUrl = url;
    }
}
