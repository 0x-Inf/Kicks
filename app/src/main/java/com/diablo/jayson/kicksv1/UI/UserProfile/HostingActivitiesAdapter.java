package com.diablo.jayson.kicksv1.UI.UserProfile;

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

public class HostingActivitiesAdapter extends FirestoreRecyclerAdapter<Activity, HostingActivitiesAdapter.HostingActivityViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HostingActivitiesAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);

    }


    @NonNull
    @Override
    public HostingActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HostingActivityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hosting_activity_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull HostingActivityViewHolder holder, int position, @NonNull Activity model) {
        Activity hostingActivity = getItem(position);
        holder.bindTo(hostingActivity);
    }

    static class HostingActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView hostingActivityTitle;
        private ImageView hostingActivityImage;
        private RecyclerView hostingActivityAttendeesRecycler;

        public HostingActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            hostingActivityTitle = itemView.findViewById(R.id.hostingActivityTitleTextView);
            hostingActivityImage = itemView.findViewById(R.id.hostingActivityImage);
            hostingActivityAttendeesRecycler = itemView.findViewById(R.id.hostingAttendeesRecycler);
        }

        void bindTo(Activity hostingActivity) {
            hostingActivityTitle.setText(hostingActivity.getActivityTitle());
            Glide.with(itemView.getContext())
                    .load(hostingActivity.getImageUrl())
                    .into(hostingActivityImage);
        }


    }


}
