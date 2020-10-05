package com.diablo.jayson.kicksv1.UI.Home;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedMapContactsAdapter extends RecyclerView.Adapter<SelectedMapContactsAdapter.SelectedContactViewHolder> {

    @NonNull
    @Override
    public SelectedContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedContactViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SelectedContactViewHolder extends RecyclerView.ViewHolder {

        public SelectedContactViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
