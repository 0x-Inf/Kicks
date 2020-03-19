package com.diablo.jayson.kicksv1.UI.SignUp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpThree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText passWordEditText, passWordConfirmEditText;

    private SignUpViewModel viewModel;
    private User userMain;

    public SignUpThree() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpThree.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpThree newInstance(String param1, String param2) {
        SignUpThree fragment = new SignUpThree();
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
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText passWord = view.findViewById(R.id.passWordEditText);
        TextInputEditText passWordConfirm = view.findViewById(R.id.passwordConfirmEditText);
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isPassWordSuitable(s)) {
                    passWord.setError("Must be More Than 6 Characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passWordConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isPassWordNotMatching(passWord.getText().toString(), s.toString())) {
                    passWordConfirmEditText.setError("Unmatching Password");
                } else {
                    passWordConfirmEditText.removeTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewModel.getUser().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userMain = user;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_three, container, false);
        FloatingActionButton nextButton = view.findViewById(R.id.next_floating_action_button);
        passWordEditText = view.findViewById(R.id.passWordEditText);
        passWordConfirmEditText = view.findViewById(R.id.passwordConfirmEditText);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passWord = passWordEditText.getText().toString().trim();
                String passWordConfirm = passWordConfirmEditText.getText().toString().trim();

                if (passWord.isEmpty() || passWordConfirm.isEmpty()) {
                    if (passWord.isEmpty()) {
                        passWordEditText.setError("Enter Suitable PassWord");
                    } else {
                        passWordConfirmEditText.setError("Confirm Input Password");
                    }
                } else if (isPassWordNotMatching(passWord, passWordConfirm)) {
                    passWordConfirmEditText.setError("Unmatching PassWord");
                } else {
                    updateViewModel();

                    SignUpFour fourthSignUp = new SignUpFour();

                    FragmentManager manager = getParentFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.signupfragment_container, fourthSignUp)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }

    private void updateViewModel() {
        String passWord = passWordConfirmEditText.getText().toString().trim();
        userMain.setPassWord(passWord);
    }

    private boolean isPassWordNotMatching(String passWord, String passWordConfirm) {
        return !passWord.equals(passWordConfirm);
    }

    private boolean isPassWordSuitable(CharSequence passWord) {
        return passWord.length() > 6;
    }
}
