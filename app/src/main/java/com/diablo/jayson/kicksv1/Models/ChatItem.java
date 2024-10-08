package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Observable;

public class ChatItem extends Observable implements Serializable {

    private String senderName;
    private String senderPicUrl;
    private String senderUid;
    private String message;
    private Timestamp timestamp;

    public ChatItem(String senderName, String senderPicUrl, String senderUid, String message, Timestamp timestamp) {
        this.senderName = senderName;
        this.senderPicUrl = senderPicUrl;
        this.senderUid = senderUid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatItem() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPicUrl() {
        return senderPicUrl;
    }

    public void setSenderPicUrl(String senderPicUrl) {
        this.senderPicUrl = senderPicUrl;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
