package com.diablo.jayson.kicksv1.UI.Profile;

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

import java.util.ArrayList;

public class GroupMessagesAdapter extends RecyclerView.Adapter<GroupMessagesAdapter.GroupMessageViewHolder> {

    private Context context;
    private ArrayList<Activity> activitiesData;

    public interface OnGroupSelectedListener {
        void onGroupSelected(Activity group);
    }

    private OnGroupSelectedListener listener;


    public GroupMessagesAdapter(Context context, ArrayList<Activity> activitiesData, OnGroupSelectedListener listener) {
        this.context = context;
        this.activitiesData = activitiesData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupMessageViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_group_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMessageViewHolder holder, int position) {
        Activity activityMessage = activitiesData.get(position);
        holder.bindTo(activityMessage, listener);
    }


    @Override
    public int getItemCount() {
        return activitiesData.size();
    }

    class GroupMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView activityImageView;
        private TextView activityTitle;

        public GroupMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            activityImageView = itemView.findViewById(R.id.groupProfilePic);
            activityTitle = itemView.findViewById(R.id.groupMessageTitle);
        }

        void bindTo(Activity groupMessage, OnGroupSelectedListener listener) {
            String groupMessageTitle = groupMessage.getActivityTitle();
            activityTitle.setText(groupMessageTitle);
            Glide.with(itemView.getContext())
                    .load(groupMessage.getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(activityImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onGroupSelected(groupMessage);
                    }
                }
            });
        }
    }
}
