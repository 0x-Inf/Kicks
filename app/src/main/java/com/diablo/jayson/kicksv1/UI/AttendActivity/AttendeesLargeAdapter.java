package com.diablo.jayson.kicksv1.UI.AttendActivity;

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
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class AttendeesLargeAdapter extends RecyclerView.Adapter<AttendeesLargeAdapter.AttendeeViewHolder> {

    private Context context;
    private ArrayList<AttendingUser> mAttendeesData;

    public interface OnAttendeeSelectedListener{
        void onAttendeeSelected(AttendingUser attendingUser);
    }
    private OnAttendeeSelectedListener listener;

    public AttendeesLargeAdapter(Context context, ArrayList<AttendingUser> mAttendeesData, OnAttendeeSelectedListener listener) {
        this.context = context;
        this.mAttendeesData = mAttendeesData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendeesLargeAdapter.AttendeeViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_attendees_item_view_large, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {

        AttendingUser currentAttendingUser = mAttendeesData.get(position);
        holder.bindTo(currentAttendingUser,listener);
    }


    @Override
    public int getItemCount() {
        return mAttendeesData.size();
    }

    static class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private ImageView attendeePic;
        private TextView attendeename;


        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            attendeePic = itemView.findViewById(R.id.attendeeitemimage);
            attendeename = itemView.findViewById(R.id.attendeeName);

//            attendeename = itemView.findViewById(R.id.textView);
        }

        void bindTo(AttendingUser attendingUser, OnAttendeeSelectedListener listener) {
            Glide.with(itemView.getContext())
                    .load(attendingUser.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(attendeePic);
            attendeename.setText(attendingUser.getUserName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onAttendeeSelected(attendingUser);
                    }
                }
            });

//            attendeename.setText(attendingUser.getUserName());
        }
    }
}
