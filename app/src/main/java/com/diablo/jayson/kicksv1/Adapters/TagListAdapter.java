package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.google.firebase.firestore.core.QueryListener;

import java.util.ArrayList;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagViewHolder> {


    private Context mContext;
    private ArrayList<Tag> mTagsData;


    public interface OnTagSelectedListener {
        void onTagListener(Tag tag);
    }

    private OnTagSelectedListener listener;
    private TextWatcher textWatcher;
    private QueryListener queryListener;


    public TagListAdapter(Context mContext, ArrayList<Tag> mTagsData, OnTagSelectedListener listener) {
        this.mContext = mContext;
        this.mTagsData = mTagsData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.tag_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag currentTag = mTagsData.get(position);
        holder.bindTo(currentTag, listener);
    }


    @Override
    public int getItemCount() {
        return mTagsData.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView tagNameTextView;
        private ImageView tagImageView;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTextView = itemView.findViewById(R.id.tagNameTextView);
            tagImageView = itemView.findViewById(R.id.tagImageView);
        }

        void bindTo(Tag currentTag, OnTagSelectedListener listener) {
            tagNameTextView.setText(currentTag.getTagName());
//            Glide.with(itemView.getContext())
//                    .load(currentTag.getTagImageLargeUrl())
//                    .into(tagImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTagListener(currentTag);
                    }
                }
            });
        }
    }
}
