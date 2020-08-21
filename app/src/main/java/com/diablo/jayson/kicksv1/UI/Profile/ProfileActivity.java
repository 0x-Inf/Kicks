package com.diablo.jayson.kicksv1.UI.Profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private TextView mProfileUserName,mNumberOfFollowers,mNumberOfFollowing;
    private ImageView mProfilePic;
    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TabLayout profileTabLayout = findViewById(R.id.profile_tab_layout);
        ViewPager profileViewPager = findViewById(R.id.profileViewPager);
        mProfilePic = findViewById(R.id.profilePic);
        mProfileUserName = findViewById(R.id.profileName);
        mContext = getApplicationContext();

        PagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        profileViewPager.setAdapter(profilePagerAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager);
        profileTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ef5350"));
        profileTabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ef5350"));

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String picUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();
        String userName = user.getDisplayName();
        mProfileUserName.setText(userName);

        Glide.with(mContext)
                .load(picUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(mProfilePic);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
