package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityLocationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener  {

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

    private String activityLocation = "";
    private AddActivityLocationData activityLocationData;

    //Location Stuff
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_activity_location, container, false);

        //Location Views
        selectedLocation = root.findViewById(R.id.setTheLocationTextView);
        searchLocationEditText = root.findViewById(R.id.searchLocationEditText);
        locationSelectionDone = root.findViewById(R.id.locationSelectionDoneButton);
        addActivityLocationRelativeLayout = root.findViewById(R.id.add_activity_location_relative_layout);

        activityLocationData = new AddActivityLocationData();

        //Location Implementation
        Places.initialize(requireActivity().getApplicationContext(), ApiThings.places_api_key);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.location_selecting_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        searchLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(requireContext());
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(android.app.Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        locationSelectionDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivityLocation();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddActivityMain = AddActivityLocationFragmentDirections.actionAddActivityLocationFragmentToNavigationAddKick(activityLocationData);
                navController.navigate(actionAddActivityMain);
//                locationCardImageView.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    private void updateActivityLocation() {
        if (activityLocation.isEmpty()) {
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

    private void getAddress(double lat, double lng) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String query = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&result_type=street_address|route|premise|point_of_interest&key=" + "";
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
                            selectedLocation.setText(formatted_address);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                activityLocation = place.getName();
                selectedLocation.setText(place.getName());
//                activityLocationTextView.setText(place.getName());
                activityLocationData.setActivityLocationName(place.getName());
                activityLocationData.setActivityLocationCoordinates(new GeoPoint(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), ZOOM));

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
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);

        updateLocationUI();
        setUsersLastLocation();
        map.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        map.setOnCameraIdleListener(this::onCameraIdle);


    }

    @Override
    public void onCameraIdle() {
        if (map != null) {
//            getAddress(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
//            activityLocation  =  map.getCameraPosition().target;
//            Toast.makeText(getContext(),String.valueOf(activityLocation.latitude),Toast.LENGTH_LONG).show();
//            selectedLocation.setText(String.valueOf(activityLocation.latitude));
        } else {

        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == 1) {
            activityLocation = "";
            selectedLocation.setText(R.string.set_the_location_text);
        }

    }

}
