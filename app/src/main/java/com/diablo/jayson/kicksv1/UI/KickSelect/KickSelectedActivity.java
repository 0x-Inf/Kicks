package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityActivity;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class KickSelectedActivity extends AppCompatActivity implements AvailableActivitiesAdapter.OnAvailableActivitySelected {
//    private ResultProfileBinding binding;

    private ImageView kickCardImageView, noAvailableActivitiesImage;
    private ImageButton menuImageButton;
    private Button createActivityButton;
    private TextView kickShortDescription, availableActivitiesTitle;
    private RecyclerView availableActivitiesRecyclerView;
    private CardView noAvailableActivities;

    private AvailableActivitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_selected);
        FirebaseApp.initializeApp(this);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Kick kick = (Kick) bundle.get("kick");
        assert kick != null;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.kick_selected_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(kick.getKickName());
//        Toast.makeText(getApplicationContext(),kick.getKickName(),Toast.LENGTH_LONG).show();


        kickCardImageView = findViewById(R.id.kick_selected_image_view);
        availableActivitiesRecyclerView = findViewById(R.id.availableActivitiesRecycler);
        createActivityButton = findViewById(R.id.createActivityButton);
        kickShortDescription = findViewById(R.id.kickShortDescriptionTextView);
//        kickShortDescription.setText(kick.getKickShortDescription());
        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(KickSelectedActivity.this, R.id.selected_nav_host_fragment).navigate(R.id.action_global_addActivityFragment);
                startActivity(new Intent(KickSelectedActivity.this, AddActivityActivity.class));
            }
        });

        checkForAvailabelActivities();


        Glide.with(this)
                .load(kick.getKickMainImageUrl())
                .into(kickCardImageView);
    }

    private void checkForAvailabelActivities() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Kick kick = (Kick) bundle.get("kick");
        assert kick != null;
        ArrayList<String> tags = kick.getKickTags();
        FirebaseFirestore.getInstance().collection("activities")
                .whereArrayContainsAny("tags", tags)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {

                        } else {
                            Query query = FirebaseFirestore.getInstance()
                                    .collection("activities")
                                    .whereArrayContainsAny("tags", tags);

                            FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                                    .setQuery(query, Activity.class)
                                    .build();


                            adapter = new AvailableActivitiesAdapter(options, this::onAvailableActivitySelected);
                            availableActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            availableActivitiesRecyclerView.setAdapter(adapter);
                            adapter.startListening();
                        }
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    @Override
    public void onAvailableActivitySelected(Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (user.getDisplayName().isEmpty()) {
            Toast.makeText(this, "Please Sign Up", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> users = new ArrayList<String>();
            for (int i = 0; i < activity.getActivityAttendees().size(); i++) {
                users.add(activity.getActivityAttendees().get(i).getUid());
            }
            if (users.contains(user.getUid())) {
                Log.e("names", activity.getActivityAttendees().get(0).getUserName());
                Intent attendActivity = new Intent(KickSelectedActivity.this, AttendActivityActivity.class);
                attendActivity.putExtra("activityId", activity.getActivityId());
                attendActivity.putExtra("activityLatitude", activity.getActivityLocationCoordinates().getLatitude());
                attendActivity.putExtra("activityLongitude", activity.getActivityLocationCoordinates().getLongitude());
                attendActivity.putExtra("alreadyAttending", true);
                startActivity(attendActivity);
            } else {
                Intent attendActivity = new Intent(KickSelectedActivity.this, AttendActivityActivity.class);
                attendActivity.putExtra("activityId", activity.getActivityId());
                attendActivity.putExtra("alreadyAttending", false);
                startActivity(attendActivity);
            }
        }
    }
}
