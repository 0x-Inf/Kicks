package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.R;

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


        public InviteItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindTo(Invite invite, OnAcceptSelectedListener onAcceptSelectedListener) {

        }
    }
}
