package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Tag extends BaseObservable implements Serializable {

    private String tagName;
    private GeoPoint tagLocation;
    private String tagOptimalMinPeople;
    private String tagOptimalMaxPeople;
    private String tagCost;
    private String tagIconUrl;
    private String tagImageLargeUrl;
    private String tagOptimalStartTime;
    private String tagLocationName;


    public Tag(String tagName, GeoPoint tagLocation, String tagOptimalMinPeople, String tagOptimalMaxPeople, String tagCost, String tagIconUrl, String tagImageLargeUrl, String tagOptimalStartTime, String tagLocationName) {
        this.tagName = tagName;
        this.tagLocation = tagLocation;
        this.tagOptimalMinPeople = tagOptimalMinPeople;
        this.tagOptimalMaxPeople = tagOptimalMaxPeople;
        this.tagCost = tagCost;
        this.tagIconUrl = tagIconUrl;
        this.tagImageLargeUrl = tagImageLargeUrl;
        this.tagOptimalStartTime = tagOptimalStartTime;
        this.tagLocationName = tagLocationName;
    }

    public Tag() {

    }

    public <T> Tag(T toObject) {

    }

    @Bindable
    public String getTagLocationName() {
        return tagLocationName;
    }

    public void setTagLocationName(String tagLocationName) {
        this.tagLocationName = tagLocationName;
    }

    @Bindable
    public String getTagOptimalStartTime() {
        return tagOptimalStartTime;
    }

    public void setTagOptimalStartTime(String tagOptimalStartTime) {
        this.tagOptimalStartTime = tagOptimalStartTime;
    }

    @Bindable
    public String getTagIconUrl() {
        return tagIconUrl;
    }

    public void setTagIconUrl(String tagIconUrl) {
        this.tagIconUrl = tagIconUrl;
    }

    @Bindable
    public String getTagImageLargeUrl() {
        return tagImageLargeUrl;
    }

    public void setTagImageLargeUrl(String tagImageLargeUrl) {
        this.tagImageLargeUrl = tagImageLargeUrl;
    }


    @Bindable
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
        notifyPropertyChanged(BR.tagName);
    }

    @Bindable
    public String getTagCost() {
        return tagCost;
    }

    public void setTagCost(String tagCost) {
        this.tagCost = tagCost;
        notifyPropertyChanged(BR.tagCost);
    }

    @Bindable
    public GeoPoint getTagLocation() {
        return tagLocation;
    }

    public void setTagLocation(GeoPoint tagLocation) {
        this.tagLocation = tagLocation;
        notifyPropertyChanged(BR.tagLocation);
    }

    @Bindable
    public String getTagOptimalMinPeople() {
        return tagOptimalMinPeople;
    }

    public void setTagOptimalMinPeople(String tagOptimalMinPeople) {
        this.tagOptimalMinPeople = tagOptimalMinPeople;
        notifyPropertyChanged(BR.tagOptimalMinPeople);
    }

    @Bindable
    public String getTagOptimalMaxPeople() {
        return tagOptimalMaxPeople;
    }

    public void setTagOptimalMaxPeople(String tagOptimalMaxPeople) {
        this.tagOptimalMaxPeople = tagOptimalMaxPeople;
        notifyPropertyChanged(BR.tagOptimalMaxPeople);
    }
}
