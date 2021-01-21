package com.diablo.jayson.kicksv1.UI.AddKick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class DurationExamplesListAdapter extends RecyclerView.Adapter<DurationExamplesListAdapter.DurationExampleViewHolder> {

    private ArrayList<DurationExample> durationExamplesData;
    private OnDurationExampleSelectedListener onDurationExampleSelectedListener;

    public DurationExamplesListAdapter(ArrayList<DurationExample> durationExamplesData,
                                       OnDurationExampleSelectedListener onDurationExampleSelectedListener) {
        this.durationExamplesData = durationExamplesData;
        this.onDurationExampleSelectedListener = onDurationExampleSelectedListener;
    }

    @NonNull
    @Override
    public DurationExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DurationExampleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_duration_example_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DurationExampleViewHolder holder, int position) {
        DurationExample durationExample = durationExamplesData.get(position);
        holder.bindTo(durationExample, onDurationExampleSelectedListener);
    }

    @Override
    public int getItemCount() {
        return durationExamplesData.size();
    }

    public interface OnDurationExampleSelectedListener {
        void onDurationExampleSelected(DurationExample durationExample);
    }

    class DurationExampleViewHolder extends RecyclerView.ViewHolder {
        private TextView durationTextView;

        public DurationExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.durationNameTextView);
        }

        void bindTo(DurationExample durationExample, OnDurationExampleSelectedListener onDurationExampleSelectedListener) {
            durationTextView.setText(durationExample.getDurationName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDurationExampleSelectedListener != null) {
                        onDurationExampleSelectedListener.onDurationExampleSelected(durationExample);
                    }
                }
            });
        }
    }
}
