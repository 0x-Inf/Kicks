package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedFragment;
import com.edmodo.rangebar.RangeBar;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddKickFragment extends Fragment {

    private static final String TAG = AddKickFragment.class.getSimpleName();
    private AddKickViewModel mViewModel;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private TextInputEditText mTimePickerInput, mDatePickerInput, mLocationTextInput;
    private Button mPickALocationButton;
    private SeekBar mSeekBar;
    private DatabaseReference mDatabase;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;


    public static AddKickFragment newInstance() {
        return new AddKickFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_kick_fragment, container, false);
        mTimePickerInput = root.findViewById(R.id.time_picker_input);
        mDatePickerInput = root.findViewById(R.id.date_picker_input);
        mLocationTextInput = root.findViewById(R.id.ActivityPLaceEditText);
        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDSiR_IHNqTBXhKXuEBWlkftFF_jLO8rBU");

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(getContext());

        mSeekBar = root.findViewById(R.id.seekBar);
        final TextInputEditText seekBarProgress = root.findViewById(R.id.minPeopleInputEditText);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String[] TAGS = new String[]{"Fishing", "Skating", "Puzzles", "Karting", "Fishing", "Skating", "Puzzles", "Karting"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.ropdown_menu_popup_item,
                        TAGS);

        MultiAutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.tagsAutoCompleteTextView);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mDatePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mTimePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });


//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if (i > 0 && seekBarProgress.getText() == null) {
//                    seekBarProgress.setText(i);
//                } else if (i > 0 && seekBarProgress.getText() != null) {
//                    seekBarProgress.setText(String.valueOf(Integer.parseInt(seekBarProgress.getText().toString()) + i));
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        // Set the fields to specify which types of place data to
// return after the user has made a selection.


        mLocationTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


//        String query = String.valueOf(mLocationTextInput.getText());
//
//        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//        RectangularBounds bounds = RectangularBounds.newInstance(
//                new LatLng(-33.8809, 151.184363),
//                new LatLng(-33.858754, 151.229596)
//        );
//
//        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//                .setLocationBias(bounds)
//                .setCountries("AU", "NZ")
//                .setTypeFilter(TypeFilter.ADDRESS)
//                .setSessionToken(token)
//                .setQuery(query)
//                .build();
//
//        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
//            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                Log.i(TAG, prediction.getPlaceId());
//                Log.i(TAG, prediction.getPrimaryText(null).toString());
//                mLocationTextInput.setText(prediction.getPrimaryText(null).toString());
//            }
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                ApiException apiException = (ApiException) exception;
//                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//            }
//        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddKickViewModel.class);
        // TODO: Use the ViewModel
    }


    public void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment(mDatePickerInput);
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {

    }


    public void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment(mTimePickerInput);
        newFragment.show(getChildFragmentManager(), "timePicker");
    }

    public void processTimePickerResult(int hour, int minute) {
        String hour_string = Integer.toString(hour);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string + ":" + minute_string);
        mTimePickerInput.setText(timeMessage);
        Log.e(TAG, timeMessage);

    }

    private void addNewKick() {


    }

    private void writenewKickToDb(String kickTitle, String kickLocation, String kickDate, String kickTime,
                                  String alreadyAttendingPeeps, String requiredPeeps, String imageUrl) {
        String key = mDatabase.child("kicks").push().getKey();
        Activity activity = new Activity(kickTitle, kickTime, kickDate, kickLocation,
                alreadyAttendingPeeps, requiredPeeps, imageUrl);


    }


}
