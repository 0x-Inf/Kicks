package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityPeopleData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityPeopleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityPeopleFragment extends Fragment {


    private AddActivityPeopleData activityPeopleData;
    private FragmentAddActivityPeopleBinding binding;
    private AddActivityViewModel addActivityViewModel;

    private ArrayList<Contact> invitedContacts;
    private ArrayList<String> invitedContactsIds;
    private boolean isActivityPrivate;
    private boolean undefinedPeopleNumber;
    private String noOfPeople;

    public AddActivityPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityPeopleBinding.inflate(inflater, container, false);
        binding.undefinedPeopleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        binding.makePrivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        binding.editInvitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.selectContactsOverlay.setVisibility(View.VISIBLE);
                binding.selectInvitingContactsCard.setVisibility(View.VISIBLE);
            }
        });
        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
    }

    private void updateActivityPeople(boolean isActivityPrivate, String noOfPeople) {

    }
}
