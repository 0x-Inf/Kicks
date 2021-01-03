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

public class AddActivityAllTagsListAdapter extends RecyclerView.Adapter<AddActivityAllTagsListAdapter.AddActivityTagViewHolder> {

    private ArrayList<Tag> tagsData;
    private OnTagSelectedListener tagSelectedListener;

    public AddActivityAllTagsListAdapter(ArrayList<Tag> tagsData, OnTagSelectedListener tagSelectedListener) {
        this.tagsData = tagsData;
        this.tagSelectedListener = tagSelectedListener;
    }

    @NonNull
    @Override
    public AddActivityTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddActivityTagViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_activity_tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddActivityTagViewHolder holder, int position) {
        Tag tag = tagsData.get(position);
        holder.bindTo(tag, tagSelectedListener);
    }

    @Override
    public int getItemCount() {
        return tagsData.size();
    }

    public interface OnTagSelectedListener {
        void onTagSelected(Tag tag);
    }

    class AddActivityTagViewHolder extends RecyclerView.ViewHolder {

        private TextView tagNameTextView;

        public AddActivityTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(R.id.tagNameTextView);
        }

        void bindTo(Tag tag, OnTagSelectedListener tagSelectedListener) {
            tagNameTextView.setText(tag.getTagName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tagSelectedListener != null) {
                        tagSelectedListener.onTagSelected(tag);
                    }
                }
            });

        }
    }
}
