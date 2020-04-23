package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String userName;
    private String firstName;
    private String secondName;
    private String userEmail;
    private String passWord;
    private String photoUrl;
    private String phoneNumber;
    private Timestamp signedUpTime;

    public User() {
    }

    public User(String uid, String userName, String firstName, String secondName, String userEmail, String passWord, String photoUrl, String phoneNumber, Timestamp signedUpTime) {
        this.uid = uid;
        this.userName = userName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userEmail = userEmail;
        this.passWord = passWord;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.signedUpTime = signedUpTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getSignedUpTime() {
        return signedUpTime;
    }

    public void setSignedUpTime(Timestamp signedUpTime) {
        this.signedUpTime = signedUpTime;
    }
}
