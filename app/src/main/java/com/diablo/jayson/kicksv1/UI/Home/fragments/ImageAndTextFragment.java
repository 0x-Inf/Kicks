package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.KickInFeaturedList;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageAndTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageAndTextFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView imageAndTextTitle, featuredKickDescription,
            featuredKickNameTextView, featuredKickDescriptionTextView;
    private ImageView imageAndTextImage, featuredKickImageView;

    private FeaturedViewModel featuredViewModel;
    private FeaturedKicks featuredKick;
    private ArrayList<KickInFeaturedList> featuredKicks;
    private ArrayList<String> tagsInFeaturedKick;

    public ImageAndTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageAndTextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageAndTextFragment newInstance(String param1, String param2) {
        ImageAndTextFragment fragment = new ImageAndTextFragment();
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
        featuredViewModel = new ViewModelProvider(requireActivity()).get(FeaturedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_image_and_text, container, false);
        imageAndTextTitle = root.findViewById(R.id.imageAndTextTitle);
        featuredKickDescription = root.findViewById(R.id.featuredKickDescription);
        featuredKickNameTextView = root.findViewById(R.id.featuredKickNameTextView);
        featuredKickDescriptionTextView = root.findViewById(R.id.featuredKickDescriptionTextView);
        imageAndTextImage = root.findViewById(R.id.imageAndTextImage);
        featuredKickImageView = root.findViewById(R.id.featured_kick_image_view);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featuredKicks = new ArrayList<KickInFeaturedList>();
        tagsInFeaturedKick = new ArrayList<String>();
        featuredViewModel.getFeaturedId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                String featuredId = s.trim();
//                imageAndTextTitle.setText(featuredId);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("featuredkicks").document(featuredId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            featuredKick = new FeaturedKicks();
                            featuredKick = documentSnapshot.toObject(FeaturedKicks.class);
                            String featuredTitle = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedTitle();
                            String featuredImage = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedImageUrl();
                            String featuredShortDescription = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedSubTitle();
                            imageAndTextTitle.setText(featuredTitle);
                            featuredKickDescription.setText(featuredShortDescription);
                            if (isAdded()) {
                                Glide.with(getActivity().getApplicationContext())
                                        .load(featuredImage)
                                        .into(imageAndTextImage);
                            }

                        } else {
                            Log.e("Not There ", "Document Isn't there");
                        }


                    }
                });
//                db.collection("featuredkicks").document(featuredId).get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()){
//                                    DocumentSnapshot documentSnapshot = task.getResult();
//                                    assert documentSnapshot != null;
//                                    String featuredTitle = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedTitle();
//                                    String featuredImage = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedImageUrl();
//                                    String featuredShortDescription = Objects.requireNonNull(documentSnapshot.toObject(FeaturedKicks.class)).getFeaturedSubTitle();
//                                    imageAndTextTitle.setText(featuredTitle);
//                                    featuredKickDescription.setText(featuredShortDescription);
//                                    Glide.with(getContext())
//                                            .load(featuredImage)
//                                            .into(imageAndTextImage);
//
//                                }
//                            }
//                        });
//
                db.collection("featuredkicks").document(featuredId).collection("kicksinlist")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                featuredKicks.add(new KickInFeaturedList(documentSnapshot.toObject(KickInFeaturedList.class).getKickName(),
                                        documentSnapshot.toObject(KickInFeaturedList.class).getKickImage(),
                                        documentSnapshot.toObject(KickInFeaturedList.class).getKickShortDescription(),
                                        documentSnapshot.toObject(KickInFeaturedList.class).getKickId(),
                                        documentSnapshot.toObject(KickInFeaturedList.class).getTags()));
                                Log.e("These bois:", documentSnapshot.toObject(KickInFeaturedList.class).getKickId());
                            }

                            String featuredKickName = featuredKicks.get(0).getKickName();
                            String featuredKickShortDescription = featuredKicks.get(0).getKickShortDescription();
                            String featuredKickImage = featuredKicks.get(0).getKickImage();
                            tagsInFeaturedKick = featuredKicks.get(0).getTags();
                            featuredKickNameTextView.setText(featuredKickName);
                            featuredKickDescriptionTextView.setText(featuredKickShortDescription);
                            if (isAdded()) {
                                Glide.with(Objects.requireNonNull(getContext()))
                                        .load(featuredKickImage)
                                        .into(featuredKickImageView);
                            }


                        }

                    }
                });


            }
        });
    }
}
