package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.ActivityAttendeesAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActiveActivitiesAdapter extends RecyclerView.Adapter<ActiveActivitiesAdapter.ActiveActivityViewHolder> {

    private ArrayList<Activity> activeActivitiesData;

    private OnActiveActivitySelectedListener listener;

    public ActiveActivitiesAdapter(ArrayList<Activity> activeActivitiesData, OnActiveActivitySelectedListener listener) {
        this.activeActivitiesData = activeActivitiesData;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveActivityViewHolder holder, int position) {
        Activity activeActivity = activeActivitiesData.get(position);
        ArrayList<AttendingUser> attendeesData;
        attendeesData = activeActivity.getActivityAttendees();
        ActivityAttendeesAdapter attendeesAdapter = new ActivityAttendeesAdapter(holder.itemView.getContext(), attendeesData);
        holder.attendeesRecycler.setAdapter(attendeesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        holder.attendeesRecycler.setLayoutManager(layoutManager);
        holder.bindTo(activeActivity, listener);
    }

    @NonNull
    @Override
    public ActiveActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActiveActivityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_active_activity_item, parent, false));
    }

    public interface OnActiveActivitySelectedListener {
        void onActiveActivitySelected(Activity activeActivity);
    }

    @Override
    public int getItemCount() {
        return activeActivitiesData.size();
    }

    class ActiveActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView dayTextView, dateTextView, monthTextView, timeTextView, locationTextView, tagTextView;
        private RecyclerView attendeesRecycler;

        public ActiveActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.dayTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            timeTextView = itemView.findViewById(R.id.timeTextview);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            attendeesRecycler = itemView.findViewById(R.id.activity_attendees_recycler);

        }

        void bindTo(Activity activeActivity, OnActiveActivitySelectedListener listener) {
            DateFormat dayFormatter = new SimpleDateFormat("EEE", Locale.getDefault());
            DateFormat dateFormatter = new SimpleDateFormat("d", Locale.getDefault());
            DateFormat monthFormatter = new SimpleDateFormat("LLL", Locale.getDefault());

            String dayText = dayFormatter.format(activeActivity.getActivityDate().toDate());
            String dateText = dateFormatter.format(activeActivity.getActivityDate().toDate());
            String monthText = monthFormatter.format(activeActivity.getActivityDate().toDate());

            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activeActivity.getActivityStartTime().toDate());
            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activeActivity.getActivityEndTime().toDate());


            String activityTime = activityStartTime + " - " + activityEndTime;

            dayTextView.setText(dayText);
            dateTextView.setText(dateText);
            monthTextView.setText(monthText);
            timeTextView.setText(activityTime);
            locationTextView.setText(activeActivity.getActivityLocationName());
            tagTextView.setText(activeActivity.getActivityTag().getTagName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onActiveActivitySelected(activeActivity);
                    }
                }
            });

        }
    }
}
