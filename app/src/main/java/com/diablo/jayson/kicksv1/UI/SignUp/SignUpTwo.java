package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private EditText emailEditText, passwordEditText, passwordconfirmEditText;
    private User userMain = new User("", "", "", "", "",
            "", "", "", true, "");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private onNext2ClickedListener listener;

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
        View view = inflater.inflate(R.layout.fragment_sign_up_two, container, false);
        FloatingActionButton next2Button = view.findViewById(R.id.next2_floating_action_button);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordconfirmEditText = view.findViewById(R.id.passwordConfirmEditText);

        next2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update2User();

                mAuth.createUserWithEmailAndPassword(userMain.getUserEmail(), userMain.getPassWord())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("users").add(userMain)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

    public interface onNext2ClickedListener {
        void onUserItemsAdded(String amail, String password);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onNext2ClickedListener) {
            listener = (onNext2ClickedListener) context;
        } else {
            throw new
                    ClassCastException(context.toString() + "must implement SignUpTwo.onNext2ClickedListener");
        }
    }

    public void update2User() {
        String email = emailEditText.getText().toString();
        String password = passwordconfirmEditText.getText().toString();
        userMain.setUserEmail(email);
        userMain.setPassWord(password);

        listener.onUserItemsAdded(email, password);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            User user1 = (User) bundle.getSerializable(EXTRA_INFO);
            if (user1 != null) {
                setUserInfo1(user1);
            }
        }
    }

    public void setUserInfo1(User user) {
        user.setUserEmail(emailEditText.getText().toString());
        user.setPassWord(passwordconfirmEditText.getText().toString());
        userMain = user;

    }
}
