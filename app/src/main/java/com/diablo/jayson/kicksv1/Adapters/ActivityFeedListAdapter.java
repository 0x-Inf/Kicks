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
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActivityFeedListAdapter extends FirestoreRecyclerAdapter<Activity, ActivityFeedListAdapter.ActivityViewHolder> {

    private ArrayList<Activity> mKicksData;
    private MutableLiveData<ArrayList<Activity>> mKicksDataM;
    private Context mContext;
    private LayoutInflater mInflater;


    public ActivityFeedListAdapter(FirestoreRecyclerOptions<Activity> options) {
        super(options);
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
        holder.bindTo(currentActivity);
        setUpActivity(holder, model, position);
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
        private ImageView mKickImage, mUploaderPic;
        private Context mContext;
        private android.app.Activity activity;



        ActivityViewHolder(View itemView) {
            super(itemView);
            mKickTitleText = itemView.findViewById(R.id.activityTitleTextView);
            mKickStartTimeText = itemView.findViewById(R.id.activityStartTimeTextView);
            mKickEndTimeText = itemView.findViewById(R.id.activityEndTimeTextView);
            mKickDateText = itemView.findViewById(R.id.activityDateTextView);
            mKickLocationText = itemView.findViewById(R.id.activityLocationTextView);
            mMinPeopleText = itemView.findViewById(R.id.minAttendingPeople);
            mMaxPeopleText = itemView.findViewById(R.id.maxAttendingPeople);
            mUploaderName = itemView.findViewById(R.id.activityHostName);
            mUploaderPic = itemView.findViewById(R.id.activityHostProfilePic);
            mKickImage = itemView.findViewById(R.id.activityImageView);


        }

        void bindTo(Activity currentActivity) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            mKickTitleText.setText(currentActivity.getkickTitle());
            mKickStartTimeText.setText(currentActivity.getkickTime());
            mKickEndTimeText.setText(currentActivity.getkickTime());
            mKickDateText.setText(currentActivity.getkickDate());
            mKickLocationText.setText(currentActivity.getkickLocation());
            mMinPeopleText.setText(currentActivity.getMinRequiredPeople());
            mMaxPeopleText.setText(currentActivity.getMaxRequiredPeeps());
            mUploaderName.setText(currentActivity.getUploaderId());
            Glide.with(itemView.getContext())
                    .load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mUploaderPic);

            Glide.with(itemView.getContext()).load(currentActivity.getimageUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(30, 5)))
                    .into(mKickImage);

        }
    }

    private void setUpActivity(ActivityViewHolder activityViewHolder, Activity activity, int position) {
        Host host = activity.getHost();
        activityViewHolder.bindTo(activity);

    }
}
