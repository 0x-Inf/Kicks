package com.diablo.jayson.kicksv1.UI.AttendActivity;

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

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private OnContactSelectedListener contactSelectedListener;
    private ArrayList<Contact> contactsData;

    public ContactListAdapter(ArrayList<Contact> contactsData, OnContactSelectedListener contactSelectedListener) {
        this.contactSelectedListener = contactSelectedListener;
        this.contactsData = contactsData;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactsData.get(position);
        holder.bindTo(contact, contactSelectedListener);
    }

    @Override
    public int getItemCount() {
        return contactsData.size();
    }

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView contactPicImageView;
        private TextView contactNameTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPicImageView = itemView.findViewById(R.id.contactPicImageView);
            contactNameTextView = itemView.findViewById(R.id.contactNameTextView);
        }

        void bindTo(Contact contact, OnContactSelectedListener selectedListener) {
            contactNameTextView.setText(contact.getContactName());
            Glide.with(itemView.getContext())
                    .load(contact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactPicImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedListener != null) {
                        selectedListener.onContactSelected(contact);
                    }
                }
            });
        }
    }
}
