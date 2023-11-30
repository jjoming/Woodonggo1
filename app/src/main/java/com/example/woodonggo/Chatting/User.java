package com.example.woodonggo.Chatting;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String name;
    private String passwd;
    private String region1;
    private String region2;
    private boolean auto;

    // 기본 생성자가 필요합니다.
    public User() {
        // 기본 생성자는 Firebase에서 데이터를 가져올 때 필요합니다.
    }

    // Getter 및 Setter 메서드를 정의합니다.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRegion1() {
        return region1;
    }

    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    public String getRegion2() {
        return region2;
    }

    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
}