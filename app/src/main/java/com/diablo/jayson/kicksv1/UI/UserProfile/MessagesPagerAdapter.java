package com.diablo.jayson.kicksv1.UI.UserProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MessagesPagerAdapter extends FragmentPagerAdapter {
    public MessagesPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GroupMessagesFragment();
            case 1:
                return new PeopleMessagesFragment();
            default:
                return new GroupMessagesFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Groups";
            case 1:
                return "People";
            default:
                return null;
        }
    }
}
