package com.diablo.jayson.kicksv1.UI.Search;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


    }

    private void doMySearch(String query) {
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

        }

        class SearchedTagViewHolder extends RecyclerView.ViewHolder {

            public SearchedTagViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
