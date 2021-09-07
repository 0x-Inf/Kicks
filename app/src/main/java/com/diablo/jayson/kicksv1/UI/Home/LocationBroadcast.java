package com.diablo.jayson.kicksv1.UI.Home;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class LocationBroadcast {

    private String broadcastingUserId;
    private String broadcastId;
    private Timestamp broadcastTime;
    private ArrayList<String> broadcastIntendedUserIds;
    private GeoPoint broadcastLocation;
    private ArrayList<Tag> broadcastTags;

    public LocationBroadcast() {
    }

    public LocationBroadcast(String broadcastingUserId, String broadcastId, Timestamp broadcastTime,
                             ArrayList<String> broadcastIntendedUserIds, GeoPoint broadcastLocation,
                             ArrayList<Tag> broadcastTags) {
        this.broadcastingUserId = broadcastingUserId;
        this.broadcastId = broadcastId;
        this.broadcastTime = broadcastTime;
        this.broadcastIntendedUserIds = broadcastIntendedUserIds;
        this.broadcastLocation = broadcastLocation;
        this.broadcastTags = broadcastTags;
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

    public ArrayList<Tag> getBroadcastTags() {
        return broadcastTags;
    }

    public void setBroadcastTags(ArrayList<Tag> broadcastTags) {
        this.broadcastTags = broadcastTags;
    }
}
