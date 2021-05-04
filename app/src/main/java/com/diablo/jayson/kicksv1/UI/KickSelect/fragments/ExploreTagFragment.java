package com.diablo.jayson.kicksv1.UI.KickSelect.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.UI.KickSelect.ExploreViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentExploreTagBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreTagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreTagFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentExploreTagBinding binding;
    private ExploreViewModel exploreViewModel;

    private String tagId;
    private Tag currentTag;

    public ExploreTagFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreTagFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreTagFragment newInstance(String param1, String param2) {
        ExploreTagFragment fragment = new ExploreTagFragment();
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
        binding = FragmentExploreTagBinding.inflate(inflater, container, false);
        exploreViewModel = new ViewModelProvider(requireActivity()).get(ExploreViewModel.class);
        assert getArguments() != null;
        tagId = ExploreTagFragmentArgs.fromBundle(getArguments()).getTagId();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exploreViewModel.getTagWithId(tagId).observe(getViewLifecycleOwner(), new Observer<Tag>() {
            @Override
            public void onChanged(Tag tag) {
                currentTag = tag;
                binding.tagNameTextView.setText(currentTag.getTagName());
            }
        });
    }
}