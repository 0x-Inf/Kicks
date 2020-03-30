package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

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

    public GeoPoint getKickLocationCordinates() {
        return kickLocationCordinates;
    }

    public void setKickLocationCordinates(GeoPoint kickLocationCordinates) {
        this.kickLocationCordinates = kickLocationCordinates;
    }

    @Bindable
    public String getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(String activityCost) {
        this.activityCost = activityCost;
        notifyPropertyChanged(BR.activityCost);
    }

    @Bindable
    public ArrayList<AttendingUser> getMattendees() {
        return mattendees;
    }

    public void setMattendees(ArrayList<AttendingUser> mattendees) {
        this.mattendees = mattendees;
    }



    @Bindable
    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }


    @Bindable
    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
        notifyPropertyChanged(BR.host);
    }




    public void setmKickTitle(String mKickTitle) {
        this.kickTitle = mKickTitle;
        notifyPropertyChanged(BR.kickTitle);
    }

    public void setmKickTime(String mKickStartTime) {
        this.kickStartTime = mKickStartTime;
        notifyPropertyChanged(BR.kickTime);
    }

    @Bindable
    public String getKickEndTime() {
        return kickEndTime;
    }

    public void setKickEndTime(String kickEndTime) {
        this.kickEndTime = kickEndTime;
    }

    @Bindable
    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
        notifyPropertyChanged(BR.uploaderId);
    }

    @Bindable
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
        notifyPropertyChanged(BR.activityId);
    }

    @Bindable
    public long getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(long uploadedTime) {
        this.uploadedTime = uploadedTime;
        notifyPropertyChanged(BR.uploadedTime);
    }

    public void setmKickDate(String mKickDate) {
        this.kickDate = mKickDate;
        notifyPropertyChanged(BR.kickDate);
    }

    public void setmKickLocation(String mKickLocation) {
        this.kickLocationName = mKickLocation;
        notifyPropertyChanged(BR.kickLocation);
    }

    public void setMinRequiredPeople(String minRequiredPeople) {
        this.minRequiredPeople = minRequiredPeople;
        notifyPropertyChanged(BR.minRequiredPeople);
    }

    public void setMaxRequiredPeeps(String maxRequiredPeeps) {
        this.maxRequiredPeeps = maxRequiredPeeps;
        notifyPropertyChanged(BR.maxRequiredPeeps);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        notifyPropertyChanged(BR.tags);
    }

    public void setmImageUrl(String mImageUrl) {
        this.imageUrl = mImageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getkickTitle() {
        return kickTitle;
    }

    @Bindable
    public String getkickTime() {
        return kickStartTime;
    }

    @Bindable
    public String getkickDate() {
        return kickDate;
    }

    @Bindable
    public String getkickLocation() {
        return kickLocationName;
    }

    @Bindable
    public String getimageUrl() {
        return imageUrl;
    }

    @Bindable
    public String getMaxRequiredPeeps() {
        return maxRequiredPeeps;
    }

    @Bindable
    public String getMinRequiredPeople() {
        return minRequiredPeople;
    }

    @Bindable
    public List<String> getTags() {
        return tags;
    }
}
