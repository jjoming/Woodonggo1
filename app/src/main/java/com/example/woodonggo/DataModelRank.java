package com.example.woodonggo;

public class DataModelRank {
    String ranking;
    int img;
    String nick;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public DataModelRank(String ranking, int img, String nick) {
        this.ranking = ranking;
        this.img = img;
        this.nick = nick;
    }
}
