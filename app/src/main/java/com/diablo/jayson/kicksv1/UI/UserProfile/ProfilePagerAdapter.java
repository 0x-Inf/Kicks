package com.diablo.jayson.kicksv1.UI.UserProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfilePagerAdapter extends FragmentPagerAdapter {


    public ProfilePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ActiveActivitiesFragment();
            case 1:
                return new RatingsFragment();
            default:
                return new RatingsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Activity";
            case 1:
                return "Stats";
            default:
                return null;
        }
    }
}
