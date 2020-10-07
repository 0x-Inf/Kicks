package com.diablo.jayson.kicksv1.UI.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class SelectedMapContactsAdapter extends RecyclerView.Adapter<SelectedMapContactsAdapter.SelectedContactViewHolder> {

    private ArrayList<Contact> selectedContactsData;

    public SelectedMapContactsAdapter(ArrayList<Contact> selectedContactsData) {
        this.selectedContactsData = selectedContactsData;
    }

    @NonNull
    @Override
    public SelectedContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_map_selected_contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedContactViewHolder holder, int position) {
        Contact selectedContact = selectedContactsData.get(position);
        holder.bindTo(selectedContact);
    }

    @Override
    public int getItemCount() {
        return selectedContactsData.size();
    }

    static class SelectedContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView selectedContactPicImageView;

        public SelectedContactViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedContactPicImageView = itemView.findViewById(R.id.selectedContactPicImageView);
        }

        void bindTo(Contact selectedContact) {
            Glide.with(itemView.getContext())
                    .load(selectedContact.getContactPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(selectedContactPicImageView);
        }
    }
}
