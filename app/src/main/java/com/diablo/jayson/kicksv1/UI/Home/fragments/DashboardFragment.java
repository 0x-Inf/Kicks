package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.HappeningSoonActivitiesAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.Utils.SharedPreferencesUtil;
import com.diablo.jayson.kicksv1.databinding.FragmentDashboardBinding;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements HappeningSoonActivitiesAdapter.OnSoonActivitySelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentDashboardBinding binding;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Timber.e("Permission Granted");
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    Timber.e("Permission Denied");
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private HappeningSoonActivitiesAdapter soonActivitiesAdapter;
    private HomeViewModel homeViewModel;
    private String activeActivitiesNumber;
    private ArrayList<Activity> happeningSoonActivities;
    private DashboardFragment listener;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private LatLng defaultLocation = new LatLng(-1.27, 36.78);
    private GoogleMap map;
    private DashboardFragment mapListener;
    private double nearbyRadius = 20 * 1000;

    private NavController navController;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferencesUtil = new SharedPreferencesUtil(getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        binding.activeActivitiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionActiveActivities = DashboardFragmentDirections.actionDashboardFragmentToActiveActivitiesFragment();
                navController.navigate(actionActiveActivities);
            }
        });
        binding.browseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionBrowse = DashboardFragmentDirections.actionDashboardFragmentToNavigationBrowse();
                navController.navigate(actionBrowse);
            }
        });
        binding.invitesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionInvites = DashboardFragmentDirections.actionDashboardFragmentToInvitesFragment();
                navController.navigate(actionInvites);
            }
        });
        binding.mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionMap = DashboardFragmentDirections.actionDashboardFragmentToMapFragment();
                navController.navigate(actionMap);
            }
        });


        DateFormat formatter = new SimpleDateFormat("EEEE  d LLLL", Locale.getDefault());
        Date dateToday = new Date();
        String dateString = formatter.format(dateToday);
        Timber.e(dateString);
        binding.dateTextView.setText(dateString);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        happeningSoonActivities = new ArrayList<>();
        listener = this;
//        activeActivitiesNumber = String.valueOf(homeViewModel.getActiveActivitiesMutableLiveData().getValue().size());
//        binding.activeActivitiesNumberTextView.setText(activeActivitiesNumber);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                defaultLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                Timber.e(defaultLocation.toString());
                            }
                        }
                    });

//            PermissionUtils.requestPermission((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // TODO: Create the ui for rationale
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        final GeoLocation center = new GeoLocation(defaultLocation.latitude, defaultLocation.longitude);
        homeViewModel.getNearbyActivityRadiusMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble != null) {
                    nearbyRadius = aDouble;
                }
            }
        });
        homeViewModel.getNearbyActivity(center, nearbyRadius);
        homeViewModel.getActiveActivitiesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> activities) {
                activeActivitiesNumber = String.valueOf(activities.size());
                happeningSoonActivities = activities;
                binding.activeActivitiesNumberTextView.setText(activeActivitiesNumber);
                soonActivitiesAdapter = new HappeningSoonActivitiesAdapter(listener, activities);
                binding.happeningSoonRecyclerView.setAdapter(soonActivitiesAdapter);
            }
        });

        homeViewModel.getNearbyActivityMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> activities) {
                int nearbyActivities = activities.size();
                String nearbyActivityText = nearbyActivities + " nearby";
                binding.nearbyActivityActualTextView.setText(nearbyActivityText);
            }
        });


    }

    @Override
    public void onSoonActivitySelected(Activity activity) {
        NavDirections actionHappeningSoonDialog = DashboardFragmentDirections
                .actionDashboardFragmentToHappeningSoonSelectedDialogFragment(activity.getActivityId());
        navController.navigate(actionHappeningSoonDialog);
    }
}