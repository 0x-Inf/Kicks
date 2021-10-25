package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.databinding.FragmentSignUpBasicDetailsBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpBasicDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpBasicDetails extends Fragment {

    private FragmentSignUpBasicDetailsBinding binding;
    private NavController navController;

    private SignUpViewModel viewModel;
    private String userName;
    private String firstName;
    private String secondName;
    private String emailAddress;
    private String phoneNumber;


    public SignUpBasicDetails() {
        // Required empty public constructor
    }

    public static SignUpBasicDetails newInstance(String param1, String param2) {
        SignUpBasicDetails fragment = new SignUpBasicDetails();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            return;
        }
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBasicDetailsBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.sign_up_nav_host_fragment);

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidDetails()) {
                    updateViewModel();
                    NavDirections actionSetPassword = SignUpBasicDetailsDirections.actionSignUpBasicDetailsToSignUpSetPassword();
                    navController.navigate(actionSetPassword);
                } else {
                    Toast.makeText(requireContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isUserNamePicked(editable.toString())) {
                    binding.userNameEditText.setError("User name has been picked");
                }
            }
        });

        return binding.getRoot();
    }

    private boolean isValidDetails() {
        boolean isValid = false;
        userName = binding.userNameEditText.getText().toString();
        firstName = binding.firstNameEditText.getText().toString();
        secondName = binding.secondNameEditText.getText().toString();
        emailAddress = binding.emailAddressEditText.getText().toString();
        phoneNumber = binding.phoneNumberEditText.getText().toString();

        isValid = isUserNameValid() && isNameValid() && isEmailAddressValid() && isPhoneValid();

        return isValid;
    }

    private boolean isPhoneValid() {
        //TODO: Make better phone number Checker with country code
        boolean isValid = false;
        if (!phoneNumber.isEmpty()) {
            isValid = true;
        } else if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            isValid = true;
        } else {
            binding.phoneNumberEditText.setError("Enter A Phone Number");
        }

        return isValid;
    }

    private boolean isEmailAddressValid() {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);

        if (!emailAddress.isEmpty()) {
            isValid = true;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            isValid = true;
        } else if (matcher.matches()) {
            isValid = true;
        } else {
            binding.emailAddressEditText.setError("Enter An Email Address");
        }
        return isValid;
    }

    private boolean isNameValid() {
        boolean isValid = false;
        if (!firstName.isEmpty() && secondName.isEmpty()) {
            isValid = true;
        } else {
            if (binding.firstNameEditText.getText().toString().isEmpty()) {
                binding.firstNameEditText.setError("Enter A Name");
            }
            if (binding.secondNameEditText.getText().toString().isEmpty()) {
                binding.secondNameEditText.setError("Enter A Name");
            }

        }
        return isValid;
    }

    private boolean isUserNameValid() {
        boolean isValid = false;
        if (!userName.isEmpty()) {
            isValid = true;
        } else {
            binding.userNameEditText.setError("Enter User Name");
        }
        if (!isUserNamePicked(userName)) {
            //TODO: check this while typing
            isValid = true;
        } else {
            binding.userNameEditText.setError("User name has been taken");
        }
        return isValid;
    }

    private boolean isUserNamePicked(String userName) {
        //TODO: Check if another user has same username in Db;
        return true;
    }

    private void updateViewModel() {
        User mainUser = new User();
        mainUser.setUserName(userName);
        mainUser.setFirstName(firstName);
        mainUser.setSecondName(secondName);
        mainUser.setPhoneNumber(phoneNumber);
        mainUser.setUserEmail(emailAddress);
        viewModel.setUser(mainUser);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

}
