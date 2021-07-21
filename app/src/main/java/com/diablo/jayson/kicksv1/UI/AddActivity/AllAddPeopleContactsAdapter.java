package com.diablo.jayson.kicksv1.UI.AddActivity;

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

public class AllAddPeopleContactsAdapter extends RecyclerView.Adapter<AllAddPeopleContactsAdapter.ContactViewHolder> {

    private ArrayList<Contact> allContactsData;
    private OnContactSelectedListener contactSelectedListener;

    public AllAddPeopleContactsAdapter(ArrayList<Contact> allContactsData, OnContactSelectedListener contactSelectedListener) {
        this.allContactsData = allContactsData;
        this.contactSelectedListener = contactSelectedListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_people_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = allContactsData.get(position);
        holder.bindTo(contact, contactSelectedListener);
    }

    @Override
    public int getItemCount() {
        return allContactsData.size();
    }

    public interface OnContactSelectedListener {
        void onContactSelected(Contact selectedContact);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView contactPicImageView;
        private TextView contactNameTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNameTextView = itemView.findViewById(R.id.addPeopleContactNameTextView);
            contactPicImageView = itemView.findViewById(R.id.addPeopleContactPicImageView);
        }

        void bindTo(Contact contact, OnContactSelectedListener contactSelectedListener) {
            contactNameTextView.setText(contact.getContactName());
            Glide.with(itemView.getContext())
                    .load(contact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactPicImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contactSelectedListener != null) {
                        contactSelectedListener.onContactSelected(contact);
                    }
                }
            });
        }
    }
}
