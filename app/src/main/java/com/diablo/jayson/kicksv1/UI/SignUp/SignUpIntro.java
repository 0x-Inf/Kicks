package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpIntro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpIntro extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = SignUpIntro.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private TextView guestTextView;

    public SignUpIntro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpIntro.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpIntro newInstance(String param1, String param2) {
        SignUpIntro fragment = new SignUpIntro();
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
        View root = inflater.inflate(R.layout.fragment_sign_up_intro, container, false);
        ExtendedFloatingActionButton signUpMainButton = root.findViewById(R.id.signUpMain);
        guestTextView = root.findViewById(R.id.guestTextView);
        guestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAnonymously();
            }
        });
        signUpMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBriefly();

            }
        });
        return root;
    }

    private void signUpBriefly() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SignUpOne firstSignUp = new SignUpOne();

                            FragmentManager manager = getParentFragmentManager();

                            manager.beginTransaction()
                                    .replace(R.id.signupfragment_container, firstSignUp)
                                    .commit();
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUpAnonymously() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
