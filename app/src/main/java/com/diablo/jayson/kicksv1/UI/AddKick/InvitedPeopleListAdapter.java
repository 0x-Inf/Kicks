package com.diablo.jayson.kicksv1.UI.AddKick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class InvitedPeopleListAdapter extends RecyclerView.Adapter<InvitedPeopleListAdapter.InvitedContactVieHolder> {

    private ArrayList<Contact> invitedContactsListData;
    private OnInvitedContactSelectedListener invitedContactSelectedListener;

    public InvitedPeopleListAdapter(ArrayList<Contact> invitedContactsListData, OnInvitedContactSelectedListener invitedContactSelectedListener) {
        this.invitedContactsListData = invitedContactsListData;
        this.invitedContactSelectedListener = invitedContactSelectedListener;
    }

    @NonNull
    @Override
    public InvitedContactVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvitedContactVieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_invited_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvitedContactVieHolder holder, int position) {
        Contact invitedContact = invitedContactsListData.get(position);
        holder.bindTo(invitedContact, invitedContactSelectedListener);
    }

    @Override
    public int getItemCount() {
        return invitedContactsListData.size();
    }

    public interface OnInvitedContactSelectedListener {
        void onInvitedContactSelected(Contact invitedContact);
    }

    class InvitedContactVieHolder extends RecyclerView.ViewHolder {

        private ImageView invitedContactImageView;
        private TextView invitedContactNameTextView;

        public InvitedContactVieHolder(@NonNull View itemView) {
            super(itemView);
            invitedContactImageView = itemView.findViewById(R.id.invitedContactPicImageView);
            invitedContactNameTextView = itemView.findViewById(R.id.invitedContactNameTextView);

        }

        void bindTo(Contact invitedContact, OnInvitedContactSelectedListener invitedContactSelectedListener) {
            invitedContactNameTextView.setText(invitedContact.getContactName());
            Glide.with(itemView.getContext())
                    .load(invitedContact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(invitedContactImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (invitedContactSelectedListener != null) {
                        invitedContactSelectedListener.onInvitedContactSelected(invitedContact);
                    }
                }
            });
        }
    }
}
