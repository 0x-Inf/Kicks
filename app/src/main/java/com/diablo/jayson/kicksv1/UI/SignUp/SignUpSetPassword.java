package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.databinding.FragmentSignUpSetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpSetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpSetPassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String EXTRA_INFO = "info";
    private static final String TAG = SignUpSetPassword.class.getSimpleName();

    private FragmentSignUpSetPasswordBinding binding;
    private NavController navController;

    private FirebaseAuth mAuth;
    private User userMain;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SignUpViewModel viewModel;


    public SignUpSetPassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpSetPassword newInstance(String param1, String param2) {
        SignUpSetPassword fragment = new SignUpSetPassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userMain = new User();
        viewModel.getUser().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userMain = user;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpSetPasswordBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.sign_up_nav_host_fragment);

        // TODO: Get the password details and get a public private key pair for data encryption
        binding.generatePrivatePublicKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePrivatePublicKeyPair();
            }
        });

        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isPasswordValid(editable)) {
                    binding.passwordEditText.setError("Password is not valid!!");
                }
            }
        });

        binding.confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.passwordEditText.getText().toString().isEmpty()) {
                    binding.passwordEditText.setError("Enter password");
                } else {
                    if (!binding.passwordEditText.getText().equals(editable)) {
                        binding.confirmPasswordEditText.setError("Passwords don't match");
                    }
                }
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        return binding.getRoot();
    }

    private boolean isPasswordValid(Editable editable) {
        String password = editable.toString();
        boolean isLongEnough = false;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;
        isLongEnough = editable.length() >= 8;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                sawSpecial = true;
            } else {
                if (!sawDigit && Character.isDigit(c)) {
                    sawDigit = true;
                } else {
                    if (!sawUpper || !sawLower) {
                        if (Character.isUpperCase(c))
                            sawUpper = true;
                        else
                            sawLower = true;
                    }
                }
            }
        }

        return sawDigit && sawSpecial && sawLower && sawUpper && isLongEnough;
    }

    private void createAccount() {
        if (checkCredentials()) {
            mAuth = FirebaseAuth.getInstance();
            String password = binding.confirmPasswordEditText.toString();
            AuthCredential credential = EmailAuthProvider.getCredential(userMain.getUserEmail(), password);
            Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Timber.e("Successfully linked account with credential");
                        NavDirections actionSetProfilePic = SignUpSetPasswordDirections.actionSignUpSetPasswordToSignUpSetProfilePic();
                        navController.navigate(actionSetProfilePic);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Timber.e(e, "Failed linking user with credential");
                }
            });
        }
    }

    private boolean checkCredentials() {
        return true;
    }

    private void generatePrivatePublicKeyPair() {

    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateViewModel() {

        viewModel.setUser(userMain);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}