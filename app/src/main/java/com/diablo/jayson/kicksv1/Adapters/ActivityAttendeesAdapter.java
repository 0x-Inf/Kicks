package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class ActivityAttendeesAdapter extends RecyclerView.Adapter<ActivityAttendeesAdapter.AttendeeViewHolder> {

    private Context mContext;
    private ArrayList<AttendingUser> attendeesData;

    public ActivityAttendeesAdapter(Context mContext, ArrayList<AttendingUser> attendeesData) {
        this.mContext = mContext;
        this.attendeesData = attendeesData;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendeeViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.activity_attendee_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        AttendingUser attendingUser = attendeesData.get(position);
        holder.bindTo(attendingUser);
    }

    @Override
    public int getItemCount() {
        return attendeesData.size();
    }

    class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private ImageView attendeeProfilePic;

        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            attendeeProfilePic = itemView.findViewById(R.id.activity_attendee_image_view);
        }

        void bindTo(AttendingUser attendingUser) {
            Glide.with(itemView.getContext()).load(attendingUser.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(attendeeProfilePic);
        }
    }
}
