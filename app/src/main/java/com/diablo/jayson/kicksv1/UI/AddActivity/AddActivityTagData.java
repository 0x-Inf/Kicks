package com.diablo.jayson.kicksv1.UI.AddActivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.diablo.jayson.kicksv1.Models.Tag;

import java.util.List;

public class AddActivityTagData implements Parcelable {

    private List<String> tags;
    private Tag activityTag;

    public AddActivityTagData() {
    }

    public AddActivityTagData(List<String> tags, Tag activityTag) {
        this.tags = tags;
        this.activityTag = activityTag;
    }

    protected AddActivityTagData(Parcel in) {
        tags = in.createStringArrayList();
    }

    public static final Creator<AddActivityTagData> CREATOR = new Creator<AddActivityTagData>() {
        @Override
        public AddActivityTagData createFromParcel(Parcel in) {
            return new AddActivityTagData(in);
        }

        @Override
        public AddActivityTagData[] newArray(int size) {
            return new AddActivityTagData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(tags);
    }
}
