package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diablo.jayson.kicksv1.ApiThings;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityLocationData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityLocationBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityLocationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

    private static final String TAG = AddActivityLocationFragment.class.getSimpleName();


    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private boolean mLocationPermissionGranted = false;
    private Context context = getContext();

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private float ZOOM = 17f;
    private double CBD_LAT = -1.28333;
    private double CBD_LONG = 36.81667;
    private LatLng CBD = new LatLng(CBD_LAT, CBD_LONG);

    private FragmentAddActivityLocationBinding binding;
    private AddActivityViewModel addActivityViewModel;

    private NavController navController;

    private String activityLocationName = "";
    private GeoPoint activityLocationGeoPoint;
    private boolean isLocationUndisclosed;
    private AddActivityLocationData activityLocationData;

    private RelativeLayout addActivityLocationRelativeLayout;
    private TextView selectedLocation;
    private EditText searchLocationEditText;
    private FloatingActionButton locationSelectionDone;

    public AddActivityLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityLocationBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        activityLocationData = new AddActivityLocationData();

        //Location Implementation
        Places.initialize(requireActivity().getApplicationContext(), ApiThings.places_api_key);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.location_selecting_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        binding.searchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationSearchOverlay();
            }
        });
        binding.undisclosedLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean undisclosedLocation) {
                isLocationUndisclosed = undisclosedLocation;
                if (undisclosedLocation) {
                    activityLocationName = "Undisclosed";
                    binding.mapCardViewContainer.setAlpha(0f);
                    binding.mapCardViewContainer.setVisibility(View.GONE);

                    binding.mapCardViewContainer.animate()
                            .alpha(0.7f)
                            .setDuration(400)
                            .setListener(null);
                    binding.activityLocationTextView.setText(R.string.undisclosed_location_text);
                } else {
                    activityLocationName = "";
                    binding.mapCardViewContainer.animate()
                            .alpha(0.9f)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    binding.mapCardViewContainer.setVisibility(View.VISIBLE);
                                }
                            });
                    binding.activityLocationTextView.setText(R.string.set_the_location_text);

                    if (map != null) {
                        updateLocationUI();
                        setUsersLastLocation();
                        setCameraMoveListeners();
                    }

                }
            }
        });
        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddActivityViewModel();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<com.diablo.jayson.kicksv1.Models.Activity>() {
            @Override
            public void onChanged(com.diablo.jayson.kicksv1.Models.Activity activity) {
                if (activity != null) {
                    if (activity.isLocationUndisclosed()) {
                        Timber.e("Location is undisclosed");
                        isLocationUndisclosed = activity.isLocationUndisclosed();
                        activityLocationName = activity.getActivityLocationName();
                        binding.activityLocationTextView.setText(activity.getActivityLocationName());
                        binding.undisclosedLocationSwitch.setChecked(activity.isLocationUndisclosed());
                    } else if (!activity.isLocationUndisclosed() && activity.getActivityLocationCoordinates() != null) {
                        Timber.e("Location had been set");
                        isLocationUndisclosed = activity.isLocationUndisclosed();
                        activityLocationGeoPoint = activity.getActivityLocationCoordinates();
                        activityLocationName = activity.getActivityLocationName();
                        binding.activityLocationTextView.setText(activity.getActivityLocationName());
                    } else {
                        Timber.e("Location is not set");
                    }
                }
            }
        });
    }

    private void updateAddActivityViewModel() {
        if (isLocationUndisclosed) {
            addActivityViewModel.updateActivityLocation(activityLocationName, null, isLocationUndisclosed);
            navigateToNextFragment();
        } else if (!activityLocationName.isEmpty() && activityLocationGeoPoint != null) {
            addActivityViewModel.updateActivityLocation(activityLocationName, activityLocationGeoPoint, isLocationUndisclosed);
            navigateToNextFragment();
        } else {
            navController.popBackStack();
        }
    }

    private void navigateToNextFragment() {
        NavDirections actionAddActivityMain = AddActivityLocationFragmentDirections.actionAddActivityLocationFragmentToNavigationAddKick();
        navController.navigate(actionAddActivityMain);
    }

    private void openLocationSearchOverlay() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void updateActivityLocation() {
        if (activityLocationName.isEmpty()) {
            activityLocationData.setActivityLocationCoordinates(new GeoPoint(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
            getAddress(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
//            addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
//            addActivityLocationRelativeLayout.setVisibility(View.GONE);
        } else {
//            addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
//            addActivityLocationRelativeLayout.setVisibility(View.GONE);
        }
//        Toast.makeText(getContext(), String.valueOf(activityMain.getKickLocationCordinates()), Toast.LENGTH_LONG).show();


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getAddress(double lat, double lng) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String query = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&result_type=street_address|route|premise|point_of_interest&key=" + ApiThings.geo_coding_api_key;
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(requireContext().getCacheDir(), 1024 * 1024);
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.start();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, query, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        String addressExample;
                        try {
                            String address = response.getString("status");
                            JSONArray resultsArray = response.getJSONArray("results");
                            JSONObject addressComponents = resultsArray.getJSONObject(0);
                            String formatted_address = addressComponents.getString("formatted_address");
                            Log.e(TAG, formatted_address);
                            binding.activityLocationTextView.setText(formatted_address);
                            activityLocationName = formatted_address;
//                            activityLocationTextView.setText(formatted_address);
                            activityLocationData.setActivityLocationName(formatted_address);
                            addressExample = address;
                            Toast.makeText(getContext(), formatted_address, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        try {
//                            JSONObject json_results = response.getJSONObject(response.toString());
//                            JSONObject results_object = json_results.getJSONObject("results");
//                            String formattedAddress = results_object.getString("formatted_address");
//                            Log.e(TAG, formattedAddress);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();

//                        Log.e(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                activityLocationName = place.getName();
                binding.activityLocationTextView.setText(place.getName());
//                activityLocationTextView.setText(place.getName());
                activityLocationData.setActivityLocationName(place.getName());
                activityLocationGeoPoint = new GeoPoint(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), ZOOM));
                hideKeyboardFrom(requireContext(), getView());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = true;
//                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setMapToolbarEnabled(true);
//                        setUsersLastLocation();
                    }
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                setUsersLastLocation();
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setUsersLastLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastKnownLocation = (Location) task.getResult();
                            assert lastKnownLocation != null;
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), ZOOM));
                        } else {
                            Log.e(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(CBD, 10f));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);

        if (activityLocationGeoPoint != null) {
            LatLng activityLocationLatLng = new LatLng(activityLocationGeoPoint.getLatitude(), activityLocationGeoPoint.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(activityLocationLatLng, ZOOM));
            setCameraMoveListeners();
        } else if (isLocationUndisclosed) {
            setCameraMoveListeners();
            return;
        } else {
            updateLocationUI();
            setUsersLastLocation();
            setCameraMoveListeners();
        }
        setCameraMoveListeners();

    }

    private void setCameraMoveListeners() {
        map.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        map.setOnCameraIdleListener(this::onCameraIdle);
    }

    @Override
    public void onCameraIdle() {
        if (map != null) {
            getAddress(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
            activityLocationGeoPoint = new GeoPoint(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
//            activityLocation  =  map.getCameraPosition().target;
//            Toast.makeText(getContext(),String.valueOf(activityLocation.latitude),Toast.LENGTH_LONG).show();
//            selectedLocation.setText(String.valueOf(activityLocation.latitude));
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == 1) {
            activityLocationName = "";
            binding.activityLocationTextView.setText(R.string.set_the_location_text);
        }

    }

}
