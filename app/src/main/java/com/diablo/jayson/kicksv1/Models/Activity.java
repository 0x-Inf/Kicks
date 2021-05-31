package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;

import com.diablo.jayson.kicksv1.UI.AddActivity.Duration;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity extends BaseObservable implements Serializable {

    private Host host;
    private String activityTitle;
    private String activityDescription;
    private Timestamp activityStartTime;
    private Timestamp activityStartDate;
    private Duration activityDuration;
    private String activityLocationName;
    private GeoPoint activityLocationCoordinates;
    private String geohash;
    private boolean isLocationUndisclosed;
    private String activityNoOfPeople;
    private ArrayList<String> invitedPeopleUserIds;
    private boolean isActivityPrivate;
    private ArrayList<Tag> activityTags;
    private String activityCost;
    private String activityId;
    private String activityUploaderId;
    private Timestamp activityUploadedTime;
    private ArrayList<AttendingUser> activityAttendees;


    public Activity() {

    }
    //Constructor for Activity data model

    public Activity(Host host, String activityTitle, String activityDescription, Timestamp activityStartTime, Timestamp activityStartDate,
                    Duration activityDuration, String activityLocationName, GeoPoint activityLocationCoordinates, String geohash,
                    boolean isLocationUndisclosed, String activityNoOfPeople, ArrayList<String> invitedPeopleUserIds, boolean isActivityPrivate,
                    ArrayList<Tag> activityTags, String activityCost, String activityId, String activityUploaderId, Timestamp activityUploadedTime,
                    ArrayList<AttendingUser> activityAttendees) {
        this.host = host;
        this.activityTitle = activityTitle;
        this.activityDescription = activityDescription;
        this.activityStartTime = activityStartTime;
        this.activityStartDate = activityStartDate;
        this.activityDuration = activityDuration;
        this.activityLocationName = activityLocationName;
        this.activityLocationCoordinates = activityLocationCoordinates;
        this.geohash = geohash;
        this.isLocationUndisclosed = isLocationUndisclosed;
        this.activityNoOfPeople = activityNoOfPeople;
        this.invitedPeopleUserIds = invitedPeopleUserIds;
        this.isActivityPrivate = isActivityPrivate;
        this.activityTags = activityTags;
        this.activityCost = activityCost;
        this.activityId = activityId;
        this.activityUploaderId = activityUploaderId;
        this.activityUploadedTime = activityUploadedTime;
        this.activityAttendees = activityAttendees;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Timestamp getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Timestamp activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Timestamp getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(Timestamp activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public Duration getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(Duration activityDuration) {
        this.activityDuration = activityDuration;
    }

    public String getActivityLocationName() {
        return activityLocationName;
    }

    public void setActivityLocationName(String activityLocationName) {
        this.activityLocationName = activityLocationName;
    }

    public GeoPoint getActivityLocationCoordinates() {
        return activityLocationCoordinates;
    }

    public void setActivityLocationCoordinates(GeoPoint activityLocationCoordinates) {
        this.activityLocationCoordinates = activityLocationCoordinates;
    }

    public String getGeoHash() {
        return geohash;
    }

    public void setGeoHash(String geohash) {
        this.geohash = geohash;
    }

    public boolean isLocationUndisclosed() {
        return isLocationUndisclosed;
    }

    public void setLocationUndisclosed(boolean locationUndisclosed) {
        isLocationUndisclosed = locationUndisclosed;
    }

    public String getActivityNoOfPeople() {
        return activityNoOfPeople;
    }

    public void setActivityNoOfPeople(String activityNoOfPeople) {
        this.activityNoOfPeople = activityNoOfPeople;
    }

    public ArrayList<String> getInvitedPeopleUserIds() {
        return invitedPeopleUserIds;
    }

    public void setInvitedPeopleUserIds(ArrayList<String> invitedPeopleUserIds) {
        this.invitedPeopleUserIds = invitedPeopleUserIds;
    }

    public boolean isActivityPrivate() {
        return isActivityPrivate;
    }

    public void setActivityPrivate(boolean activityPrivate) {
        isActivityPrivate = activityPrivate;
    }

    public ArrayList<Tag> getActivityTags() {
        return activityTags;
    }

    public void setActivityTags(ArrayList<Tag> activityTags) {
        this.activityTags = activityTags;
    }

    public String getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(String activityCost) {
        this.activityCost = activityCost;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityUploaderId() {
        return activityUploaderId;
    }

    public void setActivityUploaderId(String activityUploaderId) {
        this.activityUploaderId = activityUploaderId;
    }

    public Timestamp getActivityUploadedTime() {
        return activityUploadedTime;
    }

    public void setActivityUploadedTime(Timestamp activityUploadedTime) {
        this.activityUploadedTime = activityUploadedTime;
    }

    public ArrayList<AttendingUser> getActivityAttendees() {
        return activityAttendees;
    }

    public void setActivityAttendees(ArrayList<AttendingUser> activityAttendees) {
        this.activityAttendees = activityAttendees;
    }
}
