package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityCostFragment extends Fragment {

    //Cost Stuff
    private RelativeLayout addActivityCostRelativeLayout;
    private EditText activityCostEditText;
    private FloatingActionButton costInputDoneButton;

    private AddActivityCostData activityCostData;

    public AddActivityCostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_activity_cost, container, false);

        //Cost Views
        addActivityCostRelativeLayout = root.findViewById(R.id.add_activity_cost_relative_layout);
        activityCostEditText = root.findViewById(R.id.activity_cost_edit_text);
        costInputDoneButton = root.findViewById(R.id.costInputDoneButton);

        activityCostData = new AddActivityCostData();

        //Cost Implementation
        costInputDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityCostEditText.getText().toString().isEmpty()) {
                    activityCostEditText.setError("Enter amount");
                } else {
                    updateActivityCost();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    NavDirections actionAddActivityMain = AddActivityCostFragmentDirections.actionAddActivityCostFragmentToNavigationAddKick(activityCostData);
                    navController.navigate(actionAddActivityMain);
//                    addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
//                    addActivityCostRelativeLayout.setVisibility(View.GONE);
                }

            }
        });

        return root;
    }

    private void updateActivityCost() {
        String activityCost = activityCostEditText.getText().toString();
        activityCostData.setActivityCost(activityCost);
    }
}
