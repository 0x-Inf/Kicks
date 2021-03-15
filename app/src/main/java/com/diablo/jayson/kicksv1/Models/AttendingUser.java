package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class AttendingUser implements Serializable {

    private String userName;
    private String uid;
    private String photoUrl;

    public AttendingUser() {
    }

    public AttendingUser(String userName, String uid, String photoUrl) {
        this.userName = userName;
        this.uid = uid;
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
