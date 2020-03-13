package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private String uid;
    private String firstName;
    private String secondName;
    private String userEmail;
    private String passWord;
    private String photoUrl;
    private String idNumber;
    private String phoneNumber;
    private boolean isStudent;
    private String schoolName;


    public User(String userName, String firstName, String secondName, String userEmail, String passWord, String photoUrl, String idNumber, String phoneNumber, boolean isStudent, String schoolName) {
        this.userName = userName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userEmail = userEmail;
        this.passWord = passWord;
        this.photoUrl = photoUrl;
        this.idNumber = idNumber;
        this.isStudent = isStudent;
        this.schoolName = schoolName;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
