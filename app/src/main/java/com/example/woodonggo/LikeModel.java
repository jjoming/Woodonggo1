package com.example.woodonggo;

public class LikeModel {
    private String userId;
    private String writingId;
    private boolean isLiked;

    // 생성자
    public LikeModel(String userId, String writingId, boolean isLiked) {
        this.userId = userId;
        this.writingId = writingId;
        this.isLiked = isLiked;
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
