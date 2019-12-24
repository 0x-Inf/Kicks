package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.util.Log;
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
import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.ImageAndText;
import com.diablo.jayson.kicksv1.Models.ImageTextAndList;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;
import java.util.List;

public class FeaturedFeedAdapter extends RecyclerView.Adapter {

    private static final int IMAGE_TEXT = 1;
    private static final int IMAGE_TEXT_LIST = 2;
    private Context mContext;
    private ArrayList<Kick> mKicksData;
    private FeatureKickListAdapter mKicksAdapter;
    private List<FeaturedKicks> mTotalKicks;

    public FeaturedFeedAdapter(Context mContext, List<FeaturedKicks> totalKicks) {
        this.mContext = mContext;
        this.mTotalKicks = totalKicks;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (mTotalKicks.get(position).getFeaturedType().equals(Constants.IMAGE_AND_TEXT)) {
            viewType = IMAGE_TEXT;
        } else if (mTotalKicks.get(position).getFeaturedType().equals(Constants.IMAGE_TEXT_AND_LIST)) {
            viewType = IMAGE_TEXT_LIST;
        } else viewType = IMAGE_TEXT;
        return viewType;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int viewTypeToShow = getItemViewType(viewType);

        if (viewType == IMAGE_TEXT) {
            View imageTextView = LayoutInflater.from(mContext).
                    inflate(R.layout.image_and_text_item, parent, false);
            return new ImageAndTextOnlyActivityViewHolder(imageTextView);
        } else if (viewType == IMAGE_TEXT_LIST) {
            View imageTextListView = LayoutInflater.from(mContext).
                    inflate(R.layout.image_text_and_list_item, parent, false);
            return new ImageAndTextAndListActivityViewHolder(imageTextListView);
        }else
            return new ImageAndTextOnlyActivityViewHolder(LayoutInflater.from(mContext).
                       inflate(R.layout.image_and_text_item, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeaturedKicks featuredKicks = mTotalKicks.get(position);
        holder.getAdapterPosition();

        switch (holder.getItemViewType()) {
            case IMAGE_TEXT:
                ImageAndText currentImageAndText = featuredKicks.getmFeaturedImageAndText();
                ((ImageAndTextOnlyActivityViewHolder) holder).bindTo(currentImageAndText);
                break;
            case IMAGE_TEXT_LIST:
                ImageTextAndList currentImageTextAndList = featuredKicks.getmFeaturedImageTextAndList();
                ((ImageAndTextAndListActivityViewHolder) holder).bindTo(currentImageTextAndList);
                mKicksData = new ArrayList<Kick>();
                mKicksAdapter = new FeatureKickListAdapter(mContext, mKicksData);
                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.
                        setLayoutManager(new LinearLayoutManager(mContext));
                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.setAdapter(mKicksAdapter);
                initializeKickData();
                break;
            default:

        }
    }

    @Override
    public int getItemCount() {
        int totalKicks = mTotalKicks.size();
        Log.e("number of kicks", Integer.toString(totalKicks));
        return mTotalKicks.size();
    }


    class ImageAndTextOnlyActivityViewHolder extends MainViewHolder {

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

    class ImageAndTextAndListActivityViewHolder extends MainViewHolder {

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

    class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
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

}

