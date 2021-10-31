package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.ProfilePicExample;
import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.databinding.FragmentSignUpSetProfilePicBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpSetProfilePic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpSetProfilePic extends Fragment implements RandomPicsAdapter.OnRandomPicSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSignUpSetProfilePicBinding binding;
    private NavController navController;
    private SignUpViewModel signUpViewModel;

    private Uri profilePicUri;
    private User mainUser;

    private RandomPicsAdapter randomPicsAdapter;
    private SignUpSetProfilePic listener;

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
        navController = Navigation.findNavController(requireActivity(), R.id.sign_up_nav_host_fragment);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        binding.editPicFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilePicture();
            }
        });

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profilePicUri != null) {
                    updateProfileWithPic();
                }
            }
        });

        return binding.getRoot();
    }

    private void updateProfileWithPic() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(profilePicUri)
                .setDisplayName(mainUser.getUserName())
                .build();
        firebaseUser.updateProfile(changeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Timber.d("Profile has been updated successfully");
//                            NavDirections actionMainPage =
                            updateSharedPreferences();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e, "Failed to update profile");
            }
        });
    }

    private void updateSharedPreferences() {
        // TODO: Update sharedPreferences for new User
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpViewModel.getRandomPicsFromDb();
        signUpViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                assert user != null;
                mainUser = user;
                if (!user.getPhotoUrl().isEmpty()) {
                    Uri photoUri = Uri.parse(user.getPhotoUrl());
                    profilePicUri = photoUri;
                    Glide.with(requireContext())
                            .load(photoUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.profilePicImage);
                }
            }
        });
        listener = this;
        signUpViewModel.getRandomPicsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ProfilePicExample>>() {
            @Override
            public void onChanged(ArrayList<ProfilePicExample> profilePicExamples) {
                randomPicsAdapter = new RandomPicsAdapter(profilePicExamples, listener);
                binding.profilePicsRecycler.setAdapter(randomPicsAdapter);
            }
        });


    }

    private void editProfilePicture() {
        NavDirections actionSetProfileBottomSheet = SignUpSetProfilePicDirections.actionSignUpSetProfilePicToSetProfilePicBottomSheet();
        navController.navigate(actionSetProfileBottomSheet);
    }

    @Override
    public void onRandomPicSelected(ProfilePicExample pic) {
        String picUrl = pic.getPicUrl();
        mainUser.setPhotoUrl(picUrl);
    }
}