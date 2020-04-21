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
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
                .inflate(R.layout.recycler_available_activities_item, parent, false));
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
        private ImageView availableActivityImage;
        private TextView availableActivityTitle, availableActivityCost,
                availableActivityNoOfPeople, availableActivityLocation,
                availableActivityDateTime;

        ActiveActivityViewHolder(View itemView) {
            super(itemView);
            availableActivityImage = itemView.findViewById(R.id.availableActivityImage);
            availableActivityTitle = itemView.findViewById(R.id.availableActivityTitle);
            availableActivityCost = itemView.findViewById(R.id.availableActivityCostTextView);
            availableActivityLocation = itemView.findViewById(R.id.availableActivitylocationTextView);
            availableActivityDateTime = itemView.findViewById(R.id.availableActivityDateTimeTextView);
        }


        void bindTo(Activity activeActivity) {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(activeActivity.getActivityDate().getSeconds());
            String date = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(activeActivity.getActivityDate().toDate());
            String activityStartTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(activeActivity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activeActivity.getActivityEndTime().toDate());

            String noOfPeople = activeActivity.getActivityMinRequiredPeople() + "-" + activeActivity.getActivityMaxRequiredPeople() + " People";
            String dateTimeText = activityStartTime + " - " + activityEndTime + "  " + date;
            Glide.with(itemView.getContext())
                    .load(activeActivity.getImageUrl())
                    .into(availableActivityImage);

            availableActivityTitle.setText(activeActivity.getActivityTitle());
            availableActivityCost.setText(activeActivity.getActivityCost());
            availableActivityLocation.setText(activeActivity.getActivityLocationName());
            availableActivityDateTime.setText(dateTimeText);
        }
    }
}
