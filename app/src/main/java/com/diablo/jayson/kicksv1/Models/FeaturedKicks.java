package com.diablo.jayson.kicksv1.Models;

import java.util.ArrayList;

public class FeaturedKicks {

    private ArrayList<ImageAndText> mFeaturedImageAndText;
    private ArrayList<ImageTextAndList> mFeaturedImageTextAndList;

    public FeaturedKicks(){

    }

    public FeaturedKicks(ArrayList<ImageAndText> mFeaturedImageAndText, ArrayList<ImageTextAndList> mFeaturedImageTextAndList) {
        this.mFeaturedImageAndText = mFeaturedImageAndText;
        this.mFeaturedImageTextAndList = mFeaturedImageTextAndList;
    }

    public ArrayList<ImageAndText> getmFeaturedImageAndText() {
        return mFeaturedImageAndText;
    }

    public ArrayList<ImageTextAndList> getmFeaturedImageTextAndList() {
        return mFeaturedImageTextAndList;
    }
}
