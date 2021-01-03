package com.diablo.jayson.kicksv1.UI.MapFeed;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class KickMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = KickMapFragment.class.getSimpleName();

    private MapFragmentViewModel viewModel;

    private GoogleMap mMap;
    private Location mLastLocation;
    private LatLng mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private float ZOOM = 17f;
    private double CBD_LAT = -1.28333;
    private double CBD_LONG = 36.81667;
    private LatLng CBD = new LatLng(CBD_LAT, CBD_LONG);

    private ArrayList<Activity> allActivities;
    private ArrayList<Tag> allTags;
    private Marker Marker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(MapFragmentViewModel.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    Toast.makeText(getActivity(), R.string.location_permission_denied,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kick_map, container, false);
//        loadActiveActivitiesFromDb();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }

    private void loadActiveActivitiesFromDb() {

        allActivities = new ArrayList<Activity>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                allActivities.add(snapshot.toObject(Activity.class));
                            }
                        }
                    }
                });
    }

    public void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;

                                mCurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
//        boolean darkMode = sharedPreferences.getBoolean("darkmode", false);
//        if (darkMode){
//
//
//        }else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        enableLocation();

        mMap = googleMap;
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 12));
                        } else {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CBD, 12));
                        }
                    }
                }
        );


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        allTags = new ArrayList<Tag>();
        db.collection("tags").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                allTags.add(documentSnapshot.toObject(Tag.class));
                            }

//                            for (int i = 0; i < allTags.size(); i++) {
//
//
//                                Marker marker = mMap.addMarker(new MarkerOptions()
//                                        .position(new LatLng(allTags.get(i).getTagLocation().getLatitude(), allTags.get(i).getTagLocation().getLongitude()))
//                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//                                        .title(allTags.get(i).getTagName()));
//                                marker.setTag(allTags.get(i).getTagName());
//                                Glide.with(getContext())
//                                        .asBitmap()
//                                        .load(allTags.get(i).getTagIconUrl())
//                                        .into(new SimpleTarget<Bitmap>(30, 30) {
//                                            @Override
//                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(resource));
//                                            }
//                                        });
//                            }

                        }
                    }
                });

        mMap.setOnMarkerClickListener(this::onMarkerClick);

    }

    @Override
    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
        String tagName = Objects.requireNonNull(marker.getTag()).toString();
        viewModel.setTagName(tagName);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavDirections actionBottomDialogFragment = KickMapFragmentDirections.actionNavigationMapViewToAvailableActivitiesBottomDialogFragment();
        navController.navigate(actionBottomDialogFragment);
//        AvailableActivitiesBottomDialogFragment availableActivitiesBottomDialogFragment = AvailableActivitiesBottomDialogFragment.newInstance();
//        availableActivitiesBottomDialogFragment.show(getParentFragmentManager(), "available_activities");


//
//        Toast.makeText(getContext(), activityId, Toast.LENGTH_LONG).show();
        return false;
    }

}
