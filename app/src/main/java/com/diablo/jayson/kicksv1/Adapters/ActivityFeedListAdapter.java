package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class ActivityFeedListAdapter extends RecyclerView.Adapter<ActivityFeedListAdapter.ActivityViewHolder> {

    private ArrayList<Activity> mKicksData;
    private MutableLiveData<ArrayList<Activity>> mKicksDataM;
    private Context mContext;
    private LayoutInflater mInflater;

    public ActivityFeedListAdapter(Context context, ArrayList<Activity> kicksdata){
        this.mKicksData = kicksdata;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    public ActivityFeedListAdapter(Context context, MutableLiveData<ArrayList<Activity>> mKicksData) {
        this.mContext = context;
        this.mKicksDataM = mKicksData;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ActivityFeedListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.activity_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityFeedListAdapter.ActivityViewHolder holder, int position) {
        Activity currentActivity = mKicksData.get(position);
        holder.bindTo(currentActivity);

    }

    @Override
    public int getItemCount() {
        return mKicksData.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mKickTitleText;
        private TextView mKickTimeText;
        private TextView mKickDateText;
        private TextView mKickLocationText;
        private TextView mAlreadyAttendingPeepsText;
        private TextView mRequiredPeepsText;
        private ImageView mKickImage;


        public ActivityViewHolder(View itemView) {
            super(itemView);
            mKickTitleText = itemView.findViewById(R.id.kickNameTextView);
            mKickTimeText = itemView.findViewById(R.id.timeTextView);
            mKickDateText = itemView.findViewById(R.id.dateTextView);
            mKickLocationText = itemView.findViewById(R.id.locationTextView);
            mAlreadyAttendingPeepsText = itemView.findViewById(R.id.alreeadyKickingTextView);
            mRequiredPeepsText = itemView.findViewById(R.id.requiredKickersTextView);
            mKickImage = itemView.findViewById(R.id.kickImage);

        }

        void bindTo(Activity currentActivity){
            mKickTitleText.setText(currentActivity.getKickTitle());
            mKickTimeText.setText(currentActivity.getKickTime());
            mKickDateText.setText(currentActivity.getKickDate());
            mKickLocationText.setText(currentActivity.getKickLocation());
            mAlreadyAttendingPeepsText.setText(currentActivity.getKickAlreadyAttendingPeeps());
            mRequiredPeepsText.setText(currentActivity.getKickRequiredPeeps());
            Glide.with(mContext).load(currentActivity.getImageUrl()).into(mKickImage);

        }
    }
}
