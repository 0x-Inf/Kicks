package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.UI.Home.LocationBroadcast;
import com.diablo.jayson.kicksv1.UI.Home.MapViewModel;
import com.diablo.jayson.kicksv1.UI.Home.PublicLocationBroadcast;
import com.diablo.jayson.kicksv1.UI.Home.SelectedMapContactsAdapter;
import com.diablo.jayson.kicksv1.Utils.PermissionUtils;
import com.diablo.jayson.kicksv1.Utils.SharedPreferencesUtil;
import com.diablo.jayson.kicksv1.databinding.FragmentMapBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;
    private boolean requestingLocationUpdates = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMapBinding binding;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private HomeViewModel homeViewModel;
    private MapViewModel mapViewModel;
    private MapFragment listener;
    private NavController navController;


    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private GoogleMap map;
    private boolean isShowingCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationBroadcast locationBroadcast = new LocationBroadcast();
    private PublicLocationBroadcast publicLocationBroadcast = new PublicLocationBroadcast();
    private boolean isBroadcastingPublicly;
    private boolean isBroadcastingPrivately;
    private boolean isBroadcastingToContacts;
    private String locationBroadcastId;
    private String publicLocationBroadcastId;
    private GeoPoint locationBroadcastGeoPoint;
    private float nearbyRadius = 1000f;
    private ArrayList<LatLng> lastKnownLocations = new ArrayList<>();

    private ArrayList<String> broadcastIntendedUsersIds = new ArrayList<>();
    private ArrayList<Tag> broadcastTags = new ArrayList<>();
    private ArrayList<String> allContactsIds = new ArrayList<>();
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    private HashMap<String, Marker> broadcastLocationsMarkers = new HashMap<>();
    private HashMap<String, Marker> publicBroadcastLocationsMarkers = new HashMap<>();
    private Marker currentLocationMarker = null;

    private SelectedMapContactsAdapter selectedMapContactsAdapter;


    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        sharedPreferencesUtil = new SharedPreferencesUtil(getContext());
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("com.color.kicks", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this::onMapReady);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        createLocationRequest();
        setPublicLocationBroadcastListener();
        setPrivateLocationBroadcastListener();
        broadcastIntendedUsersIds = new ArrayList<>();
        isBroadcastingPrivately = false;
        isBroadcastingPublicly = false;
        isBroadcastingToContacts = false;

        selectedMapContactsAdapter = new SelectedMapContactsAdapter(selectedContacts);
        binding.selectedContactsRecycler.setAdapter(selectedMapContactsAdapter);
        binding.alreadySelectedContactsRecycler.setAdapter(selectedMapContactsAdapter);

        binding.openMapSettingsFab.setOnClickListener(view -> {
            NavDirections actionMapSettings = MapFragmentDirections.actionMapFragmentToMapSettingsBottomSheet();
            navController.navigate(actionMapSettings);
//            binding.mapSettingsCardView.setVisibility(View.VISIBLE);
//            binding.openMapSettingsFab.setVisibility(View.GONE);
        });
        binding.closeMapSettingsImageButton.setOnClickListener(view -> {
            binding.mapSettingsCardView.setVisibility(View.GONE);
            binding.openMapSettingsFab.setVisibility(View.VISIBLE);
        });
        binding.addContactsToSelectedFab.setOnClickListener(view -> {
            binding.selectSharingContactsCard.setVisibility(View.VISIBLE);
            binding.selectContactsOverlay.setVisibility(View.VISIBLE);
        });
        binding.removeContactSelectionCardButton.setOnClickListener(view -> {
            binding.selectSharingContactsCard.setVisibility(View.GONE);
            binding.selectContactsOverlay.setVisibility(View.GONE);
        });
        binding.selectContactsOverlay.setOnClickListener(view -> {
            binding.selectSharingContactsCard.setVisibility(View.GONE);
            binding.selectContactsOverlay.setVisibility(View.GONE);
        });

        binding.broadcastLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isBroadcastingPrivately = isChecked;
                if (isChecked) {
                    startBroadcastingLocation();
                } else {
                    stopBroadcastingLocation();
                }
            }
        });
        binding.publicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isBroadcastingToContacts) {
                    isBroadcastingToContacts = false;
                    binding.contactsOnlySwitch.setChecked(false);
                }
                isBroadcastingPublicly = isChecked;
                isBroadcastingPrivately = false;
                if (isChecked) {
                    startBroadcastingLocationPublicly();
                } else {
                    isBroadcastingPrivately = true;
                    startBroadcastingLocation();
                }

            }
        });
        binding.contactsOnlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isBroadcastingPublicly) {
                    isBroadcastingPublicly = false;
                    binding.publicSwitch.setChecked(false);
                }
                isBroadcastingToContacts = isChecked;
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            PermissionUtils.requestPermission((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            Timber.e(String.valueOf(currentLocation.getLongitude()));
                            if (map != null) {
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f));
                            }
                        }
                    }
                });
        listener = this;

        homeViewModel.getUserContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                for (Contact contact : contacts) {
                    allContactsIds.add(contact.getContactId());
                }
            }
        });

        mapViewModel.getShareLocationMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.isSharingLocationCard.setVisibility(View.VISIBLE);
                    startBroadcastingLocation();
                } else {
                    binding.isSharingLocationCard.setVisibility(View.GONE);
                    stopBroadcastingLocation();
                }
            }
        });

        mapViewModel.getShareLocationPubliclyMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isBroadcastingPublicly = aBoolean;
                isBroadcastingPrivately = !aBoolean;
                if (aBoolean) {
                    startBroadcastingLocationPublicly();
                    binding.isSharingLocationText.setText(R.string.sharing_location_publicly_textview_string);
                    binding.sharingLocationIconImageView.setImageResource(R.drawable.ic_public_location);
                } else {
                    stopBroadcastingLocation();
                    binding.isSharingLocationText.setText(R.string.sharing_location_text);
                    binding.sharingLocationIconImageView.setImageResource(R.drawable.ic_action_locate);
                }
            }
        });

        mapViewModel.getSelectedContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                selectedContacts = contacts;
                broadcastIntendedUsersIds.clear();
                for (Contact selectedContact :
                        contacts) {
                    broadcastIntendedUsersIds.add(selectedContact.getContactId());
                }
            }
        });

        mapViewModel.getSelectedBroadcastTagsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                broadcastTags = tags;
            }
        });

        homeViewModel.getNearbyActivityRadiusMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double radiusDouble) {
                if (radiusDouble != null) {
                    nearbyRadius = radiusDouble.floatValue();
                } else {
                    nearbyRadius = sharedPreferencesUtil.getDefaultRadius();
                }

            }
        });
    }

    private void startBroadcastingLocation() {
        if (checkIfBroadcastIdsAreInPreferences()) {
            startLocationUpdates();
        } else {
            createLocationBroadcastDocumentInDb();
        }
    }

    private void startBroadcastingLocationPublicly() {
        if (checkIfPublicBroadcastIdIsInPreferences()) {
            startLocationUpdates();
        } else {
            createPublicLocationBroadcastDocumentInDb();
        }
    }

    private boolean checkIfBroadcastIdsAreInPreferences() {
        boolean isBroadcastIdPresent;
        locationBroadcastId = sharedPreferencesUtil.getLocationBroadcastId();
        assert locationBroadcastId != null;
        isBroadcastIdPresent = !locationBroadcastId.equals("");
        return isBroadcastIdPresent;
    }

    private boolean checkIfPublicBroadcastIdIsInPreferences() {
        boolean isPublicBroadcastIdPresent;
        publicLocationBroadcastId = sharedPreferencesUtil.getPublicLocationBroadcastId();
        assert publicLocationBroadcastId != null;
        isPublicBroadcastIdPresent = !publicLocationBroadcastId.equals("");
        return isPublicBroadcastIdPresent;
    }

    private void createLocationBroadcastDocumentInDb() {
        locationBroadcast = new LocationBroadcast();
        showLoadingScreen();
        db.collection(Constants.location_broadcast_collection)
                .add(locationBroadcast)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        sharedPreferencesUtil.setLocationBroadcastId(documentReference.getId());
                        startLocationUpdates();
                        hideLoadingScreen();
                    }
                });
    }

    private void createPublicLocationBroadcastDocumentInDb() {
        publicLocationBroadcast = new PublicLocationBroadcast();
        showLoadingScreen();
        db.collection(Constants.public_location_broadcast_collection)
                .add(publicLocationBroadcast)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        sharedPreferencesUtil.setPublicLocationBroadcastId(documentReference.getId());
                        hideLoadingScreen();
                        startLocationUpdates();
                    }
                });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            PermissionUtils.requestPermission((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, false);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
        requestingLocationUpdates = true;
    }

    private void currentLocationUpdated() {
        LatLng lastLocation = lastKnownLocations.get(lastKnownLocations.size() - 1);
        GeoPoint lastLocationGeoPoint = new GeoPoint(lastLocation.latitude, lastLocation.longitude);
        if (isBroadcastingPublicly) {
            updatePublicLocationBroadcastInDb(lastLocationGeoPoint);
        } else {
            updateLocationBroadcastInDb(lastLocationGeoPoint);
        }
    }

    private void updatePublicLocationBroadcastInDb(GeoPoint lastLocationGeoPoint) {
        publicLocationBroadcast.setBroadcastingUserId(firebaseUser.getUid());
        publicLocationBroadcast.setBroadcastLocation(lastLocationGeoPoint);
        publicLocationBroadcast.setBroadcastId(publicLocationBroadcastId);
        publicLocationBroadcast.setBroadcastTime(Timestamp.now());
        publicLocationBroadcast.setBroadcastTags(broadcastTags);
        db.collection(Constants.public_location_broadcast_collection)
                .document(publicLocationBroadcastId)
                .set(publicLocationBroadcast)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Timber.e("public location Uploaded");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, "Failed to upload public location broadcast");
                    }
                });

    }

    private void updateLocationBroadcastInDb(GeoPoint lastLocationGeoPoint) {
        if (isBroadcastingToContacts) {
            locationBroadcast.setBroadcastIntendedUserIds(allContactsIds);
        } else {
            locationBroadcast.setBroadcastIntendedUserIds(broadcastIntendedUsersIds);
        }
        locationBroadcast.setBroadcastingUserId(firebaseUser.getUid());
        locationBroadcast.setBroadcastLocation(lastLocationGeoPoint);
        locationBroadcast.setBroadcastId(locationBroadcastId);
        locationBroadcast.setBroadcastTime(Timestamp.now());
        locationBroadcast.setBroadcastTags(broadcastTags);
        db.collection(Constants.location_broadcast_collection)
                .document(locationBroadcastId)
                .set(locationBroadcast)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Timber.d("Successfully uploaded location broadcast to db");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, "Error uploading location broadcast");
                    }
                });
    }

    private void setPublicLocationBroadcastListener() {
        db.collection(Constants.public_location_broadcast_collection)
                .whereGreaterThanOrEqualTo("broadcastTime", Timestamp.now())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Timber.e(e);
                            return;
                        }

                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    Timber.e("Added Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case MODIFIED:
                                    Timber.e("Modified Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case REMOVED:
                                    Timber.e("Removed Location Update%s", documentChange.getDocument().getId());

                            }

                            PublicLocationBroadcast publicLocationBroadcastUpdate = documentChange
                                    .getDocument().toObject(PublicLocationBroadcast.class);

                            setPublicLocationBroadcastOnMap(publicLocationBroadcastUpdate);
                        }
                    }
                });
    }

    private void setPrivateLocationBroadcastListener() {
        db.collection(Constants.location_broadcast_collection)
                .whereGreaterThanOrEqualTo("broadcastTime", Timestamp.now())
                .whereArrayContains("broadcastIntendedUserIds", firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Timber.e(e);
                            return;
                        }

                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    Timber.e("Added Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case MODIFIED:
                                    Timber.e("Modified Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case REMOVED:
                                    Timber.e("Removed Location Update%s", documentChange.getDocument().getId());

                            }

                            LocationBroadcast privateLocationBroadcastUpdate = documentChange
                                    .getDocument().toObject(LocationBroadcast.class);

                            setPrivateLocationBroadcastOnMap(privateLocationBroadcastUpdate);
                        }
                    }
                });
    }

    private void setPrivateLocationBroadcastOnMap(LocationBroadcast privateLocationBroadcastUpdate) {
        LatLng privateLocationUpdateLatLng = new LatLng(privateLocationBroadcastUpdate.getBroadcastLocation().getLatitude(),
                privateLocationBroadcastUpdate.getBroadcastLocation().getLongitude());
        if (broadcastLocationsMarkers.containsKey(privateLocationBroadcastUpdate.getBroadcastId())) {
            broadcastLocationsMarkers.get(privateLocationBroadcastUpdate.getBroadcastId())
                    .setPosition(privateLocationUpdateLatLng);
        } else {
            broadcastLocationsMarkers.put(privateLocationBroadcastUpdate.getBroadcastId(),
                    map.addMarker(new MarkerOptions().position(privateLocationUpdateLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_broadcast))));
        }
    }

    private void setPublicLocationBroadcastOnMap(PublicLocationBroadcast publicLocationBroadcastUpdate) {
        LatLng publicLocationUpdateLatLng = new LatLng(publicLocationBroadcastUpdate.getBroadcastLocation().getLatitude(),
                publicLocationBroadcastUpdate.getBroadcastLocation().getLongitude());
        if (!publicLocationBroadcastUpdate.getBroadcastId().equals(publicLocationBroadcastId)) {
            if (publicBroadcastLocationsMarkers.containsKey(publicLocationBroadcastUpdate.getBroadcastId())) {
                publicBroadcastLocationsMarkers.get(publicLocationBroadcastUpdate.getBroadcastId())
                        .setPosition(publicLocationUpdateLatLng);
            } else {
                publicBroadcastLocationsMarkers.put(publicLocationBroadcastUpdate.getBroadcastId(),
                        map.addMarker(new MarkerOptions().position(publicLocationUpdateLatLng)));
            }
        }

    }

    private void stopBroadcastingLocation() {
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                    locationBroadcastGeoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    lastKnownLocations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    currentLocationUpdated();
                    updateCurrentLocationMarker(currentLocation);
                    if (isShowingCurrentLocation) {
                        LatLng userCenter = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        CircleOptions circleOptions = new CircleOptions()
                                .center(userCenter)
                                .radius(nearbyRadius)
                                .strokeWidth(2)
                                .strokeColor(Color.GREEN)
                                .fillColor(Color.argb(128, 255, 0, 0));
                        Circle circle = map.addCircle(circleOptions);
                        startRippleAnimation(circle);
                    } else {
                        // Update for not showing icon while also broadcasting loc
                    }
                }
