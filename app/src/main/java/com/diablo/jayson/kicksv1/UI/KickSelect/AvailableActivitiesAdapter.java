package com.diablo.jayson.kicksv1.UI.KickSelect;

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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AvailableActivitiesAdapter extends FirestoreRecyclerAdapter<Activity, AvailableActivitiesAdapter.AvailableActivityViewHolder> {


    public interface OnAvailableActivitySelected {
        void onAvailableActivitySelected(Activity activity);
    }

    private OnAvailableActivitySelected listener;


    public AvailableActivitiesAdapter(@NonNull FirestoreRecyclerOptions options, OnAvailableActivitySelected listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvailableActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AvailableActivityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_activities_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull AvailableActivityViewHolder holder, int position, @NonNull Activity model) {
        Activity availableActivity = getItem(position);
        holder.bindTo(availableActivity, listener);
    }

    static class AvailableActivityViewHolder extends RecyclerView.ViewHolder {

        private ImageView availableActivityImage;
        private TextView availableActivityTitle, availableActivityCost,
                availableActivityNoOfPeople, availableActivityLocation,
                availableActivityDateTime;


        public AvailableActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            availableActivityImage = itemView.findViewById(R.id.availableActivityImage);
            availableActivityTitle = itemView.findViewById(R.id.availableActivityTitle);
            availableActivityCost = itemView.findViewById(R.id.availableActivityCostTextView);
            availableActivityLocation = itemView.findViewById(R.id.availableActivitylocationTextView);
            availableActivityDateTime = itemView.findViewById(R.id.availableActivityDateTimeTextView);
        }

        void bindTo(Activity availableActivity, OnAvailableActivitySelected listener) {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(availableActivity.getActivityDate().getSeconds());
            String date = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(availableActivity.getActivityDate().toDate());
            String activityStartTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(availableActivity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(availableActivity.getActivityEndTime().toDate());

            String noOfPeople = availableActivity.getActivityMinRequiredPeople() + "-" + availableActivity.getActivityMaxRequiredPeople() + " People";
            String dateTimeText = activityStartTime + " - " + activityEndTime + "  " + date;
            Glide.with(itemView.getContext())
                    .load(availableActivity.getImageUrl())
                    .into(availableActivityImage);

            availableActivityTitle.setText(availableActivity.getActivityTitle());
            availableActivityCost.setText(availableActivity.getActivityCost());
            availableActivityLocation.setText(availableActivity.getActivityLocationName());
            availableActivityDateTime.setText(dateTimeText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAvailableActivitySelected(availableActivity);
                    }
                }
            });

        }

    }
}
