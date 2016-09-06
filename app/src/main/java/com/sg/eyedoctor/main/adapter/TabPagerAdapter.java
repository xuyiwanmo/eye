package com.sg.eyedoctor.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * ViewPager Fragment切换Adapter
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments;

    public TabPagerAdapter(FragmentManager fm, ViewPager consultPager, ArrayList<Fragment> fragments, ViewPager.OnPageChangeListener pageChangeListener) {
        super(fm);
        mFragments = fragments;
        consultPager.setOnPageChangeListener(pageChangeListener);
    }
    public TabPagerAdapter(FragmentManager fm, ViewPager consultPager, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if(mFragments==null){
            return 0;
        }
        return mFragments.size();
    }


}

