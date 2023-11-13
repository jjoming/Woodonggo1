package com.example.woodonggo;

public class ProfileModel {
    private String imageUrl;

    ProfileModel() {

    }

    public ProfileModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
