package com.diablo.jayson.kicksv1.UI.AddKick;

import com.diablo.jayson.kicksv1.Models.Tag;

import java.util.List;

public class AddActivityTagData {

    private List<String> tags;
    private Tag activityTag;

    public AddActivityTagData() {
    }

    public AddActivityTagData(List<String> tags, Tag activityTag) {
        this.tags = tags;
        this.activityTag = activityTag;
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
}
