package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.ActivityTagsListAdapter;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityAllTagsListAdapter;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityTagData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityTagBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityTagFragment extends Fragment implements AddActivityAllTagsListAdapter.OnTagSelectedListener,
        ActivityTagsListAdapter.OnActivityTagSelectedListener {

    private static final String TAG = AddActivityTagFragment.class.getSimpleName();

    private FragmentAddActivityTagBinding binding;
    private AddActivityViewModel addActivityViewModel;

    private ActivityTagsListAdapter activityTagsListAdapter;
    private NavController navController;

    private ArrayList<Tag> allTags;
    private ArrayList<Tag> activityTags = new ArrayList<>();
    private ArrayList<Tag> newTags = new ArrayList<>();
    private AddActivityTagFragment listener;
    private AddActivityTagData activityTagData;


    public AddActivityTagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityTagBinding.inflate(inflater, container, false);
        listener = this;

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        //Tag Implementation
        binding.searchTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence typedTagName, int i, int i1, int i2) {
                String searchedTagName = typedTagName.toString();
                ArrayList<Tag> filteredTags = new ArrayList<Tag>();
                if (!searchedTagName.isEmpty()) {
                    for (Tag tag : allTags) {
                        if (tag.getTagName().toLowerCase(Locale.ROOT).contains(searchedTagName)) {
                            filteredTags.add(tag);
                            binding.createTextTextView.setVisibility(View.GONE);
                        }
                    }
                    if (filteredTags.isEmpty()) {
                        binding.createTextTextView.setVisibility(View.VISIBLE);
                    }
                    AddActivityAllTagsListAdapter filteredTagListAdapter = new AddActivityAllTagsListAdapter(filteredTags, listener);
                    binding.allTagsRecycler.setAdapter(filteredTagListAdapter);
                } else {
                    AddActivityAllTagsListAdapter allTagsListAdapter = new AddActivityAllTagsListAdapter(allTags, listener);
                    binding.allTagsRecycler.setAdapter(allTagsListAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddActivityViewModel();
            }
        });
        binding.createTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTagInViewModel();
            }
        });
        return binding.getRoot();
    }

    private void createNewTagInViewModel() {
        String newTagName = binding.searchTagsEditText.getText().toString();
        Tag newTag = new Tag(newTagName, "");
        addActivityViewModel.updateNewTagsMutableLiveData(newTag);
        addNewTagToActivityTags(newTag);
    }

    private void addNewTagToActivityTags(Tag newTag) {
        newTags.add(newTag);
        activityTags.add(newTag);
        binding.searchTagsEditText.setText("");
        activityTagsListAdapter.notifyDataSetChanged();
    }

    private void updateAddActivityViewModel() {
        if (!activityTags.isEmpty()) {
            addActivityViewModel.updateActivityTags(activityTags);
            navigateToNextFragment();
        } else {
            navController.popBackStack();
        }

    }

    private void navigateToNextFragment() {
        //TODO: Make the smart navigation for add Activity

        NavDirections actionAddActivityMain = AddActivityTagFragmentDirections.actionAddActivityTagFragmentToNavigationAddKick();
        navController.navigate(actionAddActivityMain);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);

        addActivityViewModel.getAllTagsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                allTags = tags;
                initializeRecyclerViewWithTags();
            }
        });

        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null) {
                    if (activity.getActivityTags() != null) {
                        activityTags = activity.getActivityTags();
                    }
                }
                initializeActivityTagsRecycler();
            }
        });
    }

    private void initializeActivityTagsRecycler() {
        activityTagsListAdapter = new ActivityTagsListAdapter(activityTags, this);
        binding.activityTagsRecycler.setAdapter(activityTagsListAdapter);
        binding.activityTagsRecycler.addItemDecoration(new HorizontalGridSpacingItemDecoration(10));
    }

    private void initializeRecyclerViewWithTags() {
        AddActivityAllTagsListAdapter tagListAdapter = new AddActivityAllTagsListAdapter(allTags, this);
        binding.allTagsRecycler.setAdapter(tagListAdapter);
        int spanCount = 3; // 3 columns
        int spacing = 20; //20px
        binding.allTagsRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
    }

    @Override
    public void onTagSelected(Tag tag) {
        if (!activityTags.contains(tag)) {
            activityTags.add(tag);
        } else {
            activityTags.remove(tag);
        }
        activityTagsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityTagSelected(Tag activityTag) {
        if (newTags.contains(activityTag)) {
            addActivityViewModel.removeNewTagFromMutableLiveData(activityTag);
        }
        activityTags.remove(activityTag);
        activityTagsListAdapter.notifyDataSetChanged();
    }

    static class HorizontalGridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spacing;

        public HorizontalGridSpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = spacing;
        }
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
