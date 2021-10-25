package com.diablo.jayson.kicksv1.UI.SignUp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.diablo.jayson.kicksv1.databinding.FragmentSignUpSetProfilePicBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpSetProfilePic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpSetProfilePic extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSignUpSetProfilePicBinding binding;
    private NavController navController;

    public SignUpSetProfilePic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpSetProfilePic.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpSetProfilePic newInstance(String param1, String param2) {
        SignUpSetProfilePic fragment = new SignUpSetProfilePic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpSetProfilePicBinding.inflate(inflater, container, false);

        binding.editPicFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilePicture();
            }
        });

        return binding.getRoot();
    }

    private void editProfilePicture() {
        // TODO: Open bottom sheets for editing options
    }
}