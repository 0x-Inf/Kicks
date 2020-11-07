package com.diablo.jayson.kicksv1.UI.Home;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class LocationBroadcast {

    private String broadcastingUserId;
    private String broadcastId;
    private Timestamp broadcastTime;
    private ArrayList<String> broadcastIntendedUserIds;
    private GeoPoint broadcastLocation;

    public LocationBroadcast() {
    }

    public LocationBroadcast(String broadcastingUserId, String broadcastId, Timestamp broadcastTime,
                             ArrayList<String> broadcastIntendedUserIds, GeoPoint broadcastLocation) {
        this.broadcastingUserId = broadcastingUserId;
        this.broadcastId = broadcastId;
        this.broadcastTime = broadcastTime;
        this.broadcastIntendedUserIds = broadcastIntendedUserIds;
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

    public ArrayList<String> getBroadcastIntendedUserIds() {
        return broadcastIntendedUserIds;
    }

    public void setBroadcastIntendedUserIds(ArrayList<String> broadcastIntendedUserIds) {
        this.broadcastIntendedUserIds = broadcastIntendedUserIds;
    }

    public GeoPoint getBroadcastLocation() {
        return broadcastLocation;
    }

    public void setBroadcastLocation(GeoPoint broadcastLocation) {
        this.broadcastLocation = broadcastLocation;
    }
}
