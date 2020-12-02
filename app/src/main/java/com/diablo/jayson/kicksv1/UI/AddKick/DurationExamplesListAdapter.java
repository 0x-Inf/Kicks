package com.diablo.jayson.kicksv1.UI.AddKick;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DurationExamplesListAdapter extends RecyclerView.Adapter<DurationExamplesListAdapter.DurationExampleViewHolder> {

    private ArrayList<DurationExample> durationExamplesData;

    @NonNull
    @Override
    public DurationExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DurationExampleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DurationExampleViewHolder extends RecyclerView.ViewHolder {

        public DurationExampleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
