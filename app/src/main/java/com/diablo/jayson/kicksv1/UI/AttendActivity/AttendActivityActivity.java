package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Objects;

public class AttendActivityActivity extends AppCompatActivity {

    private AttendActivityViewModel viewModel;
    private static final String TAG = AttendActivityActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_activity);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String activityId = bundle.getString("activityId");
        Long activityLatitude = bundle.getLong("activityLatitude");
        Long activityLongitude = bundle.getLong("activityLongitude");
        Boolean alreadyAttending = bundle.getBoolean("alreadyAttending");

        viewModel = new ViewModelProvider(this).get(AttendActivityViewModel.class);

        viewModel.setActivityId(activityId);

        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.attend_activity_fragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        if (alreadyAttending) {
            viewModel = new ViewModelProvider(this).get(AttendActivityViewModel.class);

            viewModel.setActivityId(activityId);
            Activity activity = new Activity();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("activities").document(activityId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        assert documentSnapshot != null;
                        if (documentSnapshot.exists()) {
                            Host host = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getHost();
                            String kickStartTime = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getKickStartTime();
                            String kickEndTime = documentSnapshot.toObject(Activity.class).getKickEndTime();
                            String kickDate = documentSnapshot.toObject(Activity.class).getKickDate();
                            String kickTitle = documentSnapshot.toObject(Activity.class).getKickTitle();
                            String kickLocation = documentSnapshot.toObject(Activity.class).getKickLocationName();
                            GeoPoint kickLocationCordinates = documentSnapshot.toObject(Activity.class).getKickLocationCordinates();
                            String minRequiredPeople = documentSnapshot.toObject(Activity.class).getMinRequiredPeople();
                            String maxRequiredPeeps = documentSnapshot.toObject(Activity.class).getMaxRequiredPeeps();
                            String imageUrl = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getImageUrl();
                            ArrayList<AttendingUser> mattendees = documentSnapshot.toObject(Activity.class).getMattendees();
                            activity.setHost(host);
                            activity.setKickStartTime(kickStartTime);
                            activity.setKickEndTime(kickEndTime);
                            activity.setKickDate(kickDate);
                            activity.setKickTitle(kickTitle);
                            activity.setKickLocationName(kickLocation);
                            activity.setKickLocationCordinates(kickLocationCordinates);
                            activity.setMinRequiredPeople(minRequiredPeople);
                            activity.setMaxRequiredPeeps(maxRequiredPeeps);
                            activity.setImageUrl(imageUrl);
                            activity.setMattendees(mattendees);
                            Log.e(TAG, imageUrl);
                            viewModel.setActivityData(activity);
                        }
                    }
                }
            });


//            AttendActivityMainFragment attending = new AttendActivityMainFragment();
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.attend_activity_fragment_container, attending)
//                    .commit();
            Intent attendActivityIntent = new Intent(AttendActivityActivity.this, MainAttendActivityActivity.class);
            attendActivityIntent.putExtra("activityId", activityId);
            attendActivityIntent.putExtra("activityLatitude", activityLatitude);
            attendActivityIntent.putExtra("activityLongitude", activityLongitude);
            startActivity(attendActivityIntent);
            finish();
        } else {
            ConfirmAttendFragment confirmAttend = new ConfirmAttendFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.attend_activity_fragment_container, confirmAttend)
                    .commit();
        }


        Log.e(TAG, activityId);
    }
}
