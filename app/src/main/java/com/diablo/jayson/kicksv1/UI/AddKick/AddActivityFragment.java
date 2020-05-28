package com.diablo.jayson.kicksv1.UI.AddKick;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Adapters.TagListAdapter;
import com.diablo.jayson.kicksv1.ApiThings;
import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddActivityFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, TagListAdapter.OnTagSelectedListener {

    private static final String TAG = AddActivityFragment.class.getSimpleName();

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

    private AddKickViewModel viewModel;
    private Activity activityMain;
    private String activityLocation = "";

    //Main Dash Stuff
    private RelativeLayout addActivityMainDashRelativeLayout, peopleCardOverlay, costCardOverlay, tagCardOverlay,
            dateTimeCardOverlay, locationCardOverlay, loadingScreen;
    private EditText activityTitleEditText;
    private CardView addActivityPeopleCard, addActivityCostCard, addActivityTagCard, addActivityTimeAndDateCard,
            addActivityLocationCard;
    private TextView activityLocationTextView;
    private ImageView peopleCardImageView, tagCardImageView, costCardImageView, locationCardImageView, timeDateCardImage;
    private ExtendedFloatingActionButton createActivityFinishEfab;

    //People Stuff
    private RelativeLayout addActivityPeopleRelativeLayout;
    private EditText activityMinPeopleEditText, activityMaxPeopleEditText, activityMinAgeEditText,
            activityMaxAgeEditText;
    private TextView makePrivateTextView;
    private Switch makePrivateSwitch;
    private FloatingActionButton peopleSelectionDoneButton;

    //Date Time Stuff
    private RelativeLayout addActivityDateTimeRelativeLayout;
    private DatePicker activityDatePicker;
    private TimePicker activityTimePicker;
    private NumberPicker activityDurationPicker;
    private FloatingActionButton dateTimeSelectionDoneButton;

    //Tag Stuff
    private RelativeLayout addActivityTagRelativeLayout, selectedTagOverlayRelativeLayout;
    private EditText searchTagsEditText;
    private RecyclerView tagsRecyclerView;
    private CardView selectedTagCard;
    private TextView selectedTagTextView, selectedTagDescriptionTextView;
    private ImageView selectedTagImageView, closeTagRelativeLayoutIcon;
    private FloatingActionButton tagSelectionDoneButton;
    private ArrayList<Tag> allTags;
    private AddActivityFragment listener;
    private Tag activityTag;

    //Location Stuff
    private RelativeLayout addActivityLocationRelativeLayout;
    private TextView selectedLocation;
    private EditText searchLocationEditText;
    private FloatingActionButton locationSelectionDone;

    //Cost Stuff
    private RelativeLayout addActivityCostRelativeLayout;
    private EditText activityCostEditText;
    private FloatingActionButton costInputDoneButton;


    public static AddActivityFragment newInstance() {
        return new AddActivityFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_activity, container, false);
        activityMain = new Activity();
        activityMain.setActivityPrivate(false);

        //Main Dash Views
        addActivityMainDashRelativeLayout = root.findViewById(R.id.add_activity_main_dash_relative_layout);
        peopleCardOverlay = root.findViewById(R.id.peopleCardOverlay);
        costCardOverlay = root.findViewById(R.id.costCardOverlay);
        tagCardOverlay = root.findViewById(R.id.tagCardOverlay);
        dateTimeCardOverlay = root.findViewById(R.id.dateTimeCardOverlay);
        locationCardOverlay = root.findViewById(R.id.locationCardOverlay);
        activityTitleEditText = root.findViewById(R.id.activity_title_edit_text);
        addActivityPeopleCard = root.findViewById(R.id.add_activity_people_card);
        addActivityCostCard = root.findViewById(R.id.add_activity_cost_card);
        addActivityTagCard = root.findViewById(R.id.add_activity_tag_card);
        addActivityLocationCard = root.findViewById(R.id.add_activity_location_card);
        addActivityTimeAndDateCard = root.findViewById(R.id.add_activity_time_date_card);
        activityLocationTextView = root.findViewById(R.id.activity_location_text_view);
        createActivityFinishEfab = root.findViewById(R.id.create_activity_finish_efab);
        peopleCardImageView = root.findViewById(R.id.people_card_image_view);
        tagCardImageView = root.findViewById(R.id.tag_card_image_view);
        costCardImageView = root.findViewById(R.id.cost_card_image_view);
        locationCardImageView = root.findViewById(R.id.location_card_image_view);
        timeDateCardImage = root.findViewById(R.id.time_date_card_image_view);
        loadingScreen = root.findViewById(R.id.loading_screen);

        //People Stuff
        addActivityPeopleRelativeLayout = root.findViewById(R.id.add_activity_people_relative_layout);
        activityMinPeopleEditText = root.findViewById(R.id.activity_min_people_edit_text);
        activityMaxPeopleEditText = root.findViewById(R.id.activity_max_people_edit_text);
        activityMinAgeEditText = root.findViewById(R.id.activity_min_age_edit_text);
        activityMaxAgeEditText = root.findViewById(R.id.activity_max_age_edit_text);
        makePrivateTextView = root.findViewById(R.id.makePrivateTextView);
        makePrivateSwitch = root.findViewById(R.id.makePrivateSwitch);
        peopleSelectionDoneButton = root.findViewById(R.id.peopleSelectionDoneButton);

        //Date Time Views
        addActivityDateTimeRelativeLayout = root.findViewById(R.id.add_activity_time_date_relative_layout);
        activityDatePicker = root.findViewById(R.id.activity_date_picker);
        activityTimePicker = root.findViewById(R.id.activity_time_picker);
        activityDurationPicker = root.findViewById(R.id.durationPicker);
        dateTimeSelectionDoneButton = root.findViewById(R.id.dateTimeSelectionDoneButton);

        //Tag Views
        addActivityTagRelativeLayout = root.findViewById(R.id.add_activity_tag_relative_layout);
        selectedTagOverlayRelativeLayout = root.findViewById(R.id.selectedTagOverlayRelativelayout);
        searchTagsEditText = root.findViewById(R.id.searchTagsEditText);
        tagsRecyclerView = root.findViewById(R.id.tags_recycler_view);
        selectedTagCard = root.findViewById(R.id.selectedTagCard);
        selectedTagTextView = root.findViewById(R.id.selectedTagTextView);
        selectedTagDescriptionTextView = root.findViewById(R.id.selectedTagDescriptionTextView);
        selectedTagImageView = root.findViewById(R.id.selected_tag_image_view);
        tagSelectionDoneButton = root.findViewById(R.id.tagSelectionDoneButton);
        closeTagRelativeLayoutIcon = root.findViewById(R.id.closeTagRelativelayoutButton);
        //Tag Data
        allTags = new ArrayList<Tag>();

        //Location Views
        selectedLocation = root.findViewById(R.id.setTheLocationTextView);
        searchLocationEditText = root.findViewById(R.id.searchLocationEditText);
        locationSelectionDone = root.findViewById(R.id.locationSelectionDoneButton);
        addActivityLocationRelativeLayout = root.findViewById(R.id.add_activity_location_relative_layout);

        //Cost Views
        addActivityCostRelativeLayout = root.findViewById(R.id.add_activity_cost_relative_layout);
        activityCostEditText = root.findViewById(R.id.activity_cost_edit_text);
        costInputDoneButton = root.findViewById(R.id.costInputDoneButton);


        //Main Dash Implementation
        peopleCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityPeopleRelativeLayout.setVisibility(View.VISIBLE);
                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        costCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityCostRelativeLayout.setVisibility(View.VISIBLE);
                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        tagCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagsFromDb();
                addActivityTagRelativeLayout.setVisibility(View.VISIBLE);
                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        dateTimeCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityDateTimeRelativeLayout.setVisibility(View.VISIBLE);
                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        locationCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityLocationRelativeLayout.setVisibility(View.VISIBLE);
                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });

        //People Implementation
        peopleSelectionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityMinPeopleEditText.getText().toString().isEmpty() || activityMaxPeopleEditText.getText().toString().isEmpty()
                        || activityMinAgeEditText.getText().toString().isEmpty() || activityMaxAgeEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Missing Details", Toast.LENGTH_LONG).show();
                    if (activityMinPeopleEditText.getText().toString().isEmpty()) {
                        activityMinPeopleEditText.setError("Input a Number");
                    } else if (activityMaxPeopleEditText.getText().toString().isEmpty()) {
                        activityMaxPeopleEditText.setError("Input a Number");
                    } else if (activityMinAgeEditText.getText().toString().isEmpty()) {
                        activityMinAgeEditText.setError("Input a Number");
                    } else if (activityMaxAgeEditText.getText().toString().isEmpty()) {
                        activityMaxAgeEditText.setError("Input a Number");
                    }
                } else if (Integer.parseInt(activityMinPeopleEditText.getText().toString()) > Integer.parseInt(activityMaxPeopleEditText.getText().toString())) {
                    activityMinPeopleEditText.setError("Invalid input");
                } else if (Integer.parseInt(activityMinAgeEditText.getText().toString()) > Integer.parseInt(activityMaxAgeEditText.getText().toString())) {
                    activityMinPeopleEditText.setError("Invalid input");
                } else {
                    updateActivityPeople();
                    addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                    addActivityPeopleRelativeLayout.setVisibility(View.GONE);
                    peopleCardImageView.setVisibility(View.VISIBLE);
                }

            }
        });
        makePrivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activityMain.setActivityPrivate(isChecked);
                    makePrivateTextView.setText(R.string.private_text);
                } else {
                    activityMain.setActivityPrivate(false);
                    makePrivateTextView.setText(R.string.make_private_text);
                }
            }
        });


        //Tag Implemetation
        activityTag = new Tag();
        searchTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tagName = s.toString();
                if (tagName.isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("tags")
                            .whereEqualTo("tagName", "")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                            Log.e(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                            allTags.add(new Tag(documentSnapshot.toObject(Tag.class).getTagName(),
                                                    documentSnapshot.toObject(Tag.class).getTagShortDescription(),
                                                    documentSnapshot.toObject(Tag.class).getTagLocation(),
                                                    documentSnapshot.toObject(Tag.class).getTagCost(),
                                                    documentSnapshot.toObject(Tag.class).getTagIconUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(),
                                                    documentSnapshot.toObject(Tag.class).getTagLocationName()));
                                            for (int i = 0; i < allTags.size(); i++) {
                                                Log.w(TAG, allTags.get(i).getTagName());
                                            }
                                        }
                                        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, listener);
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
                                        tagsRecyclerView.setLayoutManager(gridLayoutManager);
                                        tagsRecyclerView.setAdapter(mAdapter);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    ArrayList<Tag> filteredTags = new ArrayList<Tag>();
                    for (Tag tag : allTags) {
                        if (tag.getTagName().toLowerCase(Locale.ROOT).contains(s)) {
                            filteredTags.add(new Tag(tag.getTagName(), tag.getTagShortDescription(), tag.getTagLocation(), tag.getTagCost(),
                                    tag.getTagIconUrl(), tag.getTagImageLargeUrl(), tag.getTagLocationName()));
                        }
                    }
                    TagListAdapter mAdapter = new TagListAdapter(getContext(), filteredTags, listener);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
                    tagsRecyclerView.setLayoutManager(gridLayoutManager);
                    tagsRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tagSelectionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.setActivityTag(activityTag);
                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                addActivityTagRelativeLayout.setVisibility(View.GONE);
                selectedTagOverlayRelativeLayout.setVisibility(View.GONE);
                selectedTagCard.setVisibility(View.GONE);
                tagCardImageView.setVisibility(View.VISIBLE);
            }
        });
        closeTagRelativeLayoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                addActivityTagRelativeLayout.setVisibility(View.GONE);
            }
        });
        selectedTagOverlayRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTagOverlayRelativeLayout.setVisibility(View.GONE);
                selectedTagCard.setVisibility(View.GONE);
            }
        });

        //Date Time Implementation
        final String[] durations = {"<1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};


        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        activityDurationPicker.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        activityDurationPicker.setMaxValue(durations.length - 1); //to array last value

        activityDurationPicker.setDisplayedValues(durations);

        //Sets whether the selector wheel wraps when reaching the min/max value.
        activityDurationPicker.setWrapSelectorWheel(true);
        final long[] activitySeconds = new long[1];

        activityDurationPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (durations[newVal].equals("<1")) {
                    activitySeconds[0] = 59 * 60;
                } else {
                    long hours = Long.parseLong(durations[newVal]);
                    activitySeconds[0] = hours * 3600;
                }
            }
        });
        Calendar calendar = Calendar.getInstance();

        dateTimeSelectionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(activityDatePicker.getYear(), activityDatePicker.getMonth(), activityDatePicker.getDayOfMonth());
                com.google.firebase.Timestamp activityDateTimestamp = new com.google.firebase.Timestamp(calendarDate.getTime());
                long timestampSecondsDate = calendarDate.getTimeInMillis();
