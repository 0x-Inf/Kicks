package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityPeopleData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AddActivity.AllAddPeopleContactsAdapter;
import com.diablo.jayson.kicksv1.UI.AddActivity.InvitedPeopleListAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityPeopleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityPeopleFragment extends Fragment implements AllAddPeopleContactsAdapter.OnContactSelectedListener,
        InvitedPeopleListAdapter.OnInvitedContactSelectedListener {


    private AddActivityPeopleData activityPeopleData;
    private FragmentAddActivityPeopleBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private HomeViewModel homeViewModel;
    private AddActivityPeopleFragment listener;
    private NavController navController;

    private AllAddPeopleContactsAdapter allAddPeopleContactsAdapter;
    private InvitedPeopleListAdapter invitedPeopleListAdapter;

    private ArrayList<Contact> invitedContacts = new ArrayList<>();
    private ArrayList<Contact> allContacts;
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
        invitedPeopleListAdapter = new InvitedPeopleListAdapter(invitedContacts, this);
        binding.undefinedPeopleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isUndefined) {
                undefinedPeopleNumber = isUndefined;
                if (isUndefined) {
                    binding.editNoOfPeopleOverlay.setVisibility(View.VISIBLE);
                } else {
                    binding.editNoOfPeopleOverlay.setVisibility(View.GONE);
                }
            }
        });
        binding.editNoOfPeopleOverlay.setOnClickListener(null);
        binding.makePrivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isPrivate) {
                isActivityPrivate = isPrivate;
            }
        });
        binding.searchContactsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchedContactName = charSequence.toString();
                ArrayList<Contact> searchedContacts = new ArrayList<>();
                if (!searchedContactName.isEmpty()) {
                    for (Contact contact : allContacts) {
                        if (contact.getContactName().toLowerCase(Locale.ROOT).contains(searchedContactName)) {
                            searchedContacts.add(contact);
                        }
                    }
                    if (searchedContacts.isEmpty()) {
                        // TODO: show image for no contacts matching query
                    }
                    AllAddPeopleContactsAdapter searchedContactsAdapter = new AllAddPeopleContactsAdapter(searchedContacts, listener);
                    binding.allContactsRecycler.setAdapter(searchedContactsAdapter);
                } else {
                    allAddPeopleContactsAdapter = new AllAddPeopleContactsAdapter(allContacts, listener);
                    binding.allContactsRecycler.setAdapter(allAddPeopleContactsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                }

            }
        });
        return binding.getRoot();
    }

    private void updateActivityModel() {
        if (undefinedPeopleNumber) {
            noOfPeople = "undefined";
        } else {
            noOfPeople = binding.noOfPeopleEditText.getText().toString();
        }
        for (Contact contact : invitedContacts) {
            invitedContactsIds.add(contact.getContactId());
        }
        addActivityViewModel.updateActivityPeople(noOfPeople, invitedContactsIds, isActivityPrivate);
        navigateToNextFragment();
    }

    private void navigateToNextFragment() {
        NavDirections actionAddActivityMain = AddActivityPeopleFragmentDirections.actionAddActivityPeopleFragmentToNavigationAddKick();
        navController.navigate(actionAddActivityMain);
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
                allContacts = contacts;
                allAddPeopleContactsAdapter = new AllAddPeopleContactsAdapter(contacts, listener);
                binding.allContactsRecycler.setAdapter(allAddPeopleContactsAdapter);
            }
        });
        addActivityViewModel.getInvitedContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                invitedContacts = contacts;
                if (!invitedContacts.isEmpty()) {
                    invitedPeopleListAdapter = new InvitedPeopleListAdapter(invitedContacts, listener);
                    binding.invitedPeopleRecycler.setAdapter(invitedPeopleListAdapter);
                }
            }
        });
        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null) {
                    if (activity.getActivityNoOfPeople() != null) {
                        if (!activity.getActivityNoOfPeople().isEmpty()) {
                            if (activity.getActivityNoOfPeople().equals("undefined")) {
                                binding.undefinedPeopleSwitch.setChecked(true);
                                noOfPeople = "undefined";
                            } else {
                                noOfPeople = activity.getActivityNoOfPeople();
                                binding.noOfPeopleEditText.setText(noOfPeople);
                            }
                        }
                        if (activity.isActivityPrivate()) {
                            binding.makePrivateSwitch.setChecked(activity.isActivityPrivate());
                        }
                    }
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
