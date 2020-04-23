package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Tag extends BaseObservable implements Serializable {

    private String tagName;
    private String tagShortDescription;
    private GeoPoint tagLocation;
    private String tagCost;
    private String tagIconUrl;
    private String tagImageLargeUrl;
    private String tagLocationName;

    public Tag() {
    }

    public Tag(String tagName, String tagShortDescription, GeoPoint tagLocation, String tagCost, String tagIconUrl, String tagImageLargeUrl, String tagLocationName) {
        this.tagName = tagName;
        this.tagShortDescription = tagShortDescription;
        this.tagLocation = tagLocation;
        this.tagCost = tagCost;
        this.tagIconUrl = tagIconUrl;
        this.tagImageLargeUrl = tagImageLargeUrl;
        this.tagLocationName = tagLocationName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagShortDescription() {
        return tagShortDescription;
    }

    public void setTagShortDescription(String tagShortDescription) {
        this.tagShortDescription = tagShortDescription;
    }

    public GeoPoint getTagLocation() {
        return tagLocation;
    }

    public void setTagLocation(GeoPoint tagLocation) {
        this.tagLocation = tagLocation;
    }

    public String getTagCost() {
        return tagCost;
    }

    public void setTagCost(String tagCost) {
        this.tagCost = tagCost;
    }

    public String getTagIconUrl() {
        return tagIconUrl;
    }

    public void setTagIconUrl(String tagIconUrl) {
        this.tagIconUrl = tagIconUrl;
    }

    public String getTagImageLargeUrl() {
        return tagImageLargeUrl;
    }

    public void setTagImageLargeUrl(String tagImageLargeUrl) {
        this.tagImageLargeUrl = tagImageLargeUrl;
    }

    public String getTagLocationName() {
        return tagLocationName;
    }

    public void setTagLocationName(String tagLocationName) {
        this.tagLocationName = tagLocationName;
    }
}
