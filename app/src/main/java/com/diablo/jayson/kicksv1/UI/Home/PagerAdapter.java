package com.diablo.jayson.kicksv1.UI.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.diablo.jayson.kicksv1.UI.Home.fragments.FeaturedFragment;
import com.diablo.jayson.kicksv1.UI.Home.fragments.KickFeedFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new KickFeedFragment();
            case 1:
                return new FeaturedFragment();
            default:
                return new KickFeedFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Feed";
            case 1:
                return "For You";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
