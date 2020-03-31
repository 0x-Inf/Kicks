package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity extends BaseObservable implements Serializable {

    private Host host;
    private String kickTitle;
    private String kickStartTime;
    private String kickEndTime;
    private String kickDate;
    private String kickLocationName;
    private GeoPoint kickLocationCordinates;
    private String minRequiredPeople;
    private String maxRequiredPeeps;
    private String minAge;
    private String maxAge;
    private String imageUrl;
    private String uploaderId;
    private String activityId;
    private String activityCost;
    private long uploadedTime;
    private List<String> tags;
    private Tag tag;
    private ArrayList<AttendingUser> mattendees;


    public Activity() {

    }

    //Constructor for Activity data model


    public Activity(Host host, String kicktitle, String kickStartTime, String kickEndTime, String kickdate,
                    String kicklocationname, GeoPoint locationCordinates,
                    String minrequiredpeople, String maxrequiredpeeps, String minage, String maxage,
                    String imageUrl, List<String> tags, long uploadedtime,
                    String uploaderUid, String activityId, Tag tag, ArrayList<AttendingUser> mattendees,
                    String activityCost) {
        this.kickTitle = kicktitle;
        this.kickStartTime = kickStartTime;
        this.kickEndTime = kickEndTime;
        this.kickDate = kickdate;
        this.kickLocationName = kicklocationname;
        this.kickLocationCordinates = locationCordinates;
        this.minRequiredPeople = minrequiredpeople;
        this.maxRequiredPeeps = maxrequiredpeeps;
        this.minAge = minage;
        this.maxAge = maxage;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.uploadedTime = uploadedtime;
        this.uploaderId = uploaderUid;
        this.host = host;
        this.activityId = activityId;
        this.tag = tag;
        this.mattendees = mattendees;
        this.activityCost = activityCost;

    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getKickTitle() {
        return kickTitle;
    }

    public void setKickTitle(String kickTitle) {
        this.kickTitle = kickTitle;
    }

    public String getKickStartTime() {
        return kickStartTime;
    }

    public void setKickStartTime(String kickStartTime) {
        this.kickStartTime = kickStartTime;
    }

    public String getKickEndTime() {
        return kickEndTime;
    }

    public void setKickEndTime(String kickEndTime) {
        this.kickEndTime = kickEndTime;
    }

    public String getKickDate() {
        return kickDate;
    }

    public void setKickDate(String kickDate) {
        this.kickDate = kickDate;
    }

    public String getKickLocationName() {
        return kickLocationName;
    }

    public void setKickLocationName(String kickLocationName) {
        this.kickLocationName = kickLocationName;
    }

    public GeoPoint getKickLocationCordinates() {
        return kickLocationCordinates;
    }

    public void setKickLocationCordinates(GeoPoint kickLocationCordinates) {
        this.kickLocationCordinates = kickLocationCordinates;
    }

    public String getMinRequiredPeople() {
        return minRequiredPeople;
    }

    public void setMinRequiredPeople(String minRequiredPeople) {
        this.minRequiredPeople = minRequiredPeople;
    }

    public String getMaxRequiredPeeps() {
        return maxRequiredPeeps;
    }

    public void setMaxRequiredPeeps(String maxRequiredPeeps) {
        this.maxRequiredPeeps = maxRequiredPeeps;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(String activityCost) {
        this.activityCost = activityCost;
    }

    public long getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(long uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public ArrayList<AttendingUser> getMattendees() {
        return mattendees;
    }

    public void setMattendees(ArrayList<AttendingUser> mattendees) {
        this.mattendees = mattendees;
    }
}
