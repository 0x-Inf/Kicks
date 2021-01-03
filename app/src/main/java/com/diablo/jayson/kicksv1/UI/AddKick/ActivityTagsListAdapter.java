package com.diablo.jayson.kicksv1.UI.AddKick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class ActivityTagsListAdapter extends RecyclerView.Adapter<ActivityTagsListAdapter.ActivityTagViewHolder> {

    private ArrayList<Tag> activityTagsData;
    private OnActivityTagSelectedListener activityTagSelectedListener;

    public ActivityTagsListAdapter(ArrayList<Tag> activityTagsData, OnActivityTagSelectedListener activityTagSelectedListener) {
        this.activityTagsData = activityTagsData;
        this.activityTagSelectedListener = activityTagSelectedListener;
    }

    @NonNull
    @Override
    public ActivityTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityTagViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_additonal_tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityTagViewHolder holder, int position) {
        Tag activityTag = activityTagsData.get(position);
        holder.bindTo(activityTag, activityTagSelectedListener);
    }

    @Override
    public int getItemCount() {
        return activityTagsData.size();
    }

    public interface OnActivityTagSelectedListener {
        void onActivityTagSelected(Tag activityTag);
    }

    class ActivityTagViewHolder extends RecyclerView.ViewHolder {
        private TextView tagNameTextView;

        public ActivityTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(R.id.tagNameTextView);
        }

        void bindTo(Tag activityTag, OnActivityTagSelectedListener onActivityTagSelectedListener) {
            tagNameTextView.setText(activityTag.getTagName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onActivityTagSelectedListener != null) {
                        onActivityTagSelectedListener.onActivityTagSelected(activityTag);
                    }
                }
            });

        }
    }
}
