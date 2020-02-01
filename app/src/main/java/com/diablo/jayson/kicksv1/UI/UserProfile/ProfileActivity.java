package com.diablo.jayson.kicksv1.UI.UserProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.diablo.jayson.kicksv1.Adapters.ProfilePagerAdapter;
import com.diablo.jayson.kicksv1.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        mFirebaseAuth = FirebaseAuth.getInstance();

        String photoUrl = "https://comps.canstockphoto.com/bowling-eps-vectors_csp2647543.jpg";
//        String name = mFirebaseUser.getDisplayName();
        String userName = "Jayson Amati";
        mProfileUserName.setText(userName);



        Glide.with(mContext)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(mProfilePic);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