//                    return;

            }
        };
    }

    private void startRippleAnimation(Circle circle) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setIntValues(0, 100);
        valueAnimator.setDuration(1000);
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Timber.e(String.valueOf(animatedFraction));
                circle.setRadius(animatedFraction * 100);
            }
        });
        valueAnimator.start();
    }

    private void updateCurrentLocationMarker(Location currentLocation) {
        LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null) {
            currentLocationMarker = map.addMarker(
                    new MarkerOptions()
                            .position(currentLocationLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_broadcast)));
        } else {
            currentLocationMarker.setPosition(currentLocationLatLng);
        }

    }

    private void showLoadingScreen() {
        binding.loadingScreen.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen() {
        binding.loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionDenied) {
            showMissingPermissionError();
        }
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getChildFragmentManager(), "Dialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        int currentNightMode = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.map_style));

                    if (!success) {
                        Timber.e("Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Timber.e(e, "Can't find style. Error: ");
                }
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.map_style_dark));

                    if (!success) {
                        Timber.e("Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Timber.e(e, "Can't find style. Error: ");
                }
                break;
        }
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        binding.findMeCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowingCurrentLocation) {
                    binding.locateIcon.setImageResource(R.drawable.ic_action_locate_active);
                    isShowingCurrentLocation = true;
                    enableMyLocation();
                } else {
                    binding.locateIcon.setImageResource(R.drawable.ic_action_locate);
                    disableMyLocation();
                }
            }
        });
    }

    private void enableMyLocation() {

        if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                isShowingCurrentLocation = true;
                map.setMyLocationEnabled(true);
                addNearbyCircleAroundUser();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15f));
            }
        } else {
            isShowingCurrentLocation = false;
            PermissionUtils.requestPermission((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    private void addNearbyCircleAroundUser() {

    }

    private void disableMyLocation() {
        if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                isShowingCurrentLocation = false;
                map.setMyLocationEnabled(false);

            }
        } else {
            PermissionUtils.requestPermission((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }

    }
}