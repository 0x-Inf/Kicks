package com.diablo.jayson.kicksv1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActivityFeedListAdapter extends FirestoreRecyclerAdapter<Activity, ActivityFeedListAdapter.ActivityViewHolder> {


    public interface OnActivitySelectedListener {
        void onActivitySelected(Activity activity);

        void toggleLike(Activity activity);
    }

    private OnActivitySelectedListener mListener;
    private ViewPager2 viewPager2;

    public ActivityFeedListAdapter(FirestoreRecyclerOptions<Activity> options, OnActivitySelectedListener listener) {
        super(options);
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ActivityFeedListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_activity_list_item, parent, false));
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
        ArrayList<AttendingUser> attendeesData;
        attendeesData = currentActivity.getActivityAttendees();
        ActivityAttendeesAdapter attendeesAdapter = new ActivityAttendeesAdapter(holder.itemView.getContext(), attendeesData);
        holder.attendeesRecycler.setAdapter(attendeesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        holder.attendeesRecycler.setLayoutManager(layoutManager);
        holder.bindTo(currentActivity, mListener);

//        setUpActivity(holder, model, position);
    }


//    @Override
//    public int getItemCount() {
//        return mKicksData.size();
//    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView activityTitleTextView, activityTimeTextView,
                activityDateTextView, activityCurrencyTextView, activityCostTextView, activityLocationTextView,
                activityTagTextView, hostTextView;
        private ImageView activityImage, uploaderPic;
        private RecyclerView attendeesRecycler;

        ActivityViewHolder(View itemView) {
            super(itemView);
//            activityTitleTextView = itemView.findViewById(R.id.activity_title_text_view);
//            activityTimeTextView = itemView.findViewById(R.id.activity_time_text_view);
//            activityDateTextView = itemView.findViewById(R.id.activity_date_text_view);
//            activityCurrencyTextView = itemView.findViewById(R.id.currency_text_view);
//            activityCostTextView = itemView.findViewById(R.id.activity_cost_text_view);
//            activityLocationTextView = itemView.findViewById(R.id.activity_location_text_view);
//            activityTagTextView = itemView.findViewById(R.id.activity_tag_text_view);
////            hostTextView = itemView.findViewById(R.id.host_name_text_view);
//            activityImage = itemView.findViewById(R.id.activity_image_view);
////            uploaderPic = itemView.findViewById(R.id.host_pic_image_view);
//            attendeesRecycler = itemView.findViewById(R.id.activity_attendees_recycler);
        }

        void bindTo(Activity currentActivity, OnActivitySelectedListener listener) {
            String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentActivity.getActivityDate().toDate());
//            String   = DateFormat.getMediumDateFormat(itemView.getContext()).format(currentActivity.getActivityDate());
            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentActivity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(currentActivity.getActivityEndTime().toDate());


            String activityTime = activityStartTime + " - " + activityEndTime;
            String activityCost = currentActivity.getActivityCost();
            String activityLocation = currentActivity.getActivityLocationName();
            String hostName = currentActivity.getHost().getUserName();
            String tagName = currentActivity.getActivityTag().getTagName();
            activityTitleTextView.setText(currentActivity.getActivityTitle());
            activityTimeTextView.setText(activityTime);
            activityDateTextView.setText(activityDate);
            activityCostTextView.setText(activityCost);
            activityLocationTextView.setText(activityLocation);
//            hostTextView.setText(hostName);
            activityTagTextView.setText(tagName);
//            Glide.with(itemView.getContext())
//                    .load(currentActivity.getHost().getHostPic())
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(uploaderPic);

            Glide.with(itemView.getContext()).load(currentActivity.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 5)))
                    .into(activityImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onActivitySelected(currentActivity);
                    }
                }
            });


        }
    }

    private void setUpActivity(ActivityViewHolder activityViewHolder, Activity activity, int position) {
        Host host = activity.getHost();
        activityViewHolder.bindTo(activity, mListener);

    }
}
