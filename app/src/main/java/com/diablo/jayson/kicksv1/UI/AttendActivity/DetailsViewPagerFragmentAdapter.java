package com.diablo.jayson.kicksv1.UI.AttendActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DetailsViewPagerFragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public int firstElementPosition = Integer.MAX_VALUE /2;


    public DetailsViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }


    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position) ;
    }

    @Override
    public int getItemCount() {
//        if (!arrayList.isEmpty()){
//            return Integer.MAX_VALUE;
//        }else {
//            return 0;
//        }
        return arrayList.size();
    }
}
