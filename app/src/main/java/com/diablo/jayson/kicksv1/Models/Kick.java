package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Kick implements Serializable {

    private String kickName;
    private String kickId;
    private String kickMainImageUrl;
    private ArrayList<String> kickImageUrls;
    private ArrayList<String> kickTags;


    public Kick() {

    }

    public Kick(String kickName, String kickId, String kickMainImageUrl, ArrayList<String> kickImageUrls, ArrayList<String> kickTags) {
        this.kickName = kickName;
        this.kickId = kickId;
        this.kickMainImageUrl = kickMainImageUrl;
        this.kickImageUrls = kickImageUrls;
        this.kickTags = kickTags;
    }

    public String getKickName() {
        return kickName;
    }

    public void setKickName(String kickName) {
        this.kickName = kickName;
    }

    public String getKickId() {
        return kickId;
    }

    public void setKickId(String kickId) {
        this.kickId = kickId;
    }

    public String getKickMainImageUrl() {
        return kickMainImageUrl;
    }

    public void setKickMainImageUrl(String kickMainImageUrl) {
        this.kickMainImageUrl = kickMainImageUrl;
    }

    public ArrayList<String> getKickImageUrls() {
        return kickImageUrls;
    }

    public void setKickImageUrls(ArrayList<String> kickImageUrls) {
        this.kickImageUrls = kickImageUrls;
    }

    public ArrayList<String> getKickTags() {
        return kickTags;
    }

    public void setKickTags(ArrayList<String> kickTags) {
        this.kickTags = kickTags;
    }
}
