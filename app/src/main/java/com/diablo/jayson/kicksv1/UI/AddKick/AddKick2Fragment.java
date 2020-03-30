package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKick2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKick2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddKick2Fragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText minNoPeople, maxNoPeople, costEditText, minAge, maxAge;
    private FloatingActionButton nextButton;
    private AutoCompleteTextView ageTextView;

    private AddKickViewModel viewModel;

    private Activity activityMain;

    public AddKick2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKick2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKick2Fragment newInstance(String param1, String param2) {
        AddKick2Fragment fragment = new AddKick2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minNoPeople = view.findViewById(R.id.minPeopleInputEditText);
        maxNoPeople = view.findViewById(R.id.maxPeopleEditText);
//        minAge = view.findViewById(R.id.minAgeInputEditText);
//        maxAge = view.findViewById(R.id.maxAgeInputEditText);
        LinearLayout secondContent = view.findViewById(R.id.addNoOfPeopleFragContent);
        FloatingActionButton nextFragment = view.findViewById(R.id.nextCreateActivityFab);
        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                minNoPeople.setText(activity.getTag().getTagOptimalMinPeople());
                maxNoPeople.setText(activity.getTag().getTagOptimalMaxPeople());
                costEditText.setText(activity.getTag().getTagCost());
            }
        });
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViewModel();
                AddKick3Fragment nextFragment = new AddKick3Fragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayoutbase, nextFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                secondContent.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateViewModel() {
        // Access a Cloud Firestore instance from your Activity


        String minNoOfPeople = Objects.requireNonNull(minNoPeople.getText()).toString();
        String maxNoOfPeople = Objects.requireNonNull(maxNoPeople.getText()).toString();
        String costOfTag = Objects.requireNonNull(costEditText.getText()).toString();

        activityMain.setMinRequiredPeople(minNoOfPeople);
        activityMain.setMaxRequiredPeeps(maxNoOfPeople);
        activityMain.getTag().setTagCost(costOfTag);

        viewModel.setActivity1(activityMain);
        Log.e(TAG, activityMain.getMinRequiredPeople());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_kick2, container, false);
        minNoPeople = root.findViewById(R.id.minPeopleInputEditText);
        maxNoPeople = root.findViewById(R.id.maxPeopleEditText);
        costEditText = root.findViewById(R.id.costInputEditText);
        nextButton = root.findViewById(R.id.nextCreateActivityFab);

        return root;
    }

}
