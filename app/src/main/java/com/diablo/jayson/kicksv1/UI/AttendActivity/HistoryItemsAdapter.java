package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.FinishedActivity;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class HistoryItemsAdapter extends RecyclerView.Adapter<HistoryItemsAdapter.HistoryItemViewHolder> {

    private ArrayList<FinishedActivity> historyData;

    public HistoryItemsAdapter(ArrayList<FinishedActivity> historyData) {
        this.historyData = historyData;
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
        FinishedActivity finishedActivity = historyData.get(position);
        holder.bindTo(finishedActivity);
    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    class HistoryItemViewHolder extends RecyclerView.ViewHolder {

        private TextView noOfPeopleTextView, tagNameTextView;
        private RatingBar ratingBar;

        public HistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            noOfPeopleTextView = itemView.findViewById(R.id.noOfPeopleTextView);
            tagNameTextView = itemView.findViewById(R.id.tagNameTextView);
            ratingBar = itemView.findViewById(R.id.activityRatingRatingBar);
        }

        void bindTo(FinishedActivity historyItem) {
//            String noOFPeopleText = historyItem.getActivity().getActivityMaxRequiredPeople() + " People";
//            noOfPeopleTextView.setText(noOFPeopleText);
            tagNameTextView.setText(historyItem.getActivity().getActivityTags().get(0).getTagName());
            ratingBar.setRating(historyItem.getRating());
        }
    }
}
