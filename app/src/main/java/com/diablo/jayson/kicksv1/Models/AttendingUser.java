package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class AttendingUser implements Serializable {

    private String userName;
    private String uid;
    private String firstName;
    private String secondName;
    private String userEmail;
    private String photoUrl;
    private String idNumber;
    private String phoneNumber;
    private boolean isStudent;

    public AttendingUser(String userName, String uid, String firstName, String secondName, String userEmail, String photoUrl, String idNumber, String phoneNumber, boolean isStudent) {
        this.userName = userName;
        this.uid = uid;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userEmail = userEmail;
        this.photoUrl = photoUrl;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.isStudent = isStudent;
    }

    public AttendingUser() {

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }
}
