package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.ProfilePicExample;
import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFour#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFour extends Fragment implements ProfilePicsAdapter.OnPicSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = SignUpFour.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ImageView profilePicImage;
    private RelativeLayout loadingScreen;

    private FirebaseAuth mAuth;

    private SignUpViewModel viewModel;
    private ProfilePicsAdapter adapter;

    private User mainUser;

    static final int REQUEST_IMAGE_GET = 1;

    public SignUpFour() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFour.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFour newInstance(String param1, String param2) {
        SignUpFour fragment = new SignUpFour();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_four, container, false);
//        recyclerView = view.findViewById(R.id.profilePicsExamplesRecycler);
//        profilePicImage = view.findViewById(R.id.profilePicImage);
//        loadingScreen = view.findViewById(R.id.loading_screen);
//        ExtendedFloatingActionButton signUpButton = view.findViewById(R.id.sign_up_four_finish_efab);

//        profilePicImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
//                    startActivityForResult(intent, REQUEST_IMAGE_GET);
//                }
//            }
//        });
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateViewModel();
//                mAuth = FirebaseAuth.getInstance();
//                AuthCredential credential = EmailAuthProvider.getCredential(mainUser.getUserEmail(), mainUser.getPassWord());
//                MessageDigest messageDigest = null;
//                try {
//                    messageDigest = MessageDigest.getInstance("SHA-256");
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                assert messageDigest != null;
//                messageDigest.update(mainUser.getPassWord().getBytes());
//                String encryptedString = new String(messageDigest.digest());
//                mainUser.setPassWord(encryptedString);
//                showLoadingScreen();
//                Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
//                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "linkWithCredential:success");
//                                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
//                                    assert user != null;
//                                    mainUser.setUid(user.getUid());
//                                    FirebaseFirestore.getInstance().collection("users")
//                                            .document(user.getUid())
//                                            .set(mainUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                                    .setDisplayName(mainUser.getUserName())
//                                                    .setPhotoUri(Uri.parse(mainUser.getPhotoUrl()))
//                                                    .build();
//                                            user.updateProfile(profileUpdates)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                Log.e(TAG, "User profile updated.");
//                                                            }
//                                                        }
//                                                    });
//                                            startActivity(new Intent(getContext(), MainActivity.class));
//                                            hideLoadingScreen();
//                                        }
//                                    })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    hideLoadingScreen();
//                                                }
//                                            });
//
//                                } else {
//                                    hideLoadingScreen();
//                                    Log.w(TAG, "linkWithCredential:failure", task.getException());
//                                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
//            }
//        });
        loadPicsFromFirebase();
        return view;
    }

    private void showLoadingScreen() {
        loadingScreen.setVisibility(View.VISIBLE);
        loadingScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    private void hideLoadingScreen() {
        loadingScreen.setVisibility(View.GONE);

    }

    private void updateViewModel() {
        mainUser.setSignedUpTime(Timestamp.now());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_GET) {
//            assert data != null;
////            Bitmap thumbnail = data.getParcelableExtra("data");
//            Uri fullPhotoUri = data.getData();
//
//
//            Glide.with(Objects.requireNonNull(getContext()))
//                    .load(fullPhotoUri)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(profilePicImage);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private void loadPicsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = FirebaseFirestore.getInstance()
                .collection("profilepicexamples");

        FirestoreRecyclerOptions<ProfilePicExample> options = new FirestoreRecyclerOptions.Builder<ProfilePicExample>()
                .setQuery(query, ProfilePicExample.class)
                .build();
        adapter = new ProfilePicsAdapter(options, this);
        int gridrows = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), gridrows, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUser().observe(requireActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mainUser = user;
            }
        });
    }

    @Override
    public void onPicSelected(ProfilePicExample pic) {
//        String picUrl = pic.getPicUrl();
//        Glide.with(Objects.requireNonNull(getContext()))
//                .load(picUrl)
//                .apply(RequestOptions.circleCropTransform())
//                .into(profilePicImage);
//        mainUser.setPhotoUrl(picUrl);
    }
}


//
//.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//        .setDisplayName(mainUser.getUserName())
//        .setPhotoUri(Uri.parse(mainUser.getPhotoUrl()))
//        .build();
//        user.updateProfile(profileUpdates)
//        .addOnCompleteListener(new OnCompleteListener<Void>() {
//@Override
//public void onComplete(@NonNull Task<Void> task) {
//        if (task.isSuccessful()) {
//        Log.e(TAG, "User profile updated.");
//        }
//        }
//        });
//        startActivity(new Intent(getContext(), MainActivity.class));
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//
//        }
//        });
