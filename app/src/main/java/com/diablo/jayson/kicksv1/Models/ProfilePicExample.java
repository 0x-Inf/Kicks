package com.diablo.jayson.kicksv1.Models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

public class ProfilePicExample extends BaseObservable implements Serializable {

    private String picUrl;

    public ProfilePicExample(String picUrl) {
        this.picUrl = picUrl;
    }

    public ProfilePicExample() {
    }

    @Bindable
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
