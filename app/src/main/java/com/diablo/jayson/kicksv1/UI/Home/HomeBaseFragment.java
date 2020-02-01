package com.diablo.jayson.kicksv1.UI.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.diablo.jayson.kicksv1.Adapters.PagerAdapter;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Search.SearchActivity;
import com.diablo.jayson.kicksv1.UI.UserProfile.ProfileActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeBaseFragment extends Fragment implements View.OnClickListener {

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    private ImageButton mSearchButton,mSettingButton;
    private ImageView mProfilePicImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.swipable_feed_fragment,container,false);
//        mSearchButton = root.findViewById(R.id.searchImageButton);
//        mSettingButton = root.findViewById(R.id.settingsImageButton);
//        mProfilePicImageView = root.findViewById(R.id.profilePicImageView);
//        setClickListeners();
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

//        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        String photoUrl = "https://comps.canstockphoto.com/bowling-eps-vectors_csp2647543.jpg";

//        Glide.with(this)
//                .load(photoUrl)
//                .apply(RequestOptions.circleCropTransform())
//                .into(mProfilePicImageView);

    }

    private void setClickListeners(){
        mSearchButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
        mProfilePicImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        if (view.equals(mSearchButton)){
//            startActivity(new Intent(getContext(), SearchActivity.class));
//        }else if (view.equals(mSettingButton)){
//            startActivity(new Intent(getContext(),SearchActivity.class));
//        }else if (view.equals(mProfilePicImageView)){
//            startActivity(new Intent(getContext(), ProfileActivity.class));
//        }
    }
}
