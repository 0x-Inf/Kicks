package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

public class ContactRequest {

    private String senderId;
    private String targetId;
    private String senderPicUrl;
    private String targetPicUrl;
    private String senderName;
    private String targetName;
    private String requestId;
    private Timestamp requestTime;

    public ContactRequest() {
    }

    public ContactRequest(String senderId, String targetId, String senderPicUrl, String targetPicUrl,
                          String senderName, String targetName, String requestId, Timestamp requestTime) {
        this.senderId = senderId;
        this.targetId = targetId;
        this.senderPicUrl = senderPicUrl;
        this.targetPicUrl = targetPicUrl;
        this.senderName = senderName;
        this.targetName = targetName;
        this.requestId = requestId;
        this.requestTime = requestTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSenderPicUrl() {
        return senderPicUrl;
    }

    public void setSenderPicUrl(String senderPicUrl) {
        this.senderPicUrl = senderPicUrl;
    }

    public String getTargetPicUrl() {
        return targetPicUrl;
    }

    public void setTargetPicUrl(String targetPicUrl) {
        this.targetPicUrl = targetPicUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }
}
