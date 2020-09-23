package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;

public class InvitesListAdapter extends RecyclerView.Adapter<InvitesListAdapter.InviteItemViewHolder> {

    private ArrayList<Invite> invitesData;
    private OnAcceptSelectedListener onAcceptSelectedListener;

    public InvitesListAdapter(ArrayList<Invite> invitesData, OnAcceptSelectedListener onAcceptSelectedListener) {
        this.invitesData = invitesData;
        this.onAcceptSelectedListener = onAcceptSelectedListener;
    }

    @NonNull
    @Override
    public InviteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InviteItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_invite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteItemViewHolder holder, int position) {
        Invite inviteItem = invitesData.get(position);
        holder.bindTo(inviteItem, onAcceptSelectedListener);
    }

    @Override
    public int getItemCount() {
        return invitesData.size();
    }

    public interface OnAcceptSelectedListener {
        void onAcceptSelected(Invite acceptedInvite);
    }

    class InviteItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView inviterPicImageView;
        private TextView inviterNameTextView, activityTitleTextView, activityDateTextView,
                activityStartTimeTextView, activityDurationTextView, activityLocationTextView,
                activityCostTextView, activityTagTextView;
        private FloatingActionButton acceptInviteFab;


        public InviteItemViewHolder(@NonNull View itemView) {
            super(itemView);
            inviterPicImageView = itemView.findViewById(R.id.inviterPicImageView);
            inviterNameTextView = itemView.findViewById(R.id.inviterNameTextView);
            activityTitleTextView = itemView.findViewById(R.id.activityTitleTextView);
            activityDateTextView = itemView.findViewById(R.id.activityDateTextView);
            activityStartTimeTextView = itemView.findViewById(R.id.activityStartTimeTextView);
            activityDurationTextView = itemView.findViewById(R.id.activityDurationTextView);
            activityLocationTextView = itemView.findViewById(R.id.activityLocationTextView);
            activityCostTextView = itemView.findViewById(R.id.activityCostTextView);
            activityTagTextView = itemView.findViewById(R.id.activityTagNameTextView);
            acceptInviteFab = itemView.findViewById(R.id.acceptInviteFab);
        }

        void bindTo(Invite invite, OnAcceptSelectedListener onAcceptSelectedListener) {
            String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(invite.getInviteActivity().getActivityStartTime().toDate());
            String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(invite.getInviteActivity().getActivityDate().toDate());
            inviterNameTextView.setText(invite.getInviterName());
            activityTitleTextView.setText(invite.getInviteActivity().getActivityTitle());
            activityDateTextView.setText(activityDate);
            activityStartTimeTextView.setText(activityStartTime);
//            activityDurationTextView.setText(invite.getInviteActivity().);
            activityLocationTextView.setText(invite.getInviteActivity().getActivityLocationName());
            activityCostTextView.setText(invite.getInviteActivity().getActivityCost());
            activityTagTextView.setText(invite.getInviteActivity().getActivityTag().getTagName());
            Glide.with(itemView.getContext())
                    .load(invite.getInviterPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(inviterPicImageView);
            acceptInviteFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAcceptSelectedListener != null) {
                        onAcceptSelectedListener.onAcceptSelected(invite);
                    }
                }
            });
        }
    }
}
