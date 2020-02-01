package com.diablo.jayson.kicksv1.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.diablo.jayson.kicksv1.UI.Home.FeaturedFragment;
import com.diablo.jayson.kicksv1.UI.Home.KickFeedFragment;
import com.diablo.jayson.kicksv1.UI.MapFeed.KickMapFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FeaturedFragment();
            case 1:
                return new KickFeedFragment();
            case 2:
                return new KickMapFragment();
            default:
                return new KickFeedFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Featured";
            case 1:
                return "Feed";
            case 2:
                return "Map";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
