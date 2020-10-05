package com.diablo.jayson.kicksv1.UI.Home;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Contact;

import java.util.ArrayList;

public class AllMapContactsAdapter extends RecyclerView.Adapter<AllMapContactsAdapter.ContactViewHolder> {

    private ArrayList<Contact> contactsData;

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contactsData.size();
    }

    public interface OnContactSelected {
        void onContactSelected(Contact contact);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
