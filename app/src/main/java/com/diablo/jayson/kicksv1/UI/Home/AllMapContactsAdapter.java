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
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class AllMapContactsAdapter extends RecyclerView.Adapter<AllMapContactsAdapter.ContactViewHolder> {

    private OnMapContactSelectedListener mapContactSelectedListener;

    private ArrayList<Contact> contactsData;

    public AllMapContactsAdapter(ArrayList<Contact> contactsData, OnMapContactSelectedListener mapContactSelectedListener) {
        this.contactsData = contactsData;
        this.mapContactSelectedListener = mapContactSelectedListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_map_contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact mapContact = contactsData.get(position);
        holder.bindTo(mapContact, mapContactSelectedListener);
    }

    public interface OnMapContactSelectedListener {
        void onContactSelected(Contact contact);
    }

    @Override
    public int getItemCount() {
        return contactsData.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView contactPicImageView;
        private TextView contactNameTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPicImageView = itemView.findViewById(R.id.mapContactPicImageView);
            contactNameTextView = itemView.findViewById(R.id.mapContactNameTextView);
        }

        void bindTo(Contact mapContact, OnMapContactSelectedListener onMapContactSelectedListener) {
            contactNameTextView.setText(mapContact.getContactName());
            Glide.with(itemView.getContext())
                    .load(mapContact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactPicImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMapContactSelectedListener != null) {
                        onMapContactSelectedListener.onContactSelected(mapContact);
                    }
                }
            });
        }
    }
}
