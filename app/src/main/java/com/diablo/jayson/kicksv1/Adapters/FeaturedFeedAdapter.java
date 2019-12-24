package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.ImageAndText;
import com.diablo.jayson.kicksv1.Models.ImageTextAndList;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class FeaturedFeedAdapter extends RecyclerView.Adapter {

    private static final int IMAGE_TEXT = 1;
    private static final int IMAGE_TEXT_LIST = 2;
    private Context mContext;
    private ArrayList<ImageAndText> mImageAndTextData;
    private ArrayList<ImageTextAndList> mImageTextAndListData;
    private ArrayList<Kick> mKicksData;
    private FeatureKickListAdapter mKicksAdapter;
    private ArrayList<FeaturedKicks> mTotalKicks;

    public FeaturedFeedAdapter(Context mContext, ArrayList<ImageAndText> mImageAndTextData, ArrayList<ImageTextAndList> mImageTextAndListData, ArrayList<FeaturedKicks> totalKicks) {
        this.mContext = mContext;
        this.mImageAndTextData = mImageAndTextData;
        this.mImageTextAndListData = mImageTextAndListData;
        this.mTotalKicks = totalKicks;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTotalKicks.get(position) instanceof ImageAndText) {
            return IMAGE_TEXT;
        } else if (mTotalKicks.get(position) instanceof ImageTextAndList) {
            return IMAGE_TEXT_LIST;
        }else {
            return IMAGE_TEXT_LIST;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case IMAGE_TEXT:
                return new ImageAndTextOnlyActivityViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.image_and_text_item, parent, false));
            case IMAGE_TEXT_LIST:
                return new ImageAndTextAndListActivityViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.image_text_and_list_item, parent, false));
            default:
                return new ImageAndTextOnlyActivityViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.image_and_text_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        if (itemType == IMAGE_TEXT) {
            FeaturedKicks currentFeaturedKick = mTotalKicks.get(position);
            putFEaturedData(currentFeaturedKick);
            ImageAndText currentImageAndText = mImageAndTextData.get(position);
            ((ImageAndTextOnlyActivityViewHolder) holder).bindTo(currentImageAndText);
//            ImageAndTextOnlyActivityViewHolder mHolder = (ImageAndTextOnlyActivityViewHolder) holder;
//            setUpImageText(mHolder);
        } else if (itemType == IMAGE_TEXT_LIST) {
            FeaturedKicks currentFeaturedKick = mTotalKicks.get(position);
            putFEaturedData(currentFeaturedKick);
            ImageTextAndList currentImageTextAndList = mImageTextAndListData.get(position);
            ((ImageAndTextAndListActivityViewHolder) holder).bindTo(currentImageTextAndList);
            mKicksData = new ArrayList<Kick>();
            mKicksAdapter = new FeatureKickListAdapter(mContext, mKicksData);
            ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.
                    setLayoutManager(new LinearLayoutManager(mContext));
            ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.setAdapter(mKicksAdapter);

            initializeKickData();
        } else {

        }
    }
//
//    private void setUpImageText(ImageAndTextOnlyActivityViewHolder mholder) {
//        ImageAndText currentImageAndText = new ImageAndText();
//        mholder.bindTo(currentImageAndText);
//    }


    @Override
    public int getItemCount() {
        return mTotalKicks.size();
    }


    class ImageAndTextOnlyActivityViewHolder extends RecyclerView.ViewHolder {

        // TODO: Appropriately Name this stuff

        private ImageView mKickImage;
        private TextView mKickTitle;


        public ImageAndTextOnlyActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickTitle = itemView.findViewById(R.id.kickFeaturedNameTextView);
            mKickImage = itemView.findViewById(R.id.kickFeaturedImage);
        }

        void bindTo(ImageAndText currentImageText) {

            mKickTitle.setText(currentImageText.getFeaturedKickTitle());
            Glide.with(mContext).load(currentImageText.getFeaturedKickImageUrl()).into(mKickImage);

        }
    }

    class ImageAndTextAndListActivityViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickFeaturedListImage;
        private TextView mFeaturedListTitle;
        private RecyclerView mFeaturedListView;


        public ImageAndTextAndListActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickFeaturedListImage = itemView.findViewById(R.id.kickFeaturedListImage);
            mFeaturedListTitle = itemView.findViewById(R.id.kickFeaturedListNameTextView);
            mFeaturedListView = itemView.findViewById(R.id.featuredList);

        }

        void bindTo(ImageTextAndList currentImageAndTextAndList) {
            mFeaturedListTitle.setText(currentImageAndTextAndList.getFeaturedKickListTitle());
            Glide.with(mContext).load(currentImageAndTextAndList.getFeaturedKickListImageUrl()).into(mKickFeaturedListImage);

        }

    }


    private void initializeKickData() {
        String[] kickNames = mContext.getResources().getStringArray(R.array.kicks_titles);
        String[] kickImages = mContext.getResources().getStringArray(R.array.kicks_image_urls);
//        mKicksData.clear();

        for (int i = 0; i < kickImages.length; i++) {
            mKicksData.add(new Kick(kickNames[i], kickImages[i]));
        }

    }

    void putFEaturedData(FeaturedKicks currentFeaturedKick) {
        currentFeaturedKick.getmFeaturedImageAndText();
        currentFeaturedKick.getmFeaturedImageTextAndList();
    }
}

