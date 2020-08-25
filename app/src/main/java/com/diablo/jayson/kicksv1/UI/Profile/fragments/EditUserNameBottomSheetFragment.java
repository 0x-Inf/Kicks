package com.diablo.jayson.kicksv1.UI.Profile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Profile.ProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class EditUserNameBottomSheetFragment extends BottomSheetDialogFragment {

    private ProfileViewModel profileViewModel;
    private Button doneUserNameEditButton;
    private EditText editUserNameEditText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_user_name_bottom_sheet, container, false);
        doneUserNameEditButton = root.findViewById(R.id.doneEditPhoneButton);
        editUserNameEditText = root.findViewById(R.id.newUserNameEditText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        doneUserNameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileViewModel.updateUsername(editUserNameEditText.getText().toString());
            }
        });
    }
}
