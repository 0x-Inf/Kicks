package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendActivityMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendActivityMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AttendActivityViewModel viewModel;
    private Activity activityMain;
    private String activityId;
    private ActivityAttendeesAdapter attendeesAdapter;
    private ArrayList<AttendingUser> attendingUsersData;

    private TextView textView;
    private ImageView activityImage;
    private RecyclerView attendeesRecycler;
    private RecyclerView chatRecycler;

    private Toolbar myToolbar;

    public AttendActivityMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendActivityMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendActivityMainFragment newInstance(String param1, String param2) {
        AttendActivityMainFragment fragment = new AttendActivityMainFragment();
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
        View root = inflater.inflate(R.layout.fragment_attend_activity_main, container, false);
        attendeesRecycler = root.findViewById(R.id.attendeesRecycler);
//        chatRecycler = root.findViewById(R.id.chatRecycler);
//        attendingUsersData = new ArrayList<AttendingUser>();
        setHasOptionsMenu(true);
        myToolbar = (Toolbar) root.findViewById(R.id.collapsingToolBar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);
//        myToolbar.setTitle(activityMain.getkickTitle());
//        Toast.makeText(getContext(),String.valueOf(attendingUsersData.size()),Toast.LENGTH_LONG).show();
//        loadAttendeesFromDb();
        viewModel.getActivityId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("activities").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            attendingUsersData = new ArrayList<AttendingUser>();
                            attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getMattendees();
                            Log.e("yooooo", String.valueOf(attendingUsersData.size()));
                            Log.e("yooooo", attendingUsersData.get(0).getUserName());
                            Log.e("yooooo", attendingUsersData.get(1).getUserName());
                            Log.e("yooooo", attendingUsersData.get(2).getUserName());
                            Log.e("yooooo", attendingUsersData.get(3).getUserName());


                        }
//                        initializeRecyclerWithAttendees();
                        Log.e("skkdnskn", attendingUsersData.get(3).getUserName());
                        AttendeesAdapter attendeesAdapter = new AttendeesAdapter(getContext(), attendingUsersData);
                        attendeesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
                        attendeesRecycler.setAdapter(attendeesAdapter);
                    }
                });
            }
        });

        return root;
    }

    private void loadAttendeesFromDb() {
        viewModel.getActivityId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("activities").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getMattendees();
                            Log.e("yooooo", String.valueOf(attendingUsersData.size()));
                            AttendeesAdapter attendeesAdapter = new AttendeesAdapter(getContext(), attendingUsersData);
                            attendeesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
                            attendeesRecycler.setAdapter(attendeesAdapter);
                        }
//                        initializeRecyclerWithAttendees();
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
//        attendeesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        attendeesAdapter.stopListening();
    }


    private void initializeRecyclerWithAttendees() {
        AttendeesAdapter attendeesAdapter = new AttendeesAdapter(getContext(), attendingUsersData);
        attendeesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        attendeesRecycler.setAdapter(attendeesAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityImage = view.findViewById(R.id.activityImageToolBar);
//        textView = view.findViewById(R.id.costTextView);
        RecyclerView attendeesRecycler = view.findViewById(R.id.attendeesRecycler);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.collapsingToolBar);
        viewModel.getActivityData().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                Glide.with(getContext())
                        .load(activityMain.getimageUrl())
                        .into(activityImage);
//                textView.setText(activityMain.getkickTitle());
                myToolbar.setTitle(activityMain.getkickTitle());

//                loadAttendeesFromDb();
            }
        });
        viewModel.getActivityId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                activityId = s;
//                Log.e("skkdnskn", attendingUsersData.get(3).getUserName());

            }
        });
    }
}
