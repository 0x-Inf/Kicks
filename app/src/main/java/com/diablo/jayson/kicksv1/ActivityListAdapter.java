package com.diablo.jayson.kicksv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder> {

    private ArrayList<Kick> mKicksData;
    private MutableLiveData<ArrayList<Kick>> mKicksDataM;
    private Context mContext;
    private LayoutInflater mInflater;

    public ActivityListAdapter(Context context, ArrayList<Kick> kicksdata){
        this.mKicksData = kicksdata;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    public ActivityListAdapter(Context context, MutableLiveData<ArrayList<Kick>> mKicksData) {
        this.mContext = context;
        this.mKicksDataM = mKicksData;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ActivityListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.activity_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.ActivityViewHolder holder, int position) {
        Kick currentKick = mKicksData.get(position);
        holder.bindTo(currentKick);

    }

    @Override
    public int getItemCount() {
        return mKicksData.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mKickTitleText;
        private TextView mKickTimeText;
        private TextView mKickDateText;
        private TextView mKickLocationText;
        private TextView mAlreadyAttendingPeepsText;
        private TextView mRequiredPeepsText;
        private ImageView mKickImage;


        public ActivityViewHolder(View itemView) {
            super(itemView);
            mKickTitleText = itemView.findViewById(R.id.kickNameTextView);
            mKickTimeText = itemView.findViewById(R.id.timeTextView);
            mKickDateText = itemView.findViewById(R.id.dateTextView);
            mKickLocationText = itemView.findViewById(R.id.locationTextView);
            mAlreadyAttendingPeepsText = itemView.findViewById(R.id.alreeadyKickingTextView);
            mRequiredPeepsText = itemView.findViewById(R.id.requiredKickersTextView);
            mKickImage = itemView.findViewById(R.id.kickImage);

        }

        void bindTo(Kick currentKick){
            mKickTitleText.setText(currentKick.getKickTitle());
            mKickTimeText.setText(currentKick.getKickTime());
            mKickDateText.setText(currentKick.getKickDate());
            mKickLocationText.setText(currentKick.getKickLocation());
            mAlreadyAttendingPeepsText.setText(currentKick.getKickAlreadyAttendingPeeps());
            mRequiredPeepsText.setText(currentKick.getKickRequiredPeeps());
            Glide.with(mContext).load(currentKick.getImageResource()).into(mKickImage);

        }
    }
}
