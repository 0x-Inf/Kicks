package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActiveActivitiesAdapter extends RecyclerView.Adapter<ActiveActivitiesAdapter.ActiveActivityViewHolder> {

    private ArrayList<Activity> activeActivitiesData;

    @NonNull
    @Override
    public ActiveActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActiveActivityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_active_activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveActivityViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return activeActivitiesData.size();
    }

    class ActiveActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView dayTextView, dateTextView, monthTextView, timeTextView, locationTextView, tagTextView;

        public ActiveActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.dayTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            timeTextView = itemView.findViewById(R.id.timeTextview);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);

        }

        void bindTo(Activity activeActivity) {
            DateFormat dayFormatter = new SimpleDateFormat("EEE", Locale.getDefault());
            DateFormat dateFormatter = new SimpleDateFormat("d", Locale.getDefault());
            DateFormat monthFormatter = new SimpleDateFormat("LLL", Locale.getDefault());

            String dayText = dayFormatter.format(activeActivity.getActivityDate().toDate());


        }
    }
}
