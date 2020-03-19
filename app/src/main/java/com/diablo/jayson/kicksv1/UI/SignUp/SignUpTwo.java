package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String EXTRA_INFO = "info";
    private static final String TAG = SignUpTwo.class.getSimpleName();


    private FirebaseAuth mAuth;

    private EditText emailEditText, phoneEditText;
    private User userMain;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SignUpViewModel viewModel;


    public SignUpTwo() {
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
    public static SignUpTwo newInstance(String param1, String param2) {
        SignUpTwo fragment = new SignUpTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_two, container, false);
        FloatingActionButton nextSignUpButton = view.findViewById(R.id.next2_floating_action_button);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        nextSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (email.isEmpty() || phone.isEmpty()) {
                    if (email.isEmpty() || isEmailValid(email)) {
                        emailEditText.setError("Set a Valid Email Address");
                    } else if (phone.isEmpty()) {
                        phoneEditText.setError("Set a Phone Number");
                    }
                } else if (!isEmailValid(email)) {
                    Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    updateViewModel();

                    SignUpThree secondSignUp = new SignUpThree();

                    FragmentManager manager = getParentFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.signupfragment_container, secondSignUp)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        return view;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateViewModel() {


        userMain.setUserEmail(emailEditText.getText().toString());
        userMain.setPhoneNumber(phoneEditText.getText().toString());
        viewModel.setUser(userMain);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

}
