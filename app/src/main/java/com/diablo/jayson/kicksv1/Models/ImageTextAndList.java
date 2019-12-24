package com.diablo.jayson.kicksv1.Models;

import java.util.ArrayList;
import java.util.List;

public class ImageTextAndList extends FeaturedKicks {


    private ArrayList<Kick> mFeaturedKickList;
    private String mFeaturedKickListImageUrl;
    private String mFeaturedKickListTitle;

    public ImageTextAndList(ArrayList<Kick> mFeaturedKickList, String mFeaturedKickListImageUrl, String mFeaturedKickListTitle) {
        this.mFeaturedKickList = mFeaturedKickList;
        this.mFeaturedKickListImageUrl = mFeaturedKickListImageUrl;
        this.mFeaturedKickListTitle = mFeaturedKickListTitle;
    }

    public ArrayList<Kick> getFeaturedKickList() {
        return mFeaturedKickList;
    }

    public String getFeaturedKickListImageUrl() {
        return mFeaturedKickListImageUrl;
    }

    public String getFeaturedKickListTitle() {
        return mFeaturedKickListTitle;
    }
}
