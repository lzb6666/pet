package com.example.zhong.starter.vo;

public class AdoptRecord {
    private String recordID;
    private String petID;
    private String petName;
    private String petDetail;
    private String imgURL;
    private String owner;
    private String adoptTime;
    private String userID;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public AdoptRecord(String recordID, String petName, String petDetail, String imgURL, String owner, String adoptTime, String userID) {
        this.recordID = recordID;
        this.petName = petName;
        this.petDetail = petDetail;
        this.imgURL = imgURL;
        this.owner = owner;
        this.adoptTime = adoptTime;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetDetail() {
        return petDetail;
    }

    public void setPetDetail(String petDetail) {
        this.petDetail = petDetail;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAdoptTime() {
        return adoptTime;
    }

    public void setAdoptTime(String adoptTime) {
        this.adoptTime = adoptTime;
    }
}
