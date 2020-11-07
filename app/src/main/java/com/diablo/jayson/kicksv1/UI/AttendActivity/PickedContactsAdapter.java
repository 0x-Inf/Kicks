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

public class PickedContactsAdapter extends RecyclerView.Adapter<PickedContactsAdapter.PickedContactVieHolder> {

    private OnPickedContactSelectedListener pickedContactSelectedListener;
    private ArrayList<Contact> pickedContactsData;

    public PickedContactsAdapter(ArrayList<Contact> pickedContactsData, OnPickedContactSelectedListener pickedContactSelectedListener) {
        this.pickedContactSelectedListener = pickedContactSelectedListener;
        this.pickedContactsData = pickedContactsData;
    }

    @NonNull
    @Override
    public PickedContactVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PickedContactVieHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PickedContactVieHolder holder, int position) {
        Contact pickedContact = pickedContactsData.get(position);
        holder.bindTo(pickedContact, pickedContactSelectedListener);
    }

    @Override
    public int getItemCount() {
        return pickedContactsData.size();
    }

    public interface OnPickedContactSelectedListener {
        void onPickedContactSelected(Contact contact);
    }

    static class PickedContactVieHolder extends RecyclerView.ViewHolder {

        private ImageView contactPicImageView;
        private TextView contactNameTextView;

        public PickedContactVieHolder(@NonNull View itemView) {
            super(itemView);
            contactPicImageView = itemView.findViewById(R.id.contactPicImageView);
            contactNameTextView = itemView.findViewById(R.id.contactNameTextView);
        }

        void bindTo(Contact pickedContact, OnPickedContactSelectedListener pickedContactSelectedListener) {
            contactNameTextView.setText(pickedContact.getContactName());
            Glide.with(itemView.getContext())
                    .load(pickedContact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactPicImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pickedContactSelectedListener != null) {
                        pickedContactSelectedListener.onPickedContactSelected(pickedContact);
                    }
                }
            });
        }
    }
}
