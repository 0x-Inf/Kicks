package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class MainAttendActivityActivity extends AppCompatActivity {

    private RecyclerView attendeesRecycler;
    private ProgressBar attendeesProress;
    private ArrayList<AttendingUser> attendingUsersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_attend_activity);
        attendeesRecycler = findViewById(R.id.attendeesRecycler);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String activityId = bundle.getString("activityId");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert activityId != null;
        db.collection("activities").document(activityId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    attendingUsersData = new ArrayList<AttendingUser>();
                    attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getMattendees();
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
            }
        });

        Toast.makeText(this, activityId, Toast.LENGTH_SHORT).show();


    }
}
