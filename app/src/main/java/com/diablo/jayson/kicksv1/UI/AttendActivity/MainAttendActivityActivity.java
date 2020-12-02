package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.ApiThings;
import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.ChatItem;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class MainAttendActivityActivity extends AppCompatActivity implements OnMapReadyCallback,
        ExitActivityDialog.ExitActivityDialogListener, AttendeesLargeAdapter.OnAttendeeSelectedListener {

    private GroupChatAdapter groupChatAdapter;

    private RelativeLayout dashItemsRelativeLayout;
    private RelativeLayout chatActualRelativeLayout;
    private RelativeLayout attendeesActualRelativeLayout;
    private RelativeLayout detailsActualRelativeLayout;
    private FrameLayout dashItemDetailsFramelayout;
    private RecyclerView attendeesRecycler, attendeesActualRecycler, chatRecycler, chatActualRecycler;
    private ProgressBar attendeesProgress;
    private ArrayList<AttendingUser> attendingUsersData;
    private CardView chatCard;
    private ImageView activityImageView;
    private RelativeLayout chatCardOverlay;
    private RelativeLayout attendeesCardOverlay;
    private RelativeLayout detailsCardOverlay;
    private ImageView sendMessageButton;
    private EditText messageEdit, searchTagsEditText;
    private TextView activityDashTimeText, activityDashDateText, activityDashLocationText, activityDashTagText;
    private TextView activityLocationActualTextView, activityTimeActualTextView, activityDateActualTextView;
    private FloatingActionButton shareActivityFab, exitActivityFab;

    private LatLng activityLocation;
    private GoogleMap googleMap;
    private String title;
    private String activityIdMain;

    private Location location;
    private Timestamp startTime;
    private Timestamp endTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_attend_activity);
        attendeesRecycler = findViewById(R.id.attendeesRecycler);
        attendeesActualRecycler = findViewById(R.id.attendeesActualRecycler);
        chatRecycler = findViewById(R.id.chatRecycler);
        chatActualRecycler = findViewById(R.id.chatActualRecycler);
        dashItemsRelativeLayout = findViewById(R.id.dash_items_relative_Layout);
        chatActualRelativeLayout = findViewById(R.id.chatActualRelativeLayout);
        attendeesActualRelativeLayout = findViewById(R.id.attendeesActualRelativeLayout);
        detailsActualRelativeLayout = findViewById(R.id.activityDetailsActualRelativeLayout);
