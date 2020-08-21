package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Adapters.TagListAdapter;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityTagData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityTagFragment extends Fragment implements TagListAdapter.OnTagSelectedListener {

    private static final String TAG = AddActivityTagFragment.class.getSimpleName();

    //Tag Stuff
    private RelativeLayout addActivityTagRelativeLayout, selectedTagOverlayRelativeLayout;
    private EditText searchTagsEditText;
    private RecyclerView tagsRecyclerView;
    private CardView selectedTagCard;
    private TextView selectedTagTextView, selectedTagDescriptionTextView;
    private ImageView selectedTagImageView, closeTagRelativeLayoutIcon;
    private FloatingActionButton tagSelectionDoneButton;
    private ArrayList<Tag> allTags;
    private AddActivityTagFragment listener;
    private Tag activityTag;
    private AddActivityTagData activityTagData;

    public AddActivityTagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_activity_tag, container, false);

        //Tag Views
        addActivityTagRelativeLayout = root.findViewById(R.id.add_activity_tag_relative_layout);
        selectedTagOverlayRelativeLayout = root.findViewById(R.id.selectedTagOverlayRelativelayout);
        searchTagsEditText = root.findViewById(R.id.searchTagsEditText);
        tagsRecyclerView = root.findViewById(R.id.tags_recycler_view);
        selectedTagCard = root.findViewById(R.id.selectedTagCard);
        selectedTagTextView = root.findViewById(R.id.selectedTagTextView);
        selectedTagDescriptionTextView = root.findViewById(R.id.selectedTagDescriptionTextView);
        selectedTagImageView = root.findViewById(R.id.selected_tag_image_view);
        tagSelectionDoneButton = root.findViewById(R.id.tagSelectionDoneButton);
        closeTagRelativeLayoutIcon = root.findViewById(R.id.closeTagRelativelayoutButton);
        //Tag Data
        allTags = new ArrayList<Tag>();
        loadTagsFromDb();



        //Tag Implemetation
        activityTag = new Tag();
        activityTagData = new AddActivityTagData();
        searchTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tagName = s.toString();
                if (tagName.isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("tags")
                            .whereEqualTo("tagName", "")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                            Log.e(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                            allTags.add(new Tag(documentSnapshot.toObject(Tag.class).getTagName(),
                                                    documentSnapshot.toObject(Tag.class).getTagShortDescription(),
                                                    documentSnapshot.toObject(Tag.class).getTagLocation(),
                                                    documentSnapshot.toObject(Tag.class).getTagCost(),
                                                    documentSnapshot.toObject(Tag.class).getTagIconUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagLocationName()));
                                            for (int i = 0; i < allTags.size(); i++) {
                                                Log.w(TAG, allTags.get(i).getTagName());
                                            }
                                        }
                                        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, listener);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
                                        tagsRecyclerView.setLayoutManager(gridLayoutManager);
                                        tagsRecyclerView.setAdapter(mAdapter);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    ArrayList<Tag> filteredTags = new ArrayList<Tag>();
                    for (Tag tag : allTags) {
                        if (tag.getTagName().toLowerCase(Locale.ROOT).contains(s)) {
                            filteredTags.add(new Tag(tag.getTagName(), tag.getTagShortDescription(), tag.getTagLocation(), tag.getTagCost(),
                                    tag.getTagIconUrl(), tag.getTagImageLargeUrl(), tag.getTagLocationName()));
                        }
                    }
                    TagListAdapter mAdapter = new TagListAdapter(getContext(), filteredTags, listener);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
                    tagsRecyclerView.setLayoutManager(gridLayoutManager);
                    tagsRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tagSelectionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTagData.setActivityTag(activityTag);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddActivityMain = AddActivityTagFragmentDirections.actionAddActivityTagFragmentToNavigationAddKick(activityTagData);
                navController.navigate(actionAddActivityMain);
//                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                addActivityTagRelativeLayout.setVisibility(View.GONE);
                selectedTagOverlayRelativeLayout.setVisibility(View.GONE);
                selectedTagCard.setVisibility(View.GONE);
            }
        });
//        closeTagRelativeLayoutIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityTagRelativeLayout.setVisibility(View.GONE);
//            }
//        });
        selectedTagOverlayRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTagOverlayRelativeLayout.setVisibility(View.GONE);
                selectedTagCard.setVisibility(View.GONE);
            }
        });
        return root;
    }

    private void loadTagsFromDb() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tags")
                .whereGreaterThan("tagName", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.e(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                allTags.add(new Tag(documentSnapshot.toObject(Tag.class).getTagName(),
                                        documentSnapshot.toObject(Tag.class).getTagShortDescription(),
                                        documentSnapshot.toObject(Tag.class).getTagLocation(),
                                        documentSnapshot.toObject(Tag.class).getTagCost(),
                                        documentSnapshot.toObject(Tag.class).getTagIconUrl(),
                                        documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(),
                                        documentSnapshot.toObject(Tag.class).getTagLocationName()));
                                for (int i = 0; i < allTags.size(); i++) {
                                    Log.w(TAG, allTags.get(i).getTagName());
                                }
                            }
                            initializeRecyclerViewWithTags();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void initializeRecyclerViewWithTags() {
        listener = this;
        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, this::onTagListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        tagsRecyclerView.setLayoutManager(gridLayoutManager);
        tagsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onTagListener(Tag tag) {
        activityTag = tag;
        selectedTagOverlayRelativeLayout.setVisibility(View.VISIBLE);
        selectedTagCard.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(tag.getTagImageLargeUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(selectedTagImageView);
        selectedTagDescriptionTextView.setText(tag.getTagName());
    }
}
