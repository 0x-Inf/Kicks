package com.diablo.jayson.kicksv1.UI.Home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.FeaturedFeedAdapter;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.ImageAndText;
import com.diablo.jayson.kicksv1.Models.ImageTextAndList;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedFragment extends Fragment implements
        FeaturedFeedAdapter.OnFeaturedImageTextListSelected,
        FeaturedFeedAdapter.OnFeaturedImageTextSelected {

    private static final String TAG = FeaturedFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private FeaturedFeedAdapter mAdapter;
    private List<ImageAndText> mImageAndTextData = new ArrayList<>();
    private ArrayList<ImageTextAndList> mImageTextAndListData = new ArrayList<>();
    private List<Kick> mKicksData = new ArrayList<>();
    private List<FeaturedKicks> mFeaturedKicksTotal = new ArrayList<>();
    private RelativeLayout mRelativeLayout;
    private ImageButton mSearchImageButton,mSettingsImageButton;
    private FeaturedViewModel featuredViewModel;


    public FeaturedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuredViewModel = new ViewModelProvider(requireActivity()).get(FeaturedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_featured, container, false);
        View root1 = inflater.inflate(R.layout.fragment_swipable_feed, container, false);
        mRecyclerView = root.findViewById(R.id.featuredRecyclerView);


//        mRelativeLayout = root1.findViewById(R.id.searchAndProfileRelativeView);
//        mSearchImageButton = root1.findViewById(R.id.searchImageButton);
//        mSettingsImageButton = root1.findViewById(R.id.settingsImageButton);
        Query query = FirebaseFirestore.getInstance()
                .collection("featuredkicks");

        FirestoreRecyclerOptions<FeaturedKicks> options = new FirestoreRecyclerOptions.Builder<FeaturedKicks>()
                .setQuery(query, FeaturedKicks.class)
                .build();
        mAdapter = new FeaturedFeedAdapter(options, this::onFeaturedImageTextSelected, this::onFeaturedImageTextListSelected);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

        return root;
    }

    @Override
    public void onFeaturedImageTextSelected(FeaturedKicks featuredKick) {

//        Intent imageAndTextOnlyIntent = new Intent(getContext(), ImageAndTextOnlyActivity.class);
//        startActivity(imageAndTextOnlyIntent);

        ImageAndTextDialogFragment imageAndTextDialogFragment = new ImageAndTextDialogFragment();
        imageAndTextDialogFragment.show(getParentFragmentManager(), "Custom dialog fragment");
        featuredViewModel.setFeaturedId(featuredKick.getFeaturedKickId().trim());

    }

    @Override
    public void onFeaturedImageTextListSelected(FeaturedKicks featuredKick) {

        ImageTextAndListDialogFragment imageTextAndListDialogFragment = new ImageTextAndListDialogFragment();
        imageTextAndListDialogFragment.show(getParentFragmentManager(), "Custom dialog fragment");
        featuredViewModel.setFeaturedId(featuredKick.getFeaturedKickId().trim());

//        Intent imageTextAndListIntent = new Intent(getContext(), ImageTextAndListActivity.class);
//        startActivity(imageTextAndListIntent);

    }
}
