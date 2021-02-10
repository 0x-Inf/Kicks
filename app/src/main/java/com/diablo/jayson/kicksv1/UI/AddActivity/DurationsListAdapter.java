package com.diablo.jayson.kicksv1.UI.AddActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class DurationsListAdapter extends RecyclerView.Adapter<DurationsListAdapter.DurationExampleViewHolder> {

    private ArrayList<Duration> durationExamplesData;
    private OnDurationExampleSelectedListener onDurationExampleSelectedListener;


    private int checkedPosition = -1;

    public DurationsListAdapter(ArrayList<Duration> durationExamplesData,
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
        Duration duration = durationExamplesData.get(position);
        holder.bindTo(duration, onDurationExampleSelectedListener);
    }

    @Override
    public int getItemCount() {
        return durationExamplesData.size();
    }

    public void resetCheckedPosition() {
        checkedPosition = -1;
        notifyItemChanged(checkedPosition);
    }

    public interface OnDurationExampleSelectedListener {
        void onDurationExampleSelected(Duration duration);
    }

    class DurationExampleViewHolder extends RecyclerView.ViewHolder {
        private TextView durationTextView;

        public DurationExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.durationNameTextView);
        }

        void bindTo(Duration duration, OnDurationExampleSelectedListener onDurationExampleSelectedListener) {
            if (checkedPosition == -1) {
                durationTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textColorSecondary));
            } else {
                if (checkedPosition == getAbsoluteAdapterPosition()) {
                    durationTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorMain));
                } else {
                    durationTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textColorSecondary));
                }
            }
            durationTextView.setText(duration.getDurationName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    durationTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorMain));
                    if (checkedPosition != getAbsoluteAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAbsoluteAdapterPosition();
                    }
                    if (onDurationExampleSelectedListener != null) {
                        onDurationExampleSelectedListener.onDurationExampleSelected(duration);
                    }
                }
            });
        }
    }

}
