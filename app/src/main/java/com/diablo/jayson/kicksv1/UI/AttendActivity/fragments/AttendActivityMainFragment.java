package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AttendActivity.DetailsViewPagerFragmentAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentAttendActivityMain2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;


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

    private String activityId;
    private Activity attendedActivity;
    private AttendActivityViewModel viewModel;

    private FragmentAttendActivityMain2Binding binding;
    private Handler handler = new Handler();
    private int currentPage = 0;

    private DetailsViewPagerFragmentAdapter detailsViewPagerFragmentAdapter;

    //views
    private CardView chatCard, attendeesCard, detailsCard;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAttendActivityMain2Binding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
        View root = binding.getRoot();
        attendedActivity = new Activity();
//        View root = inflater.inflate(R.layout.fragment_attend_activity_main2, container, false);
        chatCard = root.findViewById(R.id.activity_chat_card);
        attendeesCard = root.findViewById(R.id.activity_attendees_card);
        detailsCard = root.findViewById(R.id.activity_details_card);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);


        attendeesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAttendees = AttendActivityMainFragmentDirections.actionAttendActivityMainFragmentToAttendeesFragment();
                navController.navigate(actionAttendees);
            }
        });
        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionChat = AttendActivityMainFragmentDirections.actionAttendActivityMainFragmentToChatFragment();
                navController.navigate(actionChat);

            }
        });
        detailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionDetails = AttendActivityMainFragmentDirections.actionAttendActivityMainFragmentToActivityDetailsFragment();
                navController.navigate(actionDetails);
            }
        });
        return root;
    }

    private void getActivityDataFromDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities").document(activityId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            attendedActivity = documentSnapshot.toObject(Activity.class);
                            viewModel.setActivityData(attendedActivity);
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        activityId = AttendActivityMainFragmentArgs.fromBundle(getArguments()).getActivityId();
        viewModel.setActivityId(activityId);
        getActivityDataFromDb();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.activityDetailsViewPager.setCurrentItem(currentPage % 3, true);
                        currentPage++;
                    }
                });
            }
        };
        Timer time = new Timer();
        time.schedule(timerTask, 0, 5000);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        detailsViewPagerFragmentAdapter = new DetailsViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle());
        detailsViewPagerFragmentAdapter.addFragment(new MapDetailsPreviewFragment());
        detailsViewPagerFragmentAdapter.addFragment(new PeopleDetailsPreviewFragment());
        detailsViewPagerFragmentAdapter.addFragment(new TimeDetailsPreviewFragment());
        binding.activityDetailsViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.activityDetailsViewPager.setAdapter(detailsViewPagerFragmentAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}