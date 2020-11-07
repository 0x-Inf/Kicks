package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.diablo.jayson.kicksv1.R;

public class HappeningSoonSelectedDialogFragment extends DialogFragment {

    private String activityId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        activityId = HappeningSoonSelectedDialogFragmentArgs.fromBundle(getArguments()).getActivityId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_happening_soon, container, false);

        HappeningSoonSelectedFragment happeningSoonSelectedFragment = HappeningSoonSelectedFragment.newInstance(activityId);
        FragmentManager manager = getChildFragmentManager();

        manager.beginTransaction()
                .replace(R.id.happeningSoonFragmentContainer, happeningSoonSelectedFragment)
                .commit();
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog customDialog = new Dialog(getContext(), R.style.DialogTheme);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        customDialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return customDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
