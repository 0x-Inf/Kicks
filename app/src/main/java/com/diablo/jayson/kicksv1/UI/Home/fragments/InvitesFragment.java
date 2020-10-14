package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.UI.Home.InvitesListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentInvitesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitesFragment extends Fragment implements InvitesListAdapter.OnAcceptSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentInvitesBinding binding;
    private InvitesListAdapter invitesListAdapter;
    private HomeViewModel homeViewModel;
    private InvitesFragment listener;
    private ArrayList<Invite> invitesData = new ArrayList<>();

    public InvitesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitesFragment newInstance(String param1, String param2) {
        InvitesFragment fragment = new InvitesFragment();
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
        binding = FragmentInvitesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        listener = this;
        homeViewModel.getInvitesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Invite>>() {
            @Override
            public void onChanged(ArrayList<Invite> invites) {
                invitesData = invites;
                invitesListAdapter = new InvitesListAdapter(invitesData, listener);
                binding.invitesListViewPager.setAdapter(invitesListAdapter);
                binding.invitesListViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            }
        });
    }

    @Override
    public void onAcceptSelected(Invite acceptedInvite) {

        invitesData.remove(acceptedInvite);
        invitesListAdapter.notifyDataSetChanged();
    }
}