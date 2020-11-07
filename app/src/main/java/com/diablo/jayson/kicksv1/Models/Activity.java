package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity extends BaseObservable implements Serializable {

    private Host host;
    private String activityTitle;
    private String activityDescription;
    private Timestamp activityStartTime;
    private Timestamp activityEndTime;
    private Timestamp activityDate;
    private String activityLocationName;
    private GeoPoint activityLocationCoordinates;
    private int activityMinRequiredPeople;
    private int activityMaxRequiredPeople;
    private int activityMinAge;
    private int activityMaxAge;
    private ArrayList<String> invitedPeopleUserIds;
    private String imageUrl;
    private String activityUploaderId;
    private String activityId;
    private String activityCost;
    private Timestamp activityUploadedTime;
    private List<String> tags;
    private Tag activityTag;
    private ArrayList<AttendingUser> activityAttendees;
    private boolean isActivityPrivate;


    public Activity() {

    }

    //Constructor for Activity data model

    public Activity(Host host, String activityTitle, String activityDescription, Timestamp activityStartTime, Timestamp activityEndTime,
                    Timestamp activityDate, String activityLocationName, GeoPoint activityLocationCoordinates, int activityMinRequiredPeople,
                    int activityMaxRequiredPeople, int activityMinAge, int activityMaxAge, ArrayList<String> invitedPeopleUserIds,
                    String imageUrl, String activityUploaderId, String activityId, String activityCost,
                    Timestamp activityUploadedTime, List<String> tags, Tag activityTag, ArrayList<AttendingUser> activityAttendees,
                    boolean isActivityPrivate) {
        this.host = host;
        this.activityTitle = activityTitle;
        this.activityDescription = activityDescription;
        this.activityStartTime = activityStartTime;
        this.activityEndTime = activityEndTime;
        this.activityDate = activityDate;
        this.activityLocationName = activityLocationName;
        this.activityLocationCoordinates = activityLocationCoordinates;
        this.activityMinRequiredPeople = activityMinRequiredPeople;
        this.activityMaxRequiredPeople = activityMaxRequiredPeople;
        this.activityMinAge = activityMinAge;
        this.activityMaxAge = activityMaxAge;
        this.invitedPeopleUserIds = invitedPeopleUserIds;
        this.imageUrl = imageUrl;
        this.activityUploaderId = activityUploaderId;
        this.activityId = activityId;
        this.activityCost = activityCost;
        this.activityUploadedTime = activityUploadedTime;
        this.tags = tags;
        this.activityTag = activityTag;
        this.activityAttendees = activityAttendees;
        this.isActivityPrivate = isActivityPrivate;
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

    public Timestamp getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Timestamp activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Timestamp getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Timestamp activityDate) {
        this.activityDate = activityDate;
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

    public int getActivityMinRequiredPeople() {
        return activityMinRequiredPeople;
    }

    public void setActivityMinRequiredPeople(int activityMinRequiredPeople) {
        this.activityMinRequiredPeople = activityMinRequiredPeople;
    }

    public int getActivityMaxRequiredPeople() {
        return activityMaxRequiredPeople;
    }

    public void setActivityMaxRequiredPeople(int activityMaxRequiredPeople) {
        this.activityMaxRequiredPeople = activityMaxRequiredPeople;
    }

    public int getActivityMinAge() {
        return activityMinAge;
    }

    public void setActivityMinAge(int activityMinAge) {
        this.activityMinAge = activityMinAge;
    }

    public int getActivityMaxAge() {
        return activityMaxAge;
    }

    public void setActivityMaxAge(int activityMaxAge) {
        this.activityMaxAge = activityMaxAge;
    }

    public ArrayList<String> getInvitedPeopleUserIds() {
        return invitedPeopleUserIds;
    }

    public void setInvitedPeopleUserIds(ArrayList<String> invitedPeopleUserIds) {
        this.invitedPeopleUserIds = invitedPeopleUserIds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityUploaderId() {
        return activityUploaderId;
    }

    public void setActivityUploaderId(String activityUploaderId) {
        this.activityUploaderId = activityUploaderId;
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

    public Timestamp getActivityUploadedTime() {
        return activityUploadedTime;
    }

    public void setActivityUploadedTime(Timestamp activityUploadedTime) {
        this.activityUploadedTime = activityUploadedTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Tag getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(Tag activityTag) {
        this.activityTag = activityTag;
    }

    public ArrayList<AttendingUser> getActivityAttendees() {
        return activityAttendees;
    }

    public void setActivityAttendees(ArrayList<AttendingUser> activityAttendees) {
        this.activityAttendees = activityAttendees;
    }

    public boolean isActivityPrivate() {
        return isActivityPrivate;
    }

    public void setActivityPrivate(boolean activityPrivate) {
        isActivityPrivate = activityPrivate;
    }
}
