package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity extends BaseObservable implements Serializable {

    private Host host;
    private String kickTitle;
    private String kickStartTime;
    private String kickEndTime;
    private String kickDate;
    private String kickLocation;
    private String minRequiredPeople;
    private String maxRequiredPeeps;
    private String imageUrl;
    private String uploaderId;
    private String activityId;
    private int likes;
    private long uploadedTime;
    private List<String> tags;
    private Tag tag;
    private ArrayList<AttendingUser> mattendees;
    private String state;


    public Activity() {

    }

    //Constructor for Activity data model


    public Activity(Host host, String kicktitle, String kickStartTime, String kickEndTime, String kickdate, String kicklocation,
                    String minrequiredpeople, String maxrequiredpeeps, String imageUrl, List<String> tags, long uploadedtime,
                    String uploaderUid, int likes, String activityId, Tag tag, ArrayList<AttendingUser> mattendees, String state) {
        this.kickTitle = kicktitle;
        this.kickStartTime = kickStartTime;
        this.kickEndTime = kickEndTime;
        this.kickDate = kickdate;
        this.kickLocation = kicklocation;
        this.minRequiredPeople = minrequiredpeople;
        this.maxRequiredPeeps = maxrequiredpeeps;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.uploadedTime = uploadedtime;
        this.uploaderId = uploaderUid;
        this.host = host;
        this.likes = likes;
        this.activityId = activityId;
        this.tag = tag;
        this.state = state;
        this.mattendees = mattendees;

    }

    @Bindable
    public ArrayList<AttendingUser> getMattendees() {
        return mattendees;
    }

    public void setMattendees(ArrayList<AttendingUser> mattendees) {
        this.mattendees = mattendees;
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
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

    @Bindable
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
        notifyPropertyChanged(BR.likes);
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
        this.kickLocation = mKickLocation;
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
        return kickLocation;
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
