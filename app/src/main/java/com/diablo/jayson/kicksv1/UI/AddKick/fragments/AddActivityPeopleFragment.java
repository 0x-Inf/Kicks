package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityPeopleData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AddKick.AllAddPeopleContactsAdapter;
import com.diablo.jayson.kicksv1.UI.AddKick.InvitedPeopleListAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityPeopleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityPeopleFragment extends Fragment implements AllAddPeopleContactsAdapter.OnContactSelectedListener, InvitedPeopleListAdapter.OnInvitedContactSelectedListener {


    private AddActivityPeopleData activityPeopleData;
    private FragmentAddActivityPeopleBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private HomeViewModel homeViewModel;
    private AddActivityPeopleFragment listener;
    private NavController navController;

    private AllAddPeopleContactsAdapter allAddPeopleContactsAdapter;
    private InvitedPeopleListAdapter invitedPeopleListAdapter;

    private ArrayList<Contact> invitedContacts;
    private ArrayList<String> invitedContactsIds = new ArrayList<>();
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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavDirections actionAddActivityMain = AddActivityPeopleFragmentDirections.actionAddActivityPeopleFragmentToNavigationAddKick();
        binding.undefinedPeopleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isUndefined) {
                if (isUndefined) {
                    noOfPeople = "Undefined";
                    binding.editNoOfPeopleOverlay.setVisibility(View.VISIBLE);
                } else {
                    binding.editNoOfPeopleOverlay.setVisibility(View.GONE);
                }
            }
        });
        binding.makePrivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isPrivate) {
                if (isPrivate) {
                    isActivityPrivate = isPrivate;
                }
            }
        });
        binding.editInvitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.selectContactsOverlay.setVisibility(View.VISIBLE);
                binding.selectInvitingContactsCard.setVisibility(View.VISIBLE);
            }
        });
        binding.removeContactSelectionCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.selectContactsOverlay.setVisibility(View.GONE);
                binding.selectInvitingContactsCard.setVisibility(View.GONE);
            }
        });
        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.noOfPeopleEditText.getText().toString().isEmpty() && !undefinedPeopleNumber) {
                    navController.popBackStack();
                } else {
                    updateActivityModel();
                    navController.navigate(actionAddActivityMain);
                }

            }
        });
        return binding.getRoot();
    }

    private void updateActivityModel() {
        String activityNoOfPeople = binding.noOfPeopleEditText.getText().toString();
        String undefinedNoOfPeople = "undefined";
        for (Contact contact : invitedContacts) {
            invitedContactsIds.add(contact.getContactId());
        }
        if (undefinedPeopleNumber) {
            addActivityViewModel.updateActivityPeople(undefinedNoOfPeople, invitedContactsIds, isActivityPrivate);
        } else {
            addActivityViewModel.updateActivityPeople(activityNoOfPeople, invitedContactsIds, isActivityPrivate);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = this;
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getUserContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                allAddPeopleContactsAdapter = new AllAddPeopleContactsAdapter(contacts, listener);
                binding.myContactsRecycler.setAdapter(allAddPeopleContactsAdapter);
            }
        });
        addActivityViewModel.getInvitedContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                invitedContacts = contacts;
                if (invitedContacts.isEmpty()) {

                } else {
                    invitedPeopleListAdapter = new InvitedPeopleListAdapter(invitedContacts, listener);
                    binding.invitedPeopleRecycler.setAdapter(invitedPeopleListAdapter);
                }
            }
        });
    }

    @Override
    public void onContactSelected(Contact selectedContact) {
        if (!invitedContacts.contains(selectedContact)) {
            invitedContacts.add(selectedContact);
        } else {
            invitedContacts.remove(selectedContact);
        }
        invitedPeopleListAdapter.notifyDataSetChanged();
        addActivityViewModel.setInvitedContactsMutableLiveData(invitedContacts);
    }

    @Override
    public void onInvitedContactSelected(Contact invitedContact) {
        invitedContacts.remove(invitedContact);
        invitedPeopleListAdapter.notifyDataSetChanged();
        addActivityViewModel.setInvitedContactsMutableLiveData(invitedContacts);
    }
}
