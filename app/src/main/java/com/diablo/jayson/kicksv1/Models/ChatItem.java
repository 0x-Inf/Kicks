package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Observable;

public class ChatItem extends Observable implements Serializable {

    private String senderName;
    private String senderPicUrl;
    private String message;
    private Timestamp timestamp;
    private boolean isSender;

    public ChatItem(String senderName, String senderPicUrl, String message, Timestamp timestamp, boolean isSender) {
        this.senderName = senderName;
        this.senderPicUrl = senderPicUrl;
        this.message = message;
        this.timestamp = timestamp;
        this.isSender = isSender;
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

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }
}