//                Timestamp activityDateTimestamp = new Timestamp(timestampSecondsDate);
                activityMain.setActivityDate(activityDateTimestamp);
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.set(activityDatePicker.getYear(), activityDatePicker.getMonth(),
                        activityDatePicker.getDayOfMonth(), activityTimePicker.getHour(), activityTimePicker.getMinute());
                com.google.firebase.Timestamp activityStartTimeTimestamp = new com.google.firebase.Timestamp(calendarTime.getTime());
                long timestampSecondsTime = calendarTime.getTimeInMillis();
                Timestamp activityStartTimestamp = new Timestamp(timestampSecondsTime);
                activityMain.setActivityStartTime(activityStartTimeTimestamp);
                calendarTime.add(Calendar.SECOND, (int) activitySeconds[0]);
                Date activityEndTime = calendarTime.getTime();
                com.google.firebase.Timestamp activityEndTimestamp = new com.google.firebase.Timestamp(activityEndTime);
//                Timestamp activityEndTimestamp = new Timestamp(calendarTime.getTimeInMillis());
                activityMain.setActivityEndTime(activityEndTimestamp);
                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                addActivityDateTimeRelativeLayout.setVisibility(View.GONE);
                timeDateCardImage.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(),DateTimeFormat.mediumTime().print(activityMain.getActivityStartTime().getTime()),Toast.LENGTH_SHORT).show();
            }
        });

        //Location Implementation
        Places.initialize(requireActivity().getApplicationContext(), "");
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
                locationCardImageView.setVisibility(View.VISIBLE);
            }
        });

        //Cost Implementation
        costInputDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityCostEditText.getText().toString().isEmpty()) {
                    activityCostEditText.setError("Enter amount");
                } else {
                    updateActivityCost();
                    addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
                    addActivityCostRelativeLayout.setVisibility(View.GONE);
                    costCardImageView.setVisibility(View.VISIBLE);
                }

            }
        });

        //Uploading Activity to Db
        createActivityFinishEfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityTitleEditText.getText().toString().isEmpty()) {
                    activityTitleEditText.setError("Set a Suitable Title");
                } else if (activityMinPeopleEditText.getText().toString().isEmpty() ||
                        selectedTagTextView.getText().toString().isEmpty() ||
                        activityCostEditText.getText().toString().isEmpty() ||
                        activityLocationTextView.getText().toString().isEmpty()) {

                    Toast.makeText(getContext(), "Missing fields", Toast.LENGTH_LONG).show();

                } else {
                    String activityTitle = activityTitleEditText.getText().toString();
                    activityMain.setActivityTitle(activityTitle);
                    updateAttendeesAndHostAndTime();
                    showLoadingScreen();
                    FirebaseFirestore.getInstance().collection("activities")
                            .add(activityMain)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    hideLoadingScreen();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });
