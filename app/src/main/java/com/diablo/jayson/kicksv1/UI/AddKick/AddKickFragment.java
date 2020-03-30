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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

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
    private ArrayList<AttendingUser> mAttendees;

    private Activity activityMain;

    public static AddKickFragment newInstance() {
        return new AddKickFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton nextButton = view.findViewById(R.id.nextcreateActivityFab);
        RelativeLayout firstContent = view.findViewById(R.id.addKickFirstContent);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddatatoDb();
                mViewModel.setActivity1(activityMain);
                AddKick1Fragment nextFrag = new AddKick1Fragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayoutbase, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                firstContent.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_kick_fragment, container, false);
        mTimePickerInput = root.findViewById(R.id.time_picker_input);
        mDatePickerInput = root.findViewById(R.id.date_picker_input);
        mLocationTextInput = root.findViewById(R.id.ActivityPLaceEditText);
        mAddActivityToDb = root.findViewById(R.id.nextcreateActivityFab);
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

        String kickTitle = mActivityTitleInput.getText().toString();
        String kickLocation = mLocationTextInput.getText().toString();
        String kickTime = mTimePickerInput.getText().toString();
        String kickDate = mDatePickerInput.getText().toString();
        String kickMinRequiredPeople = mMinRequiredInput.getText().toString();
        String kickMxnRequiredPeople = mMaxRequiredInput.getText().toString();
        String tags = mTagsAutoCompleteInput.getText().toString();
        Host host = FirebaseUtil.getHost();

        String[] tagsList = tags.split(",", -2);
//            String[] tagList = {};
        String tag = Arrays.toString(tagsList);

        tag = tag.replace("[", "");
        tag = tag.replace("]", "");

        String tagArray[] = tag.split(",");

        List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));
        mAttendees = new ArrayList<AttendingUser>();

        activityMain = new Activity(host, kickTitle, kickTime, kickTime, kickDate, kickLocation, new GeoPoint(1, 1), kickMinRequiredPeople,
                kickMxnRequiredPeople, "", "", "", tagList, Calendar.getInstance().getTimeInMillis(), FirebaseAuth.getInstance().getCurrentUser().getUid(), "", new Tag(), mAttendees, "");

        mLocationTextInput.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(getContext());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            mLocationTextInput.setText("CBD");
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
                mLocationTextInput.setText(String.format("%s%s", place.getName(), place.getId()));
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
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

    private void adddatatoDb() {


        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String kickTitle = mActivityTitleInput.getText().toString();
        String kickLocation = mLocationTextInput.getText().toString();
        String kickTime = mTimePickerInput.getText().toString();
        String kickDate = mDatePickerInput.getText().toString();
        String kickMinRequiredPeople = mMinRequiredInput.getText().toString();
        String kickMxnRequiredPeople = mMaxRequiredInput.getText().toString();
        String tags = mTagsAutoCompleteInput.getText().toString();
        Host host = FirebaseUtil.getHost();


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


            activityMain = new Activity(host, kickTitle, kickTime, kickTime, kickDate, kickLocation, new GeoPoint(1, 1), kickMinRequiredPeople,
                    kickMxnRequiredPeople, "", "", "", tagList, Calendar.getInstance().getTimeInMillis(),
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), "", new Tag(),
                    mAttendees, "");
            db.collection("users")
                    .whereEqualTo("userEmail", activityMain.getUploaderId())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                            User user = documentSnapshot.toObject(User.class);
                            activityMain.setUploaderId(user.getFirstName());
                        }
                    }
                }
            });
            mViewModel.setActivity1(activityMain);
            Log.e(TAG, activityMain.getkickTitle());
        }


    }


}
