package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.ActivityAttendeesAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;

import java.text.DateFormat;
import java.util.ArrayList;

public class HappeningSoonActivitiesAdapter extends RecyclerView.Adapter<HappeningSoonActivitiesAdapter.SoonActivityItemHolder> {

    private OnSoonActivitySelectedListener listener;
    private ArrayList<Activity> soonActivitiesData;

    public HappeningSoonActivitiesAdapter(OnSoonActivitySelectedListener listener, ArrayList<Activity> soonActivitiesData) {
        this.listener = listener;
        this.soonActivitiesData = soonActivitiesData;
    }

    @NonNull
    @Override
    public SoonActivityItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SoonActivityItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_happening_soon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SoonActivityItemHolder holder, int position) {
        Activity activity = soonActivitiesData.get(position);
        ArrayList<AttendingUser> attendeesData;
        attendeesData = activity.getActivityAttendees();
        ActivityAttendeesAdapter attendeesAdapter = new ActivityAttendeesAdapter(holder.itemView.getContext(), attendeesData);
        holder.attendeesRecycler.setAdapter(attendeesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        holder.attendeesRecycler.setLayoutManager(layoutManager);
        holder.bindTo(activity, listener);
    }

    @Override
    public int getItemCount() {
        return soonActivitiesData.size();
    }

    public interface OnSoonActivitySelectedListener {
        void onSoonActivitySelected(Activity activity);
    }

    class SoonActivityItemHolder extends RecyclerView.ViewHolder {
        private TextView activityTitleTextView, activityTimeTextView,
                activityDateTextView, activityCurrencyTextView, activityCostTextView, activityLocationTextView,
                activityTagTextView, hostTextView, activityHostNameTextView, activityDescriptionTextView;
        private ImageView activityImage, uploaderPic;
        private RecyclerView attendeesRecycler;

        public SoonActivityItemHolder(@NonNull View itemView) {
            super(itemView);

            activityTitleTextView = itemView.findViewById(R.id.activity_title_text_view);
            activityTimeTextView = itemView.findViewById(R.id.activity_time_text_view);
            activityDateTextView = itemView.findViewById(R.id.activity_date_text_view);
            activityCurrencyTextView = itemView.findViewById(R.id.currency_text_view);
            activityCostTextView = itemView.findViewById(R.id.activity_cost_text_view);
            activityLocationTextView = itemView.findViewById(R.id.activity_location_text_view);
            activityTagTextView = itemView.findViewById(R.id.activity_tag_text_view);
//            activityHostNameTextView = itemView.findViewById(R.id.hostNameTextView);
//            hostTextView = itemView.findViewById(R.id.host_name_text_view);
//            activityImage = itemView.findViewById(R.id.activity_image_view);
//            uploaderPic = itemView.findViewById(R.id.hostImageView);
            attendeesRecycler = itemView.findViewById(R.id.activity_attendees_recycler);
        }

        void bindTo(Activity activity, OnSoonActivitySelectedListener listener) {
            String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(activity.getActivityStartDate().toDate());
//            String   = DateFormat.getMediumDateFormat(itemView.getContext()).format(currentActivity.getActivityDate());
            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activity.getActivityStartTime().toDate());
//            String activityEndTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activity.getActivityEndTime().toDate());


            String activityTime = activityStartTime;
            String activityCost = activity.getActivityCost();
            String activityLocation = activity.getActivityLocationName();
            String hostName = activity.getHost().getUserName();
            String tagName = activity.getActivityTags().get(0).getTagName();
            activityTitleTextView.setText(activity.getActivityTitle());
            activityTimeTextView.setText(activityTime);
            activityDateTextView.setText(activityDate);
            activityCostTextView.setText(activityCost);
            activityLocationTextView.setText(activityLocation);
//            hostTextView.setText(hostName);
            activityTagTextView.setText(tagName);
//            Glide.with(itemView.getContext())
//                    .load(activity.getHost().getHostPic())
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(uploaderPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSoonActivitySelected(activity);
                    }
                }
            });

        }
    }
}
