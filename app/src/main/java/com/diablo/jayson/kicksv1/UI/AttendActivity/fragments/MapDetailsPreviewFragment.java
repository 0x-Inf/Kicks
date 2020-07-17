package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentMapDetailsPreviewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapDetailsPreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapDetailsPreviewFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentMapDetailsPreviewBinding binding;

    private AttendActivityViewModel attendActivityViewModel;
    private GeoPoint activityLocation;

    private GoogleMap map;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String activityId;

    public MapDetailsPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapDetailsPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapDetailsPreviewFragment newInstance(String param1, String param2) {
        MapDetailsPreviewFragment fragment = new MapDetailsPreviewFragment();
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
        binding = FragmentMapDetailsPreviewBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        activityId = "";
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapPreview);
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attendActivityViewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
        attendActivityViewModel.getActivityId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                activityId = s;
            }
        });
        activityLocation = new GeoPoint(0.1,0.0);
        attendActivityViewModel.getActivityData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                Log.e("This one" , activity.getActivityTitle());
                activityLocation = activity.getActivityLocationCoordinates();
                if (map != null){
                    LatLng activityLocation  = new LatLng(activity.getActivityLocationCoordinates().getLatitude(), activity.getActivityLocationCoordinates().getLongitude());
                    map.addMarker(new MarkerOptions().position(activityLocation));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(activityLocation, 10));
                }

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("activities").document(activityId).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            activityLocation = documentSnapshot.toObject(Activity.class).getActivityLocationCoordinates();
//                            if (map != null){
//                                LatLng activityLocation  = new LatLng(0,0);
//                                map.addMarker(new MarkerOptions().position(activityLocation));
//                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(activityLocation,30));
//                            }
//                        }
//                    }
//                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        attendActivityViewModel.getActivityData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                LatLng activityLocation  = new LatLng(activity.getActivityLocationCoordinates().getLatitude(), activity.getActivityLocationCoordinates().getLongitude());
                map.addMarker(new MarkerOptions().position(activityLocation));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(activityLocation, 10));
            }
        });
    }
}