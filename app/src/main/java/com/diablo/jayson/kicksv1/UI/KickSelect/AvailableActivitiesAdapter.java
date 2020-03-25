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

public class AvailableActivitiesAdapter extends FirestoreRecyclerAdapter<Activity, AvailableActivitiesAdapter.AvailableActivityViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AvailableActivitiesAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
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
        holder.bindTo(availableActivity);
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
            availableActivityNoOfPeople = itemView.findViewById(R.id.noOfPeopleTextView);
            availableActivityLocation = itemView.findViewById(R.id.availableActivitylocationTextView);
            availableActivityDateTime = itemView.findViewById(R.id.availableActivityDateTimeTextView);
        }

        void bindTo(Activity availableActivity) {

            String noOfPeople = availableActivity.getMinRequiredPeople() + "-" + availableActivity.getMaxRequiredPeeps() + " People";
            String dateTimeText = availableActivity.getkickTime() + " - " + availableActivity.getKickEndTime() + "  " + availableActivity.getkickDate();
            Glide.with(itemView.getContext())
                    .load(availableActivity.getimageUrl())
                    .into(availableActivityImage);

            availableActivityTitle.setText(availableActivity.getkickTitle());
            availableActivityCost.setText(availableActivity.getActivityCost());
            availableActivityNoOfPeople.setText(noOfPeople);
            availableActivityLocation.setText(availableActivity.getkickLocation());
            availableActivityDateTime.setText(dateTimeText);

        }

    }
}
