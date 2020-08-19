package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.diablo.jayson.kicksv1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageAndTextOnlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageAndTextOnlyFragment extends FragmentActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageAndTextOnlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageAndTextOnlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageAndTextOnlyFragment newInstance(String param1, String param2) {
        ImageAndTextOnlyFragment fragment = new ImageAndTextOnlyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_and_text_only);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
