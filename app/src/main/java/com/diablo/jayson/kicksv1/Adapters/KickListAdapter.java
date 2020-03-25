package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class KickListAdapter extends FirestoreRecyclerAdapter<Kick, KickListAdapter.KickViewHolder> implements LifecycleOwner {

    private Context mContext;
    private ArrayList<Kick> mKicksData;
    private LifecycleOwner owner;


    public interface OnKickSelectedListener {
        void onkickSelected(Kick kick);
    }

    private OnKickSelectedListener listener;

    public KickListAdapter(@NonNull FirestoreRecyclerOptions<Kick> options, OnKickSelectedListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public KickListAdapter.KickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.kick_list_item, parent, false));
    }


    @Override
    public void startListening() {
        super.startListening();
    }

    @Override
    public void stopListening() {
        super.stopListening();
    }



    @Override
    protected void onBindViewHolder(@NonNull KickViewHolder holder, int position, @NonNull Kick model) {
        Kick currentKick = getItem(position);
        holder.bindTo(currentKick, listener);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return null;
            }
        };
    }


    static class KickViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickImage;
        private TextView mKickName;

        KickViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickImage = itemView.findViewById(R.id.kickSelectImage);
            mKickName = itemView.findViewById(R.id.kickSelectTextView);
        }

        void bindTo(Kick currentKick, OnKickSelectedListener listener) {
            mKickName.setText(currentKick.getKickName());
            Glide.with(itemView.getContext()).load(currentKick.getKickCardImageUrl()).into(mKickImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onkickSelected(currentKick);
                    }
                }
            });
        }
    }
}
