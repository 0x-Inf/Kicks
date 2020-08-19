package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

public class ContactRequest {

    private String senderId;
    private String targetId;
    private String senderPicUrl;
    private String senderName;
    private String requestId;
    private Timestamp requestTime;

    public ContactRequest() {
    }

    public ContactRequest(String senderId, String targetId, String senderPicUrl, String senderName, String requestId, Timestamp requestTime) {
        this.senderId = senderId;
        this.targetId = targetId;
        this.senderPicUrl = senderPicUrl;
        this.senderName = senderName;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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
