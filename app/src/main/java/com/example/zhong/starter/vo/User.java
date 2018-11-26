package com.example.zhong.starter.vo;


import java.util.UUID;

public class User {
    private String userID;
    private String username;
    private String phoneNum;
    private String headImgURL;
    private String sex;
    private String age;
    private String career;
    private String city;
    private String petExperience;
    private String preference;

    public User() {
    }

    public User(String userID, String username, String headImgURL, String sex, String age, String career, String city, String petExperience, String preference) {
        this.userID = userID;
        this.username = username;
        this.headImgURL = headImgURL;
        this.sex = sex;
        this.age = age;
        this.career = career;
        this.city = city;
        this.petExperience = petExperience;
        this.preference = preference;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPetExperience() {
        return petExperience;
    }

    public void setPetExperience(String petExperience) {
        this.petExperience = petExperience;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

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
