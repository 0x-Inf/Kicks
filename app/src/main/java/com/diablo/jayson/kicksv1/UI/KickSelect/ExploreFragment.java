package com.diablo.jayson.kicksv1.UI.KickSelect;

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

import com.diablo.jayson.kicksv1.Adapters.KickCategoryListAdapter;
import com.diablo.jayson.kicksv1.Adapters.KickListAdapter;
import com.diablo.jayson.kicksv1.Adapters.KicksAdapter;
import com.diablo.jayson.kicksv1.Models.ExploreCategory;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.databinding.FragmentExploreBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements
        KicksAdapter.OnKickSelectedListener, ExploreCategoryListAdapter.OnSeeAllSelectedListener {

    private FragmentExploreBinding binding;
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mKickRecyclerView;
    private KickCategoryListAdapter mCatAdapter;
    private ArrayList<KickCategory> mKickCategoryData;
    private KickListAdapter mListAdapter;
    private ExploreCategoryListAdapter exploreCategoryListAdapter;

    private NavController navController;
    private ExploreViewModel exploreViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        mKickRecyclerView = root.findViewById(R.id.kicksRecyclerView);
        mCategoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("activities").document();
        Query query = FirebaseFirestore.getInstance()
                .collection("kickselects")
                .document("groupa")
                .collection("kickcategories")
                .orderBy("categoryName", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<KickCategory> options = new FirestoreRecyclerOptions.Builder<KickCategory>()
                .setQuery(query, KickCategory.class)
                .setLifecycleOwner(this)
                .build();

        int gridColumnCount = 1;
//        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//        mCatAdapter = new KickCategoryListAdapter(options, this, this);
//        mCategoryRecyclerView.setAdapter(mCatAdapter);
//        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));


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
        exploreViewModel = new ViewModelProvider(requireActivity()).get(ExploreViewModel.class);
        exploreViewModel.getExploreCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ExploreCategory>>() {
            @Override
            public void onChanged(ArrayList<ExploreCategory> exploreCategories) {
                exploreCategoryListAdapter = new ExploreCategoryListAdapter(exploreViewModel, getViewLifecycleOwner()
                        , exploreCategories, ExploreFragment.this::onKickSelected, ExploreFragment.this::onSeeAllSelected);
                binding.categoryRecyclerView.setAdapter(exploreCategoryListAdapter);

            }
        });
    }

//    @Override
//    public void onSeeAllClicked(KickCategory kickCategory) {
////        Toast.makeText(getContext(), kickCategory.getCategoryId(), Toast.LENGTH_LONG).show();
//
////        Intent seeAllActivity = new Intent(getContext(), KickSeeAllActivity.class);
////        seeAllActivity.putExtra("categoryId", kickCategory.getCategoryId());
////        seeAllActivity.putExtra("categoryName", kickCategory.getCategoryName());
////        startActivity(seeAllActivity);
//
//        NavDirections actionKickSeeAll = ExploreFragmentDirections.actionNavigationKickSelectToKicksSeeAllFragment();
//        navController.navigate(actionKickSeeAll);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("kickselects").document("groupa")
//                .collection("kickcategories").document(kickCategory.getCategoryId()).collection("kicksincategory")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
//                                Log.e("kicks", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                            }
//                        }
//                    }
//                });
//
//    }

    @Override
    public void onKickSelected(Kick kick) {
//        Toast.makeText(getContext(), kick.getKickName(), Toast.LENGTH_SHORT).show();
//        Intent kickSelectedActivity = new Intent(getContext(), KickSelectedActivity.class);
//        kickSelectedActivity.putExtra("kick", kick);
//        startActivity(kickSelectedActivity);

        NavDirections actionKickSelectMainFragment = ExploreFragmentDirections.actionNavigationKickSelectToKickSelectedMainFragment(kick);
        navController.navigate(actionKickSelectMainFragment);
    }

    @Override
    public void onSeeAllSelected(ExploreCategory exploreCategory) {

    }
}
