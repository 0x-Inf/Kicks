package com.diablo.jayson.kicksv1.UI.UserProfile;

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
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActiveActivitiesAdapter extends RecyclerView.Adapter<ActiveActivitiesAdapter.ActiveActivityViewHolder> {

    private Context context;
    private ArrayList<Activity> activitiesData;

    public ActiveActivitiesAdapter(Context context, ArrayList<Activity> activitiesData) {
        this.context = context;
        this.activitiesData = activitiesData;
    }

    @NonNull
    @Override
    public ActiveActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActiveActivityViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveActivityViewHolder holder, int position) {
        Activity activeActivity = activitiesData.get(position);
        holder.bindTo(activeActivity);
    }


    @Override
    public int getItemCount() {
        return activitiesData.size();
    }

    static class ActiveActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView activityTitleTextView, activityTimeTextView,
                activityDateTextView, activityCurrencyTextView, activityCostTextView, activityLocationTextView,
                activityTagTextView, hostTextView;
        private ImageView activityImage, uploaderPic;
        private RecyclerView attendeesRecycler;

        ActiveActivityViewHolder(View itemView) {
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


        void bindTo(Activity activeActivity) {
            String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(activeActivity.getActivityDate().toDate());
//            String   = DateFormat.getMediumDateFormat(itemView.getContext()).format(currentActivity.getActivityDate());
            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activeActivity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activeActivity.getActivityEndTime().toDate());


            String activityTime = activityStartTime + " - " + activityEndTime;
            String activityCost = String.valueOf(activeActivity.getActivityCost());
            String activityLocation = activeActivity.getActivityLocationName();
            String hostName = activeActivity.getHost().getUserName();
            String tagName = activeActivity.getActivityTag().getTagName();
            activityTitleTextView.setText(activeActivity.getActivityTitle());
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

            Glide.with(itemView.getContext()).load(activeActivity.getImageUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 5)))
                    .into(activityImage);

//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (listener != null) {
//                            listener.onActivitySelected(currentActivity);
//                        }
//                    }
//                });
        }
    }
}
