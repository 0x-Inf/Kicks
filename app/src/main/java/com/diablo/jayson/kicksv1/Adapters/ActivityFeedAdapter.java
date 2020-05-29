package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
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
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityFeedAdapter.ActivityItemHolder> {

    private ArrayList<Activity> activitiesData;
    private ViewPager2 viewPager2;
    private Context context;

    public ActivityFeedAdapter(ArrayList<Activity> activitiesData, ViewPager2 viewPager2,Context context) {
        this.activitiesData = activitiesData;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityItemHolder(LayoutInflater.from(context).
                inflate(R.layout.activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityItemHolder holder, int position) {
        Activity activity = activitiesData.get(position);
        ArrayList<AttendingUser> attendeesData;
        attendeesData = activity.getActivityAttendees();
        ActivityAttendeesAdapter attendeesAdapter = new ActivityAttendeesAdapter(holder.itemView.getContext(), attendeesData);
        holder.attendeesRecycler.setAdapter(attendeesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        holder.attendeesRecycler.setLayoutManager(layoutManager);
        holder.bindTo(activity);
    }

    @Override
    public int getItemCount() {
        return activitiesData.size();
    }

    static class ActivityItemHolder extends RecyclerView.ViewHolder{

        private TextView activityTitleTextView, activityTimeTextView,
                activityDateTextView, activityCurrencyTextView, activityCostTextView, activityLocationTextView,
                activityTagTextView, hostTextView;
        private ImageView activityImage, uploaderPic;
        private RecyclerView attendeesRecycler;

        public ActivityItemHolder(@NonNull View itemView) {
            super(itemView);

            activityTitleTextView = itemView.findViewById(R.id.activity_title_text_view);
            activityTimeTextView = itemView.findViewById(R.id.activity_time_text_view);
            activityDateTextView = itemView.findViewById(R.id.activity_date_text_view);
            activityCurrencyTextView = itemView.findViewById(R.id.currency_text_view);
            activityCostTextView = itemView.findViewById(R.id.activity_cost_text_view);
            activityLocationTextView = itemView.findViewById(R.id.activity_location_text_view);
            activityTagTextView = itemView.findViewById(R.id.activity_tag_text_view);
//            hostTextView = itemView.findViewById(R.id.host_name_text_view);
            activityImage = itemView.findViewById(R.id.activity_image_view);
//            uploaderPic = itemView.findViewById(R.id.host_pic_image_view);
            attendeesRecycler = itemView.findViewById(R.id.activity_attendees_recycler);
        }

        void bindTo(Activity activity){
            String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(activity.getActivityDate().toDate());
//            String   = DateFormat.getMediumDateFormat(itemView.getContext()).format(currentActivity.getActivityDate());
            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activity.getActivityEndTime().toDate());


            String activityTime = activityStartTime + " - " + activityEndTime;
            String activityCost = activity.getActivityCost();
            String activityLocation = activity.getActivityLocationName();
            String hostName = activity.getHost().getUserName();
            String tagName = activity.getActivityTag().getTagName();
            activityTitleTextView.setText(activity.getActivityTitle());
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

            Glide.with(itemView.getContext()).load(activity.getImageUrl())
//                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 5)))
                    .into(activityImage);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onActivitySelected(activity);
//                    }
//                }
//            });

        }
    }
}
