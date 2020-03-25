package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class KickSelectedActivity extends AppCompatActivity {
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(kick.getKickName());


        kickCardImageView = findViewById(R.id.kickCardImage);
        availableActivitiesRecyclerView = findViewById(R.id.availableActivitiesRecycler);
        noAvailableActivities = findViewById(R.id.noAVailableActivitiesCard);
        noAvailableActivitiesImage = findViewById(R.id.noAvailableActivities);
        noAvailableActivities.setVisibility(View.GONE);

        checkForAvailabelActivities();


        Glide.with(this)
                .load(kick.getKickCardImageUrl())
                .into(kickCardImageView);
    }

    private void checkForAvailabelActivities() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Kick kick = (Kick) bundle.get("kick");
        assert kick != null;
        ArrayList<String> tags = kick.getTags();
        FirebaseFirestore.getInstance().collection("activities")
                .whereArrayContainsAny("tags", tags)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                noAvailableActivities.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load("https://mir-s3-cdn-cf.behance.net/project_modules/1400_opt_1/3dec8a93992731.5e7331408ee1b.gif")
                                        .into(noAvailableActivitiesImage);
                            } else {
                                Query query = FirebaseFirestore.getInstance()
                                        .collection("activities")
                                        .whereArrayContainsAny("tags", tags);

                                FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                                        .setQuery(query, Activity.class)
                                        .build();


                                adapter = new AvailableActivitiesAdapter(options);
                                availableActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                availableActivitiesRecyclerView.setAdapter(adapter);
                                adapter.startListening();
                            }
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
}