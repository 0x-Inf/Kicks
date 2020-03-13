package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpOne extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private EditText userNameEditText, firstNameEditText, secondNameEditText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private onNextClickedListener listener;

    public SignUpOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpOne.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpOne newInstance(String param1, String param2) {
        SignUpOne fragment = new SignUpOne();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_one, container, false);
        FloatingActionButton nextButton = (FloatingActionButton) view.findViewById(R.id.next_floating_action_button);
        userNameEditText = view.findViewById(R.id.usernameEditText);
        firstNameEditText = view.findViewById(R.id.firstnameEditText);
        secondNameEditText = view.findViewById(R.id.secondnameEditText);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(userNameEditText.getText().toString(), firstNameEditText.getText().toString(), secondNameEditText.getText().toString());
            }
        });

        return view;
    }

    public interface onNextClickedListener {
        void onItemsAdded(User user);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onNextClickedListener) {
            listener = (onNextClickedListener) context;
        } else {
            throw new
                    ClassCastException(context.toString() + "must implement MyListFragment.onNextClickedListener");
        }
    }

    public void updateUser(String userName, String firstName, String secondName) {
        String username = userNameEditText.getText().toString();
        String firstname = firstNameEditText.getText().toString();
        String secondname = secondNameEditText.getText().toString();
        User user = new User(username, firstname, secondname, "", "", "", "",
                "", true, "");

        listener.onItemsAdded(user);
    }
}
