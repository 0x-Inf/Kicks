package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.TagListAdapter;
import com.diablo.jayson.kicksv1.Adapters.TagSelectedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKick1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKick1Fragment extends Fragment implements TagListAdapter.OnTagSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddKick1Fragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddKickViewModel viewModel;
    private RecyclerView mTagsRecyclerView;
    private RecyclerView mSelectedTagsRecyclerView;
    private TextInputEditText mTimePickerInput, mDatePickerInput, mLocationTextInput, mActivityTitleInput, mTagsTextInput;
    private MultiAutoCompleteTextView mTagsAutoCompleteInput;
    private TextInputEditText mMaxRequiredInput, mMinRequiredInput;
    private Activity activityMain;
    private String tagName;
    private FirestoreRecyclerOptions options;
    private ArrayList<Tag> allTags;
    private ArrayList<Tag> selectedTags;
    private AddKick1Fragment listener;
    private LinearLayout tagSelectionPart;
    private LinearLayout tagsSelectedPart;
    private ArrayList<AttendingUser> mAttendees;

    public AddKick1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKick2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKick1Fragment newInstance(String param1, String param2) {
        AddKick1Fragment fragment = new AddKick1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        final String message = "after";
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
        options = loadOptions();
//        allTags = loadAllTags();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_kick1, container, false);
        mActivityTitleInput = root.findViewById(R.id.kickNameEditText);
        mTagsTextInput = root.findViewById(R.id.TagsEditText);
        mTagsRecyclerView = root.findViewById(R.id.app_tags_RecyclerView);
        mSelectedTagsRecyclerView = root.findViewById(R.id.picked_tags_RecyclerView);
        tagSelectionPart = root.findViewById(R.id.tagsSection);
        tagsSelectedPart = root.findViewById(R.id.tagsSelectedSection);
        tagsSelectedPart.setVisibility(View.INVISIBLE);
        allTags = new ArrayList<Tag>();
        selectedTags = new ArrayList<Tag>();
        loadTagsFromDb();
        tagName = Objects.requireNonNull(mTagsTextInput.getText()).toString();
        mTagsTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tagsSelectedPart.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagSelectionPart.setVisibility(View.VISIBLE);
                tagsSelectedPart.setVisibility(View.INVISIBLE);
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
                                                    documentSnapshot.toObject(Tag.class).getTagLocation(),
                                                    documentSnapshot.toObject(Tag.class).getTagOptimalMinPeople(),
                                                    documentSnapshot.toObject(Tag.class).getTagOptimalMaxPeople(),
                                                    documentSnapshot.toObject(Tag.class).getTagCost(),
                                                    documentSnapshot.toObject(Tag.class).getTagIconUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagOptimalStartTime(),
                                                    documentSnapshot.toObject(Tag.class).getTagLocationName()));
                                            for (int i = 0; i < allTags.size(); i++) {
                                                Log.w(TAG, allTags.get(i).getTagName());
                                            }
                                        }
                                        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, listener);
                                        mTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        mTagsRecyclerView.setAdapter(mAdapter);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    ArrayList<Tag> filteredTags = new ArrayList<Tag>();
                    for (Tag tag : allTags) {
                        if (tag.getTagName().toLowerCase(Locale.ROOT).contains(s)) {
                            filteredTags.add(new Tag(tag.getTagName(), tag.getTagLocation(), tag.getTagOptimalMinPeople(), tag.getTagOptimalMaxPeople(), tag.getTagCost(),
                                    tag.getTagIconUrl(), tag.getTagImageLargeUrl(), tag.getTagOptimalStartTime(), tag.getTagLocationName()));
                        }
                    }
                    TagListAdapter mAdapter = new TagListAdapter(getContext(), filteredTags, listener);
                    mTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mTagsRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tagsSelectedPart.setVisibility(View.INVISIBLE);
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
                                allTags.add(new Tag(documentSnapshot.toObject(Tag.class).getTagName(), documentSnapshot.toObject(Tag.class).getTagLocation(), documentSnapshot.toObject(Tag.class).getTagOptimalMinPeople(), documentSnapshot.toObject(Tag.class).getTagOptimalMaxPeople(),
                                        documentSnapshot.toObject(Tag.class).getTagCost(), documentSnapshot.toObject(Tag.class).getTagIconUrl(), documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(), documentSnapshot.toObject(Tag.class).getTagOptimalStartTime(), documentSnapshot.toObject(Tag.class).getTagLocationName()));
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

    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        LinearLayout firstContent = view.findViewById(R.id.firstPartContent);
        Activity activityUploaded = new Activity();
