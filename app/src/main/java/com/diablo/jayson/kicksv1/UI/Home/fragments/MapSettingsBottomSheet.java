package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityAllTagsListAdapter;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.Home.AllMapContactsAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.UI.Home.MapViewModel;
import com.diablo.jayson.kicksv1.UI.Home.SelectedMapContactsAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentMapSettingsBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapSettingsBottomSheet extends BottomSheetDialogFragment implements
        AllMapContactsAdapter.OnMapContactSelectedListener, AddActivityAllTagsListAdapter.OnTagSelectedListener {

    private FragmentMapSettingsBottomSheetBinding binding;

//    private TextView shareLocationTextView, isSharingLocationTextView, shareWithTextView, sharePubliclyTextView;
//    private SwitchCompat shareLocationSwitch, sharePubliclySwitch;
//    private TextView nearbyRadiusActualTextView, nearbyRadiusTextView;
//    private SeekBar nearbyRadiusSeekBar;
//    private ConstraintLayout mapSettingConstraintLayout, addSharingContactsConstraintLayout;
//    private RecyclerView sharingWithRecycler, allContactsRecyler, broadcastingTagsRecyclerView, allTagsRecycler;
//    private CardView addSharingCardView, doneSharingCardView, editTagsCardView, doneAddingTagsCardView;

    private int shortAnimationDuration;
    private boolean isSharingPublicly;

    private double nearbyRadius;

    private MapViewModel mapViewModel;
    private HomeViewModel homeViewModel;
    private AddActivityViewModel addActivityViewModel;

    private SelectedMapContactsAdapter selectedMapContactsAdapter;
    private AllMapContactsAdapter allMapContactsAdapter;
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    private ArrayList<Tag> selectedTags = new ArrayList<>();

    private MapSettingsBottomSheet listener;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentMapSettingsBottomSheetBinding.inflate(inflater, container, false);

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        selectedMapContactsAdapter = new SelectedMapContactsAdapter(selectedContacts);
        listener = this;

        binding.broadcastLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean shareLocation) {
                mapViewModel.setShareLocationMutableLiveData(shareLocation);
                if (shareLocation) {
                    if (isSharingPublicly) {
                        updateUIForPubliclySharingLocation();
                    } else {
                        updateUIForSharingLocation();
                    }

                } else {
                    if (isSharingPublicly) {
                        isSharingPublicly = false;
                        mapViewModel.setShareLocationPubliclyMutableLiveData(false);
                        resetUIToDefault();
                    }
                    resetUIToDefault();
                }
            }
        });
        binding.nearbyRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0) {
                    nearbyRadius = i * 1000;
//                    String radiusText =  i * 1000+"m";
//                    nearbyRadiusActualTextView.setText(radiusText);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                homeViewModel.setNearbyActivityRadius(nearbyRadius);
            }
        });

        binding.addSharingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForAddingSharing();
            }
        });

        binding.doneAddSharingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForDoneSharing();
            }
        });

        binding.editTagsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForEditTags();
            }
        });

        binding.doneAddTagsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForDoneEditingTags();
            }
        });

        binding.sharePubliclySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean publiclySharing) {
                mapViewModel.setShareLocationPubliclyMutableLiveData(publiclySharing);
                isSharingPublicly = publiclySharing;
                if (publiclySharing) {
                    updateUIForPubliclySharingLocation();
                } else {
                    resetUIToDefaultSharing();
                }
            }
        });

        homeViewModel.getNearbyActivityRadiusMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                String radiusText = (aDouble.intValue()) + "m";
                binding.radiusActualTextView.setText(radiusText);
                binding.nearbyRadiusSeekBar.setProgress((int) (aDouble / 1000));
            }
        });

        homeViewModel.getUserContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                allMapContactsAdapter = new AllMapContactsAdapter(contacts, listener);
                binding.allContactsRecycler.setAdapter(allMapContactsAdapter);
            }
        });

        addActivityViewModel.getAllTagsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                AddActivityAllTagsListAdapter allTagsListAdapter = new AddActivityAllTagsListAdapter(tags, listener);
                binding.allTagsRecycler.setAdapter(allTagsListAdapter);
            }
        });

        mapViewModel.getShareLocationMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shareLocation) {
                binding.broadcastLocationSwitch.setChecked(shareLocation);
            }
        });

        mapViewModel.getSelectedContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                selectedContacts = contacts;
                selectedMapContactsAdapter = new SelectedMapContactsAdapter(selectedContacts);
                binding.sharingWithRecycler.setAdapter(selectedMapContactsAdapter);
            }
        });

        mapViewModel.getSelectedBroadcastTagsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                selectedTags = tags;
                AddActivityAllTagsListAdapter selectedTagsAdapter = new AddActivityAllTagsListAdapter(tags, listener);
                binding.broadcastingTagsRecyclerView.setAdapter(selectedTagsAdapter);
            }
        });

        mapViewModel.getShareLocationPubliclyMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shareLocationPublicly) {
                if (shareLocationPublicly != null) {
                    isSharingPublicly = shareLocationPublicly;
                    binding.sharePubliclySwitch.setChecked(shareLocationPublicly);
                } else {
                    isSharingPublicly = false;
                    binding.sharePubliclySwitch.setChecked(false);
                }
            }
        });

    }


    private void resetUIToDefaultSharing() {

        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(R.id.shareWithTextView, ConstraintSet.VISIBLE);
        constraintSet.setVisibility(binding.sharingWithRecycler.getId(), ConstraintSet.VISIBLE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.VISIBLE);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP,
                binding.sharingWithRecycler.getId(), ConstraintSet.BOTTOM, 15);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);

    }

    private void updateUIForEditTags() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(binding.addBroadcastingTagsConstraintLayout.getId(), ConstraintSet.VISIBLE);
        constraintSet.setVisibility(binding.editTagsCardView.getId(), ConstraintSet.GONE);
        constraintSet.connect(binding.addBroadcastingTagsConstraintLayout.getId(), ConstraintSet.TOP,
                binding.tagsTextView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.connect(binding.nearbyRadiusTextView.getId(), ConstraintSet.TOP,
                binding.addBroadcastingTagsConstraintLayout.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);

    }

    private void updateUIForDoneEditingTags() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(binding.addBroadcastingTagsConstraintLayout.getId(), ConstraintSet.GONE);
        constraintSet.setVisibility(binding.editTagsCardView.getId(), ConstraintSet.VISIBLE);
        constraintSet.connect(binding.nearbyRadiusTextView.getId(), ConstraintSet.TOP,
                binding.broadcastingTagsRecyclerView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);

    }

    private void updateUIForPubliclySharingLocation() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(R.id.shareWithTextView, ConstraintSet.GONE);
        constraintSet.setVisibility(binding.sharingWithRecycler.getId(), ConstraintSet.GONE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.GONE);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP,
                R.id.isSharingLocationTextView, ConstraintSet.BOTTOM, 15);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);
    }

    private void updateUIForDoneSharing() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(R.id.addSharingContactsConstraintLayout, ConstraintSet.GONE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.VISIBLE);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP,
                binding.sharingWithRecycler.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP,
                binding.broadcastingTagsRecyclerView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);

    }

    private void updateUIForAddingSharing() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(R.id.addSharingContactsConstraintLayout, ConstraintSet.VISIBLE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.GONE);
        constraintSet.connect(R.id.addSharingContactsConstraintLayout, ConstraintSet.TOP,
                binding.sharingWithRecycler.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP, R.id.addSharingContactsConstraintLayout, ConstraintSet.BOTTOM, 16);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP,
                binding.broadcastingTagsRecyclerView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);
    }

    private void resetUIToDefault() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);
