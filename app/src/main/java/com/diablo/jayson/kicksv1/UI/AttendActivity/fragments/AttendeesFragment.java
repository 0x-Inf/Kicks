package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendeesLargeAdapter;
import com.diablo.jayson.kicksv1.UI.KickSelect.KicksSeeAllFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendeesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendeesFragment extends Fragment implements AttendeesLargeAdapter.OnAttendeeSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView attendeesRecycler;
    private AttendeesLargeAdapter attendeesLargeAdapter;
    private AttendActivityViewModel viewModel;

    private String activityId;
    private ArrayList<AttendingUser> attendingUsersData;
    private AttendeesFragment listener;


    public AttendeesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendeesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendeesFragment newInstance(String param1, String param2) {
        AttendeesFragment fragment = new AttendeesFragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_attendees, container, false);
        attendeesRecycler = root.findViewById(R.id.attendeesRecyclerView);
        getActivityIdViewModel();
        getAttendeesData();
        return root;
    }

    private void getActivityIdViewModel() {
        viewModel.getActivityId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                activityId = s;
            }
        });
    }

    private void getAttendeesData() {
        listener = this;
        attendingUsersData = new ArrayList<AttendingUser>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities").document(activityId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getActivityAttendees();
                        }
                        attendeesLargeAdapter = new AttendeesLargeAdapter(getContext(),attendingUsersData,listener);
                        attendeesRecycler.setAdapter(attendeesLargeAdapter);
                        int spanCount = 2;// 2 columns
                        int spacing = 40; // 40px
                        boolean includeEdge = true;
                        attendeesRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));

                    }
                });
    }

    @Override
    public void onAttendeeSelected(AttendingUser attendingUser) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavDirections actionAttendeesBottomDialog = AttendeesFragmentDirections.actionAttendeesFragmentToAttendeeSelectedBottomDialogFragment(attendingUser.getUid());
        navController.navigate(actionAttendeesBottomDialog);
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