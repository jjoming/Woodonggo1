package com.example.woodonggo.Raingking;

public class DataModelRank {
    int ranking;
    String nick,userId;



    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.nick = userId;
    }

    public DataModelRank(int ranking, String nick, String userId) {
        this.ranking = ranking;
        this.nick = nick;
        this.userId = userId;
    }
}
