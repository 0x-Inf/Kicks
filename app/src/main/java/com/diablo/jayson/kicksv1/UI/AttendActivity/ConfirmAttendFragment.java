package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmAttendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmAttendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ConfirmAttendFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AttendActivityViewModel viewModel;
    private TextView titleTextView;
    private ImageView imageView;

    private String activityId;
    private Activity activity;


    public ConfirmAttendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmAttendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmAttendFragment newInstance(String param1, String param2) {
        ConfirmAttendFragment fragment = new ConfirmAttendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_confirm_attend, container, false);
        ExtendedFloatingActionButton imIn = root.findViewById(R.id.AttendActivityFab);
        ExtendedFloatingActionButton imOut = root.findViewById(R.id.CancelAttendFab);
        imOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        imIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViewModel();
                activity.getActivityAttendees().add(FirebaseUtil.getAttendingUser());
//                AttendActivityMainFragment attendFragment = new AttendActivityMainFragment();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("activities").document(activityId);
                documentReference.update("mattendees", FieldValue.arrayUnion(FirebaseUtil.getAttendingUser()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    getActivity().getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.attend_activity_fragment_container, attendFragment)
//                                            .commit();
                                    Intent attendActivityIntent = new Intent(getContext(), MainAttendActivityActivity.class);
                                    attendActivityIntent.putExtra("activityId", activityId);
                                    startActivity(attendActivityIntent);
                                } else {
                                    Toast.makeText(getContext(), "Problem Adding You", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });
        return root;
    }

    private void updateViewModel() {
        viewModel.setActivityData(activity);
    }

    private void loadActivityFromDb() {
        activity = new Activity();

    }

    private void initializeRecyclerViewWithTags() {


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.activityImageView);
        titleTextView = view.findViewById(R.id.activityNameTitleTextView);

        viewModel.getActivityId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                activityId = s;
                loadActivityFromDb();
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
//                                Host host = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getHost();
//                                String kickStartTime = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getKickStartTime();
//                                String kickEndTime = documentSnapshot.toObject(Activity.class).getKickEndTime();
//                                String kickDate = documentSnapshot.toObject(Activity.class).getKickDate();
//                                String kickTitle = documentSnapshot.toObject(Activity.class).getKickTitle();
//                                String kickLocation = documentSnapshot.toObject(Activity.class).getKickLocationName();
//                                String minRequiredPeople = documentSnapshot.toObject(Activity.class).getMinRequiredPeople();
//                                String maxRequiredPeeps = documentSnapshot.toObject(Activity.class).getMaxRequiredPeeps();
//                                String imageUrl = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getImageUrl();
//                                ArrayList<AttendingUser> mattendees = documentSnapshot.toObject(Activity.class).getMattendees();
//                                activity.setHost(host);
//                                activity.setKickStartTime(kickStartTime);
//                                activity.setKickEndTime(kickEndTime);
//                                activity.setKickDate(kickDate);
//                                activity.setKickTitle(kickTitle);
//                                activity.setKickLocationName(kickLocation);
//                                activity.setMinRequiredPeople(minRequiredPeople);
//                                activity.setMaxRequiredPeeps(maxRequiredPeeps);
//                                activity.setImageUrl(imageUrl);
//                                activity.setMattendees(mattendees);
                                Glide.with(Objects.requireNonNull(getContext())).load(activity.getImageUrl())
                                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(20, 5)))
                                        .into(imageView);
                                titleTextView.setText(activity.getActivityTitle());
                                Log.e(TAG, activity.getImageUrl());
                            }
                        }
                    }
                });
            }
        });
    }
}
