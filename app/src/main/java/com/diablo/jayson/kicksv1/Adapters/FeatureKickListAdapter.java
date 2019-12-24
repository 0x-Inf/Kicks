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
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class FeatureKickListAdapter extends RecyclerView.Adapter<FeatureKickListAdapter.KickViewHolder>{

    private Context mContext;
    private ArrayList<Kick> mKicksData;

    public FeatureKickListAdapter(Context mContext, ArrayList<Kick> mKicksData) {
        this.mContext = mContext;
        this.mKicksData = mKicksData;
    }

    @NonNull
    @Override
    public FeatureKickListAdapter.KickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeatureKickListAdapter.KickViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.featured_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureKickListAdapter.KickViewHolder holder, int position) {
        Kick currentKick = mKicksData.get(position);
        holder.bindTo(currentKick);
    }



    @Override
    public int getItemCount() {
        return mKicksData.size();
    }

    class KickViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickImage;
        private TextView mKickName;

        public KickViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickImage = itemView.findViewById(R.id.featuredKickInlistimageView);
            mKickName = itemView.findViewById(R.id.featuredKickInlistName);
        }

        void bindTo(Kick currentKick){
            mKickName.setText(currentKick.getKickName());
            Glide.with(mContext).load(currentKick.getImageUrl()).into(mKickImage);
        }
    }
}
