package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class Tag extends BaseObservable implements Serializable {

    private String tagName;
    private String tagId;

    public Tag() {
    }

    public Tag(String tagName, String tagId) {
        this.tagName = tagName;
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
