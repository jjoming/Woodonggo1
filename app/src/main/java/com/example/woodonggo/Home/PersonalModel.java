package com.example.woodonggo.Home;

import com.google.firebase.Timestamp;

public class PersonalModel {
    String userId;
    String writingId,writer,content;
    boolean team;
    Timestamp uploadDate;

    public PersonalModel(String content, Timestamp uploadDate, boolean team, String userId,
                     String writingId, String writer) {
        this.writer = writer;
        this.uploadDate = uploadDate;
        this.content = content;
        this.team = team;
        this.userId = userId;
        this.writingId = writingId;
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

}