//        loadingScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                return;
//            }
//        });

        return root;
    }

    private void hideLoadingScreen() {
        loadingScreen.setVisibility(View.GONE);
    }

    private void showLoadingScreen() {
        loadingScreen.setVisibility(View.VISIBLE);
        loadingScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    private void updateAttendeesAndHostAndTime() {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add(activityTag.getTagName());

        ArrayList<AttendingUser> attendingUsers = new ArrayList<AttendingUser>();
        attendingUsers.add(FirebaseUtil.getAttendingUser());

        activityMain.setTags(tags);
        activityMain.setActivityAttendees(attendingUsers);
        activityMain.setHost(FirebaseUtil.getHost());
        activityMain.setActivityUploaderId(Objects.requireNonNull(FirebaseUtil.getHost()).getUid());
        Calendar calendarUploadedTime = Calendar.getInstance();
        Timestamp uploadedTimestamp = new Timestamp(calendarUploadedTime.getTimeInMillis());
        activityMain.setActivityUploadedTime(com.google.firebase.Timestamp.now());

    }


    private void updateActivityPeople() {

        int activityMinPeople = Integer.parseInt(activityMinPeopleEditText.getText().toString());
        int activityMaxPeople = Integer.parseInt(activityMaxPeopleEditText.getText().toString());
        int activityMinAge = Integer.parseInt(activityMinAgeEditText.getText().toString());
        int activityMaxAge = Integer.parseInt(activityMaxAgeEditText.getText().toString());
        activityMain.setActivityMinRequiredPeople(activityMinPeople);
        activityMain.setActivityMaxRequiredPeople(activityMaxPeople);
        activityMain.setActivityMinAge(activityMinAge);
        activityMain.setActivityMaxAge(activityMaxAge);
    }

    private void updateActivityLocation() {
        if (activityLocation.isEmpty()) {
            activityMain.setActivityLocationCordinates(new GeoPoint(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
            getAddress(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
            addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
            addActivityLocationRelativeLayout.setVisibility(View.GONE);
        } else {
            addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
            addActivityLocationRelativeLayout.setVisibility(View.GONE);
        }
//        Toast.makeText(getContext(), String.valueOf(activityMain.getKickLocationCordinates()), Toast.LENGTH_LONG).show();


    }

    private void updateActivityCost() {
        String activityCost = activityCostEditText.getText().toString();
        activityMain.setActivityCost(activityCost);
    }

    private void loadTagsFromDb() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tags")
                .whereGreaterThan("tagName", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.e(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                allTags.add(new Tag(documentSnapshot.toObject(Tag.class).getTagName(),
                                        documentSnapshot.toObject(Tag.class).getTagShortDescription(),
                                        documentSnapshot.toObject(Tag.class).getTagLocation(),
                                        documentSnapshot.toObject(Tag.class).getTagCost(),
                                        documentSnapshot.toObject(Tag.class).getTagIconUrl(),
                                        documentSnapshot.toObject(Tag.class).getTagImageLargeUrl(),
                                        documentSnapshot.toObject(Tag.class).getTagLocationName()));
                                for (int i = 0; i < allTags.size(); i++) {
                                    Log.w(TAG, allTags.get(i).getTagName());
                                }
                            }
                            initializeRecyclerViewWithTags();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void initializeRecyclerViewWithTags() {
        listener = this;
        TagListAdapter mAdapter = new TagListAdapter(getContext(), allTags, listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.HORIZONTAL, false);
        tagsRecyclerView.setLayoutManager(gridLayoutManager);
        tagsRecyclerView.setAdapter(mAdapter);
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
                            activityLocationTextView.setText(formatted_address);
                            activityMain.setActivityLocationName(formatted_address);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);

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
                activityLocationTextView.setText(place.getName());
                activityMain.setActivityLocationName(place.getName());
                activityMain.setActivityLocationCordinates(new GeoPoint(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude));
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
                        map.setMyLocationEnabled(true);
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

    @Override
    public void onTagListener(Tag tag) {
        activityTag = tag;
        selectedTagOverlayRelativeLayout.setVisibility(View.VISIBLE);
        selectedTagCard.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(tag.getTagImageLargeUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(selectedTagImageView);
        selectedTagDescriptionTextView.setText(tag.getTagName());
    }
}
