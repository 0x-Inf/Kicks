package com.diablo.jayson.kicksv1.UI.Home;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class PublicLocationBroadcast {

    private String broadcastingUserId;
    private String broadcastId;
    private Timestamp broadcastTime;
    private GeoPoint broadcastLocation;

    public PublicLocationBroadcast() {
    }

    public PublicLocationBroadcast(String broadcastingUserId, String broadcastId, Timestamp broadcastTime, GeoPoint broadcastLocation) {
        this.broadcastingUserId = broadcastingUserId;
        this.broadcastId = broadcastId;
        this.broadcastTime = broadcastTime;
        this.broadcastLocation = broadcastLocation;
    }

    public String getBroadcastingUserId() {
        return broadcastingUserId;
    }

    public void setBroadcastingUserId(String broadcastingUserId) {
        this.broadcastingUserId = broadcastingUserId;
    }

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public Timestamp getBroadcastTime() {
        return broadcastTime;
    }

    public void setBroadcastTime(Timestamp broadcastTime) {
        this.broadcastTime = broadcastTime;
    }

    public GeoPoint getBroadcastLocation() {
        return broadcastLocation;
    }

    public void setBroadcastLocation(GeoPoint broadcastLocation) {
        this.broadcastLocation = broadcastLocation;
    }
}
