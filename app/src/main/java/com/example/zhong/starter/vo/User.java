package com.example.zhong.starter.vo;

import java.util.UUID;

public class User {
    private String userID;
    private String username;
    private String phoneNum;
    private String headImgURL;

    public User(String phoneNum) {
        this.userID= UUID.randomUUID().toString();
        this.phoneNum = phoneNum;
        this.username=phoneNum;
    }

    //mybatis select返回时的构造函数
    public User(String userID, String username, String phoneNum, String headImgURL) {
        this.userID = userID;
        this.username = username;
        this.phoneNum = phoneNum;
        this.headImgURL = headImgURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getHeadImgURL() {
        return headImgURL;
    }

    public void setHeadImgURL(String headImgURL) {
        this.headImgURL = headImgURL;
    }
}
