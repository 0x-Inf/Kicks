package com.diablo.jayson.kicksv1.UI.Home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
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
public class FeaturedFragment extends Fragment {

    private static final String TAG = FeaturedFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private FeaturedFeedAdapter mAdapter;
    private List<ImageAndText> mImageAndTextData = new ArrayList<>();
    private ArrayList<ImageTextAndList> mImageTextAndListData = new ArrayList<>();
    private List<Kick> mKicksData = new ArrayList<>();
    private List<FeaturedKicks> mFeaturedKicksTotal = new ArrayList<>();
    private RelativeLayout mRelativeLayout;
    private ImageButton mSearchImageButton,mSettingsImageButton;


    public FeaturedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_featured, container, false);
        View root1 = inflater.inflate(R.layout.fragment_swipable_feed, container, false);
        loadFeaturedKicksFromFireBase();
        mRecyclerView = root.findViewById(R.id.featuredRecyclerView);
//        mRelativeLayout = root1.findViewById(R.id.searchAndProfileRelativeView);
//        mSearchImageButton = root1.findViewById(R.id.searchImageButton);
//        mSettingsImageButton = root1.findViewById(R.id.settingsImageButton);
        Query query = FirebaseFirestore.getInstance()
                .collection("featuredkicks");

        FirestoreRecyclerOptions<FeaturedKicks> options = new FirestoreRecyclerOptions.Builder<FeaturedKicks>()
                .setQuery(query, FeaturedKicks.class)
                .build();
        mAdapter = new FeaturedFeedAdapter(options);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

        return root;
    }

    private void initializeData() {
//        textAndImageData();
//        textImageAndListData();

        loadFeaturedKicksFromFireBase();
        mAdapter.notifyDataSetChanged();

    }

    private void textAndImageData() {

//        String[] kickCategories = getResources().getStringArray(R.array.featured_kicks_Titles);
//        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_images);
//        mImageAndTextData.clear();
//
//        for (int i = 0; i < kickCategories.length; i++) {
//            FeaturedKicks featuredKicks = new FeaturedKicks();
//            featuredKicks.setmFeaturedImageAndText(new ImageAndText(kickCategories[i], kickImages[i]));
//            mFeaturedKicksTotal.add(featuredKicks);
//
//        }
    }

    private void textImageAndListData() {


//        String[] kickCategories = getResources().getStringArray(R.array.featured_kicks_Titles);
//        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_images);
//        mImageTextAndListData.clear();
//
//        for (int i = 0; i < kickCategories.length; i++) {
//            mKicksData = new ArrayList<>();
//            initializeKickData();
//            FeaturedKicks featuredKicks = new FeaturedKicks();
//            featuredKicks.setmFeaturedImageTextAndList(new ImageTextAndList(mKicksData, kickImages[i], kickCategories[i]));
//            mFeaturedKicksTotal.add(featuredKicks);
//
//        }


    }

    private void initializeKickData() {
//        String[] kickNames = getResources().getStringArray(R.array.featured_kicks_list_names);
//        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_list_images);
//        mKicksData.clear();
//
//        for (int i = 0; i < kickNames.length; i++) {
//            mKicksData.add(new Kick(kickNames[i], kickImages[i]));
//        }

    }

    private void loadFeaturedKicksFromFireBase() {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("FeaturedKicks");
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    if (snap.child(Constants.IMAGE_AND_TEXT).exists()) {
//                        ImageAndText imageAndText = snap.child(Constants.IMAGE_AND_TEXT).getValue(ImageAndText.class);
//                        FeaturedKicks featuredKicks1 = new FeaturedKicks();
//                        featuredKicks1.setFeaturedType(Constants.IMAGE_AND_TEXT);
//                        featuredKicks1.setmFeaturedImageAndText(imageAndText);
////                    mImageAndTextData.add(imageAndText);
//                        mFeaturedKicksTotal.add(featuredKicks1);
//                        int totalFeatured = mFeaturedKicksTotal.size();
//                        int totalImageAndText = mImageAndTextData.size();
//                        Log.e(TAG, Integer.toString(totalFeatured));
//                        Log.e(TAG, Integer.toString(totalImageAndText));
//                    } else if (snap.child(Constants.IMAGE_TEXT_AND_LIST).exists()) {
//                        ImageTextAndList imageTextAndList = snap.child(Constants.IMAGE_TEXT_AND_LIST).getValue(ImageTextAndList.class);
//                        FeaturedKicks featuredKicks = new FeaturedKicks();
//                        featuredKicks.setFeaturedType(Constants.IMAGE_TEXT_AND_LIST);
//                        featuredKicks.setmFeaturedImageTextAndList(imageTextAndList);
//                        mFeaturedKicksTotal.add(featuredKicks);
//                    }
//                }
//                initializeRecyclerViewWithKicks();
//                dissappearItemsOnScroll();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "load featuredKicks:Cancelled", databaseError.toException());
//            }
//        });


    }

    private void initializeRecyclerViewWithKicks() {
//        int gridColumnCount = 1;
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//        mAdapter = new FeaturedFeedAdapter(getContext(), mFeaturedKicksTotal);
////        Toast.makeText(getContext(),Integer.toString(mFeaturedKicksTotal.size()),Toast.LENGTH_LONG).show();
//        mRecyclerView.setAdapter(mAdapter);
    }

    private void dissappearItemsOnScroll(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                mRelativeLayout.setVisibility(View.INVISIBLE);
//                mSearchImageButton.setVisibility(View.INVISIBLE);
//                mSettingsImageButton.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mRelativeLayout.setVisibility(View.INVISIBLE);
//                mSearchImageButton.setVisibility(View.INVISIBLE);
//                mSettingsImageButton.setVisibility(View.INVISIBLE);
//            }
        });
    }


}
