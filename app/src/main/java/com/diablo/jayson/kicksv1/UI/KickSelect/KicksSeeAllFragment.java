package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.KickListAdapter;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KicksSeeAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KicksSeeAllFragment extends Fragment implements KickListAdapter.OnKickSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private KickSelectViewModel viewModel;
    private KickListAdapter adapter;

    private RecyclerView kickRecyclerView;
    private TextView categoryNameView;
    private String categoryId;
    private Toolbar myToolbar;

    public KicksSeeAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KicksSeeAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KicksSeeAllFragment newInstance(String param1, String param2) {
        KicksSeeAllFragment fragment = new KicksSeeAllFragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(KickSelectViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_kicks_see_all, container, false);
        kickRecyclerView = root.findViewById(R.id.kicksRecyclerView);
        categoryNameView = root.findViewById(R.id.categoryNameTextView);
        myToolbar = root.findViewById(R.id.seeAllAppBar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);
        viewModel.getCategoryName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                myToolbar.setTitle(s);
                categoryNameView.setText(s);
            }
        });
        loadKicksFromDb();
        return root;
    }

    private void loadKicksFromDb() {

        viewModel.getCategoryId().observe(requireActivity(), s -> {
            Query query = FirebaseFirestore.getInstance()
                    .collection("kickselects")
                    .document("groupa")
                    .collection("kickcategories")
                    .document(s)
                    .collection("kicksincategory");

            FirestoreRecyclerOptions<Kick> options = new FirestoreRecyclerOptions.Builder<Kick>()
                    .setQuery(query, Kick.class)
                    .build();
            adapter = new KickListAdapter(options, this::onkickSelected);
            kickRecyclerView.setAdapter(adapter);
            kickRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
            int spanCount = 2;// 2 columns
            int spacing = 80; // 100px
            boolean includeEdge = true;
            kickRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            adapter.startListening();
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCategoryId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                categoryId = s;
            }
        });
    }

    @Override
    public void onkickSelected(Kick kick) {
//        Toast.makeText(getContext(),kick.getKickName(),Toast.LENGTH_SHORT).show();
        Intent kickSelectedActivity = new Intent(getContext(), KickSelectedActivity.class);
        kickSelectedActivity.putExtra("kick", kick);
        startActivity(kickSelectedActivity);
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
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

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
