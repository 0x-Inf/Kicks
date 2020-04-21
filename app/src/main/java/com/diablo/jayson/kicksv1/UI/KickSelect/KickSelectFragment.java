package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.KickCategoryListAdapter;
import com.diablo.jayson.kicksv1.Adapters.KickListAdapter;
import com.diablo.jayson.kicksv1.Adapters.KicksAdapter;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class KickSelectFragment extends Fragment implements KickCategoryListAdapter.OnSeeAllClickedListener, KicksAdapter.OnKickSelectedListener {

    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mKickRecyclerView;
    private KickCategoryListAdapter mCatAdapter;
    private ArrayList<KickCategory> mKickCategoryData;
    private KickListAdapter mListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kick_select, container, false);

        mKickRecyclerView = root.findViewById(R.id.kicksRecyclerView);
//        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));



        return root;
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
        mCategoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
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
        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mCatAdapter = new KickCategoryListAdapter(options, this, this);
        mCategoryRecyclerView.setAdapter(mCatAdapter);

    }

    @Override
    public void onSeeAllClicked(KickCategory kickCategory) {
//        Toast.makeText(getContext(), kickCategory.getCategoryId(), Toast.LENGTH_LONG).show();

        Intent seeAllActivity = new Intent(getContext(), KickSeeAllActivity.class);
        seeAllActivity.putExtra("categoryId", kickCategory.getCategoryId());
        seeAllActivity.putExtra("categoryName", kickCategory.getCategoryName());
        startActivity(seeAllActivity);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("kickselects").document("groupa")
                .collection("kickcategories").document(kickCategory.getCategoryId()).collection("kicksincategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.e("kicks", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            }
                        }
                    }
                });

    }

    @Override
    public void onkickSelected(Kick kick) {
        Toast.makeText(getContext(), kick.getKickName(), Toast.LENGTH_SHORT).show();
        Intent kickSelectedActivity = new Intent(getContext(), KickSelectedActivity.class);
        kickSelectedActivity.putExtra("kick", kick);
        startActivity(kickSelectedActivity);
    }
}
