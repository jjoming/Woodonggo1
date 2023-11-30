package com.example.woodonggo.Home;

import com.google.firebase.Timestamp;

public class TeamModel {

    String userId,sports;
    String writingId,writer,content;
    boolean team;
    Timestamp uploadDate;
    private int likesCount;
    String region;
    public TeamModel() {
        // 매개변수가 없는 기본 생성자가 필요합니다.
    }
    public TeamModel(String content, Timestamp uploadDate, boolean team, String userId,
                     String writingId, String writer, String sports, int likesCount,
                        String region) {
        this.writer = writer;
        this.uploadDate = uploadDate;
        this.content = content;
        this.team = team;
        this.userId = userId;
        this.writingId = writingId;
        this.sports = sports;
        this.likesCount = likesCount;
        this.region = region;
    }


    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getTeam() {
        return team;
    }

    public void setTeam(boolean team) {
        this.team = team;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWritingId() {
        return writingId;
    }

    public void setWritingId(String writingId) {
        this.writingId = writingId;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


}