package com.diablo.jayson.kicksv1.UI.AttendActivity;

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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ActivityAttendeesAdapter extends FirestoreRecyclerAdapter<AttendingUser, ActivityAttendeesAdapter.AttendeeViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ActivityAttendeesAdapter(@NonNull FirestoreRecyclerOptions<AttendingUser> options) {
        super(options);
    }


    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendeeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_attendees_item_view, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position, @NonNull AttendingUser model) {

        AttendingUser attendingUser = getItem(position);
        holder.bindTo(attendingUser);
    }

    static class AttendeeViewHolder extends RecyclerView.ViewHolder {

        private ImageView attendeePic;

        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            attendeePic = itemView.findViewById(R.id.attendeeitemimage);
        }

        void bindTo(AttendingUser attendingUser) {
            Glide.with(itemView.getContext())
                    .load(attendingUser.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(attendeePic);
        }
    }
}
