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

public class KickListAdapter extends RecyclerView.Adapter<KickListAdapter.KickViewHolder> {

    private Context mContext;
    private ArrayList<Kick> mKicksData;

    public KickListAdapter(Context mContext, ArrayList<Kick> mKicksData) {
        this.mContext = mContext;
        this.mKicksData = mKicksData;
    }

    @NonNull
    @Override
    public KickListAdapter.KickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.kick_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull KickViewHolder holder, int position) {
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

            mKickImage = itemView.findViewById(R.id.kickSelectImage);
            mKickName = itemView.findViewById(R.id.kickSelectTextView);
        }

        void bindTo(Kick currentKick){
            mKickName.setText(currentKick.getKickName());
            Glide.with(mContext).load(currentKick.getKickImageUrl()).into(mKickImage);
        }
    }
}
