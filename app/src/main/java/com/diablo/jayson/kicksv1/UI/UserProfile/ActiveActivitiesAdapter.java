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

import java.util.ArrayList;

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
                .inflate(R.layout.active_activity_item, parent, false));
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

        private TextView activityTitle;
        private ImageView activityImage;

        public ActiveActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            activityTitle = itemView.findViewById(R.id.activeActivityTitleTextView);
            activityImage = itemView.findViewById(R.id.activeActivityImage);
        }

        void bindTo(Activity activeActivity) {
            activityTitle.setText(activeActivity.getKickTitle());
            Glide.with(itemView.getContext()).load(activeActivity.getImageUrl())
                    .into(activityImage);
        }
    }
}