//        dashItemDetailsFramelayout = findViewById(R.id.dashItems_fragment_container);
//        dashItemDetailsFramelayout.setVisibility(View.GONE);
        chatCardOverlay = findViewById(R.id.chatCardOverlay);
        attendeesCardOverlay = findViewById(R.id.attendeesCardOverlay);
        detailsCardOverlay = findViewById(R.id.detailsCardOverlay);
        chatCard = findViewById(R.id.activityChatCard);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messageEdit = findViewById(R.id.messageEditText);
        activityDashTimeText = findViewById(R.id.activityDashTimeText);
        activityDashDateText = findViewById(R.id.activityDashDateText);
        activityDashLocationText = findViewById(R.id.activityDashLocationText);
        activityDashTagText = findViewById(R.id.activityDashTagTextView);
        activityImageView = findViewById(R.id.activityImageView);
        activityLocationActualTextView = findViewById(R.id.activity_actual_location_text_view);
        activityTimeActualTextView = findViewById(R.id.activity_time_actual_text_view);
        activityDateActualTextView = findViewById(R.id.activity_actual_date_text_view);
        shareActivityFab = findViewById(R.id.shareActivityFab);
        exitActivityFab = findViewById(R.id.exitActivityFab);


        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String activityId = bundle.getString("activityId");
        activityIdMain = activityId;
        boolean fromGroupMessage = bundle.getBoolean("fromGroupMessages");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (fromGroupMessage) {
            chatActualRelativeLayout.setVisibility(View.VISIBLE);
            dashItemsRelativeLayout.setVisibility(View.GONE);
        } else {
            dashItemsRelativeLayout.setVisibility(View.VISIBLE);
        }

        Log.e("nnn", activityId);

        Places.initialize(this,ApiThings.places_api_key);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.activity_map_location);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.attend_activity_toolbar);
        setSupportActionBar(myToolbar);


        db.collection("activities").document(activityId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    Log.e("date boi", documentSnapshot.toObject(Activity.class).getActivityTitle());
                    String activityDate = DateFormat.getMediumDateFormat(getApplicationContext()).format(documentSnapshot.toObject(Activity.class).getActivityStartDate().toDate());
                    String activityStartTime = DateFormat.getTimeFormat(getApplicationContext()).format(documentSnapshot.toObject(Activity.class).getActivityStartTime().toDate());
//                    String activityEndTime = DateFormat.getTimeFormat(getApplicationContext()).format(documentSnapshot.toObject(Activity.class).getActivityEndTime().toDate());


//                    String activityTime = activityStartTime + " - " + activityEndTime;
//                    String activityImageUrl = documentSnapshot.toObject(Activity.class).getImageUrl();
                    String activityTitle = documentSnapshot.toObject(Activity.class).getActivityTitle();
                    String activityLocationName = documentSnapshot.toObject(Activity.class).getActivityLocationName();
                    String activityTag = documentSnapshot.toObject(Activity.class).getActivityTag().getTagName();
                    startTime = documentSnapshot.toObject(Activity.class).getActivityStartTime();
                    title = activityTitle;
                    activityLocation = new LatLng(documentSnapshot.toObject(Activity.class).getActivityLocationCoordinates().getLatitude(),
                            documentSnapshot.toObject(Activity.class).getActivityLocationCoordinates().getLongitude());
//                    activityDashTimeText.setText(activityTime);
                    activityDashLocationText.setText(activityLocationName);
                    activityDashTagText.setText(activityTag);
                    activityDashDateText.setText(activityDate);
                    activityLocationActualTextView.setText(activityLocationName);
//                    activityTimeActualTextView.setText(activityTime);
                    activityDateActualTextView.setText(activityDate);
//                    Glide.with(getApplicationContext())
//                            .load(activityImageUrl)
//                            .into(activityImageView);
                    Objects.requireNonNull(getSupportActionBar()).setTitle(activityTitle);
//                    googleMap.clear();
//                    googleMap.addMarker(new MarkerOptions().position(activityLocation)
//                            .title(activityTitle));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(activityLocation, 15));

                    attendingUsersData = new ArrayList<AttendingUser>();
                    attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getActivityAttendees();
//                            Log.e("yooooo", String.valueOf(attendingUsersData.size()));
//                            Log.e("yooooo", attendingUsersData.get(0).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(1).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(2).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(3).getUserName());


                }
//                        initializeRecyclerWithAttendees();
//                        Log.e("skkdnskn", attendingUsersData.get(3).getUserName());
                AttendeesAdapter attendeesAdapter = new AttendeesAdapter(MainAttendActivityActivity.this, attendingUsersData);
                attendeesRecycler.setLayoutManager(new GridLayoutManager(MainAttendActivityActivity.this, 2, GridLayoutManager.HORIZONTAL, false));
                attendeesRecycler.setAdapter(attendeesAdapter);
//                AttendeesLargeAdapter attendeesLargeAdapter = new AttendeesLargeAdapter(MainAttendActivityActivity.this, attendingUsersData,listener);
//                attendeesActualRecycler.setLayoutManager(new GridLayoutManager(MainAttendActivityActivity.this, 2, GridLayoutManager.VERTICAL, false));
//                attendeesActualRecycler.setAdapter(attendeesLargeAdapter);
            }
        });

        exitActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment exitActivityDialogFragment = new ExitActivityDialog();
                exitActivityDialogFragment.show(getSupportFragmentManager(), "exit");

            }
        });

        shareActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is an invitation to join "+FirebaseUtil.getHost().getUserName()+" in a Kicks Activity");
                Intent appIntent = new Intent();
                appIntent.setAction(Intent.ACTION_VIEW);
//                appIntent.setAction("com.color.kicks.SHARE_ACTION");
                appIntent.setPackage("com.color.kicks");
