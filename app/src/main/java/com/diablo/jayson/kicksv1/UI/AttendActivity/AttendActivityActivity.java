package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.fragments.ConfirmAttendFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AttendActivityActivity extends AppCompatActivity {

    private AttendActivityViewModel viewModel;
    private static final String TAG = AttendActivityActivity.class.getSimpleName();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_activity);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String activityId = bundle.getString("activityId");
        Long activityLatitude = bundle.getLong("activityLatitude");
        Long activityLongitude = bundle.getLong("activityLongitude");
        boolean alreadyAttending = bundle.getBoolean("alreadyAttending");

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
            activity = new Activity();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("activities").document(activityId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        assert documentSnapshot != null;
                        if (documentSnapshot.exists()) {
                            activity = documentSnapshot.toObject(Activity.class);
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

    }
}