//        shareLocationTextView.setVisibility(View.VISIBLE);
        binding.isSharingLocationTextView.setVisibility(View.GONE);
        binding.shareWithTextView.setVisibility(View.GONE);
        binding.sharingWithRecycler.setVisibility(View.GONE);
        binding.addSharingCardView.setVisibility(View.GONE);
        binding.sharePubliclyTextView.setVisibility(View.GONE);
        binding.sharePubliclySwitch.setVisibility(View.GONE);
        binding.tagsTextView.setVisibility(View.GONE);
        binding.editTagsCardView.setVisibility(View.GONE);
        binding.broadcastingTagsRecyclerView.setVisibility(View.GONE);

        if (binding.addSharingContactsConstraintLayout.isShown()) {
            binding.addSharingContactsConstraintLayout.setVisibility(View.GONE);
        }
        if (binding.addBroadcastingTagsConstraintLayout.isShown()) {
            binding.addBroadcastingTagsConstraintLayout.setVisibility(View.GONE);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.clear(R.id.addSharingContactsConstraintLayout);
        constraintSet.setVisibility(R.id.broadcastLocationTextTextView, View.VISIBLE);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.START, R.id.mapSettingsConstraintLayout, ConstraintSet.START);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.END, R.id.mapSettingsConstraintLayout, ConstraintSet.END);
        constraintSet.setHorizontalBias(R.id.broadcastLocationSwitch, 0.5f);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.TOP, R.id.broadcastLocationTextTextView, ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM, R.id.nearbyRadiusTextView, ConstraintSet.TOP);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP, R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.5f);
        constraintSet.applyTo(binding.mapSettingsConstraintLayout);
    }

    private void updateUIForSharingLocation() {
        TransitionManager.beginDelayedTransition(binding.mapSettingsConstraintLayout);

//        shareLocationTextView.setVisibility(View.GONE);
        binding.isSharingLocationTextView.setVisibility(View.VISIBLE);
        binding.shareWithTextView.setVisibility(View.VISIBLE);
        binding.sharingWithRecycler.setVisibility(View.VISIBLE);
        binding.addSharingCardView.setVisibility(View.VISIBLE);
        binding.sharePubliclyTextView.setVisibility(View.VISIBLE);
        binding.sharePubliclySwitch.setVisibility(View.VISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.mapSettingsConstraintLayout);
        constraintSet.setVisibility(R.id.broadcastLocationTextTextView, View.GONE);
        constraintSet.setVisibility(binding.tagsTextView.getId(), ConstraintSet.VISIBLE);
        constraintSet.setVisibility(binding.broadcastingTagsRecyclerView.getId(), ConstraintSet.VISIBLE);
        constraintSet.setVisibility(binding.editTagsCardView.getId(), ConstraintSet.VISIBLE);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.START, R.id.isSharingLocationTextView, ConstraintSet.END);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.END, R.id.mapSettingsConstraintLayout, ConstraintSet.END, 16);
        constraintSet.setHorizontalBias(R.id.broadcastLocationSwitch, 1.0f);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.TOP, R.id.isSharingLocationTextView, ConstraintSet.TOP);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM, R.id.isSharingLocationTextView, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP,
                binding.broadcastingTagsRecyclerView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
//        constraintSet.setIntValue(R.id.nearbyRadiusTextView,getResources().getResourceName(R.attr.titleMarginStart),16);
//        ConstraintLayout.LayoutParams nearbyParams = new ConstraintLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        nearbyParams.setMarginStart(16);
//        nearbyRadiusTextView.setLayoutParams(nearbyParams);

        constraintSet.applyTo(binding.mapSettingsConstraintLayout);
    }

    @Override
    public void onContactSelected(Contact contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact);
            selectedMapContactsAdapter.notifyItemRemoved(selectedContacts.indexOf(contact));
        } else {
            selectedContacts.add(contact);
            selectedMapContactsAdapter.notifyItemInserted(selectedContacts.indexOf(contact));
        }
        mapViewModel.setSelectedContactsMutableLiveData(selectedContacts);

    }

    @Override
    public void onTagSelected(Tag tag) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag);
        } else {
            selectedTags.add(tag);
        }
        mapViewModel.setSelectedBroadcastTagsMutableLiveData(selectedTags);
    }
}
