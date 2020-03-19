package com.diablo.jayson.kicksv1.Adapters;

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
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class TagSelectedListAdapter extends RecyclerView.Adapter<TagSelectedListAdapter.TagSelectedViewHolder> {

    private Context mContext;
    private ArrayList<Tag> mSelectedTagsData;

    public TagSelectedListAdapter(Context mContext, ArrayList<Tag> mSelectedTagsData) {
        this.mContext = mContext;
        this.mSelectedTagsData = mSelectedTagsData;
    }

    @NonNull
    @Override
    public TagSelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagSelectedViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.tag_selected, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagSelectedViewHolder holder, int position) {
        Tag selectedTag = mSelectedTagsData.get(position);
        holder.bindTo(selectedTag);
    }


    @Override
    public int getItemCount() {
        return mSelectedTagsData.size();
    }

    static class TagSelectedViewHolder extends RecyclerView.ViewHolder {
        private TextView mSelectedTagName;
        private ImageView mTagIcon;


        public TagSelectedViewHolder(@NonNull View itemView) {
            super(itemView);
            mSelectedTagName = itemView.findViewById(R.id.activityTagTextView);
            mTagIcon = itemView.findViewById(R.id.tagIconImage);

        }

        void bindTo(Tag selectedTag) {
            mSelectedTagName.setText(selectedTag.getTagName());
            Glide.with(itemView.getContext())
                    .load(selectedTag.getTagIconUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mTagIcon);
        }
    }
}
