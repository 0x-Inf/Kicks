package com.diablo.jayson.kicksv1.UI.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class FeaturedKickListAdapter extends RecyclerView.Adapter<FeaturedKickListAdapter.FeaturedKickViewHolder> {

    private Context context;
    private ArrayList<Kick> kickData;

    public FeaturedKickListAdapter(Context context, ArrayList<Kick> kickData) {
        this.context = context;
        this.kickData = kickData;
    }

    @NonNull
    @Override
    public FeaturedKickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeaturedKickViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_featured_kick_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedKickViewHolder holder, int position) {
        Kick featuredKick = kickData.get(position);
        holder.bindTo(featuredKick);
    }

    @Override
    public int getItemCount() {
        return kickData.size();
    }

    class FeaturedKickViewHolder extends RecyclerView.ViewHolder {

        private ImageView featuredKickImage;
        private TextView featuredKickName, featuredKickShortDescriptionTextView;

        public FeaturedKickViewHolder(@NonNull View itemView) {
            super(itemView);
            featuredKickImage = itemView.findViewById(R.id.featuredKickImage);
            featuredKickName = itemView.findViewById(R.id.featuredKickName);
            featuredKickShortDescriptionTextView = itemView.findViewById(R.id.featuredKickShortDescriptionTextView);
        }

        void bindTo(Kick featuredKick) {
            String kickName = featuredKick.getKickName();
//            String kickShortDescription = featuredKick.getKickShortDescription();
            featuredKickName.setText(kickName);
//            featuredKickShortDescriptionTextView.setText(kickShortDescription);
            Glide.with(itemView.getContext())
                    .load(featuredKick.getKickMainImageUrl())
                    .into(featuredKickImage);
        }
    }
}