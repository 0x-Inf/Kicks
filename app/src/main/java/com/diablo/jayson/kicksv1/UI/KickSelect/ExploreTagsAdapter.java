package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class ExploreTagsAdapter extends RecyclerView.Adapter<ExploreTagsAdapter.ExploreTagsViewHolder> {

    private final ArrayList<Tag> tagArrayList;
    private final OnExploreTagSelectedListener onExploreTagSelectedListener;

    public ExploreTagsAdapter(ArrayList<Tag> tagArrayList, OnExploreTagSelectedListener onExploreTagSelectedListener) {
        this.tagArrayList = tagArrayList;
        this.onExploreTagSelectedListener = onExploreTagSelectedListener;
    }

    @NonNull
    @Override
    public ExploreTagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreTagsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_explore_tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreTagsViewHolder holder, int position) {
        Tag currentTag = tagArrayList.get(position);
        holder.bindTo(currentTag, onExploreTagSelectedListener);
    }

    @Override
    public int getItemCount() {
        return tagArrayList.size();
    }

    public interface OnExploreTagSelectedListener {
        void onTagSelected(Tag selectedTag);
    }

    static class ExploreTagsViewHolder extends RecyclerView.ViewHolder {

        private final TextView tagNameTextView;

        public ExploreTagsViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(R.id.tagNameTextView);
        }

        void bindTo(Tag tag, OnExploreTagSelectedListener onExploreTagSelectedListener) {
            tagNameTextView.setText(tag.getTagName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onExploreTagSelectedListener != null) {
                        onExploreTagSelectedListener.onTagSelected(tag);
                    }
                }
            });
        }
    }
}
