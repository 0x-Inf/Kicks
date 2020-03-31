package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActivityFeedListAdapter extends FirestoreRecyclerAdapter<Activity, ActivityFeedListAdapter.ActivityViewHolder> {


    public interface OnActivitySelectedListener {
        void onActivitySelected(Activity activity);
        void toggleLike(Activity activity);
    }

    private OnActivitySelectedListener mListener;

    public ActivityFeedListAdapter(FirestoreRecyclerOptions<Activity> options, OnActivitySelectedListener listener) {
        super(options);
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ActivityFeedListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_list_item, parent, false));
    }

//    @Override
//    public void onBindViewHolder(@NonNull ActivityFeedListAdapter.ActivityViewHolder holder, int position) {
//        Activity currentActivity = mKicksData.get(position);
//        holder.bindTo(currentActivity);
//
//    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityViewHolder holder, int position, @NonNull Activity model) {
        Activity currentActivity = getItem(position);
        holder.bindTo(currentActivity, mListener);

//        setUpActivity(holder, model, position);
    }


//    @Override
//    public int getItemCount() {
//        return mKicksData.size();
//    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mKickTitleText;
        private TextView mKickStartTimeText;
        private TextView mKickEndTimeText;
        private TextView mKickDateText;
        private TextView mKickLocationText;
        private TextView mMinPeopleText;
        private TextView mMaxPeopleText;
        private TextView mUploaderName;
        private TextView activityTag;
        private TextView peopleAgeText;
        private TextView activityCostText;
        private TextView mNoOfLikes;
        private boolean liked = false;
        private ImageView mKickImage, mUploaderPic, mLikeIcon;
        private Context mContext;
        private android.app.Activity activity;



        ActivityViewHolder(View itemView) {
            super(itemView);
            mKickTitleText = itemView.findViewById(R.id.activityTitleTextView);
            mKickStartTimeText = itemView.findViewById(R.id.activityStartTimeTextView);
            mKickEndTimeText = itemView.findViewById(R.id.activityEndTimeTextView);
            mKickDateText = itemView.findViewById(R.id.activityDateTextView);
            mKickLocationText = itemView.findViewById(R.id.activityLocationTextView);
            mMinPeopleText = itemView.findViewById(R.id.activityMinPeopleTextView);
            mMaxPeopleText = itemView.findViewById(R.id.activityMaxPeopleTextView);
            mUploaderName = itemView.findViewById(R.id.hostNameTextView);
            mUploaderPic = itemView.findViewById(R.id.hostPicImageView);
            mKickImage = itemView.findViewById(R.id.activityImageView);
            activityTag = itemView.findViewById(R.id.activityTagTextView);
            peopleAgeText = itemView.findViewById(R.id.activityPeopleAgeTextView);
            activityCostText = itemView.findViewById(R.id.activityCostTextView);
        }

        void bindTo(Activity currentActivity, OnActivitySelectedListener listener) {

            String ageText = " (" + currentActivity.getMinAge() + " - " + currentActivity.getMaxAge() + " Y/o)";
            mKickTitleText.setText(currentActivity.getKickTitle());
            mKickStartTimeText.setText(currentActivity.getKickStartTime());
            mKickEndTimeText.setText(currentActivity.getKickEndTime());
            mKickDateText.setText(currentActivity.getKickDate());
            mKickLocationText.setText(currentActivity.getKickLocationName());
            mMinPeopleText.setText(currentActivity.getMinRequiredPeople());
            mMaxPeopleText.setText(currentActivity.getMaxRequiredPeeps());
            mUploaderName.setText(currentActivity.getUploaderId());
            activityTag.setText(currentActivity.getTag().getTagName());
            peopleAgeText.setText(ageText);
            activityCostText.setText(currentActivity.getActivityCost());
            Glide.with(itemView.getContext())
                    .load(currentActivity.getHost().getHostPic())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mUploaderPic);

            Glide.with(itemView.getContext()).load(currentActivity.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(30, 5)))
                    .into(mKickImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onActivitySelected(currentActivity);
                    }
                }
            });


        }

        private void changeLikeIcon() {
            liked = true;
            mLikeIcon.setImageResource(R.drawable.ic_bookmark_icon);

        }
    }

    private void setUpActivity(ActivityViewHolder activityViewHolder, Activity activity, int position) {
        Host host = activity.getHost();
        activityViewHolder.bindTo(activity, mListener);

    }
}
