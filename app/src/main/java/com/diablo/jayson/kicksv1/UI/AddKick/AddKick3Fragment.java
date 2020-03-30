package com.diablo.jayson.kicksv1.UI.AddKick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKick3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKick3Fragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddKick3Fragment.class.getSimpleName();
    private FragmentActivity myContext;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private LatLng tagLocation;
    private GoogleMap googleMap;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText mDateTextInput, mTimeTextInput, mLocationTextInput;

    private AddKickViewModel viewModel;

    private Activity activityMain;

    public AddKick3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKick3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKick3Fragment newInstance(String param1, String param2) {
        AddKick3Fragment fragment = new AddKick3Fragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocationTextInput = view.findViewById(R.id.ActivityPLaceEditText);
        mTimeTextInput = view.findViewById(R.id.time_picker_input);
        FloatingActionButton nextButton = view.findViewById(R.id.nextCreateActivityFab);
        LinearLayout thirdContent = view.findViewById(R.id.locationAndTimeContent);
        // Initialize the SDK
//        Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(), "AIzaSyDrZtRYNPGMye467hX4Y0SWmkTp9mSUpCs");
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);



        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                mLocationTextInput.setText(String.valueOf(activity.getTag().getTagLocationName()));
                mTimeTextInput.setText(activity.getTag().getTagOptimalStartTime());
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViewModel();
                AddKickConfirmFragment confirmFragment = new AddKickConfirmFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayoutbase, confirmFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                thirdContent.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateViewModel() {
        String activityLocationName = Objects.requireNonNull(mLocationTextInput.getText()).toString();
        GeoPoint activityLocationCordinates = activityMain.getTag().getTagLocation();
        activityMain.setKickLocationCordinates(activityLocationCordinates);
        activityMain.setmKickLocation(activityLocationName);
        activityMain.setUploadedTime(Calendar.getInstance().getTimeInMillis());
        activityMain.setmKickTime(Objects.requireNonNull(mTimeTextInput.getText()).toString());
        activityMain.setmKickDate(Objects.requireNonNull(mDateTextInput.getText()).toString());
        viewModel.setActivity1(activityMain);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_kick3, container, false);
        mTimeTextInput = root.findViewById(R.id.time_picker_input);
        mDateTextInput = root.findViewById(R.id.date_picker_input);
        mLocationTextInput = root.findViewById(R.id.ActivityPLaceEditText);

        // Initialize the SDK
        Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(), "AIzaSyDrZtRYNPGMye467hX4Y0SWmkTp9mSUpCs");

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mLocationTextInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(Objects.requireNonNull(getContext()));
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mLocationTextInput.setText(String.format("%s", place.getName()));
                tagLocation = place.getLatLng();
                activityMain.setKickLocationCordinates(new GeoPoint(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        tagLocation = new LatLng(activityMain.getTag().getTagLocation().getLatitude(), activityMain.getTag().getTagLocation().getLongitude());
        googleMap.addMarker(new MarkerOptions().position(tagLocation)
                .title(activityMain.getTag().getTagLocationName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tagLocation, 15));
//        googleMap.addMarker(new MarkerOptions().position(activityLocation)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(activityLocation, 20));

        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {

            }
        });
    }
}
