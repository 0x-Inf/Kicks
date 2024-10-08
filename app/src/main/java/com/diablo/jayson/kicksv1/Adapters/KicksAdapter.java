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

public class KicksAdapter extends RecyclerView.Adapter<KicksAdapter.KickViewHolder> {

    private Context mContext;
    private ArrayList<Kick> kicksData;

    public interface OnKickSelectedListener {
        void onKickSelected(Kick kick);
    }

    private OnKickSelectedListener listener;

    public KicksAdapter(Context mContext, ArrayList<Kick> kicksData, OnKickSelectedListener listener) {
        this.mContext = mContext;
        this.kicksData = kicksData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.recycler_kick_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull KickViewHolder holder, int position) {

        Kick currentKick = kicksData.get(position);
        holder.bindTo(currentKick, listener);
    }


    @Override
    public int getItemCount() {
        return kicksData.size();
    }

    static class KickViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickImage;
        private TextView mKickName;

        public KickViewHolder(@NonNull View itemView) {
            super(itemView);
            mKickImage = itemView.findViewById(R.id.kickSelectImage);
            mKickName = itemView.findViewById(R.id.kickSelectTextView);
        }

        void bindTo(Kick currentKick, OnKickSelectedListener listener) {
            mKickName.setText(currentKick.getKickName());
            Glide.with(itemView.getContext()).load(currentKick.getKickMainImageUrl()).into(mKickImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onKickSelected(currentKick);
                    }
                }
            });
        }
    }
}
