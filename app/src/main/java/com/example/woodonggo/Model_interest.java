package com.example.woodonggo;

public class Model_interest {
    private int profileImageResource;
    private String name;
    private String uploadTime;
    private String title;
    private boolean isPersonal;
    private boolean isTeam;

    public Model_interest(int profileImageResource, String name, String uploadTime, String title, boolean isPersonal, boolean isTeam) {
        this.profileImageResource = profileImageResource;
        this.name = name;
        this.uploadTime = uploadTime;
        this.title = title;
        this.isPersonal = isPersonal;
        this.isTeam = isTeam;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }

    public String getName() {
        return name;
    }

    public String getUploadTime() {
        return uploadTime;
    }


    public String getTitle() {
        return title;
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public boolean isTeam() {
        return isTeam;
    }
}
