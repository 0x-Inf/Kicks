package com.diablo.jayson.kicksv1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TagsListAdapter extends FirestoreRecyclerAdapter<Tag, TagsListAdapter.TagViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TagsListAdapter(@NonNull FirestoreRecyclerOptions<Tag> options) {
        super(options);
    }

    @NonNull
    @Override
    public TagsListAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TagViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.tag_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull TagsListAdapter.TagViewHolder holder, int position, @NonNull Tag model) {

        Tag currentTag = getItem(position);
        holder.bindTo(currentTag);
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {

        private TextView mTagTextView;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            mTagTextView = itemView.findViewById(R.id.activityTagTextView);
        }

        void bindTo(Tag currentTag) {
            mTagTextView.setText(currentTag.getTagName());
        }


    }
}
