package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.databinding.FragmentKickSelectedMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KickSelectedMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KickSelectedMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentKickSelectedMainBinding binding;

    private Kick kickSelectedMain;
    private ArrayList<Activity> availableActivitiesData;

    public KickSelectedMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KickSelectedMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KickSelectedMainFragment newInstance(String param1, String param2) {
        KickSelectedMainFragment fragment = new KickSelectedMainFragment();
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKickSelectedMainBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        kickSelectedMain = KickSelectedMainFragmentArgs.fromBundle(getArguments()).getKick();
        Glide.with(requireContext())
                .load(kickSelectedMain.getKickMainImageUrl())
                .into(binding.kickImageImageView);
        binding.kickNameTextView.setText(kickSelectedMain.getKickName());

        binding.startActivityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.facilitiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return binding.getRoot();
    }
}