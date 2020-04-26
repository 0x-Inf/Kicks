package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Kick implements Serializable {

    private String kickName;
    private String kickShortDescription;
    private String kickCardImageUrl;
    private String kickLargeImageUrl;
    private ArrayList<String> tags;


    public Kick() {

    }

    public Kick(String kickName, String kickShortDescription, String kickCardImageUrl, String kickLargeImageUrl, ArrayList<String> tags) {
        this.kickName = kickName;
        this.kickShortDescription = kickShortDescription;
        this.kickCardImageUrl = kickCardImageUrl;
        this.kickLargeImageUrl = kickLargeImageUrl;
        this.tags = tags;
    }

    public String getKickName() {
        return kickName;
    }

    public void setKickName(String kickName) {
        this.kickName = kickName;
    }

    public String getKickShortDescription() {
        return kickShortDescription;
    }

    public void setKickShortDescription(String kickShortDescription) {
        this.kickShortDescription = kickShortDescription;
    }

    public String getKickCardImageUrl() {
        return kickCardImageUrl;
    }

    public void setKickCardImageUrl(String kickCardImageUrl) {
        this.kickCardImageUrl = kickCardImageUrl;
    }

    public String getKickLargeImageUrl() {
        return kickLargeImageUrl;
    }

    public void setKickLargeImageUrl(String kickLargeImageUrl) {
        this.kickLargeImageUrl = kickLargeImageUrl;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
