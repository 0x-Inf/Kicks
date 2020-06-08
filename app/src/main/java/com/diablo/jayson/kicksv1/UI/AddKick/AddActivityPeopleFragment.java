package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityPeopleFragment extends Fragment {


    private AddActivityPeopleData activityPeopleData;

    //People Stuff
    private RelativeLayout addActivityPeopleRelativeLayout;
    private EditText activityMinPeopleEditText, activityMaxPeopleEditText, activityMinAgeEditText,
            activityMaxAgeEditText;
    private TextView makePrivateTextView;
    private Switch makePrivateSwitch;
    private FloatingActionButton peopleSelectionDoneButton;



    public AddActivityPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_activity_people, container, false);
        activityPeopleData = new AddActivityPeopleData();

        //People Stuff
        addActivityPeopleRelativeLayout = root.findViewById(R.id.add_activity_people_relative_layout);
        activityMinPeopleEditText = root.findViewById(R.id.activity_min_people_edit_text);
        activityMaxPeopleEditText = root.findViewById(R.id.activity_max_people_edit_text);
        activityMinAgeEditText = root.findViewById(R.id.activity_min_age_edit_text);
        activityMaxAgeEditText = root.findViewById(R.id.activity_max_age_edit_text);
        makePrivateTextView = root.findViewById(R.id.makePrivateTextView);
        makePrivateSwitch = root.findViewById(R.id.makePrivateSwitch);
        peopleSelectionDoneButton = root.findViewById(R.id.peopleSelectionDoneButton);



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
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    NavDirections actionAddActivityMain = AddActivityPeopleFragmentDirections.actionAddActivityPeopleFragmentToNavigationAddKick(activityPeopleData);
                    navController.navigate(actionAddActivityMain);
                }

            }
        });
        makePrivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activityPeopleData.setActivityPrivate(isChecked);
                    makePrivateTextView.setText(R.string.private_text);
                } else {
                    activityPeopleData.setActivityPrivate(false);
                    makePrivateTextView.setText(R.string.make_private_text);
                }
            }
        });
        return root;
    }

    private void updateActivityPeople() {

        int activityMinPeople = Integer.parseInt(activityMinPeopleEditText.getText().toString());
        int activityMaxPeople = Integer.parseInt(activityMaxPeopleEditText.getText().toString());
        int activityMinAge = Integer.parseInt(activityMinAgeEditText.getText().toString());
        int activityMaxAge = Integer.parseInt(activityMaxAgeEditText.getText().toString());
        activityPeopleData.setActivityMinRequiredPeople(activityMinPeople);
        activityPeopleData.setActivityMaxRequiredPeople(activityMaxPeople);
        activityPeopleData.setActivityMinAge(activityMinAge);
        activityPeopleData.setActivityMaxAge(activityMaxAge);
    }
}
