package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.List;

public class Activity extends BaseObservable {

    private Host host;
    private String kickTitle;
    private String kickTime;
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

    public Activity() {

    }

    //Constructor for Activity data model


    public Activity(Host host, String kicktitle, String kicktime, String kickdate, String kicklocation,
                    String minrequiredpeople, String maxrequiredpeeps, String imageUrl, List<String> tags, long uploadedtime, String uploaderUid, int likes, String activityId) {
        this.kickTitle = kicktitle;
        this.kickTime = kicktime;
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

    public void setmKickTime(String mKickTime) {
        this.kickTime = mKickTime;
        notifyPropertyChanged(BR.kickTime);
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
        return kickTime;
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
