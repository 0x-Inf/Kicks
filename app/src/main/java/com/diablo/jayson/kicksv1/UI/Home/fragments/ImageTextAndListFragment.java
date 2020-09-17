package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedKickListAdapter;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageTextAndListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageTextAndListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView imageAndTextTitle, featuredKickDescription;
    private ImageView imageAndTextImage;
    private RecyclerView featuredListRecycler;

    private ArrayList<Kick> kicksData;

    private FeaturedViewModel featuredViewModel;

    public ImageTextAndListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageTextAndListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageTextAndListFragment newInstance(String param1, String param2) {
        ImageTextAndListFragment fragment = new ImageTextAndListFragment();
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
        View root = inflater.inflate(R.layout.fragment_image_text_and_list, container, false);
        imageAndTextTitle = root.findViewById(R.id.imageAndTextTitle);
        featuredKickDescription = root.findViewById(R.id.featuredKickDescription);
        imageAndTextImage = root.findViewById(R.id.imageAndTextImage);
        featuredListRecycler = root.findViewById(R.id.featuredList);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kicksData = new ArrayList<Kick>();
        featuredViewModel.getFeaturedId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                String featuredId = s.trim();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("featuredkicks").document(featuredId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
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
                                }
                            }
                        });

                db.collection("featuredkicks").document(featuredId).collection("kicksinlist")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                kicksData.add(documentSnapshot.toObject(Kick.class));
                            }
                            FeaturedKickListAdapter kickListAdapter = new FeaturedKickListAdapter(getContext(), kicksData);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            featuredListRecycler.setLayoutManager(layoutManager);
                            featuredListRecycler.setAdapter(kickListAdapter);
                        }
                    }
                });
            }
        });
    }
}
