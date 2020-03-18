package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.firebase.firestore.GeoPoint;

public class Tag extends BaseObservable {

    private String tagName;
    private GeoPoint tagLocation;
    private String tagOptimalMinPeople;
    private String tagOptimalMaxPeople;

    public Tag(String tagName, GeoPoint tagLocation, String tagOptimalMinPeople, String tagOptimalMaxPeople) {
        this.tagName = tagName;
        this.tagLocation = tagLocation;
        this.tagOptimalMinPeople = tagOptimalMinPeople;
        this.tagOptimalMaxPeople = tagOptimalMaxPeople;
    }

    public Tag() {

    }

    public <T> Tag(T toObject) {

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
