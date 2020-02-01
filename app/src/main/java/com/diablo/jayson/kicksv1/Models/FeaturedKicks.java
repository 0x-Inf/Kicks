package com.diablo.jayson.kicksv1.Models;

import com.diablo.jayson.kicksv1.Constants;

import java.util.ArrayList;

public class FeaturedKicks {

    private ImageAndText mFeaturedImageAndText;
    private ImageTextAndList mFeaturedImageTextAndList;
    private String mFeaturedType;



    public FeaturedKicks() {

    }


    public ImageAndText getmFeaturedImageAndText() {
        return mFeaturedImageAndText;
    }

    public ImageTextAndList getmFeaturedImageTextAndList() {
        return mFeaturedImageTextAndList;
    }

    public void setmFeaturedImageAndText(ImageAndText mFeaturedImageAndText) {
        this.mFeaturedImageAndText = mFeaturedImageAndText;
    }

    public void setmFeaturedImageTextAndList(ImageTextAndList mFeaturedImageTextAndList) {
        this.mFeaturedImageTextAndList = mFeaturedImageTextAndList;
    }

    public String getFeaturedType(){
        String type;
        if (mFeaturedImageAndText!=null){
            type = Constants.IMAGE_AND_TEXT;
        }else if (mFeaturedImageTextAndList!=null){
            type = Constants.IMAGE_TEXT_AND_LIST;
        }else type = null;
        return type;
    }

    public String getFeaturedType2(){
        return mFeaturedType;
    }

    public void setFeaturedType(String mFeaturedType) {
        this.mFeaturedType = mFeaturedType;
    }

}
