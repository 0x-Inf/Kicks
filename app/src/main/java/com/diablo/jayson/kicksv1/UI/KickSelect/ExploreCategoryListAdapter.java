package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.KicksAdapter;
import com.diablo.jayson.kicksv1.Models.ExploreCategory;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreCategoryListAdapter extends RecyclerView.Adapter<ExploreCategoryListAdapter.ExploreCategoryViewHolder> {


    private ExploreViewModel exploreViewModel;
    private LifecycleOwner lifecycleOwner;
    private ArrayList<ExploreCategory> exploreCategoriesData;
    private ArrayList<Kick> kicksData;
    private KicksAdapter.OnKickSelectedListener kickSelectedListener;
    private OnSeeAllSelectedListener onSeeAllSelectedListener;


    public ExploreCategoryListAdapter(ExploreViewModel exploreViewModel, LifecycleOwner lifecycleOwner,
                                      ArrayList<ExploreCategory> exploreCategoriesData,
                                      KicksAdapter.OnKickSelectedListener kickSelectedListener,
                                      OnSeeAllSelectedListener onSeeAllSelectedListener) {
        this.exploreViewModel = exploreViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.exploreCategoriesData = exploreCategoriesData;
        this.kickSelectedListener = kickSelectedListener;
        this.onSeeAllSelectedListener = onSeeAllSelectedListener;
    }

    @NonNull
    @Override
    public ExploreCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_explore_category_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreCategoryViewHolder holder, int position) {
        ExploreCategory exploreCategory = exploreCategoriesData.get(position);
        holder.bindTo(exploreCategory);
        getKicksDataFromViewModel(exploreCategory.getExploreCategoryId());
        KicksAdapter kicksAdapter = new KicksAdapter(holder.itemView.getContext(), kicksData, kickSelectedListener);
        holder.kicksRecycler.setAdapter(kicksAdapter);

    }

    private void getKicksDataFromViewModel(String exploreCategoryId) {
        exploreViewModel.exploreKicksHashMapMutableLiveData.observe(lifecycleOwner, new Observer<HashMap<String, ArrayList<Kick>>>() {
            @Override
            public void onChanged(HashMap<String, ArrayList<Kick>> stringArrayListHashMap) {
                kicksData = new ArrayList<>();
                kicksData = stringArrayListHashMap.get(exploreCategoryId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exploreCategoriesData.size();
    }

    public interface OnSeeAllSelectedListener {
        void onSeeAllSelected(ExploreCategory exploreCategory);
    }

    static class ExploreCategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView exploreCategoryTitleTextView, seeAllTextView;
        private RecyclerView kicksRecycler;

        public ExploreCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            exploreCategoryTitleTextView = itemView.findViewById(R.id.exploreCategoryTitleTextView);
            seeAllTextView = itemView.findViewById(R.id.seeAllTextTextView);
            kicksRecycler = itemView.findViewById(R.id.kicksRecycler);
        }

        void bindTo(ExploreCategory exploreCategory) {
            exploreCategoryTitleTextView.setText(exploreCategory.getExploreCategoryName());
        }
    }


}
