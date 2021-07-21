package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.AllMapContactsAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.UI.Home.MapViewModel;
import com.diablo.jayson.kicksv1.UI.Home.SelectedMapContactsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapSettingsBottomSheet extends BottomSheetDialogFragment implements
        AllMapContactsAdapter.OnMapContactSelectedListener {

    private TextView shareLocationTextView, isSharingLocationTextView, shareWithTextView, sharePubliclyTextView;
    private SwitchCompat shareLocationSwitch, sharePubliclySwitch;
    private TextView nearbyRadiusActualTextView, nearbyRadiusTextView;
    private SeekBar nearbyRadiusSeekBar;
    private ConstraintLayout mapSettingConstraintLayout, addSharingContactsConstraintLayout;
    private RecyclerView sharingWithRecycler, allContactsRecyler;
    private CardView addSharingCardView, doneSharingCardView;

    private int shortAnimationDuration;

    private double nearbyRadius;

    private MapViewModel mapViewModel;
    private HomeViewModel homeViewModel;

    private SelectedMapContactsAdapter selectedMapContactsAdapter;
    private AllMapContactsAdapter allMapContactsAdapter;
    private ArrayList<Contact> selectedContacts = new ArrayList<>();

    private MapSettingsBottomSheet listener;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map_settings_bottom_sheet, container, false);
        shareLocationTextView = root.findViewById(R.id.broadcastLocationTextTextView);
        isSharingLocationTextView = root.findViewById(R.id.isSharingLocationTextView);
        shareWithTextView = root.findViewById(R.id.shareWithTextView);
        sharePubliclyTextView = root.findViewById(R.id.sharePubliclyTextView);
        shareLocationSwitch = root.findViewById(R.id.broadcastLocationSwitch);
        sharePubliclySwitch = root.findViewById(R.id.sharePubliclySwitch);
        nearbyRadiusActualTextView = root.findViewById(R.id.radiusActualTextView);
        nearbyRadiusTextView = root.findViewById(R.id.nearbyRadiusTextView);
        nearbyRadiusSeekBar = root.findViewById(R.id.nearbyRadiusSeekBar);
        mapSettingConstraintLayout = root.findViewById(R.id.mapSettingsConstraintLayout);
        addSharingContactsConstraintLayout = root.findViewById(R.id.addSharingContactsConstraintLayout);
        sharingWithRecycler = root.findViewById(R.id.sharingWithRecyler);
        allContactsRecyler = root.findViewById(R.id.allContactsRecycler);
        addSharingCardView = root.findViewById(R.id.addSharingCardView);
        doneSharingCardView = root.findViewById(R.id.doneAddSharingCardView);

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime
        );
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        selectedMapContactsAdapter = new SelectedMapContactsAdapter(selectedContacts);
        listener = this;
        shareLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean shareLocation) {
                mapViewModel.setShareLocationMutableLiveData(shareLocation);
                if (shareLocation) {
                    updateUIForSharingLocation();
                } else {
                    resetUIToDefault();
                }
            }
        });
        nearbyRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        homeViewModel.getNearbyActivityRadiusMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                String radiusText = (aDouble.intValue()) + "m";
                nearbyRadiusActualTextView.setText(radiusText);
                nearbyRadiusSeekBar.setProgress((int) (aDouble / 1000));
            }
        });

        homeViewModel.getUserContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                allMapContactsAdapter = new AllMapContactsAdapter(contacts, listener);
                allContactsRecyler.setAdapter(allMapContactsAdapter);
            }
        });

        mapViewModel.getShareLocationMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shareLocation) {
                shareLocationSwitch.setChecked(shareLocation);
            }
        });

        mapViewModel.getSelectedContactsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                selectedContacts = contacts;
                selectedMapContactsAdapter = new SelectedMapContactsAdapter(selectedContacts);
                sharingWithRecycler.setAdapter(selectedMapContactsAdapter);
            }
        });


        addSharingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForAddingSharing();
            }
        });

        doneSharingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIForDoneSharing();
            }
        });
    }

    private void updateUIForDoneSharing() {
        TransitionManager.beginDelayedTransition(mapSettingConstraintLayout);
//        addSharingCardView.setAlpha(0f);
//        addSharingCardView.setVisibility(View.VISIBLE);
//
//        addSharingCardView.animate()
//                .alpha(1f)
//                .setDuration(shortAnimationDuration)
//                .setListener(null);
//
//        // Animate the loading view to 0% opacity. After the animation ends,
//        // set its visibility to GONE as an optimization step (it won't
//        // participate in layout passes, etc.)
//        addSharingContactsConstraintLayout.animate()
//                .alpha(0f)
//                .setDuration(shortAnimationDuration)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        addSharingContactsConstraintLayout.setVisibility(View.GONE);
//                    }
//                });


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapSettingConstraintLayout);
        constraintSet.setVisibility(R.id.addSharingContactsConstraintLayout, ConstraintSet.GONE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.VISIBLE);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP, R.id.sharingWithRecyler, ConstraintSet.BOTTOM, 16);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP, R.id.sharePubliclySwitch, ConstraintSet.BOTTOM, 16);
        constraintSet.applyTo(mapSettingConstraintLayout);

    }

    private void updateUIForAddingSharing() {
        TransitionManager.beginDelayedTransition(mapSettingConstraintLayout);
//        addSharingContactsConstraintLayout.setAlpha(0f);
//        addSharingContactsConstraintLayout.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
//        addSharingContactsConstraintLayout.animate()
//                .alpha(1f)
//                .setDuration(shortAnimationDuration)
//                .setListener(null);


//        addSharingCardView.animate()
//                .alpha(0f)
//                .setDuration(shortAnimationDuration)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        addSharingCardView.setVisibility(View.GONE);
//                    }
//                });


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapSettingConstraintLayout);
        constraintSet.setVisibility(R.id.addSharingContactsConstraintLayout, ConstraintSet.VISIBLE);
        constraintSet.setVisibility(R.id.addSharingCardView, ConstraintSet.GONE);
        constraintSet.connect(R.id.addSharingContactsConstraintLayout, ConstraintSet.TOP, R.id.sharingWithRecyler, ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.sharePubliclyTextView, ConstraintSet.TOP, R.id.addSharingContactsConstraintLayout, ConstraintSet.BOTTOM, 16);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP, R.id.sharePubliclySwitch, ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
        constraintSet.applyTo(mapSettingConstraintLayout);
    }

    private void resetUIToDefault() {
        TransitionManager.beginDelayedTransition(mapSettingConstraintLayout);
//        shareLocationTextView.setVisibility(View.VISIBLE);
        isSharingLocationTextView.setVisibility(View.GONE);
        shareWithTextView.setVisibility(View.GONE);
        sharingWithRecycler.setVisibility(View.GONE);
        addSharingCardView.setVisibility(View.GONE);
        sharePubliclyTextView.setVisibility(View.GONE);
        sharePubliclySwitch.setVisibility(View.GONE);

        if (addSharingContactsConstraintLayout.isShown()) {
            addSharingContactsConstraintLayout.setVisibility(View.GONE);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapSettingConstraintLayout);
        constraintSet.clear(R.id.addSharingContactsConstraintLayout);
        constraintSet.setVisibility(R.id.broadcastLocationTextTextView, View.VISIBLE);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.START, R.id.mapSettingsConstraintLayout, ConstraintSet.START);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.END, R.id.mapSettingsConstraintLayout, ConstraintSet.END);
        constraintSet.setHorizontalBias(R.id.broadcastLocationSwitch, 0.5f);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.TOP, R.id.broadcastLocationTextTextView, ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM, R.id.nearbyRadiusTextView, ConstraintSet.TOP);
        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP, R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.5f);
        constraintSet.applyTo(mapSettingConstraintLayout);
    }

    private void updateUIForSharingLocation() {
        TransitionManager.beginDelayedTransition(mapSettingConstraintLayout);
//        shareLocationTextView.setVisibility(View.GONE);
        isSharingLocationTextView.setVisibility(View.VISIBLE);
        shareWithTextView.setVisibility(View.VISIBLE);
        sharingWithRecycler.setVisibility(View.VISIBLE);
        addSharingCardView.setVisibility(View.VISIBLE);
        sharePubliclyTextView.setVisibility(View.VISIBLE);
        sharePubliclySwitch.setVisibility(View.VISIBLE);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mapSettingConstraintLayout);
        constraintSet.setVisibility(R.id.broadcastLocationTextTextView, View.GONE);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.START, R.id.isSharingLocationTextView, ConstraintSet.END);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.END, R.id.mapSettingsConstraintLayout, ConstraintSet.END, 16);
        constraintSet.setHorizontalBias(R.id.broadcastLocationSwitch, 1.0f);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.TOP, R.id.isSharingLocationTextView, ConstraintSet.TOP);
        constraintSet.connect(R.id.broadcastLocationSwitch, ConstraintSet.BOTTOM, R.id.isSharingLocationTextView, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.nearbyRadiusTextView, ConstraintSet.TOP, R.id.sharePubliclySwitch, ConstraintSet.BOTTOM, 16);
        constraintSet.setHorizontalBias(R.id.nearbyRadiusTextView, 0.0f);
//        constraintSet.setIntValue(R.id.nearbyRadiusTextView,getResources().getResourceName(R.attr.titleMarginStart),16);
//        ConstraintLayout.LayoutParams nearbyParams = new ConstraintLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        nearbyParams.setMarginStart(16);
//        nearbyRadiusTextView.setLayoutParams(nearbyParams);

        constraintSet.applyTo(mapSettingConstraintLayout);
    }

    @Override
    public void onContactSelected(Contact contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact);
        } else {
            selectedContacts.add(contact);
        }

        selectedMapContactsAdapter.notifyDataSetChanged();
        mapViewModel.setSelectedContactsMutableLiveData(selectedContacts);

    }
}