//                appIntent.setPackage("com.diablo.jayson.kicksv1");
                ComponentName componentName = new ComponentName("com.color.kicks", "com.diablo.jayson.kicksv1.MainActivity");
                appIntent.setComponent(componentName);
                appIntent.putExtra("activityId", activityId);
                String appUri = appIntent.toUri(Intent.URI_ANDROID_APP_SCHEME);
                sendIntent.putExtra(Intent.EXTRA_TEXT, appUri);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });


        Query query = FirebaseFirestore.getInstance()
                .collection("activities")
                .document(activityId)
                .collection("chatsession")
                .orderBy("timestamp", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<ChatItem> options = new FirestoreRecyclerOptions.Builder<ChatItem>()
                .setQuery(query, ChatItem.class)
                .build();
        groupChatAdapter = new GroupChatAdapter(options, getApplicationContext());
        int gridColumnCount = 1;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecycler.setLayoutManager(layoutManager);

//                layoutManager.setStackFromEnd(true);
//                layoutManager.setReverseLayout(true);


        chatRecycler.post(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(Integer.MAX_VALUE, 20) {
                    public void onTick(long millis) {
                        chatRecycler.scrollBy(0, 1);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });
        chatRecycler.setAdapter(groupChatAdapter);
//                chatAdapter.notifyDataSetChanged();
        LinearLayoutManager chatActuallayoutManager = new LinearLayoutManager(this);

        chatActualRecycler.setLayoutManager(chatActuallayoutManager);
        chatActualRecycler.setAdapter(groupChatAdapter);
        groupChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatActualRecycler.scrollToPosition(groupChatAdapter.getItemCount() - 1);
            }
        });

        groupChatAdapter.startListening();

        chatCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatActualRelativeLayout.setVisibility(View.VISIBLE);
                dashItemsRelativeLayout.setVisibility(View.GONE);
            }
        });
        attendeesCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendeesActualRelativeLayout.setVisibility(View.VISIBLE);
                dashItemsRelativeLayout.setVisibility(View.GONE);
            }
        });
        detailsCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsActualRelativeLayout.setVisibility(View.VISIBLE);
                dashItemsRelativeLayout.setVisibility(View.GONE);
                if (googleMap != null) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(activityLocation)
                            .title(title));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(activityLocation, 15));
                } else {

                }
            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String message = messageEdit.getText().toString();
                if (!message.isEmpty()) {
                    ChatItem messageItem = new ChatItem();
                    messageItem.setMessage(message);
                    assert user != null;
                    messageItem.setSenderName(user.getDisplayName());
                    messageItem.setSenderPicUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());
                    messageItem.setSenderUid(user.getUid());
                    messageItem.setTimestamp(Timestamp.now());

                    FirebaseFirestore.getInstance().collection("activities").document(activityId)
                            .collection("chatsession")
                            .add(messageItem)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.e("Yellow", "Added Message");
                                    messageEdit.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Try Sending Message Again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attend_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_calendar:
                Intent addEventIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, title)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                if (addEventIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(addEventIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (chatActualRelativeLayout.getVisibility() == View.VISIBLE) {
            chatActualRelativeLayout.setVisibility(View.GONE);
            dashItemsRelativeLayout.setVisibility(View.VISIBLE);
        } else if (attendeesActualRelativeLayout.getVisibility() == View.VISIBLE) {

            attendeesActualRelativeLayout.setVisibility(View.GONE);
            dashItemsRelativeLayout.setVisibility(View.VISIBLE);
        } else if (detailsActualRelativeLayout.getVisibility() == View.VISIBLE) {
            detailsActualRelativeLayout.setVisibility(View.GONE);
            dashItemsRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
//

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

//        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0))
//                .title(activityTitle));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 15));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("activities").document(activityIdMain);
        documentReference.update("activityAttendees", FieldValue.arrayRemove(FirebaseUtil.getAttendingUser()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
//                            mainActivityIntent.setAction(Intent.ACTION_VIEW);
                            startActivity(mainActivityIntent);
                        }
                    }
                });
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        return;
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {
        return;
    }

    @Override
    public void onAttendeeSelected(AttendingUser attendingUser) {

    }
}
