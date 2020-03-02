package com.diablo.jayson.kicksv1.UI.AddKick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddKickFragment extends Fragment {

    private static final String TAG = AddKickFragment.class.getSimpleName();
    private AddKickViewModel mViewModel;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private TextInputEditText mTimePickerInput, mDatePickerInput, mLocationTextInput, mActivityTitleInput;
    private MultiAutoCompleteTextView mTagsAutoCompleteInput;
    private TextInputEditText mMaxRequiredInput, mMinRequiredInput;
    private TextInputLayout mActivityTitleInputLayout, mActivityLocationInputLayout, mActivityTimeInputLayout,
            mActivityDateInputLayout, mActivityMinPeopleInputLayout, mActivityMaxPeopleInputLayout,
            mActivityTagsInputLayout;
    private Button mPickALocationButton;
    private SeekBar mSeekBar;
    private DatabaseReference mDatabase;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private ExtendedFloatingActionButton mAddActivityToDb;

    public static AddKickFragment newInstance() {
        return new AddKickFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_kick_fragment, container, false);
        mTimePickerInput = root.findViewById(R.id.time_picker_input);
        mDatePickerInput = root.findViewById(R.id.date_picker_input);
        mLocationTextInput = root.findViewById(R.id.ActivityPLaceEditText);
        mAddActivityToDb = root.findViewById(R.id.createActivityFab);
        mActivityTitleInput = root.findViewById(R.id.kickNameEditText);
        mTagsAutoCompleteInput = root.findViewById(R.id.tagsAutoCompleteTextView);
        mMinRequiredInput = root.findViewById(R.id.minPeopleInputEditText);
        mMaxRequiredInput = root.findViewById(R.id.maxPeopleEditText);
        mActivityTitleInputLayout = root.findViewById(R.id.ActivityNameInput);
        mActivityLocationInputLayout = root.findViewById(R.id.ActivityPlaceInput);
        mActivityTimeInputLayout = root.findViewById(R.id.activityTimeInput);
        mActivityDateInputLayout = root.findViewById(R.id.activityDateInput);
        mActivityMinPeopleInputLayout = root.findViewById(R.id.minPeopleInputLayout);
        mActivityMaxPeopleInputLayout = root.findViewById(R.id.maxPeopleInputLayout);
        mActivityTagsInputLayout = root.findViewById(R.id.tagsAutoCompleteInputLayout);


        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDSiR_IHNqTBXhKXuEBWlkftFF_jLO8rBU");

// Create a new Places client instance
        PlacesClient placesClient = Places.createClient(getContext());

        mSeekBar = root.findViewById(R.id.seekBar);
//        final TextInputEditText seekBarProgress = root.findViewById(R.id.minPeopleInputEditText);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String[] TAGS = new String[]{"Fishing", "Skating", "Puzzles", "Karting", "Fishing", "Skating", "Puzzles", "Karting"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.ropdown_menu_popup_item,
                        TAGS);

//        MultiAutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.tagsAutoCompleteTextView);
        mTagsAutoCompleteInput.setAdapter(adapter);
        mTagsAutoCompleteInput.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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
        mAddActivityToDb.setOnClickListener(v -> adddatatoDb());

//        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
//                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME));
//
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                Log.w(TAG,"Place: "+ place.getName()+", "+ place.getId());
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });

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


        mLocationTextInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(getContext());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            mLocationTextInput.setText("CBD");
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mLocationTextInput.setText(place.getName() + place.getId());
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

    private void adddatatoDb() {


        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("Middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("a", 5);
        nestedData.put("b", false);

        user.put("ObjectExample", nestedData);

//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//
//        db.collection("data").document("one")
//                .set(user, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });

        String kickTitle = mActivityTitleInput.getText().toString();
        String kickLocation = mLocationTextInput.getText().toString();
        String kickTime = mTimePickerInput.getText().toString();
        String kickDate = mDatePickerInput.getText().toString();
        String kickMinRequiredPeople = mMinRequiredInput.getText().toString();
        String kickMxnRequiredPeople = mMaxRequiredInput.getText().toString();
        String tags = mTagsAutoCompleteInput.getText().toString();


        if (kickTitle.matches("") || kickLocation.matches("")
                || kickTime.matches("") || kickDate.matches("")
                || kickMinRequiredPeople.matches("") || kickMxnRequiredPeople.matches("")
                || tags.matches("")) {

//            if (kickTitle.matches("")) {
//                mActivityTitleInputLayout.setError("Input Title.E.g The Boys Hang out");
//            }

            Toast.makeText(getContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
        } else if (!kickTitle.matches("") && !kickLocation.matches("")
                && !kickTime.matches("") && !kickDate.matches("")
                && !kickMinRequiredPeople.matches("") && !kickMxnRequiredPeople.matches("")
                && !tags.matches("")) {

            String[] tagsList = tags.split(",", -2);
//            String[] tagList = {};
            String tag = Arrays.toString(tagsList);

            tag = tag.replace("[", "");
            tag = tag.replace("]", "");

            String tagArray[] = tag.split(",");

            List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));


            Log.e(TAG, Arrays.toString(tagsList));
            Log.e(TAG, tagsList.getClass().toString());


            Activity activity = new Activity(kickTitle, kickTime, kickDate, kickLocation, kickMinRequiredPeople,
                    kickMxnRequiredPeople, "", tagList, Calendar.getInstance().getTimeInMillis(), FirebaseAuth.getInstance().getCurrentUser().getUid());
            db.collection("activities").add(activity)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }


    }


}
