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

public class ImageAndTextDialogFragment extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.image_and_text_dialog_fragment, container, false);

        ImageAndTextFragment imageAndTextOnlyFragment = new ImageAndTextFragment();

        FragmentManager manager = getChildFragmentManager();

        manager.beginTransaction()
                .replace(R.id.imageAndTextFragmentcontainer, imageAndTextOnlyFragment)
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
}
