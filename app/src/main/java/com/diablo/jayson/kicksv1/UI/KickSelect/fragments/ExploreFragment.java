package com.diablo.jayson.kicksv1.UI.KickSelect.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.KicksAdapter;
import com.diablo.jayson.kicksv1.Models.ExploreCategory;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.KickSelect.ExploreCategoryListAdapter;
import com.diablo.jayson.kicksv1.UI.KickSelect.ExploreTagsAdapter;
import com.diablo.jayson.kicksv1.UI.KickSelect.ExploreViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentExploreBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements
        KicksAdapter.OnKickSelectedListener, ExploreCategoryListAdapter.OnSeeAllSelectedListener, ExploreTagsAdapter.OnExploreTagSelectedListener {

    private FragmentExploreBinding binding;
    private ExploreFragment listener;

    private ExploreTagsAdapter exploreTagsAdapter;
    private ArrayList<Tag> tagsArrayList;

    private NavController navController;
    private ExploreViewModel exploreViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        exploreViewModel = new ViewModelProvider(requireActivity()).get(ExploreViewModel.class);

        return binding.getRoot();
    }


    @Override
    public void onStop() {
        super.onStop();
//        mCatAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
//        mCatAdapter.startListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = this;
        exploreViewModel.getExploreCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ExploreCategory>>() {
            @Override
            public void onChanged(ArrayList<ExploreCategory> exploreCategories) {
//                exploreCategoryListAdapter = new ExploreCategoryListAdapter(exploreViewModel, getViewLifecycleOwner()
//                        , exploreCategories, ExploreFragment.this::onKickSelected, ExploreFragment.this::onSeeAllSelected);
//                binding.categoryRecyclerView.setAdapter(exploreCategoryListAdapter);

            }
        });

        exploreViewModel.getTagsArrayMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                tagsArrayList = tags;
                exploreTagsAdapter = new ExploreTagsAdapter(tagsArrayList, listener);
                binding.tagsRecyclerView.setAdapter(exploreTagsAdapter);
                binding.tagsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 10, true));
            }
        });

    }

    @Override
    public void onSeeAllSelected(ExploreCategory exploreCategory) {

    }

    @Override
    public void onKickSelected(Kick kick) {

    }

    @Override
    public void onTagSelected(Tag selectedTag) {

        NavDirections actionTagExplore = ExploreFragmentDirections.actionNavigationKickSelectToExploreTagFragment(selectedTag.getTagId());
        navController.navigate(actionTagExplore);

    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NotNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//                (column + 1) * spacing / spanCount

                if (position < spanCount) { // top edge
                    outRect.top = 5;
                }
                outRect.bottom = 10; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
