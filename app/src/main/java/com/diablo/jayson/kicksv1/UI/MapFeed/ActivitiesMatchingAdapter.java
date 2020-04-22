package com.diablo.jayson.kicksv1.UI.MapFeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ActivitiesMatchingAdapter extends FirestoreRecyclerAdapter<Activity, ActivitiesMatchingAdapter.MatchedActivityViewHolder> {
    public interface OnActivitySelectedListener {
        void onActivitySelected(Activity activity);
    }

    private OnActivitySelectedListener listener;

    ActivitiesMatchingAdapter(@NonNull FirestoreRecyclerOptions options, OnActivitySelectedListener listener) {
        super(options);
        this.listener = listener;
    }


    @NonNull
    @Override
    public MatchedActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchedActivityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_searched_tag_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull MatchedActivityViewHolder holder, int position, @NonNull Activity model) {

        Activity searchedActivity = getItem(position);
        holder.bindTo(searchedActivity, listener);
    }

    static class MatchedActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView searchedTagName;

        MatchedActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            searchedTagName = itemView.findViewById(R.id.searchedTagTextView);
        }

        void bindTo(Activity matchedActivity, OnActivitySelectedListener listener) {
            searchedTagName.setText(matchedActivity.getActivityTitle());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onActivitySelected(matchedActivity);
                }
            });
        }
    }
}
