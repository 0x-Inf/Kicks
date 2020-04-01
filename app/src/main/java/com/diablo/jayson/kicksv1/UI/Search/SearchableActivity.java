package com.diablo.jayson.kicksv1.UI.Search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class SearchableActivity extends AppCompatActivity {
    private RecyclerView searchedTagsRecycler;
    private RecyclerView searchedActivitiesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchedTagsRecycler = findViewById(R.id.searchedTagRecycler);
        searchedActivitiesRecycler = findViewById(R.id.searchedActivityRecycler);




        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


    }

    private void doMySearch(String query) {

        String smallQuery = query.toLowerCase();


        Query fsQuery = FirebaseFirestore.getInstance()
                .collection("tags")
                .whereGreaterThanOrEqualTo("searchablename", smallQuery);


        FirestoreRecyclerOptions<Tag> options = new FirestoreRecyclerOptions.Builder<Tag>()
                .setQuery(fsQuery, Tag.class)
                .build();

        TagSearchListAdapter tagsAdapter = new TagSearchListAdapter(options);
        searchedTagsRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchedTagsRecycler.setAdapter(tagsAdapter);


        Query activitiesQuery = FirebaseFirestore.getInstance()
                .collection("activities")
                .whereArrayContainsAny("tags", Arrays.asList(query));


        FirestoreRecyclerOptions<Activity> activitiesOptions = new FirestoreRecyclerOptions.Builder<Activity>()
                .setQuery(activitiesQuery, Activity.class)
                .build();

        ActivitiesSearchListAdapter activitiesSearchListAdapter = new ActivitiesSearchListAdapter(activitiesOptions);
        searchedActivitiesRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchedActivitiesRecycler.setAdapter(activitiesSearchListAdapter);

        activitiesSearchListAdapter.startListening();
        tagsAdapter.startListening();


    }


    public class TagSearchListAdapter extends FirestoreRecyclerAdapter<Tag, TagSearchListAdapter.SearchedTagViewHolder> {

        /**
         * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
         * FirestoreRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public TagSearchListAdapter(@NonNull FirestoreRecyclerOptions options) {
            super(options);
        }


        @NonNull
        @Override
        public SearchedTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SearchedTagViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searched_tag_item, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull SearchedTagViewHolder holder, int position, @NonNull Tag model) {
            Tag searchedTag = getItem(position);
            holder.bindTo(searchedTag);
        }

        class SearchedTagViewHolder extends RecyclerView.ViewHolder {
            private TextView searchedTagName;

            public SearchedTagViewHolder(@NonNull View itemView) {
                super(itemView);

                searchedTagName = itemView.findViewById(R.id.searchedTagTextView);
            }

            void bindTo(Tag searchedTag) {
                searchedTagName.setText(searchedTag.getTagName());
            }
        }
    }

    public class ActivitiesSearchListAdapter extends FirestoreRecyclerAdapter<Activity, ActivitiesSearchListAdapter.SearchedActivityViewHolder> {

        /**
         * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
         * FirestoreRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public ActivitiesSearchListAdapter(@NonNull FirestoreRecyclerOptions options) {
            super(options);
        }


        @NonNull
        @Override
        public SearchedActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SearchedActivityViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.searched_tag_item, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull SearchedActivityViewHolder holder, int position, @NonNull Activity model) {

            Activity searchedActivity = getItem(position);
            holder.bindTo(searchedActivity);
        }

        class SearchedActivityViewHolder extends RecyclerView.ViewHolder {
            private TextView searchedTagName;

            public SearchedActivityViewHolder(@NonNull View itemView) {
                super(itemView);

                searchedTagName = itemView.findViewById(R.id.searchedTagTextView);
            }

            void bindTo(Activity searchedActivity) {
                searchedTagName.setText(searchedActivity.getKickTitle());
            }
        }
    }
}