//        final String[] tagName = new String[1];
        final FloatingActionButton[] nextButton = {view.findViewById(R.id.nextCreateActivityFab)};

        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
            }
        });

        nextButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTagsTextInput.getText().toString().isEmpty() || selectedTags.isEmpty()) {
                    mTagsTextInput.setError("Please Pick A Tag");
                } else if (mActivityTitleInput.getText().toString().isEmpty()) {
                    mActivityTitleInput.setError("Please Input a Title");
                } else {
                    updateActivityModel();
//                viewModel.setActivity1(activityMain);
//                    AddKick2Fragment nextFrag = new AddKick2Fragment();
//                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.framelayoutbase, nextFrag, "findThisFragment")
//                            .addToBackStack(null)
//                            .commit();
                    firstContent.setVisibility(View.INVISIBLE);
                    nextButton[0].setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateActivityModel() {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAttendees = new ArrayList<AttendingUser>();

        String activityTitle = Objects.requireNonNull(mActivityTitleInput.getText()).toString();
        String tags = mTagsTextInput.getText().toString();
        String[] tagsList = tags.split(",", -2);
//            String[] tagList = {};
        String tag = Arrays.toString(tagsList);

        tag = tag.replace("[", "");
        tag = tag.replace("]", "");

        String tagArray[] = tag.split(",");

        List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));
        GeoPoint locationCod = new GeoPoint(22, 12);

        if (selectedTags.isEmpty()) {
            activityMain = new Activity();
            mTagsTextInput.setError("Please Pick a tag");
        } else {
            activityMain = new Activity();
        }
        db.collection("users")
                .whereEqualTo("userEmail", activityMain.getActivityUploaderId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        AttendingUser user = documentSnapshot.toObject(AttendingUser.class);
                        activityMain.setActivityUploaderId(user.getFirstName());
                    }
                }
            }
        });
        viewModel.setActivity1(activityMain);
        Log.e(TAG, activityMain.getActivityTitle());
    }

    public void updateActivity() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }

    public FirestoreRecyclerOptions loadOptions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("tags")
                .whereGreaterThan("tagName", "");

        return new FirestoreRecyclerOptions.Builder<Tag>()
                .setQuery(query, Tag.class)
                .build();
    }

    public ArrayList<Tag> loadAllTags() {
        ArrayList<Tag> allTags = new ArrayList<Tag>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tags")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.e(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                allTags.add(documentSnapshot.toObject(Tag.class));
                                initializeRecyclerViewWithTags();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return allTags;
    }

    private void initializeRecyclerViewWithTags() {
        listener = this;
        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, listener);
        mTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTagsRecyclerView.setAdapter(mAdapter);
    }

    private void initializeRecyclerViewWithSelectedTags() {
        TagSelectedListAdapter mAdapter = new TagSelectedListAdapter(getContext(), selectedTags);
        mSelectedTagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSelectedTagsRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onTagListener(Tag tag) {
        Toast.makeText(getContext(), tag.getTagName(), Toast.LENGTH_SHORT).show();
        mTagsTextInput.setText(tag.getTagName() + ",");
        tagSelectionPart.setVisibility(View.INVISIBLE);
        selectedTags.clear();
        selectedTags.add(tag);
        initializeRecyclerViewWithSelectedTags();
        tagsSelectedPart.setVisibility(View.VISIBLE);
    }
}
